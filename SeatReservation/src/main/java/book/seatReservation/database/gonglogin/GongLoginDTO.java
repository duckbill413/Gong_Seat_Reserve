package book.seatReservation.database.gonglogin;

import book.seatReservation.database.gonglogin.model.GongLogin;
import book.seatReservation.database.repository.GongLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GongLoginDTO {
    private final GongLoginRepository gongLoginRepository;

    public void updateGongLoginData(long userIdx, String gongId, String gongPwd){
        GongLogin gongLogin = gongLoginRepository.findById(userIdx).orElse(null);
        if (gongId != null)
            gongLogin.setGongId(gongId);
        if (gongPwd != null)
            gongLogin.setGongPwd(gongPwd);
        gongLoginRepository.save(gongLogin);
    }

    public GongLogin getGongLoginData(long userIdx){
        Optional<GongLogin> gongLogin = gongLoginRepository.findById(userIdx);
        return gongLogin.orElse(null);
    }

    public void deleteGongLoginData(long userIdx){
        gongLoginRepository.deleteById(userIdx);
    }
}
