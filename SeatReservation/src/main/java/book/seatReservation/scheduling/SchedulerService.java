package book.seatReservation.scheduling;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.notification.NotificationService;
import book.seatReservation.database.notification.model.Notification;
import book.seatReservation.database.seat.SeatService;
import book.seatReservation.database.seat.model.Seat;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.fcm.FirebaseMainController;
import book.seatReservation.fcm.model.ReqPushMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final TaskScheduler taskScheduler;
    private final UserService userService;
    private final NotificationService notificationService;
    private final SeatService seatService;
    private final FirebaseMainController firebaseMainController;

    public void register(Runnable runnable, int period, String schedulerName) {
        logger.info(Thread.currentThread().getName() + '\t' + "등록!");
        ScheduledFuture<?> task = taskScheduler.scheduleAtFixedRate(runnable, period);
        scheduledTasks.put(schedulerName, task);
    }

    public void remove(String name) {
        logger.info(Thread.currentThread().getName() + "종료!");
        scheduledTasks.get(name).cancel(true);
    }

    @Scheduled(cron = "0 50 6 * * *")
    public void SchedulingCheck() {
        logger.info("작동중 알림");
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void BookScheduleNotify() throws BaseException {
        List<User> users = userService.getAllUser();
        Iterator<User> iterator = users.iterator();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "요일";
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 ");
        String date = sdf.format(calendar.getTime()) + day;

        while (iterator.hasNext()) {
            User user = iterator.next();
            Notification notification = notificationService.getAlarmActive(user.getIdx());
            Seat seat = seatService.getSeat(user.getIdx());
            // 알람 수신 미동의
            if (!notification.getPushAlarm())
                continue;

            if (user.isScheduling()) {
                String seatText;
                if (seat == null)
                    seatText = "좌석이 지정되지 않았습니다.";
                else
                    seatText = String.format("%c행 %d열 좌석 선택됨", seat.getX(), seat.getY());

                String yourMessage = String.format("%s\n%s\n%s", "내일 날짜: " + date,
                        seatText, "자리예약이 예정");
                ReqPushMessage reqPushMessage = ReqPushMessage.builder()
                        .userId(user.getUserId())
                        .userPwd(user.getUserPwd())
                        .title("자리 예약 예정 알림")
                        .message(yourMessage)
                        .build();
                firebaseMainController.pushMessage(reqPushMessage);
            }
            else{
                ReqPushMessage reqPushMessage = ReqPushMessage.builder()
                        .userId(user.getUserId())
                        .userPwd(user.getUserPwd())
                        .title("자리 예약 예정 없음")
                        .message("자리 예약 시스템이 작동중이지 않습니다.\n예약 하시려면 앱에서 '자동예약'을 활성화 해주세요.")
                        .build();

                firebaseMainController.pushMessage(reqPushMessage);
            }
        }
    }
}
