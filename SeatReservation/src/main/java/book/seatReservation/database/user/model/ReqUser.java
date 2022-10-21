package book.seatReservation.database.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqUser {
    private String userId;
    private String userPwd;
    private String email;
    private String loginOption;
}
