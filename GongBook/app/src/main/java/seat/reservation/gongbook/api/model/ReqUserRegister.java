package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.SerializedName;

public class ReqUserRegister {
    @SerializedName(value = "userId")
    String userId;
    @SerializedName(value = "userPwd")
    String userPwd;
    @SerializedName(value = "email")
    String email;
    @SerializedName(value = "loginOption")
    String loginOption;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ReqUserRegister(String userId, String userPwd, String email) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.email = email;
    }
}
