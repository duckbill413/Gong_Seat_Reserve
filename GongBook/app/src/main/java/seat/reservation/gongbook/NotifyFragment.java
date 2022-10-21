package seat.reservation.gongbook;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seat.reservation.gongbook.api.list_model.Board;
import seat.reservation.gongbook.api.list_model.BoardAdapter;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqUserLogin;
import seat.reservation.gongbook.api.model.ResResult;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;
import seat.reservation.gongbook.loading.LoadingDialog;
import seat.reservation.gongbook.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class NotifyFragment extends Fragment {
    String userId, userPwd;
    MainActivity mainActivity;
    LoadingDialog loadingDialog;
    Handler handler = new Handler();

    ArrayList<Board> boards;
    ListView listView;
    private static BoardAdapter boardAdapter;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notify_list, container, false);
        loadingDialog = mainActivity.loadingDialog;

        userId = getArguments().getString("userId");
        userPwd = getArguments().getString("userPwd");

        listView = (ListView) rootView.findViewById(R.id.listView_custom);
        boards = new ArrayList<>();
        setListValues();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            showBoardDetail(position);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        loadingDialog.show();
//        setListValues();
    }

    public void setListValues() {
        ReqUserLogin reqUserLogin = new ReqUserLogin(userId, userPwd);
        loadingDialog.show();
        Call<BaseResponse<List<ResResult>>> call = RetrofitClient.getApiService().loadResults(reqUserLogin);
        call.enqueue(new Callback<BaseResponse<List<ResResult>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ResResult>>> call, Response<BaseResponse<List<ResResult>>> response) {
                loadingDialog.dismiss();
                BaseResponse<List<ResResult>> result = response.body();
                if (result == null || !result.getIsSuccess()) {
                    handler.post(() -> Toast.makeText(context, "로딩 실패", Toast.LENGTH_SHORT).show());
                } else {
                    boards.clear();
                    List<ResResult> resResults = result.getResult();
                    Iterator<ResResult> iterator = resResults.iterator();

                    int size = 1;
                    while (iterator.hasNext()) {
                        ResResult res = iterator.next();
                        Board item = new Board();
                        if (res.getTodayClass() == null) {
                            item.setTitle("자리 예약 결과");
                        } else item.setTitle(res.getTodayClass());

                        item.setContents(res.getMessage());
                        item.setTime("No. " + size++);
                        item.setMap(res.getMapping());

                        item.setLocation(res.getClassLocation());
                        item.setInfo(res.getClassInfo());
                        boards.add(item);
                    }

                    boardAdapter = new BoardAdapter(getContext(), boards);
                    listView.setAdapter(boardAdapter);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ResResult>>> call, Throwable t) {
                loadingDialog.dismiss();
                handler.post(() -> Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show());
            }
        });
    }

    void showBoardDetail(int position) {
        Board selected = boards.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (selected.getTitle().equals("자리 예약 결과")) {
            builder.setTitle(selected.getContents());
            builder.setMessage("");
        } else {
            builder.setTitle(selected.getTitle());
            builder.setMessage(String.format("%s\n%s\n%s", selected.getLocation(), selected.getInfo(), selected.getContents()));
        }

        builder.setPositiveButton("확인", (dialog, which) -> {
        });
        if (selected.getMap() != null)
            builder.setNeutralButton("좌석확인", ((dialog, which) -> {
                ResResult resResult = new ResResult();
                resResult.setTodayClass(selected.getTitle());
                resResult.setClassLocation(selected.getLocation());
                resResult.setClassInfo(selected.getInfo());
                resResult.setMessage(selected.getContents());
                resResult.setMapping(selected.getMap());
                mainActivity.replaceNotifyMapFragment(resResult);
            }));
        builder.create().show();
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