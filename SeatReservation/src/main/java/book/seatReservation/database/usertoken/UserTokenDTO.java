package book.seatReservation.database.usertoken;

import book.seatReservation.database.repository.TokenRepository;
import book.seatReservation.database.usertoken.model.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserTokenDTO {
    private final TokenRepository tokenRepository;
    /** 유저의 안드로이드 Token 정보 업데이트 **/
    public void updateUserToken(long userIdx, String token){
        UserToken userToken = UserToken.builder()
                .userIdx(userIdx)
                .token(token)
                .build();
        tokenRepository.save(userToken);
    }
    /** 유저의 안드로이드 Token 정보 로드 **/
    public UserToken getUserToken(long userIdx){
        Optional<UserToken> userToken = tokenRepository.findById(userIdx);
        return userToken.orElse(null);
    }

    public void deleteUserToken(long userIdx){
        tokenRepository.deleteById(userIdx);
    }
}
