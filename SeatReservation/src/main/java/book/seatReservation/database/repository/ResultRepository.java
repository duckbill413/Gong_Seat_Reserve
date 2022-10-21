package book.seatReservation.database.repository;

import book.seatReservation.database.result.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<List<Result>> findByUserIdx(long userIdx);
    Optional<Result> findTopByUserIdxOrderByCreatedAtDesc(long userIdx);
    Optional<Result> findTopByUserIdxAndMappingIsNotNullAndCreatedAtAfterOrderByCreatedAtDesc(long userIdx, LocalDateTime today);
    Optional<List<Result>> findTop20ByUserIdxOrderByCreatedAtDesc(long userIdx);
}