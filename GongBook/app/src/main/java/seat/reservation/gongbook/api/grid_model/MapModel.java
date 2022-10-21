package seat.reservation.gongbook.api.grid_model;

import java.nio.channels.FileChannel;

public class MapModel {
    int code;
    String x;
    String y;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public MapModel(){}

    public MapModel(int code, String x, String y) {
        this.code = code;
        this.x = x;
        this.y = y;
    }
}
