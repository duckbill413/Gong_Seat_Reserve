package book.seatReservation.database.result.model;

import book.seatReservation.database.BaseEntity;
import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"idx", "todayClass", "classLocation", "classInfo", "message", "mapping"})
public class Result extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;
    @NotNull
    @JsonIgnore
    private long userIdx;
    @JsonProperty(value = "수업명")
    private String todayClass;
    @JsonProperty(value = "수업 장소")
    private String classLocation;
    @JsonProperty(value = "수업 정보")
    private String classInfo;
    @JsonProperty(value = "예약 결과")
    @NotNull
    private String message;
    private String mapping;
    @Builder
    public Result(long userIdx, String todayClass, String classLocation,
                  String classInfo, String message, String mapping){
        this.userIdx = userIdx;
        this.todayClass = todayClass;
        this.classLocation = classLocation;
        this.classInfo = classInfo;
        this.message = message;
        this.mapping = mapping;
    }
}
