package book.seatReservation.database.seat;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.seat.model.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatDTO seatDTO;

    public Seat updateSeat(long userIdx, char x, long y) throws BaseException {
        try{
            Seat seat = seatDTO.updateSeat(userIdx, x, y);
            return seat;
        } catch (Exception e){
            throw new BaseException(UPDATE_SEAT_FAILED);
        }
    }

    public Seat getSeat(long userIdx) throws BaseException {
        try{
            Seat seat = seatDTO.getSeat(userIdx);
            return seat;
        } catch (Exception e){
            throw new BaseException(FAILED_TO_LOAD_SEAT);
        }
    }

    public void deleteSeat(long userIdx) throws BaseException {
        try{
            seatDTO.deleteSeat(userIdx);
        } catch (Exception e){
            throw new BaseException(FAILED_TO_DELETE_SEAT);
        }
    }
}
