package book.seatReservation.database.gonglogin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqGongLogin {
    String userId;
    String userPwd;
    String gongId;
    String gongPwd;
}
