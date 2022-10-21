package book.seatReservation.database.user;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.config.response.BaseResponseStatus;
import book.seatReservation.database.gonglogin.GongLoginService;
import book.seatReservation.database.naverlogin.NaverLoginService;
import book.seatReservation.database.notification.NotificationService;
import book.seatReservation.database.seat.SeatService;
import book.seatReservation.database.user.model.*;
import book.seatReservation.database.usertoken.UserTokenService;
import book.seatReservation.gong.model.ReqLogin;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;
    private final GongLoginService gongLoginService;
    private final NaverLoginService naverLoginService;
    private final SeatService seatService;
    private final UserTokenService userTokenService;
    private final NotificationService notificationService;

    @ResponseBody
    @PostMapping("/update/register")
    public BaseResponse<String> registerUser(@RequestBody ReqUser reqUser) {
        try {
            userService.checkUserId(reqUser.getUserId());
            userService.checkUserEmail(reqUser.getEmail());
            userService.registerUser(reqUser);

            User user = userService.getUser(reqUser.getUserId(), reqUser.getUserPwd());
            notificationService.updateNotification(user.getIdx(), true, true);
            return new BaseResponse<>("유저 등록 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/update/user")
    public BaseResponse<String> updateUser(@RequestBody ReqUser reqUser) {
        try {
            userService.updateUser(reqUser);
            return new BaseResponse<>("유저 정보 수정 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/update/email")
    public BaseResponse<String> updateUserEmail(@RequestBody ReqChangeEmail reqChangeEmail) {
        try {
            User user = userService.getUser(reqChangeEmail.getUserId(), reqChangeEmail.getUserPwd());
            if (user == null) throw new BaseException(BaseResponseStatus.UPDATE_USER_EMAIL_FAILED);

            user.setEmail(reqChangeEmail.getEmail());
            userService.updateUserEmail(user);
            return new BaseResponse<>("이메일 업데이트 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/update/pwd")
    public BaseResponse<String> updateUserPwd(@RequestBody ReqChangePwd reqChangePwd) {
        try {
            User user = userService.getUser(reqChangePwd.getUserId(), reqChangePwd.getUserPwd());
            if (user == null) throw new BaseException(BaseResponseStatus.UPDATE_USER_PWD_FAILED);

            user.setUserPwd(reqChangePwd.getPassword());
            userService.updateUserPwd(user);
            return new BaseResponse<>("비밀번호 업데이트 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/schedule/stop")
    public BaseResponse<String> scheduleStop(@RequestBody ReqLogin reqLogin) {
        try {
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null) throw new BaseException(BaseResponseStatus.UPDATE_USER_SCHEDULE_FAILED);

            user.setScheduling(false);
            userService.updateUserStatus(user);
            return new BaseResponse<>("Service Stopped!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/schedule/start")
    public BaseResponse<String> scheduleStart(@RequestBody ReqLogin reqLogin) {
        try {
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null) throw new BaseException(BaseResponseStatus.UPDATE_USER_SCHEDULE_FAILED);

            user.setScheduling(true);
            userService.updateUserSchedule(user);
            return new BaseResponse<>("Service Started!");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/load")
    public BaseResponse<User> getUser(@RequestBody ReqUserData reqUserData) {
        try {
            User user = userService.getUser(reqUserData.getUserId(), reqUserData.getUserPwd());
            return new BaseResponse<>(user);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/delete")
    public BaseResponse<String> deleteUser(@RequestBody ReqUserData reqUserData) {
        try {
            User user = userService.getUser(reqUserData.getUserId(), reqUserData.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.FAILED_TO_DELETE_USER);

            userService.deleteUser(user);
            deleteUserData(user.getIdx());

            return new BaseResponse<>("유저 삭제 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    private void deleteUserData(long userIdx) {
        try {
            gongLoginService.deleteGongLoginData(userIdx);
        } catch (BaseException e) {
            logger.debug(userIdx + ": " + e.getStatus());
        }

        try {
            naverLoginService.deleteNaverLoginData(userIdx);
        } catch (BaseException e) {
            logger.debug(userIdx + ": " + e.getStatus());
        }

        try {
            notificationService.deleteNotifyData(userIdx);
        } catch (BaseException e) {
            logger.debug(userIdx + ": " + e.getStatus());
        }

        try {
            seatService.deleteSeat(userIdx);
        } catch (BaseException e) {
            logger.debug(userIdx + ": " + e.getStatus());
        }

        try {
            userTokenService.deleteUserToken(userIdx);
        } catch (BaseException e) {
            logger.debug(userIdx + ": " + e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/check/user")
    public BaseResponse<String> checkUser(@RequestBody ReqUserData reqUserData) {
        try {
            userService.checkUser(reqUserData.getUserId(), reqUserData.getUserPwd());

            return new BaseResponse<>("유저 정보 확인");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/check/id")
    public BaseResponse<String> checkUserId(@RequestParam String id) {
        try {
            userService.checkUserId(id);

            return new BaseResponse<>("유저 아이디 사용 가능");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/check/email")
    public BaseResponse<String> checkUserEmail(@RequestParam String email) {
        try {
            userService.checkUserEmail(email);

            return new BaseResponse<>("유저 이메일 사용 가능");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/select/all")
    public BaseResponse<List<User>> getAllUsers(@RequestBody ReqUserData reqUserData) {
        try {
            User user = userService.getUser(reqUserData.getUserId(), reqUserData.getUserPwd());
            if (user.getIdx() != 1) {
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
            }

            List<User> users = userService.getAllUser();
            return new BaseResponse<>(users);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/update/login/option")
    public BaseResponse<String> updateLoginOption(@RequestBody ReqChangeLoginOption reqChangeLoginOption) {
        try {
            User user = userService.getUser(reqChangeLoginOption.getUserId(), reqChangeLoginOption.getUserPwd());

            user.setLoginOption(reqChangeLoginOption.getLoginOption());
            userService.updateUserLoginOption(user);
            return new BaseResponse<>("login option updated");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
