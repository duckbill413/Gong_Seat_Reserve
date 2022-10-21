package seat.reservation.gongbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seat.reservation.gongbook.api.model.ReqUpdateGongLogin;
import seat.reservation.gongbook.api.model.ReqUpdateNaverLogin;
import seat.reservation.gongbook.api.model.ReqUserRegister;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;

public class ApplyActivity extends AppCompatActivity {
    private EditText et_id, et_pwd, et_email, et_sub_id, et_sub_pwd;
    private Button apply, cancel;
    private RadioGroup radioGroup;
    private RequestQueue requestQueue;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(this);

        et_id = findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        et_sub_id = findViewById(R.id.et_sub_id);
        et_sub_pwd = findViewById(R.id.et_sub_pwd);
        radioGroup = findViewById(R.id.radio_selector);
        apply = findViewById(R.id.btn_register);
        cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(view -> moveToLoginActivity());
        apply.setOnClickListener(view -> {
            if (!checkParamsInitiated(this))
                return;
            callToRegister(this);
        });
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean checkParamsInitiated(Context context) {
        String id = et_id.getText().toString();
        String pwd = et_pwd.getText().toString();
        String email = et_email.getText().toString();
        String sub_id = et_sub_id.getText().toString();
        String sub_pwd = et_sub_pwd.getText().toString();
        boolean radio_group = true;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_naver:
            case R.id.radio_gong:
                break;
            default:
                radio_group = false;
        }
        if (id.length() < 1) {
            handler.post(() -> Toast.makeText(context, "유저 아이디를 확인해주세요.", Toast.LENGTH_SHORT).show());
            return false;
        }
        if (pwd.length() < 1) {
            handler.post(() -> Toast.makeText(context, "유저 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show());
            return false;
        }
        if (email.length() < 1) {
            handler.post(() -> Toast.makeText(context, "유저 이메일을 확인해주세요.", Toast.LENGTH_SHORT).show());
            return false;
        } else if (email.length() > 0) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                handler.post(() -> Toast.makeText(context, "이메일 형식을 확인해주세요.", Toast.LENGTH_SHORT).show());
                return false;
            }
        }
        if (!radio_group) {
            handler.post(() -> Toast.makeText(context, "로그인 옵션을 선택해주세요.", Toast.LENGTH_SHORT).show());
            return false;
        }
        if (sub_id.length() < 1) {
            handler.post(() -> Toast.makeText(context, "옵션 아이디를 확인해주세요.", Toast.LENGTH_SHORT).show());
            return false;
        }
        if (sub_pwd.length() < 1) {
            handler.post(() -> Toast.makeText(context, "옵션 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show());
        }

        return true;
    }

    private void callToRegister(Context context) {
        ReqUserRegister reqUserRegister = new ReqUserRegister(
                et_id.getText().toString(),
                et_pwd.getText().toString(),
                et_email.getText().toString()
        );
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_naver:
                reqUserRegister.setLoginOption("naver");
                break;
            case R.id.radio_gong:
                reqUserRegister.setLoginOption("gong");
                break;
        }
        //Retrofit 호출
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().registerUser(reqUserRegister);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, retrofit2.Response<BaseResponse<String>> response) {
                BaseResponse user = response.body();
                if (user == null) {
                    handler.post(() -> Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show());
                    return;
                }
                if (user.getIsSuccess()) {
                    callToSaveSubLogin(context, reqUserRegister.getLoginOption()); // 서브 로그인 저장
                } else {
                    handler.post(() -> Toast.makeText(context, user.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 연결 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void callToSaveSubLogin(Context context, String loginOption) {
        String userId = et_id.getText().toString();
        String userPwd = et_pwd.getText().toString();
        String subId = et_sub_id.getText().toString();
        String subPwd = et_sub_pwd.getText().toString();

        Call<BaseResponse<String>> call = null;
        if (loginOption.equals("naver")) {
            ReqUpdateNaverLogin reqUpdateNaverLogin = new ReqUpdateNaverLogin(
                    userId, userPwd, subId, subPwd
            );
            call = RetrofitClient.getApiService().updateNaverLogin(reqUpdateNaverLogin);
        } else if (loginOption.equals("gong")) {
            ReqUpdateGongLogin reqUpdateGongLogin = new ReqUpdateGongLogin(
                    userId, userPwd, subId, subPwd
            );
            call = RetrofitClient.getApiService().updateGongLogin(reqUpdateGongLogin);
        }
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse subLogin = response.body();
                if (subLogin == null) {
                    handler.post(() -> Toast.makeText(context, "회원 가입 성공\n포털 ID, PWD를 수정해주세요!", Toast.LENGTH_LONG).show());
                    moveToLoginActivity();
                    return;
                }
                if (subLogin.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "회원가입 완료\n로그인해 주세요~", Toast.LENGTH_LONG).show());
                    moveToLoginActivity();
                } else {
                    handler.post(() -> Toast.makeText(context, subLogin.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 연결 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }
}