package book.seatReservation.database.gonglogin;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.gonglogin.model.GongLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class GongLoginService {
    private final GongLoginDTO gongLoginDTO;

    public void updateGongLoginData(long userIdx, String gongId, String gongPwd) throws BaseException {
        try{
            gongLoginDTO.updateGongLoginData(userIdx, gongId, gongPwd);
        }
        catch (Exception e){
            throw new BaseException(UPDATE_GONGLOGIN_DATA_FAILED);
        }
    }

    public GongLogin getGongLoginData(long userIdx) throws BaseException {
        try{
            GongLogin gongLogin = gongLoginDTO.getGongLoginData(userIdx);
            return gongLogin;
        }
        catch (Exception e){
            throw new BaseException(FAILED_TO_LOAD_GONGLOGIN_DATA);
        }
    }

    public void deleteGongLoginData(long userIdx) throws BaseException {
        try{
            gongLoginDTO.deleteGongLoginData(userIdx);
        } catch (Exception e){
            throw new BaseException(FAILED_TO_DELETE_GONGLOGIN_DATA);
        }
    }
}
