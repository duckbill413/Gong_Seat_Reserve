package book.seatReservation.database.notification;

import book.seatReservation.database.notification.model.Notification;
import book.seatReservation.database.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationDTO {
    private final NotificationRepository notificationRepository;

    public void updateNotification(long userIdx, boolean pushAlarm, boolean notifyEmail){
        Notification notification = Notification.builder()
                .userIdx(userIdx)
                .pushAlarm(pushAlarm)
                .notifyEmail(notifyEmail)
                .build();
        notificationRepository.save(notification);
    }

    public void pushAlarmOff(long userIdx){
        Optional<Notification> notification = notificationRepository.findById(userIdx);
        Notification notify = notification.orElse(null);
        notify.setPushAlarm(false);

        notificationRepository.save(notify);
    }

    public void pushAlarmOn(long userIdx){
        Optional<Notification> notification = notificationRepository.findById(userIdx);
        Notification notify = notification.orElse(null);
        notify.setPushAlarm(true);

        notificationRepository.save(notify);
    }

    public void notifyEmailOff(long userIdx){
        Optional<Notification> notification = notificationRepository.findById(userIdx);
        Notification notify = notification.orElse(null);
        notify.setNotifyEmail(false);

        notificationRepository.save(notify);
    }

    public void notifyEmailOn(long userIdx){
        Optional<Notification> notification = notificationRepository.findById(userIdx);
        Notification notify = notification.orElse(null);
        notify.setNotifyEmail(true);

        notificationRepository.save(notify);
    }

    public void deleteNotify(long userIdx){
        notificationRepository.deleteById(userIdx);
    }

    public Notification getAlarmActive(long userIdx){
        Optional<Notification> notification = notificationRepository.findById(userIdx);
        return notification.orElse(null);
    }
}
