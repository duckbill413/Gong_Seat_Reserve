package book.seatReservation.database.result;

import book.seatReservation.book.model.TodayClass;
import book.seatReservation.database.result.model.Result;
import book.seatReservation.database.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ResultDTO {
    private final ResultRepository resultRepository;

    public void updateResult(long userIdx, String message) {
        Result result = Result.builder()
                .userIdx(userIdx)
                .message(message)
                .build();
        resultRepository.save(result);
    }

    public void updateResult(long userIdx, String todayClass, String classLocation, String classInfo, String message){
        Result result = Result.builder()
                .userIdx(userIdx)
                .todayClass(todayClass)
                .classLocation(classLocation)
                .classInfo(classInfo)
                .message(message).build();
        resultRepository.save(result);
    }

    public void updateResult(long userIdx, String message, TodayClass todayClass){
        Result result = Result.builder()
                .userIdx(userIdx)
                .todayClass(todayClass.getTodayClass())
                .classLocation(todayClass.getClassLocation())
                .classInfo(todayClass.getClassInfo())
                .message(message).build();
        resultRepository.save(result);
    }

    public void updateResult(Result result){
        resultRepository.save(result);
    }
    public List<Result> getResults(long userIdx) {
        Optional<List<Result>> results = resultRepository.findByUserIdx(userIdx);
        return results.orElse(null);
    }

    public Result getResult(long userIdx){
        Optional<Result> result = resultRepository.findTopByUserIdxOrderByCreatedAtDesc(userIdx);
        return result.orElse(null);
    }

    public Result getTodayFinalResult(long userIdx){
        LocalDateTime startDatetime = LocalDateTime.of(
                LocalDate.now(), LocalTime.of(0,0,0)); // 오늘 00:00:00
        Optional<Result> result = resultRepository.findTopByUserIdxAndMappingIsNotNullAndCreatedAtAfterOrderByCreatedAtDesc(userIdx, startDatetime);
        return result.orElse(null);
    }

    public List<Result> getResultSelected(long userIdx) {
        Optional<List<Result>> results = resultRepository.findTop20ByUserIdxOrderByCreatedAtDesc(userIdx);
        return results.orElse(null);
    }
}
