package seat.reservation.gongbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqNewSeatInfo;
import seat.reservation.gongbook.api.model.ReqUserLogin;
import seat.reservation.gongbook.api.model.ResResult;
import seat.reservation.gongbook.api.model.ResSeat;
import seat.reservation.gongbook.api.model.ResUser;
import seat.reservation.gongbook.api.model.ResUserDetail;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;
import seat.reservation.gongbook.loading.LoadingDialog;

public class SeatSetFragment extends Fragment {
    MainActivity mainActivity;
    Context context;
    ViewGroup rootView;

    String userId, userPwd;
    TextView id, email, seatMessage;
    TextView cur_x, cur_y;
    EditText next_x, next_y;
    Handler handler = new Handler();
    LoadingDialog loadingDialog;

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_set, container, false);
        // 사용자 정보 로드
        userId = getArguments().getString("userId");
        userPwd = getArguments().getString("userPwd");
        // 객체 초기화
        initial();
        // 화면 데이터 불러오기
        loadingDialog.show();
        loadGongInfo(context);

        /** 좌석 정보 변경 버튼(실제적인 변경은 X)**/
        Button changeSeat = rootView.findViewById(R.id.seat_change_button);
        changeSeat.setOnClickListener(view -> {
            if (next_x.getText().length() == 0 || next_y.getText().length() == 0) {
                Toast toast = Toast.makeText(context, R.string.check_seat_val, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                String x = next_x.getText().toString().toUpperCase();
                Long y = Long.parseLong(next_y.getText().toString());
                setNewSeat(context, x, y);
            }
        });
        /** 좌석 정보 초기화 **/
        Button initButton = rootView.findViewById(R.id.seat_init_button);
        initButton.setOnClickListener(view -> {
            initialSeat(context);
        });
        /** 화면 새로고침 버튼 **/
        ImageButton refreshButton = rootView.findViewById(R.id.btn_refresh);
        refreshButton.setOnClickListener(view -> fragmentRefresh());
        /** 좌석 정보 재로딩 버튼 **/
        ImageButton reloadSeatsButton = rootView.findViewById(R.id.reload_seats);
        reloadSeatsButton.setOnClickListener(view -> {
            loadingDialog.show();
            reloadSeatInfo(context);
        });
        /** 좌석 배치 지도 로딩 **/
        TextView seatMap = rootView.findViewById(R.id.seatInfo_textView);
        seatMap.setOnClickListener(view -> {
            mainActivity.replaceFragment();
        });

        return rootView;
    }

    public void initial(){
        loadingDialog = mainActivity.loadingDialog;
        id = rootView.findViewById(R.id.id_textView);
        email = rootView.findViewById(R.id.email_textView);
        seatMessage = rootView.findViewById(R.id.seatInfo_textView);
        cur_x = rootView.findViewById(R.id.cur_row);
        cur_y = rootView.findViewById(R.id.cur_col);
        next_x = rootView.findViewById(R.id.next_row);
        next_y = rootView.findViewById(R.id.next_col);
        email.setSelected(true);
        TextView scrollTxt = rootView.findViewById(R.id.seatInfo_textView_detail);
        scrollTxt.setMovementMethod(new ScrollingMovementMethod());
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
                BaseResponse<ResUserDetail> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "스케줄링 로딩 실패", Toast.LENGTH_SHORT).show());
                }
                assert result != null : "로딩 실패";
                ResUserDetail userDetail = result.getResult();
                ResUser user = userDetail.getUser();
                ResSeat seat = userDetail.getSeat();
                ResResult resResult = userDetail.getResult();
//                String seatValue = String.format("%s행 %d열 좌석 선택됨", seat.getX(), seat.getY());

                handler.post(() -> {
                    id.setText(user.getUserId());
                    email.setText(user.getEmail());

                    if (seat != null) {
                        cur_x.setText(seat.getX());
                        cur_y.setText(seat.getY().toString());
                    }
                    if (resResult != null) {
                        if (resResult.getMessage() != null)
                            seatMessage.setText(resResult.getMessage());

                        if (resResult.getTodayClass() != null) {
                            String seatDetail = String.format("%s\n%s\n%s",
                                    resResult.getTodayClass(), resResult.getClassLocation(), resResult.getClassInfo());
                            TextView txtView_seat_detail = rootView.findViewById(R.id.seatInfo_textView_detail);
                            txtView_seat_detail.setText(seatDetail);
                        }
                    }
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
     * 좌석 정보 변경
     **/
    private void setNewSeat(Context context, String x, Long y) {
        ReqNewSeatInfo reqNewSeatInfo = new ReqNewSeatInfo(userId, userPwd,
                x, y);
        Call<BaseResponse<ResSeat>> call = RetrofitClient.getApiService().setNewSeat(reqNewSeatInfo);
        call.enqueue(new Callback<BaseResponse<ResSeat>>() {
            @Override
            public void onResponse(Call<BaseResponse<ResSeat>> call, Response<BaseResponse<ResSeat>> response) {
                BaseResponse<ResSeat> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "좌석 정보 변경 실패", Toast.LENGTH_SHORT).show());
                } else {
                    ResSeat newSeat = result.getResult();
                    handler.post(() -> {
                        cur_x.setText(newSeat.getX());
                        cur_y.setText(newSeat.getY().toString());
                        next_x.setText(null);
                        next_y.setText(null);
                        Toast.makeText(context, "좌석 업데이트 성공", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ResSeat>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * 좌석 정보 초기화
     **/
    private void initialSeat(Context context) {
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().initialSeat(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse<String> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "좌석 정보 초기화 실패", Toast.LENGTH_SHORT).show());
                } else {
                    handler.post(() -> {
                        cur_x.setText("정보없음");
                        cur_y.setText("정보없음");
                        Toast.makeText(context, "좌석 정보 초기화 성공", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * 공단기 접속후 좌석 정보 재로딩
     **/
    private void reloadSeatInfo(Context context) {
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        Call<BaseResponse<ResResult>> call = RetrofitClient.getApiService().reloadBookedSeat(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<ResResult>>() {
            @Override
            public void onResponse(Call<BaseResponse<ResResult>> call, Response<BaseResponse<ResResult>> response) {
                loadingDialog.dismiss();
                BaseResponse<ResResult> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "좌석 정보 재로딩 실패", Toast.LENGTH_SHORT).show());
                } else {
                    ResResult resResult = result.getResult();
//                    String seatMap = resBookedSeat.getSeatMap(); // 좌석 지도

                    handler.post(() -> {
                        if (resResult != null) {
                            if (resResult.getMessage() != null)
                                seatMessage.setText(resResult.getMessage());
                            if (resResult.getTodayClass() != null) {
                                String seatDetail = String.format("%s\n%s\n%s",
                                        resResult.getTodayClass(), resResult.getClassLocation(), resResult.getClassInfo());
                                TextView txtView_seat_detail = rootView.findViewById(R.id.seatInfo_textView_detail);
                                txtView_seat_detail.setText(seatDetail);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ResResult>> call, Throwable t) {
                loadingDialog.dismiss();
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void fragmentRefresh() {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }
}