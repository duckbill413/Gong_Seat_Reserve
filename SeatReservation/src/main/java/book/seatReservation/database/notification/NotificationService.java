package book.seatReservation.database.notification;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationDTO notificationDTO;

    public void updateNotification(long userIdx, boolean pushAlarm, boolean notifyEmail) throws BaseException {
        try{
            notificationDTO.updateNotification(userIdx, pushAlarm, notifyEmail);
        } catch (Exception e){
            throw new BaseException(FAILED_TO_UPDATE_NOTIFICATION);
        }
    }

    public void pushAlarmOff(long userIdx) throws BaseException {
        try{
            notificationDTO.pushAlarmOff(userIdx);
        } catch (Exception e){
            throw new BaseException(FAILED_TO_TURNOFF_PUSHALARM);
        }
    }

    public void pushAlarmOn(long userIdx) throws BaseException {
        try{
            notificationDTO.pushAlarmOn(userIdx);
        } catch (Exception e){
            throw new BaseException(FAILED_TO_TURNON_PUSHALARM);
        }
    }

    public void notifyEmailOff(long userIdx) throws BaseException {
        try{
            notificationDTO.notifyEmailOff(userIdx);
        } catch (Exception e){
            throw new BaseException(FAILED_TO_TURNOFF_NOTIFY_EMAIL);
        }
    }

    public void notifyEmailOn(long userIdx) throws BaseException {
        try{
            notificationDTO.notifyEmailOn(userIdx);
        } catch (Exception e){
            throw new BaseException(FAILED_TO_TURNON_NOTIFY_EMAIL);
        }
    }

    public void deleteNotifyData(long userIdx) throws BaseException {
        try{
            notificationDTO.deleteNotify(userIdx);
        } catch (Exception e){
            throw new BaseException(FAILED_TO_DELETE_NOTIFY);
        }
    }

    public Notification getAlarmActive(long userIdx) throws BaseException {
        try{
            Notification notification = notificationDTO.getAlarmActive(userIdx);
            if (notification == null) throw new BaseException(FAILED_TO_LOAD_ALARM);
            return notification;
        } catch (Exception e){
            throw new BaseException(FAILED_TO_LOAD_ALARM);
        }
    }
}
