package seat.reservation.gongbook;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import retrofit2.Call;
import retrofit2.Callback;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqUserLogin;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;

public class LoginActivity extends AppCompatActivity {
    private EditText editId, editPwd;
    private Button btn_login, btn_register;
    private Handler handler = new Handler();
    private SharedPreferences preferences;
    private RequestQueue requestQueue;
    private final String TAG = "duckbill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editId = findViewById(R.id.et_id);
        editPwd = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        preferences = getSharedPreferences("gongbook", MODE_PRIVATE);


        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(this);
        // 로그인 버튼 클릭시
        btn_login.setOnClickListener((view) -> {
            if (editId.getText().length() == 0) {
                Toast.makeText(this.getApplicationContext(), "ID를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editPwd.getText().length() == 0) {
                Toast.makeText(this.getApplicationContext(), "패스워드를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            ReqUserLogin reqUserLogin = new ReqUserLogin(editId.getText().toString(), editPwd.getText().toString());
            callUserCheck(this, reqUserLogin);
        });
        // 자동 로그인 구현
        String rememberId = preferences.getString("userId", null);
        String rememberPwd = preferences.getString("userPwd", null);
        if (rememberId != null && rememberPwd != null) {
            editId.setText(rememberId);
            editPwd.setText(rememberPwd);
            ReqUserLogin reqUserLogin = new ReqUserLogin(editId.getText().toString(), editPwd.getText().toString());
            callUserCheck(this, reqUserLogin);
        }

        btn_register.setOnClickListener(view -> moveToRegister());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    private void moveToMainActivity(String userId, String userPwd) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userPwd", userPwd);
        startActivity(intent);
    }

    private void moveToRegister() {
        Intent intent = new Intent(this, ApplyActivity.class);
        startActivity(intent);
    }

    public void callUserCheck(Context context, ReqUserLogin reqUserLogin) {
        //Retrofit 호출
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().checkingUser(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, retrofit2.Response<BaseResponse<String>> response) {
                BaseResponse user = response.body();
                if (user == null)
                    return;
                if (user.getIsSuccess()) {
                    SharedPreferences.Editor editor = preferences.edit();
                    String userId = editId.getText().toString();
                    String userPwd = editPwd.getText().toString();
                    editor.putString("userId", userId);
                    editor.putString("userPwd", userPwd);
                    editor.commit();
                    handler.post(() -> Toast.makeText(context, "로그인 완료!", Toast.LENGTH_SHORT).show());
                    moveToMainActivity(userId, userPwd);
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
}