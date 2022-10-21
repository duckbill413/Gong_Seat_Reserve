package book.seatReservation.database.repository;

import book.seatReservation.database.usertoken.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<UserToken, Long> {
}
