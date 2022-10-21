package book.seatReservation.database.user.model;


import book.seatReservation.database.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"idx", "userId", "email", "scheduling", "loginOption"})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;
    @NotNull
    @Column(length = 50)
    private String userId;
    @NotNull
    @JsonIgnore
    private String userPwd;
    @NotNull
    @Email
    @Column(length = 100)
    private String email;
    @NotNull
    private boolean scheduling;
    @NotNull
    private String loginOption;

    @Builder
    public User(long userIdx, String userId, String userPwd, String email, boolean scheduling, String loginOption){
        this.idx = userIdx;
        this.userId = userId;
        this.userPwd = userPwd;
        this.email = email;
        this.scheduling = scheduling;
        this.loginOption = loginOption;
    }
}
