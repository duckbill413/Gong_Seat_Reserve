package seat.reservation.gongbook.api.grid_model;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seat.reservation.gongbook.R;
import seat.reservation.gongbook.SeatMapFragment;
import seat.reservation.gongbook.SettingsPreferenceFragment;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqNewSeatInfo;
import seat.reservation.gongbook.api.model.ResSeat;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;

public class SeatAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MapModel> buttons = new ArrayList<>();
    String userId, userPwd;

    public SeatAdapter(String userId, String userPwd) {
        this.userId = userId;
        this.userPwd = userPwd;
    }

    public void add(MapModel mapModel) {
        buttons.add(mapModel);
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Override
    public Object getItem(int position) {
        return buttons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        convertView = layoutInflater.inflate(R.layout.seat_sector_item, parent, false);

        Button btn = convertView.findViewById(R.id.seat_sector_button);
        MapModel mapModel = buttons.get(position);

        Drawable d;
        switch (mapModel.getCode()) {
            case 0:
            case 1:
                d = ContextCompat.getDrawable(context, R.drawable.btn_blue);
                btn.setText(mapModel.x + "\n" + mapModel.y);
                break;
            case 3:
                d = ContextCompat.getDrawable(context, R.drawable.btn_red);
                btn.setText(mapModel.x + "\n" + mapModel.y);
                btn.setId(R.id.my_seat);
                break;
            case 4:
                d = ContextCompat.getDrawable(context, R.drawable.btn_transparent);
                break;
            default:
                d = ContextCompat.getDrawable(context, R.drawable.btn_grey);
                btn.setText(mapModel.x + "\n" + mapModel.y);
        }
        btn.setBackground(d);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);

        btn.setOnClickListener(v -> {
            buildAlertButton(mapModel);
        });

        return convertView;
    }

    public void buildAlertButton(MapModel mapModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("희망 좌석 변경");
        String message = String.format("%s행 %s열로 희망좌석을 변경하시 겠습니까?", mapModel.getX(), mapModel.getY());
        builder.setMessage(message);
        builder.setPositiveButton("변경", (dialog, which) -> {
            setNewSeat(mapModel.getX(), Long.parseLong(mapModel.getY()));
        });
        builder.setNegativeButton("취소", (dialog, which) -> {
        });
        builder.create().show();
    }


    /**
     * 좌석 정보 변경
     **/
    private void setNewSeat(String x, Long y) {
        ReqNewSeatInfo reqNewSeatInfo = new ReqNewSeatInfo(userId, userPwd, x, y);
        Call<BaseResponse<ResSeat>> call = RetrofitClient.getApiService().setNewSeat(reqNewSeatInfo);
        call.enqueue(new Callback<BaseResponse<ResSeat>>() {
            @Override
            public void onResponse(Call<BaseResponse<ResSeat>> call, Response<BaseResponse<ResSeat>> response) {
                BaseResponse<ResSeat> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    Toast.makeText(context, "좌석 정보 변경 실패", Toast.LENGTH_SHORT).show();
                } else {
                    ResSeat newSeat = result.getResult();
                    Toast.makeText(context, x + "행 " + y + "열\n" + "좌석 업데이트 성공", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ResSeat>> call, Throwable t) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
