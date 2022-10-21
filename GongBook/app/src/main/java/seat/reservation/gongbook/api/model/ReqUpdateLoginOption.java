package seat.reservation.gongbook.api.model;

public class ReqUpdateLoginOption {
    String userId;
    String userPwd;
    String loginOption;

    public ReqUpdateLoginOption(String userId, String userPwd, String loginOption) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.loginOption = loginOption;
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

    public String getLoginOption() {
        return loginOption;
    }

    public void setLoginOption(String loginOption) {
        this.loginOption = loginOption;
    }
}
