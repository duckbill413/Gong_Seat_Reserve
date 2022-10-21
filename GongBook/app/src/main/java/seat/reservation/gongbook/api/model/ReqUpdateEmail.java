package seat.reservation.gongbook.api.model;

public class ReqUpdateEmail {
    String userId;
    String userPwd;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public ReqUpdateEmail(String userId, String userPwd, String email) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.email = email;
    }
}
