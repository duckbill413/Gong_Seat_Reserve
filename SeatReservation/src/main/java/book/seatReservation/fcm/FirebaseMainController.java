package book.seatReservation.fcm;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.config.response.BaseResponseStatus;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.database.usertoken.UserTokenService;
import book.seatReservation.fcm.model.ReqPushMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FirebaseMainController {
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final UserTokenService userTokenService;
    private final UserService userService;

    @PostMapping("/api/fcm")
    public BaseResponse<ResponseEntity> pushMessage(@RequestBody ReqPushMessage reqPushMessage){
        try {
            User user = userService.getUser(reqPushMessage.getUserId(), reqPushMessage.getUserPwd());
            String token = userTokenService.getUserToken(user.getIdx()).getToken();
            if (token == null)
                throw new BaseException(BaseResponseStatus.FAILED_TO_LOAD_TOKEN);

            firebaseCloudMessageService.sendMessageTo(
                    token,
                    reqPushMessage.getTitle(),
                    reqPushMessage.getMessage());
            return new BaseResponse<>(ResponseEntity.ok().build());
        }
        catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
