package book.seatReservation.database.usertoken;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.config.response.BaseResponseStatus;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.database.usertoken.model.ReqToken;
import book.seatReservation.database.usertoken.model.UserToken;
import book.seatReservation.gong.model.ReqLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class UserTokenController {
    private final UserTokenService userTokenService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/update")
    public BaseResponse<String> updateUserToken(@RequestBody ReqToken reqToken){
        try{
            User user = userService.getUser(reqToken.getUserId(), reqToken.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            userTokenService.updateUserToken(user.getIdx(), reqToken.getToken());
            String message = "토큰 업데이트 성공";
            return new BaseResponse<>(message);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/load")
    public BaseResponse<UserToken> getUserToken(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            UserToken userToken = userTokenService.getUserToken(user.getIdx());
            return new BaseResponse<>(userToken);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/delete")
    public BaseResponse<String> deleteUserToken(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            userTokenService.deleteUserToken(user.getIdx());
            return new BaseResponse<>("토큰 삭제 성공");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
