package book.seatReservation.database.repository;

import book.seatReservation.database.naverlogin.model.NaverLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverLoginRepository extends JpaRepository<NaverLogin, Long> {
}
