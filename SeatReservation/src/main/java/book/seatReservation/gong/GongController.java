package book.seatReservation.gong;

import book.seatReservation.scheduling.SchedulerService;
import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.database.result.ResultService;
import book.seatReservation.database.result.model.Result;
import book.seatReservation.database.seat.SeatService;
import book.seatReservation.database.seat.model.Seat;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.gong.model.GongResponse;
import book.seatReservation.gong.model.ReqLogin;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@RestController
@RequestMapping("/api/gong")
@RequiredArgsConstructor
public class GongController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SchedulerService schedulerService;
    private final GongService gongService;
    private final SeatService seatService;
    private final UserService userService;
    private final ResultService resultService;

    // 좌석 예약
    @ResponseBody
    @PostMapping("/book")
    public BaseResponse<String> Booking(@RequestBody ReqLogin reqLogin) {
        try {
            String message = gongService.bookingMethod(reqLogin);
            logger.info(reqLogin.getUserId() + " <-- Seat Booking Success");
            return new BaseResponse<>(message);
        } catch (BaseException e) {
            logger.error(reqLogin.getUserId() + " <-- Seat Booking Error");
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/change")
    public BaseResponse<String> Changing(@RequestBody ReqLogin reqLogin) {
        try {
            String message = gongService.changingMethod(reqLogin);
            logger.info(reqLogin.getUserId() + " <-- Seat Changing Success");
            return new BaseResponse<>(message);
        } catch (BaseException e) {
            logger.error(reqLogin.getUserId() + " <-- Seat Changing Error");
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/cancel")
    public BaseResponse<String> Canceling(@RequestBody ReqLogin reqLogin) {
        try {
            String message = gongService.cancelingMethod(reqLogin);
            logger.info(reqLogin.getUserId() + " <-- Seat Canceling Success");
            return new BaseResponse<>(message);
        } catch (BaseException e) {
            logger.error(reqLogin.getUserId() + " <-- Seat Canceling Error");
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/booked")
    public BaseResponse<Result> Booked(@RequestBody ReqLogin reqLogin) {
        try {
            Result result = gongService.checkingBooked(reqLogin);
            logger.info(reqLogin.getUserId() + " <-- Seat Booked Checking Success");
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            logger.error(reqLogin.getUserId() + " <-- Seat Booked Checking Error");
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/hear")
    public BaseResponse<GongResponse> ShowInfo(@RequestBody ReqLogin reqLogin) {
        try {
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            Seat seat = seatService.getSeat(user.getIdx());
            Result result = resultService.getTodayResult(user.getIdx());
            GongResponse gongResponse = new GongResponse(user, seat, result);
            return new BaseResponse<>(gongResponse);
        } catch (Exception e) {
            return new BaseResponse<>(FAILED_TO_LOAD_USER);
        }
    }

    @Scheduled(cron = "0 9 7 * * ?") // 7시 7~9분까지 1분마다 실행
    private void NetUserLoginSchedule() {
        List<User> users = null;
        try {
            users = userService.getAllUser();
            if (users == null)
                throw new BaseException(FAILED_TO_LOAD_USER);
        } catch (BaseException e) {
            logger.error("유저 데이터 조회 실패");
        }
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();
            gongService.loginScheduling(user);
        }
    }

    @Scheduled(cron = "0 10 7 * * ?") // 7시 10~15분까지 1분마다 실행
    private void NetUserBookSchedule() {
        List<User> users = null;
        try {
            users = userService.getAllUser();
            if (users == null)
                throw new BaseException(FAILED_TO_LOAD_USER);
        } catch (BaseException e) {
            logger.error("유저 데이터 조회 실패");
        }
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();
            gongService.bookScheduling(user);
        }
    }

    /**
     * WebDriver 해제 및 좌석 예약 결과 메일 및 알림 발송
     **/
    @Scheduled(cron = "0 11 7 * * ?")
    private void ScheduleWebDriverInit() {
        List<User> users = null;
        try {
            users = userService.getAllUser();
            if (users == null)
                throw new BaseException(FAILED_TO_LOAD_USER);
        } catch (BaseException e) {
            logger.error("유저 데이터 조회 실패");
        }
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();
            gongService.driverInitScheduling(user);
        }
    }
}