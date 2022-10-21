package seat.reservation.gongbook.api.model;

public class ReqUpdatePwd {
    String userId;
    String userPwd;
    String password;

    public ReqUpdatePwd(String userId, String userPwd, String password) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
