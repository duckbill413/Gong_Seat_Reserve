package book.seatReservation.database.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReqUpdateNotify {
    String userId;
    String userPwd;
    Boolean pushAlarm;
    Boolean notifyEmail;
}
