package book.seatReservation.book.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"first", "second", "third"})
public class Triple<X, Y, Z>{
    @JsonProperty(value = "수업 정보")
    X first;
    @JsonProperty(value = "예약 정보")
    Y second;
    @JsonProperty(value = "좌석 정보")
    Z third;


    public Triple(X first, Y second, Z third){
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
