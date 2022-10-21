package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqUpdateNaverLogin {
    @SerializedName(value = "userId")
    @Expose
    String userId;
    @SerializedName(value = "userPwd")
    @Expose
    String userPwd;
    @SerializedName(value = "naverId")
    @Expose
    String naverId;
    @SerializedName(value = "naverPwd")
    @Expose
    String naverPwd;

    public ReqUpdateNaverLogin(String userId, String userPwd, String naverId, String naverPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.naverId = naverId;
        this.naverPwd = naverPwd;
    }

    public ReqUpdateNaverLogin(String userId, String userPwd) {
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

    public String getNaverId() {
        return naverId;
    }

    public void setNaverId(String naverId) {
        this.naverId = naverId;
    }

    public String getNaverPwd() {
        return naverPwd;
    }

    public void setNaverPwd(String naverPwd) {
        this.naverPwd = naverPwd;
    }
}
