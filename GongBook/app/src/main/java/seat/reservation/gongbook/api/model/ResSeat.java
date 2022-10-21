package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResSeat {
    @SerializedName("x")
    @Expose
    private String x;
    @SerializedName("y")
    @Expose
    private Long y;

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

    public ResSeat(String x, Long y) {
        this.x = x;
        this.y = y;
    }
}
