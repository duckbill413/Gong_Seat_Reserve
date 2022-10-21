package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResUserDetail {
    @SerializedName("result")
    @Expose
    private ResResult result;
    @SerializedName("seat")
    @Expose
    private ResSeat seat;
    @SerializedName("user")
    @Expose
    private ResUser user;

    public ResResult getResult() {
        return result;
    }

    public void setResult(ResResult result) {
        this.result = result;
    }

    public ResSeat getSeat() {
        return seat;
    }

    public void setSeat(ResSeat seat) {
        this.seat = seat;
    }

    public ResUser getUser() {
        return user;
    }

    public void setUser(ResUser user) {
        this.user = user;
    }
}
