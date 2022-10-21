package book.seatReservation.fcm.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqPushMessage {
    String userId;
    String userPwd;
    String title;
    String message;

    public ReqPushMessage(String userId, String userPwd){
        this.userId = userId;
        this.userPwd = userPwd;
    }
}
