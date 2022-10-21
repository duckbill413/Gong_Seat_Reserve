package book.seatReservation.book;

import book.seatReservation.book.model.Pair;
import book.seatReservation.book.model.TodayClass;
import book.seatReservation.config.response.BaseException;
import book.seatReservation.database.seat.model.Seat;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static book.seatReservation.config.response.BaseResponseStatus.*;

@Component
public class SeleniumAct {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    String chromedriverPath;

    public SeleniumAct() {
        System.setProperty("java.awt.headless", "false");
        WebDriverManager webDriverManager = WebDriverManager.chromedriver();
        webDriverManager.setup();
        this.chromedriverPath = webDriverManager.getDownloadedDriverPath();
        System.out.println("WebDriverManager.getDownloadedDriverPath(): " + this.chromedriverPath);
        System.out.println("WebDriverManager.getDownloadedDriverVersion(): " + webDriverManager.getDownloadedDriverVersion());
    }

    public String refreshPage(WebDriver driver) {
        Duration intervalTime = Duration.ofSeconds(1);
        WebDriverWait interval = new WebDriverWait(driver, intervalTime);
        String class_url = "https://gong.conects.com/gong/academy/classroom";
        driver.get(class_url);
        while (true) {
            try {
                interval.until(ExpectedConditions.alertIsPresent());
                Alert alert = driver.switchTo().alert();
                if (alert != null) {
                    String alertMessage = alert.getText();
                    alert.accept();
                    return alertMessage;
                }
            } catch (NoAlertPresentException noAlert) {
                break;
            } catch (Exception e) {
                break;
            }
        }
        return null;
    }

    public String bookSeat(WebDriver driver, Seat seat, int option) throws BaseException {
        // option: 0) for book 1) for scheduling booking
        if (driver == null)
            throw new BaseException(WEBDRIVER_NOT_FOUND);

        Duration waitTime = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        Duration intervalTime = Duration.ofSeconds(1);
        WebDriverWait interval = new WebDriverWait(driver, intervalTime);

        String refresh = refreshPage(driver);
        if (refresh != null)
            return refresh;

        /** 좌석 기예약 확인 **/
        try {
            String seatMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]"))).getText();
            if (seatMessage.contains("좌석이 예약되었습니다.")) {
                return "좌석이 이미 예약 되어있습니다.\n" + seatMessage;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(SEAT_ALREADY_BOOKED);
        }
        /** 좌석 예약 체크 박스 체크 **/
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"note_check\"]"))).click(); // 동의 체크
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(CHECK_AGREE_BOX_FAIL);
        }

        /** 자리 선택 및 예약 **/
        long[] dx = {0, 0, 0, 1, 1, 1, -1, -1, -1, 0, 0, 1, 1, -1, -1, 2, 2, 2, 2, 2, -2, -2, -2, -2, -2};
        long[] dy = {0, 1, -1, 0, 1, -1, 0, 1, -1, 2, -2, 2, -2, 2, -2, 0, 1, -1, 2, -2, 0, 1, -1, 2, -2};

        if (seat == null) // 좌석 정보 로드 실패
            throw new BaseException(FAILED_TO_LOAD_SEAT);

        String message = "";
        long seat_x = seat.getLongX(); // 지정 좌석 x축
        long seat_y = seat.getY(); // 지정 좌석 y축
        for (int i = 0; i < dx.length; i++) {
            long next_x = seat_x + dx[i];
            long next_y = seat_y + dy[i];

            if (i > 0) { // 지정 좌석 예약 실패
                message = String.format("대체자리 예약(%c행 %d열 예약 실패)\n", seat.getX(), seat.getY());
            }

            // 예약할 자리 선택
            try {
                String location = String.format("//*[@id=\"seat_layout_section\"]/div/div[%d]/a[%d]", next_x, next_y);
                interval.until(ExpectedConditions.presenceOfElementLocated(By.xpath(location))).click();
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"reserve_button_section\"]/div[2]/a")));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
            } catch (UnhandledAlertException e) {
                alertSkipping(driver);
            } catch (TimeoutException e) {
                continue; // 선택 불가 좌석
            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException(FAILED_TO_BOOK);
            }
            // 예약 결과 확인
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
            } catch (UnhandledAlertException e) {
                alertSkipping(driver);
            }
            try {
                message += wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]"))).getText();
                if (message.contains("좌석이 예약되었습니다.")) {
                    return message;
                } else if (message.contains("좌석이 선택되었습니다.")) {
                    String class_url = "https://gong.conects.com/gong/academy/classroom";
                    driver.get(class_url);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
                    message += wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]"))).getText();
                    if (message.contains("좌석이 예약되었습니다.")) { // 좌석 선택 완료시
                        return message;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (option == 0)
                break;
        }
        // 좌석 예약 실패
        throw new BaseException(FAILED_TO_BOOK);
    }

    public String changeSeat(WebDriver driver, Seat seat) throws BaseException {
        if (driver == null)
            throw new BaseException(WEBDRIVER_NOT_FOUND);
        String location = String.format("//*[@id=\"seat_layout_section\"]/div/div[%d]/a[%d]", seat.getLongX(), seat.getY());
        Duration waitTime = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, waitTime);

        String refresh = refreshPage(driver);
        if (refresh != null)
            return refresh;

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(location))).click();

            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"reserve_button_section\"]/div[2]/a")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
        } catch (UnhandledAlertException e) {
            alertSkipping(driver);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_CHANGE_SEAT);
        }

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
        } catch (UnhandledAlertException e) {
            alertSkipping(driver);
        }

        String message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]"))).getText();
        char x = seat.getX();
        long y = seat.getY();
        String expectResult = String.format("%c행 %d열 좌석이 예약되었습니다.", x, y);
        if (!message.equals(expectResult))
            throw new BaseException(FAILED_TO_CHANGE_SEAT);
        return message;
    }

    public String cancelSeat(WebDriver driver) throws BaseException {
        if (driver == null)
            throw new BaseException(WEBDRIVER_NOT_FOUND);
        Duration waitTime = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, waitTime);

        String refresh = refreshPage(driver);
        if (refresh != null)
            return refresh;

        try {
            // 예약 취소 버튼 클릭
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"reserve_button_section\"]/div[3]/a"))).click();
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"reserve_button_section\"]/div[3]/a")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
        } catch (UnhandledAlertException e) {
            alertSkipping(driver);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_CANCEL_SEAT);
        }
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
        } catch (UnhandledAlertException e) {
            alertSkipping(driver);
        }

        String result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]"))).getText();
        String message;
        if (result.equals("좌석을 선택해주세요."))
            message = "좌석 예약 취소 완료";
        else {
            throw new BaseException(FAILED_TO_CANCEL_SEAT);
        }
        return message;

    }

    public String bookedSeat(WebDriver driver) throws BaseException {
        if (driver == null)
            throw new BaseException(WEBDRIVER_NOT_FOUND);
        Duration waitTime = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        String refresh = refreshPage(driver);
        if (refresh != null)
            return refresh;

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
        } catch (UnhandledAlertException e) {
            alertSkipping(driver);
        }
        String message = null;
        try {
            message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]"))).getText();
        } catch (UnhandledAlertException e) {
            alertSkipping(driver);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOAD_SEAT);
        }
        return message;
    }

    @Deprecated
    public void SeatScreenShot(WebDriver driver) throws BaseException, IOException {
        Duration waitTime = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        Duration intervalTime = Duration.ofNanos(200);
        WebDriverWait interval = new WebDriverWait(driver, intervalTime);
        WebElement ele = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"container\"]/div[3]/div[2]/div/div[2]")));
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);

        Point point = ele.getLocation();
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), 1200, 1000);
        ImageIO.write(eleScreenshot, "png", screenshot);
        File screenshotLocation = new File(String.format("C:\\test\\%s", LocalDate.now() + ".png"));
//        File screenshotLocation = new File(String.format("\\home\\ubuntu\\%s", LocalDate.now()+".png"));
        FileUtils.copyFile(screenshot, screenshotLocation);
    }


    public Pair<WebDriver, String> naverLogin(String id, String pwd) throws BaseException {
        System.setProperty("webdriver.chrome.driver", this.chromedriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.addArguments("disable-gpu"); // applicable to windows os only
        options.addArguments("no-sandbox"); // Bypass OS security model
        options.addArguments("disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("lang=ko");
//        options.addArguments("headless"); // !headless!

        WebDriver driver = new ChromeDriver(options);
        Duration waitTime = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        Duration intervalTime = Duration.ofSeconds(2);
        WebDriverWait interval = new WebDriverWait(driver, intervalTime);

        try {
            String url = "https://member.conects.com/member/global/login?skin_key=GONG&redirect_url=https://gong.conects.com/gong/main/academy";
            driver.get(url);
            // 공단기 로그인
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"member_login_frm\"]/div/div[1]/ul/li[1]/a"))).click();

            // 네이버 로그인
            try {
                // 자바스크립트 로그인 테스트
                WebElement idBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"id\"]")));
                WebElement pwdBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"pw\"]")));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value=arguments[1]", idBox, id);
                js.executeScript("arguments[0].value=arguments[1]", pwdBox, pwd);
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"log.login\"]"))).click();
            } catch (Exception e) {
                throw new BaseException(FAILED_TO_LOGIN);
            }
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
        try {
            wait.until(ExpectedConditions.urlMatches("https://gong.conects.com/gong/main/academy"));
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOGIN);
        }
        String cur_url = driver.getCurrentUrl();
        // 로그인 실패 처리
        if (!cur_url.equals("https://gong.conects.com/gong/main/academy")) {
            driver.quit();
            throw new BaseException(FAILED_TO_LOGIN);
        } else {
            return new Pair<>(driver, "로그인 완료");
        }
    }

    public void quitDriver(WebDriver webDriver) {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    public TodayClass getReservedInfo(WebDriver driver) throws BaseException {
        if (driver == null)
            throw new BaseException(WEBDRIVER_NOT_FOUND);
        if (!driver.getCurrentUrl().equals("https://gong.conects.com/gong/academy/classroom"))
            throw new BaseException(FAILED_TO_LOAD_SEAT);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("#schedule_idx")));
            String doc = driver.getPageSource();
            Document document = Jsoup.parse(doc);
            Elements classes = document.select("#schedule_idx option");
            Elements classrooms = document.select("#info_idx option");
            Elements classInfos = document.selectXpath("//*[@id=\"container\"]/div[3]/div[1]/div/div[2]/ul/li/strong");

            String todayClass = classes.get(0).text().trim().replace("\n", "");
            String todayClassInfo = classrooms.get(0).text().trim().replace("\n", "");
            String todayTime = classInfos.get(0).text().trim().replace("※", "-").replace("□", "-").replace("\n\n", "\n");
            TodayClass retTodayClass = new TodayClass();
            if (todayClass != null)
                retTodayClass.setTodayClass(todayClass);
            if (todayClassInfo != null)
                retTodayClass.setClassLocation(todayClassInfo);
            if (todayTime != null)
                retTodayClass.setClassInfo(todayTime);
            return retTodayClass;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(FAILED_TO_LOAD_TEXT);
        }
    }

    public List<List<Integer>> seatPage(WebDriver driver) throws BaseException {
        if (driver == null)
            throw new BaseException(WEBDRIVER_NOT_FOUND);
        try {
            Duration waitTime = Duration.ofSeconds(10);
            WebDriverWait wait = new WebDriverWait(driver, waitTime);
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
            } catch (UnhandledAlertException e) {
                alertSkipping(driver);
            }
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"reserve_txt\"]")));
            String doc = driver.getPageSource();
            Document document = Jsoup.parse(doc);
            Elements elements = document.select(".table_group");

            List<List<Integer>> table = new ArrayList<>();
            for (var e : elements) {
                Elements seats = e.getElementsByTag("a");
                List<Integer> t = new ArrayList<>();
                for (var s : seats) {
                    String attr = s.attr("class");
                    Integer integer = null;
                    switch (attr) {
                        case "seat_no":
                        case "seat": // 예약 가능좌석
                            integer = 1;
                            break;
                        case "off":  // 이미 예약된 좌석
                            integer = 2;
                            break;
                        case "active": // 내 좌석
                            integer = 3;
                            break;
                        case "way": // 길
                            integer = 4;
                            break;
                        case "block": // 예약 불가 좌석
                            integer = 5;
                            break;
                    }
                    t.add(integer);
                }
                table.add(t);
            }
            return table;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(FAILED_TO_LOAD_SEAT);
        }
    }

    private void alertSkipping(WebDriver driver) {
        Duration intervalTime = Duration.ofNanos(500);
        WebDriverWait interval = new WebDriverWait(driver, intervalTime);
        while (true) {
            try {
                interval.until(ExpectedConditions.alertIsPresent());
                Alert alert = driver.switchTo().alert();
                if (alert != null)
                    alert.accept();
            } catch (NoAlertPresentException noAlert) {
                break;
            } catch (TimeoutException timeoutException) {
                break;
            } catch (Exception exception) {
                exception.printStackTrace();
                driver.get(driver.getCurrentUrl());
                break;
            }
        }
    }
}
