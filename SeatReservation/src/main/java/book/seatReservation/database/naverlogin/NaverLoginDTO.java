package book.seatReservation.database.naverlogin;

import book.seatReservation.database.naverlogin.model.NaverLogin;
        import book.seatReservation.database.repository.NaverLoginRepository;
        import lombok.RequiredArgsConstructor;
        import org.springframework.stereotype.Repository;

        import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NaverLoginDTO {
    private final NaverLoginRepository naverLoginRepository;

    public void updateNaverLoginData(long userIdx, String naverId, String naverPwd) {
        NaverLogin naverLogin = naverLoginRepository.findById(userIdx).orElse(null);
        if (naverId != null)
            naverLogin.setNaverId(naverId);
        if (naverPwd != null)
            naverLogin.setNaverPwd(naverPwd);
        naverLoginRepository.save(naverLogin);
    }

    public NaverLogin getNaverLoginData(long userIdx) {
        Optional<NaverLogin> naverLogin = naverLoginRepository.findById(userIdx);
        return naverLogin.orElse(null);
    }

    public void deleteNaverLoginData(long userIdx) {
        naverLoginRepository.deleteById(userIdx);
    }
}
