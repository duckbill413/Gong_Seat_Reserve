package book.seatReservation.database.result;

import book.seatReservation.book.model.TodayClass;
import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.result.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static book.seatReservation.config.response.BaseResponseStatus.FAILED_TO_LOAD_RESULTS;
import static book.seatReservation.config.response.BaseResponseStatus.UPDATE_RESULT_FAILED;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultDTO resultDTO;

    public void updateResult(long userIdx, String message) throws BaseException {
        try{
            resultDTO.updateResult(userIdx, message);
        } catch (Exception e){
            throw new BaseException(UPDATE_RESULT_FAILED);
        }
    }

    public void updateResult(Result result) throws BaseException {
        try{
            resultDTO.updateResult(result);
        } catch (Exception e){
            throw new BaseException(UPDATE_RESULT_FAILED);
        }
    }

    public void updateResult(long userIdx, String todayClass, String classLocation, String classInfo, String message) throws BaseException {
        try{
            resultDTO.updateResult(userIdx, todayClass, classLocation, classInfo, message);
        } catch (Exception e){
            throw new BaseException(UPDATE_RESULT_FAILED);
        }
    }

    public void updateResult(long userIdx, String message, TodayClass todayClass) throws BaseException {
        try{
            resultDTO.updateResult(userIdx, message, todayClass);
        } catch (Exception e){
            throw new BaseException(UPDATE_RESULT_FAILED);
        }
    }

    public List<Result> getResults(long userIdx) throws BaseException {
        try{
            List<Result> results = resultDTO.getResults(userIdx);
            return results;
        } catch (Exception e){
            throw new BaseException(FAILED_TO_LOAD_RESULTS);
        }
    }

    public Result getResult(long userIdx) throws BaseException {
        try{
            Result result = resultDTO.getResult(userIdx);
            return result;
        } catch (Exception e){
            throw new BaseException(FAILED_TO_LOAD_RESULTS);
        }
    }

    public List<Result> getResultSelected(long userIdx) throws BaseException {
        try{
            List<Result> results = resultDTO.getResultSelected(userIdx);
            return results;
        } catch (Exception e){
            throw new BaseException(FAILED_TO_LOAD_RESULTS);
        }
    }

    public Result getTodayResult(long userIdx) throws BaseException {
        try{
            Result result = resultDTO.getTodayFinalResult(userIdx);
            return result;
        } catch (Exception e){
            throw new BaseException(FAILED_TO_LOAD_RESULTS);
        }
    }
}
