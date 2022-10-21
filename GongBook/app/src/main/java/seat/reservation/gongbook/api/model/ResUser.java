package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResUser {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("idx")
    @Expose
    private Long idx;
    @SerializedName("loginOption")
    @Expose
    private String loginOption;
    @SerializedName("scheduling")
    @Expose
    private Boolean scheduling;
    @SerializedName("userId")
    @Expose
    private String userId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getLoginOption() {
        return loginOption;
    }

    public void setLoginOption(String loginOption) {
        this.loginOption = loginOption;
    }

    public Boolean getScheduling() {
        return scheduling;
    }

    public void setScheduling(Boolean scheduling) {
        this.scheduling = scheduling;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
