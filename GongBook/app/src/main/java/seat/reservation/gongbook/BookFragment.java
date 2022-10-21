package seat.reservation.gongbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqUserLogin;
import seat.reservation.gongbook.api.model.ResResult;
import seat.reservation.gongbook.api.model.ResSeat;
import seat.reservation.gongbook.api.model.ResUser;
import seat.reservation.gongbook.api.model.ResUserDetail;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;
import seat.reservation.gongbook.loading.LoadingDialog;

public class BookFragment extends Fragment {
    public static final int REQUEST_CODE_MENU = 101;
    MainActivity mainActivity;
    Context context;
    ViewGroup rootView;

    String userId, userPwd;
    Handler handler = new Handler();
    Switch workSwitch;
    LoadingDialog loadingDialog;
    AlertDialog bookingDialog;

    TextView txt_seat_body, txt_booked_body;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bookingDialog != null) {
            bookingDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
        initial();
        loadingDialog = mainActivity.loadingDialog;
        // 사용자 정보 로드
        userId = getArguments().getString("userId");
        userPwd = getArguments().getString("userPwd");

        /** 화면 로딩 **/
        loadingDialog.show();
        loadGongInfo(context);


        /** 좌석 예약, 변경, 취소 버튼 **/
        TextView txt_seat = rootView.findViewById(R.id.txtView_seat_body);
        Button bookButton = rootView.findViewById(R.id.book_seat);
        Button changeButton = rootView.findViewById(R.id.change_seat);
        Button cancelButton = rootView.findViewById(R.id.cancel_seat);
        bookButton.setOnClickListener(view -> {
            bookingDialog = showDialog(context, "자리예약", txt_seat.getText().toString());
            bookingDialog.show();
        });
        changeButton.setOnClickListener(view -> {
            bookingDialog = showDialog(context, "자리변경", txt_seat.getText().toString());
            bookingDialog.show();
        });
        cancelButton.setOnClickListener(view -> {
            bookingDialog = showDialog(context, "자리취소", txt_seat.getText().toString());
            bookingDialog.show();
        });
        /** 작동 스위치 버튼 **/
        workSwitch.setOnClickListener((view) -> {
            toggleSwitch(context, workSwitch.isChecked());
        });

        // 좌석 배치
        TextView booked_value = rootView.findViewById(R.id.txtView_booked_body);
        booked_value.setOnClickListener(v -> {
            mainActivity.replaceFragment();
        });
        return rootView;
    }


    public void initial() {
        workSwitch = rootView.findViewById(R.id.work_switch);
        txt_seat_body = rootView.findViewById(R.id.txtView_seat_body);
        txt_booked_body = rootView.findViewById(R.id.txtView_booked_body);
    }
//    /**
//     * SetActivity 이동
//     **/
//    private void moveSubActivity() {
//        Intent intent = new Intent(MainActivity.this, SetActivity.class);
//        intent.putExtra("userId", userId);
//        intent.putExtra("userPwd", userPwd);
////        startActivityResult.launch(intent);
//        startActivity(intent);
//    }

//    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == Activity.RESULT_OK) {
////                    Log.d(TAG, "MainActivity back");
//                }
//            });
    /** 공단기 좌석 예약 **/
    private void BookSeat(Context context){
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().bookSeat(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                loadingDialog.dismiss();
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "자리 예약 실패", Toast.LENGTH_SHORT).show());
                } else {
                    handler.post(() -> {
                        txt_booked_body.setText(result.getResult());
                        Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                loadingDialog.dismiss();
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }
    /** 공단기 좌석 변경 **/
    private void ChangeSeat(Context context){
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().changeSeat(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                loadingDialog.dismiss();
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "자리 변경 실패", Toast.LENGTH_SHORT).show());
                } else {
                    handler.post(() -> {
                        txt_booked_body.setText(result.getResult());
                        Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                loadingDialog.dismiss();
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }
    /**
     * 공단기 좌석 취소
     **/
    private void CancelSeat(Context context) {
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().cancelSeat(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                loadingDialog.dismiss();
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "자리 취소 실패", Toast.LENGTH_SHORT).show());
                } else {
                    handler.post(() -> {
                        txt_booked_body.setText(result.getResult());
                        Toast.makeText(context, result.getResult(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                loadingDialog.dismiss();
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * 화면 로딩
     **/
    private void loadGongInfo(Context context) {
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        Call<BaseResponse<ResUserDetail>> call = RetrofitClient.getApiService().getUserDetail(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<ResUserDetail>>() {
            @Override
            public void onResponse(Call<BaseResponse<ResUserDetail>> call, Response<BaseResponse<ResUserDetail>> response) {
                loadingDialog.dismiss();
                BaseResponse result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "스케줄링 로딩 실패", Toast.LENGTH_SHORT).show());
                }
                assert result != null : "로딩 실패";
                ResUserDetail userDetail = (ResUserDetail) result.getResult();
                ResUser user = userDetail.getUser();
                ResSeat seat = userDetail.getSeat();
                ResResult resResult = userDetail.getResult();
                Boolean isOperation = user.getScheduling();

                String seatValue = null;
                String bookedSeat = null;
                if (seat != null)
                    seatValue = String.format("%s행 %d열 좌석 선택됨", seat.getX(), seat.getY());
                if (resResult != null)
                    bookedSeat = resResult.getMessage();

                String finalSeatValue = seatValue;
                String finalBookedSeat = bookedSeat;
                handler.post(() -> {
                    if (finalSeatValue != null)
                        txt_seat_body.setText(finalSeatValue);
                    else
                        txt_seat_body.setText("희망 좌석 없음");
                    if (finalBookedSeat != null)
                        txt_booked_body.setText(finalBookedSeat);
                    else
                        txt_booked_body.setText("예약된 좌석 없음");
                    if (isOperation)
                        workSwitch.setChecked(true);
                    else
                        workSwitch.setChecked(false);
                });
            }

            @Override
            public void onFailure(Call<BaseResponse<ResUserDetail>> call, Throwable t) {
                loadingDialog.dismiss();
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * 서버 스케줄링 작동 토글 쓰레드
     **/
    private void toggleSwitch(Context context, @NonNull Boolean scheduling) {
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        Call<BaseResponse<String>> call;
        if (scheduling)
            call = RetrofitClient.getApiService().startScheduling(reqUserLogin);
        else
            call = RetrofitClient.getApiService().stopScheduling(reqUserLogin);

        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "작동 상태 변경 실패", Toast.LENGTH_SHORT).show());
                } else {
                    String message = result.getResult();
                    if (message.contains("Started"))
                        workSwitch.setChecked(true);
                    else
                        workSwitch.setChecked(false);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
                if (scheduling)
                    workSwitch.setChecked(true);
                else
                    workSwitch.setChecked(false);
            }
        });
    }

    private AlertDialog showDialog(Context context, String title, String seat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        // 좌석 선택 X Toast Message
        if (seat.equals("정보없음")) {
            builder.setMessage("좌석이 선택되지 않았습니다.\n좌석을 선택해 주세요");
            builder.setPositiveButton("확인", ((dialog, which) -> {
            }));
            return builder.create();
        }
        String seatMessage = String.format("지정좌석: %s", seat);
        String commonMessage = title + " 하시겠습니까?";
        builder.setMessage(String.format("%s\n%s", seatMessage, commonMessage));
        builder.setNegativeButton("아니요", (dialog, which) -> {
        });

        builder.setPositiveButton("실행", (dialog, which) -> {
            loadingDialog.show();
            if (title.equals("자리예약")) {
                BookSeat(context);
            } else if (title.equals("자리변경")) {
                ChangeSeat(context);
            } else {
                CancelSeat(context);
            }
        });
        AlertDialog ad = builder.create();
        return ad;
    }
}