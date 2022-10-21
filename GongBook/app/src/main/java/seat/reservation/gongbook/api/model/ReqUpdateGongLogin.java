package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqUpdateGongLogin {
    @SerializedName(value = "userId")
    @Expose
    String userId;
    @SerializedName(value = "userPwd")
    @Expose
    String userPwd;
    @SerializedName(value = "gongId")
    @Expose
    String gongId;
    @SerializedName(value = "gongPwd")
    @Expose
    String gongPwd;

    public ReqUpdateGongLogin(String userId, String userPwd, String gongId, String gongPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.gongId = gongId;
        this.gongPwd = gongPwd;
    }

    public ReqUpdateGongLogin(String userId, String userPwd) {
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

    public String getGongId() {
        return gongId;
    }

    public void setGongId(String gongId) {
        this.gongId = gongId;
    }

    public String getGongPwd() {
        return gongPwd;
    }

    public void setGongPwd(String gongPwd) {
        this.gongPwd = gongPwd;
    }
}
