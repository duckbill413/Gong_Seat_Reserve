package book.seatReservation.database.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqUserData {
    String userId;
    String userPwd;
}
