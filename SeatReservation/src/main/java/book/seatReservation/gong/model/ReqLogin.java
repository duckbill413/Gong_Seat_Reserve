package book.seatReservation.gong.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqLogin {
    String userId;
    String userPwd;
}
