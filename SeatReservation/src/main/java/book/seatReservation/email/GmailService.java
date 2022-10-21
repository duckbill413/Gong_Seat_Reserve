package book.seatReservation.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GmailService {
    private final JavaMailSender javaMailSender;

    public void send(String email, String title, String content) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email);
        smm.setSubject(title);
        smm.setText(content);
        javaMailSender.send(smm);
    }
}