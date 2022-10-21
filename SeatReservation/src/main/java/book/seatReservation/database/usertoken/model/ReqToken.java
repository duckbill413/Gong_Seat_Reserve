package book.seatReservation.database.usertoken.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqToken {
    String userId;
    String userPwd;
    String token;
}
