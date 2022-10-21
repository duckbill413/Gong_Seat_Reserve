package seat.reservation.gongbook;

import static android.content.ContentValues.TAG;

import static java.lang.Math.abs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import kotlin.Triple;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seat.reservation.gongbook.api.grid_model.MapModel;
import seat.reservation.gongbook.api.grid_model.SeatAdapter;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqNewSeatInfo;
import seat.reservation.gongbook.api.model.ReqUserLogin;
import seat.reservation.gongbook.api.model.ResResult;
import seat.reservation.gongbook.api.model.ResSeat;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;
import seat.reservation.gongbook.loading.LoadingDialog;

public class SeatMapFragment extends Fragment {
    MainActivity mainActivity;
    Context context;
    ViewGroup rootView;
    LoadingDialog loadingDialog;
    GridView gridView;
    List<MapModel> mapModels;
    Handler handler = new Handler();
    SeatAdapter seatAdapter;
    HorizontalScrollView scrollView;

    String userId, userPwd;
    ResResult resResult;

    boolean my_seat = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    public void my_seat_true() {
        my_seat = true;
    }

    public void my_seat_false() {
        my_seat = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        my_seat_false();
        resResult = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mapping, container, false);
        // 사용자 정보 로딩
        gridView = (GridView) rootView.findViewById(R.id.grid_table_seat);
        userId = getArguments().getString("userId");
        userPwd = getArguments().getString("userPwd");

        seatAdapter = new SeatAdapter(userId, userPwd);
        loadingDialog = mainActivity.loadingDialog;
        scrollView = (HorizontalScrollView) rootView.findViewById(R.id.grid_scroll_view);

        userId = getArguments().getString("userId");
        userPwd = getArguments().getString("userPwd");

        // GridView 생성
        if (this.resResult == null)
            loadMapModels();
        else {
            Triple<List<MapModel>, Integer, Integer> res = convertToMap(resResult.getMapping());
            mapModels = res.getFirst();
            gridView.setNumColumns(res.getThird());

            Iterator<MapModel> iterator = mapModels.iterator();
            while (iterator.hasNext()) {
                MapModel tmp = iterator.next();
                seatAdapter.add(tmp);
            }

            TextView className = rootView.findViewById(R.id.today_class_name);
            TextView classLocation = rootView.findViewById(R.id.today_class_location);
            TextView classInfo = rootView.findViewById(R.id.today_class_info);
            className.setText(resResult.getTodayClass());
            classLocation.setText(resResult.getClassLocation());
            classInfo.setText(resResult.getClassInfo());

            gridView.setAdapter(seatAdapter);
        }

        gridView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            Button button = gridView.findViewById(R.id.my_seat);
            if (button != null && my_seat == false) {
                int[] location = new int[2];
                button.getLocationOnScreen(location);
                Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                int a = location[0];
                int b = v.getWidth();
                int c = size.x;

                int center = abs((int) ((a + c) - (b / 2)));
//                int center = (int) (v.getWidth() / 2.0) - location[0] - (int) (size.x / 2.0);
                scrollView.scrollTo(center, 0);
                my_seat_true();
            }
        });
        /** 좌석 정보 재로딩 이미지 버튼 **/
        ImageButton imageButton = rootView.findViewById(R.id.reload_seats);
        imageButton.setOnClickListener(view -> {
            loadingDialog.show();
            reloadSeatInfo();
        });

        return rootView;
    }

    public void setResResult(ResResult resResult) {
        this.resResult = resResult;
    }

    public Triple<List<MapModel>, Integer, Integer> convertToMap(String json) {
        List<MapModel> map = new ArrayList<>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Collection<Integer>>>() {
        }.getType();
        Integer r, c;
        r = c = 0;

        Collection<Collection<Integer>> collection = gson.fromJson(json, collectionType);
        Iterator<Collection<Integer>> i = collection.iterator();
        r = collection.size();
        char row = 'A';
        while (i.hasNext()) {
            Collection<Integer> array = i.next();
            Iterator<Integer> j = array.iterator();
            c = array.size();

            int col = 1;
            while (j.hasNext()) {
                int code = j.next();
                MapModel model = new MapModel();
                model.setCode(code);
                model.setX(String.valueOf(row));
                model.setY(String.valueOf(col++));
                map.add(model);
            }
            row++;
        }

        return new Triple<>(map, r, c);
    }

    public void loadMapModels() {
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        loadingDialog.show();
        Call<BaseResponse<ResResult>> call = RetrofitClient.getApiService().loadOneResult(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<ResResult>>() {
            @Override
            public void onResponse(Call<BaseResponse<ResResult>> call, Response<BaseResponse<ResResult>> response) {
                BaseResponse<ResResult> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    loadingDialog.dismiss();
                    handler.post(() -> Toast.makeText(context, "좌석 정보 없음", Toast.LENGTH_SHORT).show());
                } else {
                    resResult = result.getResult();
                    if (resResult == null) {
                        loadingDialog.dismiss();
                        handler.post(() -> Toast.makeText(context, "오늘의 좌석 정보가 없습니다.", Toast.LENGTH_LONG).show());
                        mainActivity.returnToMainFragment(); // 좌석 정보 없음...
                    }
                    Triple<List<MapModel>, Integer, Integer> res = convertToMap(resResult.getMapping());
                    mapModels = res.getFirst();
                    gridView.setNumColumns(res.getThird());

                    handler.post(() -> {
                        Iterator<MapModel> iterator = mapModels.iterator();
                        while (iterator.hasNext()) {
                            MapModel tmp = iterator.next();
                            seatAdapter.add(tmp);
                        }

                        TextView className = rootView.findViewById(R.id.today_class_name);
                        TextView classLocation = rootView.findViewById(R.id.today_class_location);
                        TextView classInfo = rootView.findViewById(R.id.today_class_info);
                        className.setText(resResult.getTodayClass());
                        classLocation.setText(resResult.getClassLocation());
                        classInfo.setText(resResult.getClassInfo());

                        gridView.setAdapter(seatAdapter);
                    });
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ResResult>> call, Throwable t) {
                loadingDialog.dismiss();
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void refreshFragment() {
        seatAdapter.notifyDataSetChanged();
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    /**
     * 공단기 접속후 좌석 정보 재로딩
     **/
    private void reloadSeatInfo() {
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
                    handler.post(() -> {
                        TextView className = rootView.findViewById(R.id.today_class_name);
                        TextView classLocation = rootView.findViewById(R.id.today_class_location);
                        TextView classInfo = rootView.findViewById(R.id.today_class_info);
                        className.setText(resResult.getTodayClass());
                        classLocation.setText(resResult.getClassLocation());
                        classInfo.setText(resResult.getClassInfo());
                        Toast.makeText(context, "업데이트 성공", Toast.LENGTH_SHORT).show();
                        refreshFragment();
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
}
