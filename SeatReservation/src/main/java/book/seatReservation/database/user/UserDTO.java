package book.seatReservation.database.user;

import book.seatReservation.database.repository.UserRepository;
import book.seatReservation.database.user.model.ReqChangeEmail;
import book.seatReservation.database.user.model.ReqChangePwd;
import book.seatReservation.database.user.model.ReqUser;
import book.seatReservation.database.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDTO {
    private final UserRepository userRepository;

    public void registerUser(ReqUser reqUser) {
        User user = User.builder()
                .userId(reqUser.getUserId())
                .userPwd(reqUser.getUserPwd())
                .email(reqUser.getEmail())
                .scheduling(false)
                .loginOption(reqUser.getLoginOption())
                .build();
        userRepository.save(user);
    }

    public void updateUser(long userIdx, ReqUser reqUser){
        User user = User.builder()
                .userIdx(userIdx)
                .userId(reqUser.getUserId())
                .userPwd(reqUser.getUserPwd())
                .email(reqUser.getEmail())
                .loginOption(reqUser.getLoginOption()).build();
        userRepository.save(user);
    }

    public void updateUserStatus(User user) {
        userRepository.save(user);
    }

    public User getUser(String userId, String userPwd) {
        Optional<User> user = userRepository.findByUserIdAndUserPwd(userId, userPwd);
        return user.orElse(null);
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public boolean checkUser(String userId, String userPwd){
        boolean check = userRepository.existsByUserIdAndUserPwd(userId, userPwd);
        return check;
    }

    public boolean checkUserId(String userId){
        boolean check = userRepository.existsByUserId(userId);
        return check;
    }

    public boolean checkUserEmail(String email){
        boolean check = userRepository.existsByEmail(email);
        return check;
    }
}
