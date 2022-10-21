package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqNewSeatInfo {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userPwd")
    @Expose
    private String userPwd;
    @SerializedName("x")
    @Expose
    private String x;
    @SerializedName("y")
    @Expose
    private Long y;

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

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public ReqNewSeatInfo(String userId, String userPwd, String x, Long y) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.x = x;
        this.y = y;
    }
}
