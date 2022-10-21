package book.seatReservation.database.seat;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import book.seatReservation.config.response.BaseResponseStatus;
import book.seatReservation.database.seat.model.ReqSeat;
import book.seatReservation.database.seat.model.Seat;
import book.seatReservation.database.user.UserService;
import book.seatReservation.database.user.model.User;
import book.seatReservation.gong.model.ReqLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seat")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/update")
    public BaseResponse<Seat> updateSeat(@RequestBody ReqSeat reqSeat){
        try{
            User user = userService.getUser(reqSeat.getUserId(), reqSeat.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            char longX = String.valueOf(reqSeat.getX()).toUpperCase().charAt(0);
            Seat seat = seatService.updateSeat(user.getIdx(), longX, reqSeat.getY());
            return new BaseResponse<>(seat);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/load")
    public BaseResponse<Seat> getSeat(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            Seat seat = seatService.getSeat(user.getIdx());
            return new BaseResponse<>(seat);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/delete")
    public BaseResponse<String> deleteSeat(@RequestBody ReqLogin reqLogin){
        try{
            User user = userService.getUser(reqLogin.getUserId(), reqLogin.getUserPwd());
            if (user == null)
                throw new BaseException(BaseResponseStatus.INVALID_USER_ACCOUNT);

            seatService.deleteSeat(user.getIdx());
            return new BaseResponse<>("좌석 삭제 성공");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
