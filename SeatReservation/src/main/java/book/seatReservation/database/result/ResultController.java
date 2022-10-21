package book.seatReservation.database.result;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.config.response.BaseResponseStatus;
import book.seatReservation.database.result.model.ReqResult;
import book.seatReservation.database.result.model.ReqResultAllArgs;
import book.seatReservation.database.result.model.Result;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.gong.model.ReqLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/update/all")
    public BaseResponse<String> updateResult(@RequestBody ReqResultAllArgs reqResultAllArgs) {
        try {
            User user = userService.getUser(reqResultAllArgs.getUserId(), reqResultAllArgs.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            resultService.updateResult(user.getIdx(), reqResultAllArgs.getTodayClass(),
                    reqResultAllArgs.getClassLocation(), reqResultAllArgs.getClassTime(), reqResultAllArgs.getMessage());
            return new BaseResponse<>("결과 업데이트 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/update/message")
    public BaseResponse<String> updateResult(@RequestBody ReqResult reqResult) {
        try {
            User user = userService.getUser(reqResult.getUserId(), reqResult.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            resultService.updateResult(user.getIdx(), reqResult.getMessage());
            return new BaseResponse<>("결과 업데이트 성공");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/load")
    public BaseResponse<List<Result>> getResults(@RequestBody ReqLogin reqLogin) {
        try {
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            List<Result> results = resultService.getResults(user.getIdx());
            return new BaseResponse<>(results);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/load/one")
    public BaseResponse<Result> getResult(@RequestBody ReqLogin reqLogin) {
        try {
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            Result result = resultService.getResult(user.getIdx());
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/load/selected")
    public BaseResponse<List<Result>> getResultSelected(@RequestBody ReqLogin reqLogin) {
        try {
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            List<Result> results = resultService.getResultSelected(user.getIdx());
            return new BaseResponse<>(results);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/load/today")
    public BaseResponse<Result> getTodayResult(@RequestBody ReqLogin reqLogin) {
        try {
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            Result result = resultService.getTodayResult(user.getIdx());
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }
    }
}
