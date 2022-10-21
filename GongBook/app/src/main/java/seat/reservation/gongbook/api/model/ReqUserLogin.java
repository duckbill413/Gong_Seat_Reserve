package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.SerializedName;

public class ReqUserLogin {
    @SerializedName("userId")
    String userId;
    @SerializedName("userPwd")
    String userPwd;

    public ReqUserLogin(String userId, String userPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
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

    @Override
    public String toString() {
        return "ReqLogin{" +
                "userId='" + userId + '\'' +
                ", userPwd='" + userPwd + '\'' +
                '}';
    }
}
