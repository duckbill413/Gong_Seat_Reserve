package book.seatReservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class SeatReservationApplication {

    @PostConstruct
    public void setTimezone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("서버 시작 시간: " + new Date());
    }
    public static void main(String[] args) {
        SpringApplication.run(SeatReservationApplication.class, args);
    }
}
