package book.seatReservation.database.naverlogin.model;

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
public class NaverLogin extends BaseEntity {
    @Id
    @JsonIgnore
    private long userIdx;
    @NotNull
    @Column(length = 50)
    private String naverId;
    @NotNull
    @JsonIgnore
    private String naverPwd;

    @Builder
    public NaverLogin(long userIdx, String naverId, String naverPwd){
        this.userIdx = userIdx;
        this.naverId = naverId;
        this.naverPwd = naverPwd;
    }
}
