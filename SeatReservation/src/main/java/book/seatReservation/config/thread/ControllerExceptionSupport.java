package book.seatReservation.config.thread;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.config.response.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionSupport {
    @ExceptionHandler(BaseException.class)
    public BaseResponse<String> baseExceptionHandler(BaseException e){
        return new BaseResponse<>(e.getStatus());
    }
}
