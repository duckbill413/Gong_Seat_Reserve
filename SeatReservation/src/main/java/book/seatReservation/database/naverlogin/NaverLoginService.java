package book.seatReservation.database.naverlogin;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.naverlogin.model.NaverLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class NaverLoginService {
    private final NaverLoginDTO naverLoginDTO;

    public void updateNaverLoginData(long userIdx, String naverId, String naverPwd) throws BaseException {
        try{
            naverLoginDTO.updateNaverLoginData(userIdx, naverId, naverPwd);
        } catch (Exception exception){
            throw new BaseException(UPDATE_NAVERLOGIN_DATA_FAILED);
        }
    }

    public NaverLogin getNaverLoginData(long userIdx) throws BaseException {
        try{
            NaverLogin naverLogin = naverLoginDTO.getNaverLoginData(userIdx);
            return naverLogin;
        } catch (Exception exception){
            throw new BaseException(FAILED_TO_LOAD_NAVERLOGIN_DATA);
        }
    }

    public void deleteNaverLoginData(long userIdx) throws BaseException {
        try{
            naverLoginDTO.deleteNaverLoginData(userIdx);
        } catch (Exception exception){
            throw new BaseException(FAILED_TO_DELETE_NAVERLOGIN_DATA);
        }
    }
}
