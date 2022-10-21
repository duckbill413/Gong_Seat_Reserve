package book.seatReservation.database.user;

import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.user.model.ReqUser;
import book.seatReservation.database.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDTO userDTO;

    public void registerUser(ReqUser reqUser) throws BaseException {
        try {
            userDTO.registerUser(reqUser);
        } catch (Exception exception) {
            throw new BaseException(UPDATE_USER_FAILED);
        }
    }

    public void updateUser(ReqUser reqUser) throws BaseException {
        try {
            User user = userDTO.getUser(reqUser.getUserId(), reqUser.getUserPwd());
            long userIdx = user.getIdx();
            userDTO.updateUser(userIdx, reqUser);
        } catch (Exception exception) {
            throw new BaseException(UPDATE_USER_FAILED);
        }
    }

    public void updateUserStatus(User user) throws BaseException {
        try{
            userDTO.updateUserStatus(user);
        } catch (Exception exception){
            throw new BaseException(UPDATE_USER_FAILED);
        }
    }

    public User getUser(String userId, String userPwd) throws BaseException {
        try {
            User user = userDTO.getUser(userId, userPwd);
            if (user==null)
                throw new BaseException(FAILED_TO_LOAD_USER);

            return user;
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOAD_USER);
        }
    }

    public List<User> getAllUser() throws BaseException {
        try{
            List<User> users = userDTO.getAllUser();
            if (users==null)
                throw new BaseException(FAILED_TO_LOAD_USER);

            return users;
        } catch (Exception exception){
            throw new BaseException(FAILED_TO_LOAD_USER);
        }
    }

    public void deleteUser(User user) throws BaseException {
        try {
            userDTO.deleteUser(user);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }
    }

    public void updateUserEmail(User user) throws BaseException {
        try {
            userDTO.updateUserStatus(user);
        } catch (Exception exception) {
            throw new BaseException(UPDATE_USER_EMAIL_FAILED);
        }
    }

    public void updateUserPwd(User user) throws BaseException {
        try{
            userDTO.updateUserStatus(user);
        } catch (Exception exception){
            throw new BaseException(UPDATE_USER_PWD_FAILED);
        }
    }

    public void updateUserLoginOption(User user) throws BaseException {
        try {
            userDTO.updateUserStatus(user);
        } catch (Exception exception) {
            throw new BaseException(UPDATE_USER_EMAIL_FAILED);
        }
    }

    public void updateUserSchedule(User user) throws BaseException {
        try{
            userDTO.updateUserStatus(user);
        } catch (Exception exception){
            throw new BaseException(UPDATE_USER_SCHEDULE_FAILED);
        }
    }

    public void checkUser(String userId, String userPwd) throws BaseException {
        try{
            boolean check = userDTO.checkUser(userId, userPwd);
            if(!check)
                throw new BaseException(INVALID_USER_ACCOUNT);
        } catch (Exception exception){
            throw new BaseException(INVALID_USER_ACCOUNT);
        }
    }

    public void checkUserId(String userId) throws BaseException {
        try{
            boolean check = userDTO.checkUserId(userId);
            if (check)
                throw new BaseException(DUPLICATED_USERID);
        } catch (Exception exception){
            throw new BaseException(DUPLICATED_USERID);
        }
    }

    public void checkUserEmail(String email) throws BaseException {
        try{
            boolean check = userDTO.checkUserEmail(email);
            if (check)
                throw new BaseException(DUPLICATED_EMAIL);
        } catch (Exception exception){
            throw new BaseException(DUPLICATED_EMAIL);
        }
    }
}
