package book.seatReservation.database.gonglogin;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.database.gonglogin.model.GongLogin;
import book.seatReservation.database.gonglogin.model.ReqGongLogin;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.gong.model.ReqLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static book.seatReservation.config.response.BaseResponseStatus.INVALID_USER_ACCOUNT;

@RestController
@RequestMapping("/gong/login")
@RequiredArgsConstructor
public class GongLoginController {
    private final GongLoginService gongLoginService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/update")
    public BaseResponse<String> updateGongLoginData(@RequestBody ReqGongLogin reqGongLogin){
        try{
            User user = userService.getUser(reqGongLogin.getUserId(), reqGongLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);

            gongLoginService.updateGongLoginData(user.getIdx(), reqGongLogin.getGongId(), reqGongLogin.getGongPwd());
            return new BaseResponse<>("공단기 로그인 데이터 업데이트 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/load")
    public BaseResponse<GongLogin> getGongLoginData(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);

            GongLogin gongLogin = gongLoginService.getGongLoginData(user.getIdx());
            return new BaseResponse<>(gongLogin);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/delete")
    public BaseResponse<String> deleteGongLoginData(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(INVALID_USER_ACCOUNT);

            gongLoginService.deleteGongLoginData(user.getIdx());
            return new BaseResponse<>("공단기 로그인 데이터 삭제 완료");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
