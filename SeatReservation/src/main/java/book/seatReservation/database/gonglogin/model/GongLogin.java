package book.seatReservation.database.gonglogin.model;

import book.seatReservation.database.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class GongLogin extends BaseEntity {
    @Id
    @JsonIgnore
    private long userIdx;
    @NotNull
    @Column(length = 50)
    private String gongId;
    @NotNull
    @JsonIgnore
    private String gongPwd;

    @Builder
    public GongLogin(long userIdx, String gongId, String gongPwd){
        this.userIdx = userIdx;
        this.gongId = gongId;
        this.gongPwd = gongPwd;
    }
}
