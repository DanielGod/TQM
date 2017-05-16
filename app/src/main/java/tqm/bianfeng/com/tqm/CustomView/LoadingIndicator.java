package tqm.bianfeng.com.tqm.CustomView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import com.wang.avi.AVLoadingIndicatorView;

import tqm.bianfeng.com.tqm.R;

/**
 * Created by johe on 2017/5/15.
 */

public class LoadingIndicator extends AVLoadingIndicatorView {

    public LoadingIndicator(Context context) {
        super(context);
        init();
    }

    public LoadingIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    CountDownTimer countDownTimer;
    int progressIndex = 6;

    public void init(){
        super.setIndicator("BallSpinFadeLoaderIndicator");
        super.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
    }

    public void showLoading(){
        this.show();
        this.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
        countDownTimer = new CountDownTimer(1000 * 100, 1000) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                progressIndex++;
                switch (progressIndex % 6) {
                    case 0:
                        LoadingIndicator.this.setIndicatorColor(getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 1:
                        LoadingIndicator.this.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 2:
                        LoadingIndicator.this.setIndicatorColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        LoadingIndicator.this.setIndicatorColor(getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        LoadingIndicator.this.setIndicatorColor(getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        LoadingIndicator.this.setIndicatorColor(getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        LoadingIndicator.this.setIndicatorColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                progressIndex = 6;
            }
        }.start();
    }

    public void hideLoading(){
        this.hide();
        countDownTimer.onFinish();
    }


}
