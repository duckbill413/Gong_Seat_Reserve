package book.seatReservation.database.seat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqSeat {
    String userId;
    String userPwd;
    char x;
    long y;
}
