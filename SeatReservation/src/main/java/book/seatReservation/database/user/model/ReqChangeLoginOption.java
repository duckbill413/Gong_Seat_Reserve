package book.seatReservation.database.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqChangeLoginOption {
    String userId;
    String userPwd;
    String loginOption;
}
