package book.seatReservation.database.repository;

import book.seatReservation.database.seat.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
