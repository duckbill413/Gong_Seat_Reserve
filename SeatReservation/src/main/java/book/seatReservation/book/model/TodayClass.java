package book.seatReservation.book.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"todayClass", "classLocation", "classInfo"})
public class TodayClass {
    @JsonProperty(value = "수업명")
    String todayClass;
    @JsonProperty(value = "수업 장소")
    String classLocation;
    @JsonProperty(value = "수업 정보")
    String classInfo;
    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n", todayClass, classLocation, classInfo);
    }

    @Builder
    public TodayClass(String todayClass, String classLocation, String classInfo){
        this.todayClass = todayClass;
        this.classLocation = classLocation;
        this.classInfo = classInfo;
    }
}
