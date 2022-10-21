package book.seatReservation.database.seat.model;

import book.seatReservation.database.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"x", "y"})
public class Seat extends BaseEntity {
    @Id
    @JsonIgnore
    private long userIdx;
    @NotNull
    char x;
    @NotNull
    long y;
    long classNum;
    String time;

    @Builder
    public Seat(long userIdx, char x, long y, long classNum, String time){
        this.userIdx = userIdx;
        String tmp = String.valueOf(x).toUpperCase();
        this.x = tmp.charAt(0);
        this.y = y;
        this.classNum = classNum;
        this.time = time;
    }
    @JsonIgnore
    public long getLongX(){
        long res = this.x - 'A' + 1;
        return res;
    }
}
