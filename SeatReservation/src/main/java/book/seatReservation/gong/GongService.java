package book.seatReservation.gong;

import book.seatReservation.book.SeleniumAct;
import book.seatReservation.book.model.Pair;
import book.seatReservation.book.model.TodayClass;
import book.seatReservation.database.notification.NotificationService;
import book.seatReservation.database.notification.model.Notification;
import book.seatReservation.email.GmailService;
import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.gonglogin.GongLoginService;
import book.seatReservation.database.gonglogin.model.GongLogin;
import book.seatReservation.database.naverlogin.NaverLoginService;
import book.seatReservation.database.naverlogin.model.NaverLogin;
import book.seatReservation.database.result.ResultService;
import book.seatReservation.database.result.model.Result;
import book.seatReservation.database.seat.SeatService;
import book.seatReservation.database.seat.model.Seat;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.fcm.FirebaseMainController;
import book.seatReservation.fcm.model.ReqPushMessage;
import book.seatReservation.gong.model.ReqLogin;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GongService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SeleniumAct seleniumAct;
    private final NaverLoginService naverLoginService;
    private final GongLoginService gongLoginService;
    private final FirebaseMainController firebaseMainController;
    private final UserService userService;
    private final SeatService seatService;
    private final ResultService resultService;
    private final GmailService gmailService;
    public HashMap<Long, WebDriver> driverGroup;
    private final NotificationService notificationService;

    @PostConstruct
    private void init() {
        driverGroup = new HashMap<>();
    }

    public String bookingMethod(ReqLogin reqLogin) throws BaseException {
        long userIdx = 0;
        try {
            userIdx = loginMethod(reqLogin);
            Seat seat = seatService.getSeat(userIdx);
            WebDriver driver = driverGroup.get(userIdx);
            if (driver == null)
                throw new BaseException(FAILED_TO_LOAD_SCHEDULE);

            String message = seleniumAct.bookSeat(driver, seat, 0);
            getClassResult(userIdx, message);
            return message;
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_BOOK);
        } finally {
            seleniumAct.quitDriver(driverGroup.get(userIdx));
            driverGroup.remove(userIdx);
        }
    }

    public String changingMethod(ReqLogin reqLogin) throws BaseException {
        long userIdx = 0;
        try {
            userIdx = loginMethod(reqLogin);
            Seat seat = seatService.getSeat(userIdx);
            String message = seleniumAct.changeSeat(driverGroup.get(userIdx), seat);
            getClassResult(userIdx, message);
            return message;
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_CHANGE_SEAT);
        } finally {
            seleniumAct.quitDriver(driverGroup.get(userIdx));
            driverGroup.remove(userIdx);
        }
    }

    public String cancelingMethod(ReqLogin reqLogin) throws BaseException {
        long userIdx = 0;
        try {
            userIdx = loginMethod(reqLogin);
            String message = seleniumAct.cancelSeat(driverGroup.get(userIdx));
            getClassResult(userIdx, message);
            return message;
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_CANCEL_SEAT);
        } finally {
            seleniumAct.quitDriver(driverGroup.get(userIdx));
            driverGroup.remove(userIdx);
        }
    }

    public Result checkingBooked(ReqLogin reqLogin) throws BaseException {
        long userIdx = 0;
        try {
            userIdx = loginMethod(reqLogin);
            Result result = getClassResult(userIdx, null);
            return result;
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_LOAD_SEAT);
        } finally {
            seleniumAct.quitDriver(driverGroup.get(userIdx));
            driverGroup.remove(userIdx);
        }
    }

    Result getClassResult(long userIdx, String message) throws BaseException {
        try {
            if (message == null)
                message = seleniumAct.bookedSeat(driverGroup.get(userIdx));
            TodayClass todayClassInfo = seleniumAct.getReservedInfo(driverGroup.get(userIdx));
            List<List<Integer>> seatPage = seleniumAct.seatPage(driverGroup.get(userIdx));
            String json = new Gson().toJson(seatPage);

            Result result = Result.builder()
                    .userIdx(userIdx)
                    .todayClass(todayClassInfo.getTodayClass())
                    .classLocation(todayClassInfo.getClassLocation())
                    .classInfo(todayClassInfo.getClassInfo())
                    .message(message)
                    .mapping(json).build();
            resultService.updateResult(result);
            return result;
        } catch (BaseException exception) {
            throw exception;
        }
    }

    public void loginScheduling(User user) {
        try {
            if (user.isScheduling() == false)
                return;
            if (driverGroup.get(user.getIdx()) != null) // 이미 로그인 되어있는 경우
                return;

            ReqLogin reqLogin = new ReqLogin(user.getUserId(), user.getUserPwd());
            loginMethod(reqLogin);
            logger.info(user.getUserId() + " <-- " + "스케줄링 로그인 성공");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(user.getUserId() + " <-- " + "스케줄링 로그인 실패");
        }
    }

    public void bookScheduling(User user) {
        try {
            long userIdx = user.getIdx();
            if (!user.isScheduling())
                return;
            if (driverGroup.get(userIdx) == null)
                return;

            Seat seat = seatService.getSeat(userIdx);
            String message = seleniumAct.bookSeat(driverGroup.get(userIdx), seat, 1);
            if (!message.contains("좌석이 예약되었습니다."))
                throw new BaseException(FAILED_TO_BOOK);
            else logger.info(user.getUserId() + " <-- 자리예약 결과: " + message); // logger

            getClassResult(userIdx, message);

            if (message != null)
                if (message.contains("좌석이 예약되었습니다.") || message.contains("예약 가능한 스케줄이 존재하지 않습니다")) {
                    seleniumAct.quitDriver(driverGroup.get(userIdx));
                    driverGroup.remove(userIdx);
                }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(user.getUserId() + " <-- " + "스케줄링 자리예약 실패");
        }
    }

    public void driverInitScheduling(User user) {
        try {
            long userIdx = user.getIdx();
            if (!user.isScheduling())
                return;
            // WebDriver 이미 삭제됨.
            if (driverGroup.get(userIdx) != null) {
                // WebDriver 해제
                seleniumAct.quitDriver(driverGroup.get(userIdx));
                driverGroup.remove(userIdx);
            }
            // 푸쉬 알림 발송
            ReqPushMessage reqPushMessage = new ReqPushMessage(user.getUserId(), user.getUserPwd());
            Result result = resultService.getTodayResult(user.getIdx());
            if (result == null)
                throw new BaseException(FAILED_TO_SEND_PUSH);
            if (result.getMessage().contains("좌석이 예약되었습니다.")) {
                reqPushMessage.setTitle("좌석 예약 성공!");
                reqPushMessage.setMessage(result.getMessage());
            } else {
                reqPushMessage.setTitle("좌석 예약 실패..ㅠ");
            }
            TodayClass todayClass = TodayClass.builder()
                    .todayClass(result.getTodayClass())
                    .classLocation(result.getClassLocation())
                    .classInfo(result.getClassInfo())
                    .build();

            // 푸쉬 알람
            Notification notification = notificationService.getAlarmActive(userIdx);
            if (notification.getPushAlarm())
                firebaseMainController.pushMessage(reqPushMessage);
            // 이메일 알람
            if (notification.getNotifyEmail())
                sendGMail(todayClass, result.getMessage(), user.getEmail());
            logger.info(user.getUserId() + " <-- " + "스케줄링 드라이버 초기화 및 알림 완료");
        } catch (Exception e) {
            logger.info(user.getUserId() + " <-- " + "스케줄링 드라이버 초기화 및 알림 실패");
        }
    }

    public long loginMethod(ReqLogin reqLogin) throws BaseException {
        String id, pwd;
        try {
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(FAILED_TO_LOAD_USER);
            long userIdx = user.getIdx();

            // 네이버 로그인
            if (user.getLoginOption().equals("naver")) {
                NaverLogin naverLogin = naverLoginService.getNaverLoginData(userIdx);
                id = naverLogin.getNaverId();
                pwd = naverLogin.getNaverPwd();
                Pair<WebDriver, String> login = seleniumAct.naverLogin(id, pwd);
                if (login.getSecond().equals("예약 가능한 스케줄이 존재하지 않습니다.") ||
                        login.equals("페이지에 접근할 수 있는 권한이 없습니다.")) {
                    resultService.updateResult(userIdx, login.getSecond());
                } else
                    driverGroup.put(userIdx, login.getFirst());
                // 공단기 로그인
            } else if (user.getLoginOption().equals("gong")) {
                GongLogin gongLogin = gongLoginService.getGongLoginData(userIdx);
                id = gongLogin.getGongId();
                pwd = gongLogin.getGongPwd();
//                Pair<WebDriver, String> login = driverGroup.put(userIdx, seleniumAct.gongLogin(id, pwd));
            }
            return userIdx;
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public void sendGMail(TodayClass todayClassInfo, String message, String email) throws BaseException {
        try {
            gmailService.send(email, todayClassInfo.getTodayClass() + "\t자리 예약 결과",
                    String.format("%s\n%s\n완료시각: %s\n오늘도 화이팅!!\n\n홈페이지 링크: https://gong.conects.com/gong/main/academy",
                            todayClassInfo,
                            message,
                            LocalDateTime.now()));
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_SEND_EMAIL);
        }
    }

    public boolean VerifiedMessage(Result result) throws BaseException {
        try {
            LocalDateTime resultTime = result.getCreatedAt();
            LocalDateTime verifiedTime = LocalDateTime.now();
            // 생성된지 20분 이내 결과만 인정
            int min = (verifiedTime.getMinute() + 40) % 60;
            int hour = verifiedTime.getMinute() < 20 ? verifiedTime.getHour() - 1 : verifiedTime.getHour();
            verifiedTime = verifiedTime.withHour(hour);
            verifiedTime = verifiedTime.withMinute(min);
            verifiedTime = verifiedTime.withSecond(0);

            boolean isVerified = resultTime.isAfter(verifiedTime);
            return isVerified;
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_VERIFIED_RESULT);
        }
    }
}
