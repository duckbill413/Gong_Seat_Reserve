package seat.reservation.gongbook.api.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import seat.reservation.gongbook.api.model.ReqNewSeatInfo;
import seat.reservation.gongbook.api.model.ReqToken;
import seat.reservation.gongbook.api.model.ReqUpdateEmail;
import seat.reservation.gongbook.api.model.ReqUpdateGongLogin;
import seat.reservation.gongbook.api.model.ReqUpdateLoginOption;
import seat.reservation.gongbook.api.model.ReqUpdateNaverLogin;
import seat.reservation.gongbook.api.model.ReqUpdatePwd;
import seat.reservation.gongbook.api.model.ReqUserRegister;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqUserLogin;
import seat.reservation.gongbook.api.model.ResResult;
import seat.reservation.gongbook.api.model.ResSeat;
import seat.reservation.gongbook.api.model.ResUser;
import seat.reservation.gongbook.api.model.ResUserDetail;

public interface RetrofitAPI {
    /** 로그인 액티비티 **/
    @Headers("Content-Type: application/json")
    @POST("user/check/user")
    Call<BaseResponse<String>> checkingUser(@Body ReqUserLogin reqUserLogin);
    /** 회원가입 액티비티 **/
    @Headers("Content-Type: application/json")
    @POST("user/update/register")
    Call<BaseResponse<String>> registerUser(@Body ReqUserRegister reqUserRegister);

    /** 메인 액티비티 **/
    @Headers("Content-Type: application/json")
    @POST("/api/gong/book")
    Call<BaseResponse<String>> bookSeat(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/api/gong/change")
    Call<BaseResponse<String>> changeSeat(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/api/gong/cancel")
    Call<BaseResponse<String>> cancelSeat(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/api/gong/hear")
    Call<BaseResponse<ResUserDetail>> getUserDetail(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/user/schedule/start")
    Call<BaseResponse<String>> startScheduling(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/user/schedule/stop")
    Call<BaseResponse<String>> stopScheduling(@Body ReqUserLogin reqUserLogin);

    /** 셋업 액티비티 **/
    @Headers("Content-Type: application/json")
    @POST("/seat/update")
    Call<BaseResponse<ResSeat>> setNewSeat(@Body ReqNewSeatInfo reqNewSeatInfo);

    @Headers("Content-Type: application/json")
    @POST("/seat/delete")
    Call<BaseResponse<String>> initialSeat(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/api/gong/booked")
    Call<BaseResponse<ResResult>> reloadBookedSeat(@Body ReqUserLogin reqUserLogin);

    /** 토큰 서비스 **/
    @Headers("Content-Type: application/json")
    @POST("/token/update")
    Call<BaseResponse<String>> updateToken(@Body ReqToken reqToken);

    /** 셋팅 서비스 **/
    @Headers("Content-Type: application/json")
    @POST("/user/load")
    Call<BaseResponse<ResUser>> getUser(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @GET("/user/check/email")
    Call<BaseResponse<String>> checkEmail(@Query("email") String email);

    @Headers("Content-Type: application/json")
    @POST("/user/update/email")
    Call<BaseResponse<String>> updateEmail(@Body ReqUpdateEmail reqUpdateEmail);

    @Headers("Content-Type: application/json")
    @POST("/user/update/pwd")
    Call<BaseResponse<String>> updatePwd(@Body ReqUpdatePwd reqUpdatePwd);

    @Headers("Content-Type: application/json")
    @POST("/user/update/login/option")
    Call<BaseResponse<String>> updateLoginOption(@Body ReqUpdateLoginOption reqUpdateLoginOption);

    @Headers("Content-Type: application/json")
    @POST("/naver/login/update")
    Call<BaseResponse<String>> updateNaverLogin(@Body ReqUpdateNaverLogin reqUpdateNaverLogin);

    @Headers("Content-Type: application/json")
    @POST("/gong/login/update")
    Call<BaseResponse<String>> updateGongLogin(@Body ReqUpdateGongLogin reqUpdateGongLogin);

    @Headers("Content-Type: application/json")
    @POST("/notification/push/start")
    Call<BaseResponse<String>> startAlertPush(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/notification/push/stop")
    Call<BaseResponse<String>> stopAlertPush(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/notification/email/start")
    Call<BaseResponse<String>> startAlertEmail(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/notification/email/stop")
    Call<BaseResponse<String>> stopAlertEmail(@Body ReqUserLogin reqUserLogin);

    @Headers("Content-Type: application/json")
    @POST("/user/delete")
    Call<BaseResponse<String>> deleteUser(@Body ReqUserLogin reqUserLogin);

    /** 알림 서비스 **/
    @Headers("Content-Type: application/json")
    @POST("/result/load/selected")
    Call<BaseResponse<List<ResResult>>> loadResults(@Body ReqUserLogin reqUserLogin);

    /** 마지막 좌석 데이터 불러오기 **/
    @Headers("Content-Type: application/json")
    @POST("/result/load/one")
    Call<BaseResponse<ResResult>> loadOneResult(@Body ReqUserLogin reqUserLogin);
}
