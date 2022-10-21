package book.seatReservation.database.seat;

import book.seatReservation.database.repository.SeatRepository;
import book.seatReservation.database.seat.model.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatDTO {
    private final SeatRepository seatRepository;

    public Seat updateSeat(long userIdx, char x, long y){
        Seat seat = Seat.builder()
                .userIdx(userIdx)
                .x(x)
                .y(y)
                .build();
        Seat newSeat = seatRepository.save(seat);
        return newSeat;
    }

    public Seat getSeat(long userIdx){
        Optional<Seat> seat = seatRepository.findById(userIdx);
        return seat.orElse(null);
    }

    public void deleteSeat(long userIdx){
        seatRepository.deleteById(userIdx);
    }
}
