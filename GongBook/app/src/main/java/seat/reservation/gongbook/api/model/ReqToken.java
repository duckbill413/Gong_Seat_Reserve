package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.SerializedName;

public class ReqToken {
    @SerializedName("userId")
    String userId;
    @SerializedName("userPwd")
    String userPwd;
    @SerializedName("token")
    String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ReqToken(String userId, String userPwd, String token) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.token = token;
    }
}
