package seat.reservation.gongbook.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Window;
import android.widget.ImageView;

import seat.reservation.gongbook.R;

public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_loading_dialog);
        ImageView img_loading_frame = findViewById(R.id.iv_frame_loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(() -> animationDrawable.start());
        setCancelable(false);
    }
}
