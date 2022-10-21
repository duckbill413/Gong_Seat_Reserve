package book.seatReservation.database.repository;

import book.seatReservation.database.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUserIdAndUserPwd(String userId, String userPwd);
    void deleteByUserIdAndUserPwd(String userId, String userPwd);
    boolean existsByUserIdAndUserPwd(String userId, String userPwd);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
}