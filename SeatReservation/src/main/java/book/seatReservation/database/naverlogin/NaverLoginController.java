package book.seatReservation.database.naverlogin;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.database.naverlogin.model.NaverLogin;
import book.seatReservation.database.naverlogin.model.ReqNaverLogin;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.gong.model.ReqLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static book.seatReservation.config.response.BaseResponseStatus.INVALID_USER_ACCOUNT;

@RestController
@RequestMapping("/naver/login")
@RequiredArgsConstructor
public class NaverLoginController {
    private final NaverLoginService naverLoginService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/update")
    public BaseResponse<String> updateNaverLoginData(@RequestBody ReqNaverLogin reqNaverLogin){
        try{
            User user = userService.getUser(reqNaverLogin.getUserId(), reqNaverLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);

            naverLoginService.updateNaverLoginData(user.getIdx(), reqNaverLogin.getNaverId(), reqNaverLogin.getNaverPwd());
            return new BaseResponse<>("네이버 로그인 정보 업데이트 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/load")
    public BaseResponse<NaverLogin> getNaverLoginData(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);

            NaverLogin naverLogin = naverLoginService.getNaverLoginData(user.getIdx());
            return new BaseResponse<>(naverLogin);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/delete")
    public BaseResponse<String> deleteNaverLoginData(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);

            naverLoginService.deleteNaverLoginData(user.getIdx());
            return new BaseResponse<>("네이버 로그인 정보 삭제 성공");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
