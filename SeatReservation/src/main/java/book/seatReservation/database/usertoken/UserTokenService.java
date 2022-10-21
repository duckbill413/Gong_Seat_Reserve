package book.seatReservation.database.usertoken;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.usertoken.model.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final UserTokenDTO userTokenDTO;

    public void updateUserToken(long userIdx, String token) throws BaseException {
        try{
            userTokenDTO.updateUserToken(userIdx, token);
        }
        catch (Exception exception){
            throw new BaseException(UPDATE_TOKEN_FAILED);
        }
    }

    public UserToken getUserToken(long userIdx) throws BaseException {
        try{
            UserToken userToken = userTokenDTO.getUserToken(userIdx);
            return userToken;
        } catch (Exception exception){
            throw new BaseException(FAILED_TO_LOAD_TOKEN);
        }
    }

    public void deleteUserToken(long userIdx) throws BaseException {
        try{
            userTokenDTO.deleteUserToken(userIdx);
        } catch (Exception exception){
            throw new BaseException(FAILED_TO_DELETE_TOKEN);
        }
    }
}
