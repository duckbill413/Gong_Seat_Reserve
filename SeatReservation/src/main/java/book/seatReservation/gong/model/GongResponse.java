package book.seatReservation.gong.model;

import book.seatReservation.database.result.model.Result;
import book.seatReservation.database.seat.model.Seat;
import book.seatReservation.database.user.model.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"user", "seat", "result"})
public class GongResponse {
    User user;
    Seat seat;
    Result result;
}
