package book.seatReservation.database.naverlogin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReqNaverLogin {
    String userId;
    String userPwd;
    String naverId;
    String naverPwd;
}
