package seat.reservation.gongbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import seat.reservation.gongbook.api.model.ResResult;
import seat.reservation.gongbook.fcm.MyFirebaseMessagingService;
import seat.reservation.gongbook.loading.LoadingDialog;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();
    BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰

    FragmentManager fragmentManager = getSupportFragmentManager();
    BookFragment bookFragment = new BookFragment();
    SettingsPreferenceFragment settingsPreferenceFragment = new SettingsPreferenceFragment();
    NotifyFragment notifyFragment = new NotifyFragment();
    SeatSetFragment seatFragment = new SeatSetFragment();
    SeatMapFragment seatMapFragment = new SeatMapFragment();

    SeatMapFragment notifySeatMap;

    LoadingDialog loadingDialog;
    Bundle bundle = new Bundle();
    String userId, userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());
        // 로딩 다이얼 로그
        loadingDialog = new LoadingDialog(this); // 로딩 Dialog
        loadingDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT)
        );

        // 유저 로그인 정보 불러오기
        Intent intent_login = getIntent();
        userId = intent_login.getStringExtra("userId");
        userPwd = intent_login.getStringExtra("userPwd");
        // 번들에 유저 로그인 정보 저장
        bundle.putString("userId", userId);
        bundle.putString("userPwd", userPwd);
        // 초기 Fragment 화면
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        bookFragment.setArguments(bundle);
        transaction.replace(R.id.menu_frame_layout, bookFragment).commitAllowingStateLoss();

        // Fcm Service 로딩
        Intent fcm = new Intent(getApplicationContext(), MyFirebaseMessagingService.class);
        fcm.putExtra("userId", userId);
        fcm.putExtra("userPwd", userPwd);
        startService(fcm);
    }

    public void replaceFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        seatMapFragment.setArguments(bundle);
        transaction.replace(R.id.menu_frame_layout, seatMapFragment).commitAllowingStateLoss();
    }

    public void replaceNotifyMapFragment(ResResult resResult){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        notifySeatMap = new SeatMapFragment();
        notifySeatMap.setArguments(bundle);
        notifySeatMap.setResResult(resResult);
        transaction.replace(R.id.menu_frame_layout, notifySeatMap).commitAllowingStateLoss();
    }

    public void returnToMainFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        bookFragment.setArguments(bundle);
        transaction.replace(R.id.menu_frame_layout, bookFragment).commitAllowingStateLoss();
    }

    class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.tab_schedule:
                    bookFragment.setArguments(bundle);
                    transaction.replace(R.id.menu_frame_layout, bookFragment).commitAllowingStateLoss();
                    break;
                case R.id.tab_seat:
                    seatFragment.setArguments(bundle);
                    transaction.replace(R.id.menu_frame_layout, seatFragment).commitAllowingStateLoss();
                    break;
                case R.id.tab_notify:
                    notifyFragment.setArguments(bundle);
                    transaction.replace(R.id.menu_frame_layout, notifyFragment).commitAllowingStateLoss();
                    break;
                case R.id.tab_settings:
                    settingsPreferenceFragment.setArguments(bundle);
                    transaction.replace(R.id.menu_frame_layout, settingsPreferenceFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}