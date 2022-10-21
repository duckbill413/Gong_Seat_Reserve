package book.seatReservation.database.notification;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.database.notification.model.ReqUpdateNotify;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.gong.model.ReqLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static book.seatReservation.config.response.BaseResponseStatus.INVALID_USER_ACCOUNT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/update")
    public BaseResponse<String> updateNotification(@RequestBody ReqUpdateNotify reqUpdateNotify){
        try{
            User user = userService.getUser(reqUpdateNotify.getUserId(), reqUpdateNotify.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);
            notificationService.updateNotification(user.getIdx(), reqUpdateNotify.getPushAlarm(), reqUpdateNotify.getNotifyEmail());
            return new BaseResponse<>("알림 수정 성공");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/push/start")
    public BaseResponse<String> startPushAlarm(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);
            notificationService.pushAlarmOn(user.getIdx());
            return new BaseResponse<>("푸쉬알림 ON");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    @ResponseBody
    @PostMapping("/push/stop")
    public BaseResponse<String> stopPushAlarm(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);
            notificationService.pushAlarmOff(user.getIdx());
            return new BaseResponse<>("푸쉬알림 OFF");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    @ResponseBody
    @PostMapping("/email/start")
    public BaseResponse<String> startNotifyEmail(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);
            notificationService.notifyEmailOn(user.getIdx());
            return new BaseResponse<>("이메일 알림 ON");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
    @ResponseBody
    @PostMapping("/email/stop")
    public BaseResponse<String> stopNotifyEmail(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);
            notificationService.notifyEmailOff(user.getIdx());
            return new BaseResponse<>("이메일 알림 OFF");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
