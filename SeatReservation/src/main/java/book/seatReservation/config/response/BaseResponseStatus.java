package book.seatReservation.config.response;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    INACTIVE_ACCOUNT(false, 2011, "비활성화된 유저입니다."),
    INVALID_USER_ACCOUNT(false, 2012, "유저 정보를 확인해 주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EMPTY_NAME(false, 2018, "이름을 입력해주세요."),
    POST_USERS_EMPTY_NICKNAME(false, 2019, "닉네임을 입력해주세요."),
    POST_USERS_EXISTS_NICKNAME(false, 2020, "중복된 닉네임 입니다."),
    POST_USERS_EMPTY_PASSWORD(false, 2021, "비밀번호를 입력해주세요"),
    POST_USERS_EMPTY_PHONE(false, 2022, "전화번호를 입력해주세요"),
    POST_USERS_INVALID_PHONE(false, 2023, "전화번호 형식을 확인해주세요 {000-0000-0000}"),
    POST_USERS_EXISTS_PHONE(false, 2024, "중복된 전화번호 입니다."),

    // post
    POST_POSTS_EMPTY_CONTENTS(false, 2200, "POST 작성 내용이 없습니다."),
    POST_POSTS_INVALID_CONTENTS(false, 2201, "게시물 작성 글자수(450)를 초과하였습니다"),
    POST_POSTS_EMPTY_IMG(false, 2202, "POST 작성 이미지가 없습니다."),
    POSTS_EMPTY_POST_ID(false, 2203, "POST ID가 올바르지 않습니다"),
    POSTS_EMPTY_USER_POST(false, 2204, "유저가 작성한 게시물이 없습니다."),

    // post patch
    MODIFY_FAIL_POST(false, 2501, "게시물 수정에 실패하였습니다."),
    DELETE_FAIL_POST(false, 2502, "게시물 삭제에 실패하였습니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    DUPLICATED_USERID(false, 3012, "중복된 아이디 입니다."),
    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일 입니다."),

    ALREADY_SAME_STATUS(false, 3030, "같은 Status 입니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    /**
     * 5000: WebDriver 오류
     */
    WEBDRIVER_NOT_FOUND(false, 5001, "WebDriver 설정이 완료되지 않았습니다."),
    WEBDRIVER_REFRESH_FAIL(false, 5002, "WebDriver 새로고침 실패"),
    SEAT_ALREADY_BOOKED(false, 5003, "좌석이 이미 예약되어있습니다."),
    CHECK_AGREE_BOX_FAIL(false, 5004, "동의 체크 박스 체크에 실패 하였습니다."),
    FAILED_TO_BOOK_RESERVED(false, 5005, "지정 좌석 예약에 실패하였습니다."),
    FAILED_TO_BOOK(false, 5006, "빈 자리 예약에 실패하였습니다."),
    FAILED_TO_CHANGE_SEAT(false, 5007, "자리 변경에 실패하였습니다."),
    FAILED_TO_CANCEL_SEAT(false, 5008, "자리 취소에 실패하였습니다."),
    FAILED_TO_LOGIN(false,5009,"로그인에 실패 하였습니다."),
    FAILED_TO_LOAD_TEXT(false, 5010, "정보를 불러올수 없습니다."),
    FAILED_TO_LOAD_SEAT(false, 5011, "좌석 정보를 불러올수 없습니다."),
    FAILED_TO_INITIALIZE(false, 5012, "WebDriver 초기화 실패"),
    RUNTIME_EXCEPTION_OCCUR(false, 5013, "실행중 런타임 오류 발생"),
    FAILED_TO_LOAD_SCHEDULE(false, 5014, "예약 가능한 스케줄 확인 불가"),

    /**
     * 6000: Database 오류
     */
    UPDATE_TOKEN_FAILED(false, 6001, "유저의 토큰 정보 업데이트에 실패하였습니다."),
    FAILED_TO_LOAD_TOKEN(false, 6002, "유저의 토큰 정보를 불러올수 없습니다."),
    UPDATE_GONGLOGIN_DATA_FAILED(false, 6003, "유저의 공단기 로그인 데이터 업데이트 실패"),
    FAILED_TO_LOAD_GONGLOGIN_DATA(false, 6004, "유저의 공단기 로그인 데이터 불러오기 실패"),
    FAILED_TO_DELETE_GONGLOGIN_DATA(false, 6005, "유저의 공단기 로그인 데이터 삭제 실패"),
    UPDATE_NAVERLOGIN_DATA_FAILED(false, 6006, "유저의 공다닉 로그인 데이터 업데이트 실패"),
    FAILED_TO_LOAD_NAVERLOGIN_DATA(false, 6007, "유저의 네이버 로그인 데이터 불러오기 실패"),
    FAILED_TO_DELETE_NAVERLOGIN_DATA(false, 6008, "유저의 네이버 로그인 데이터 삭제 실패"),
    UPDATE_SEAT_FAILED(false, 6009, "유저의 좌석 정보 업데이트 실패"),
    FAILED_TO_DELETE_SEAT(false, 6010, "유저의 좌석 정보 삭제 실패"),
    UPDATE_USER_FAILED(false, 6011, "유저 정보 업데이트 실패"),
    FAILED_TO_LOAD_USER(false, 6012, "유저 정보 로드 실패"),
    FAILED_TO_DELETE_USER(false, 6013, "유저 정보 삭제 실패"),
    UPDATE_USER_EMAIL_FAILED(false, 6014, "유저 이메일 업데이트 실패"),
    UPDATE_USER_PWD_FAILED(false, 6015, "유저 비밀번호 업데이트 실패"),
    UPDATE_RESULT_FAILED(false, 6016, "결과 메시지 업데이트 실패"),
    FAILED_TO_LOAD_RESULTS(false, 6017, "유저의 결과 메시지 로드 실패"),
    FAILED_TO_DELETE_TOKEN(false, 6018, "유저의 토큰 정보 삭제 실패"),
    UPDATE_USER_SCHEDULE_FAILED(false, 6019, "유저 스케줄링 수정 실패"),
    FAILED_TO_VERIFIED_RESULT(false, 6020, "유저 결과 TIME 인증 실패"),
    FAILED_TO_SEND_EMAIL(false, 6021, "이메일 발송 실패"),
    FAILED_TO_SEND_PUSH(false, 6022, "푸쉬 알림 발송 실패"),
    FAILED_TO_UPDATE_NOTIFICATION(false, 6023, "알림 업데이트 실패"),
    FAILED_TO_TURNOFF_PUSHALARM(false, 6024, "푸쉬 알람 종료 실패"),
    FAILED_TO_TURNON_PUSHALARM(false, 6025, "푸쉬 알람 실행 실패"),
    FAILED_TO_TURNOFF_NOTIFY_EMAIL(false, 6026, "이메일 알람 종료 실패"),
    FAILED_TO_TURNON_NOTIFY_EMAIL(false, 6027, "이메일 알람 실행 실패"),
    FAILED_TO_DELETE_NOTIFY(false, 6028, "알람 설정 삭제 실패"),
    FAILED_TO_LOAD_ALARM(false, 6029, "알람 실행 상태 불러오기 실패");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
