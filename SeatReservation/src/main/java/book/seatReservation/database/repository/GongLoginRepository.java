package book.seatReservation.database.repository;

import book.seatReservation.database.gonglogin.model.GongLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GongLoginRepository extends JpaRepository<GongLogin, Long> {
}
