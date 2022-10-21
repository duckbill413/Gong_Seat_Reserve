package book.seatReservation.database.usertoken.model;

import book.seatReservation.database.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class UserToken extends BaseEntity {
    @Id
    @JsonIgnore
    private long userIdx;
    @NotNull
    private String token;

    @Builder
    public UserToken(long userIdx, String token){
        this.userIdx = userIdx;
        this.token = token;
    }
}
