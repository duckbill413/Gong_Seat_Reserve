package book.seatReservation.database.result.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqResultAllArgs {
    private String userId;
    private String userPwd;
    private String todayClass;
    private String classLocation;
    private String classTime;
    private String message;
}
