package book.seatReservation.database.result.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqResult {
    private String userId;
    private String userPwd;
    private String message;
}
