package book.seatReservation.database.notification.model;

import book.seatReservation.database.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import kotlin.BuilderInference;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"userIdx", "pushAlarm", "notifyEmail"})
public class Notification extends BaseEntity {
    @Id
    @JsonIgnore
    private long userIdx;
    @NotNull
    private Boolean pushAlarm;
    @NotNull
    private Boolean notifyEmail;

}
