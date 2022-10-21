package seat.reservation.gongbook;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.lang.reflect.Method;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqUpdateEmail;
import seat.reservation.gongbook.api.model.ReqUpdateGongLogin;
import seat.reservation.gongbook.api.model.ReqUpdateLoginOption;
import seat.reservation.gongbook.api.model.ReqUpdateNaverLogin;
import seat.reservation.gongbook.api.model.ReqUpdatePwd;
import seat.reservation.gongbook.api.model.ReqUserLogin;
import seat.reservation.gongbook.api.model.ResUser;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;

public class SettingsPreferenceFragment extends PreferenceFragmentCompat {
    String userId, userPwd;
    ReqUserLogin reqUserLogin;

    MainActivity mainActivity;
    Context context;
    SharedPreferences mainPreferences;
    SharedPreferences settingPreferences;
    Handler handler = new Handler();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        settingPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        settingPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        mainPreferences = getActivity().getSharedPreferences("gongbook", MODE_PRIVATE);
        settingPreferences = getPreferenceManager().getSharedPreferences();
        userId = mainPreferences.getString("userId", null);
        userPwd = mainPreferences.getString("userPwd", null);
        reqUserLogin = new ReqUserLogin(userId, userPwd);

        EditTextPreference dropUser = findPreference("drop_user");
        if (dropUser != null)
            dropUser.setOnBindEditTextListener(editText -> {
                int color = ContextCompat.getColor(context, android.R.color.holo_red_dark);
                editText.setTextColor(color);
                editText.setHint("탈퇴하시려면 \"회원탈퇴\"를 입력하세요.");
            });

        EditTextPreference password = findPreference("password");
        if (password != null) {
            password.setOnBindEditTextListener(editText -> {
                editText.setText("");
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(new PasswordTransformationMethod());
            });
        }
        EditTextPreference portalPassword = findPreference("portal_password");
        if (portalPassword != null) {
            portalPassword.setOnBindEditTextListener(editText -> {
                editText.setText("");
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(new PasswordTransformationMethod());
            });
        }

        Preference logout = findPreference("logout");
        logout.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(context, LoginActivity.class);
            SharedPreferences.Editor editor = mainPreferences.edit();
            editor.clear();
            editor.commit();
            editor = settingPreferences.edit();
            editor.clear();
            editor.commit();
            startActivity(intent);
            return true;
        });
        Preference portalLogin = findPreference("check_portal_login");
        portalLogin.setOnPreferenceClickListener(preference -> {
            Toast.makeText(context, "이메일 문의해주세요", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setPreference();
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
        if (key.equals("email")) {
            showVerifyEmailDialog();
        } else if (key.equals("password")) {
            showVerifyPwdDialog();
        } else if (key.equals("login_option")) {
            updateLoginOption();
        } else if (key.equals("portal_id")) {
            changePortalLogin();
        } else if (key.equals("portal_password")) {
            changePortalLogin();
        } else if (key.equals("app_push_alarm")) {
            toggleAlertPush();
        } else if (key.equals("email_push_alarm")) {
            toggleAlertEmail();
        } else if (key.equals("drop_user")) {
            dropUserDialog();
        }
    };

    public void changePortalLogin() {
        String loginOption = settingPreferences.getString("login_option", null);
        if (loginOption == null) return;
        String portalId = settingPreferences.getString("portal_id", null);
        String portalPwd = settingPreferences.getString("portal_password", null);
        if (portalId == null && portalPwd == null)
            return;

        Call<BaseResponse<String>> call = null;
        if (loginOption.equals("gong")) {
            ReqUpdateGongLogin reqUpdateGongLogin = new ReqUpdateGongLogin(userId, userPwd);
            if (portalId != null)
                reqUpdateGongLogin.setGongId(portalId);
            if (portalPwd != null)
                reqUpdateGongLogin.setGongPwd(portalPwd);
            call = RetrofitClient.getApiService().updateGongLogin(reqUpdateGongLogin);
        } else if (loginOption.equals("naver")) {
            ReqUpdateNaverLogin reqUpdateNaverLogin = new ReqUpdateNaverLogin(userId, userPwd);
            if (portalId != null)
                reqUpdateNaverLogin.setNaverId(portalId);
            if (portalPwd != null)
                reqUpdateNaverLogin.setNaverPwd(portalPwd);
            call = RetrofitClient.getApiService().updateNaverLogin(reqUpdateNaverLogin);
        }

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "포털 로그인 정보 업데이트 실패", Toast.LENGTH_SHORT).show());
                } else {
                    handler.post(() -> Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });

        SharedPreferences.Editor editor = settingPreferences.edit();
        editor.remove("portal_id");
        editor.remove("portal_password");
        editor.commit();
    }

    public void toggleAlertPush() {
        boolean checked = settingPreferences.getBoolean("app_push_alarm", true);

        Call<BaseResponse<String>> call;
        if (checked == true)
            call = RetrofitClient.getApiService().startAlertPush(reqUserLogin);
        else call = RetrofitClient.getApiService().stopAlertPush(reqUserLogin);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess())
                    handler.post(() -> Toast.makeText(context, "알림 설정 변경 실패", Toast.LENGTH_SHORT).show());
                else
                    handler.post(() -> Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void toggleAlertEmail() {
        boolean checked = settingPreferences.getBoolean("email_push_alarm", true);

        Call<BaseResponse<String>> call = null;
        if (checked == true)
            call = RetrofitClient.getApiService().startAlertEmail(reqUserLogin);
        else call = RetrofitClient.getApiService().stopAlertEmail(reqUserLogin);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess())
                    handler.post(() -> Toast.makeText(context, "알림 설정 변경 실패", Toast.LENGTH_SHORT).show());
                else
                    handler.post(() -> Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void checkEmail(String password, String newEmail) {
        String email = settingPreferences.getString("email", null);
        if (email == null) return;

        Call<BaseResponse<String>> call = RetrofitClient.getApiService().checkEmail(email);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null)
                    return;
                if (result.getIsSuccess()) {
                    updateEmail(password, newEmail);
                } else
                    handler.post(() -> Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void updateEmail(String password, String newEmail) {
        ReqUpdateEmail reqUpdateEmail = new ReqUpdateEmail(userId, password, newEmail);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().updateEmail(reqUpdateEmail);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "이메일 업데이트 실패", Toast.LENGTH_SHORT).show());
                } else
                    handler.post(() -> Toast.makeText(context, "이메일 업데이트 성공", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void updatePwd(String curPassword, String newPassword) {
        ReqUpdatePwd reqUpdatePwd = new ReqUpdatePwd(userId, curPassword, newPassword);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().updatePwd(reqUpdatePwd);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    if (result == null)
                        handler.post(() -> Toast.makeText(context, "비밀번호 업데이트 실패", Toast.LENGTH_SHORT).show());
                    else
                        handler.post(() -> Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show());
                } else {
                    handler.post(() -> Toast.makeText(context, "비밀번호 업데이트 성공", Toast.LENGTH_SHORT).show());
                    SharedPreferences.Editor editor = settingPreferences.edit();
                    editor.remove("password");
                    editor.commit();

                    editor = mainPreferences.edit();
                    editor.putString("userPwd", newPassword);
                    editor.commit();
                    userPwd = newPassword;
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void updateLoginOption() {
        String loginOption = settingPreferences.getString("login_option", null);
        if (loginOption == null) return;
        ReqUpdateLoginOption reqUpdateLoginOption = new ReqUpdateLoginOption(userId, userPwd, loginOption);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().updateLoginOption(reqUpdateLoginOption);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess())
                    handler.post(() -> Toast.makeText(context, "로그인 옵션 업데이트 실패", Toast.LENGTH_SHORT).show());
                else
                    handler.post(() -> Toast.makeText(context, "로그인 옵션 업데이트 성공", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void setPreference() {
        Call<BaseResponse<ResUser>> call = RetrofitClient.getApiService().getUser(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<ResUser>>() {
            @Override
            public void onResponse(Call<BaseResponse<ResUser>> call, Response<BaseResponse<ResUser>> response) {
                BaseResponse<ResUser> result = response.body();
                if (result == null || !result.getIsSuccess())
                    handler.post(() -> Toast.makeText(context, "유저 데이터 로딩 실패", Toast.LENGTH_SHORT).show());
                else {
                    EditTextPreference email = findPreference("email");
                    ListPreference loginOption = findPreference("login_option");
                    email.setText(result.getResult().getEmail());
                    switch (result.getResult().getLoginOption()) {
                        case "gong":
                            loginOption.setValueIndex(0);
                            break;
                        case "naver":
                            loginOption.setValueIndex(1);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ResUser>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void showVerifyPwdDialog() {
        String password = settingPreferences.getString("password", null);
        if (password == null) return;
        EditTextPreference pwdText = findPreference("password");
        pwdText.setText(null);

        final EditText editText = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("비밀번호 변경");
        builder.setMessage("기존 비밀번호를 입력해주세요.");
        builder.setView(editText);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTransformationMethod(new PasswordTransformationMethod());

        builder.setPositiveButton("변경",
                (dialog, which) -> updatePwd(editText.getText().toString(), password));
        builder.setNegativeButton("취소", (dialog, which) -> {
        });
        builder.create().show();
    }

    public void showVerifyEmailDialog() {
        String email = settingPreferences.getString("email", null);
        if (email == null) return;

        final EditText editText = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("이메일 변경");
        builder.setMessage("기존 비밀번호를 입력해주세요");
        builder.setView(editText);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTransformationMethod(new PasswordTransformationMethod());

        builder.setPositiveButton("변경", (dialog, which) -> {
            checkEmail(editText.getText().toString(), email);
        });
        builder.setNegativeButton("취소", (dialog, which) -> {
        });
        builder.create().show();
    }

    public void dropUserDialog() {
        String dropText = settingPreferences.getString("drop_user", null);
        if (dropText == null) return;
        if (!dropText.equals("회원탈퇴")) {
            handler.post(() -> Toast.makeText(context, "탈퇴하시려면 '회원탈퇴'를 입력하세요.", Toast.LENGTH_LONG).show());
            EditTextPreference dropUserText = findPreference("drop_user");
            dropUserText.setText(null);
            return;
        }

        final EditText editText = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("회원 탈퇴");
        builder.setMessage("비밀번호를 입력해주세요.");
        builder.setView(editText);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTransformationMethod(new PasswordTransformationMethod());

        builder.setPositiveButton("탈퇴",
                (dialog, which) -> dropUser(editText.getText().toString()));
        builder.setNegativeButton("취소", ((dialog, which) -> {
        }));
        builder.create().show();
    }

    public void dropUser(String password) {
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, password);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().deleteUser(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null)
                    handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
                else {
                    handler.post(() -> Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(context, LoginActivity.class);
                    SharedPreferences.Editor editor = mainPreferences.edit();
                    editor.clear();
                    editor.commit();
                    editor = settingPreferences.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }
}