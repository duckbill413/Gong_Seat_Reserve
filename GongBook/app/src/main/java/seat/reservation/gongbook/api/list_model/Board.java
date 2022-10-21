package seat.reservation.gongbook.api.list_model;

public class Board {
    private String title;
    private String contents;
    private String location;
    private String info;
    private String time;
    private String map;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Board(String title, String contents, String time) {
        this.title = title;
        this.contents = contents;
        this.time = time;
    }
    public Board(String title, String contents, String location, String info, String time) {
        this.title = title;
        this.contents = contents;
        this.location = location;
        this.info = info;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Board(){}
}
