package seat.reservation.gongbook.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResResult {
    @SerializedName("idx")
    @Expose
    private Long idx;
    @SerializedName("수업명")
    @Expose
    private String todayClass;
    @SerializedName("수업 장소")
    @Expose
    private String classLocation;
    @SerializedName("수업 정보")
    @Expose
    private String classInfo;
    @SerializedName("예약 결과")
    @Expose
    private String message;
    @SerializedName("mapping")
    @Expose
    private String mapping;

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getTodayClass() {
        return todayClass;
    }

    public void setTodayClass(String todayClass) {
        this.todayClass = todayClass;
    }

    public String getClassLocation() {
        return classLocation;
    }

    public void setClassLocation(String classLocation) {
        this.classLocation = classLocation;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
