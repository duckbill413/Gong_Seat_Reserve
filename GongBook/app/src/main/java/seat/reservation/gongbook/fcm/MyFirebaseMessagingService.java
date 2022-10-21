package seat.reservation.gongbook.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import seat.reservation.gongbook.BookFragment;
import seat.reservation.gongbook.R;
import seat.reservation.gongbook.api.model.BaseResponse;
import seat.reservation.gongbook.api.model.ReqToken;
import seat.reservation.gongbook.api.retrofit.RetrofitClient;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String userId, userPwd;

    @Override
    protected Intent getStartCommandIntent(Intent originalIntent) {
        userId = originalIntent.getStringExtra("userId");
        userPwd = originalIntent.getStringExtra("userPwd");
        return super.getStartCommandIntent(originalIntent);
    }

    public MyFirebaseMessagingService() {
        super();
        Task<String> token = FirebaseMessaging.getInstance().getToken();
        token.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UpdateToken(token.getResult());
            }
        });
    }

    /**
     * 푸시 메시지 수신시 할 작업
     **/
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
//        super.onMessageReceived(message);
        if (message.getData().size() > 0) {
            showNotification(message.getData().get("title"), message.getData().get("body"));
        }
    }

    /**
     * 새로운 토큰이 발급되었을시 작업
     **/
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM NewToken", token);
        UpdateToken(token);
    }

    private void UpdateToken(String token) {
        ReqToken reqToken = new ReqToken(userId, userPwd, token);
        Call<BaseResponse<String>> call = RetrofitClient.getApiService().updateToken(reqToken);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                BaseResponse result = response.body();
                if (result == null) {
                    Log.e("Token update", "Failed  " + LocalDateTime.now());
                    return;
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                t.printStackTrace();
                Log.e("Token update", "Failed  " + LocalDateTime.now());
            }
        });
    }

    private RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.popup_design);
//        remoteViews.setTextViewText(R.id.noti_title, LocalDate.now().toString() + title);
        remoteViews.setTextViewText(R.id.noti_title, title);
        remoteViews.setTextViewText(R.id.noti_message, message);
//        remoteViews.setImageViewResource(R.id.logo, R.drawable.popup_logo); // 로고 이미지 수정
        return remoteViews;
    }

    private void showNotification(String title, String message) {
        //팝업 터치시 이동할 액티비티를 지정합니다.
        Intent intent = new Intent(this, BookFragment.class);
        //알림 채널 아이디 : 본인 하고싶으신대로...
        String channel_id = "CHN_ID";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //기본 사운드로 알림음 설정. 커스텀하려면 소리 파일의 uri 입력
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.gongseat_logo)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000}) //알림시 진동 설정 : 1초 진동, 1초 쉬고, 1초 진동
                .setOnlyAlertOnce(true) //동일한 알림은 한번만.. : 확인 하면 다시 울림
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //안드로이드 버전이 커스텀 알림을 불러올 수 있는 버전이면
            //커스텀 레이아웃 호출
            builder = builder.setContent(getCustomDesign(title, message));
        } else { //아니면 기본 레이아웃 호출
            builder = builder
                    .setContentText(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.gongseat_logo); //커스텀 레이아웃에 사용된 로고 파일과 동일하게..
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //알림 채널이 필요한 안드로이드 버전을 위한 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "CHN_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        //알림 표시 !
        notificationManager.notify(0, builder.build());
    }
}