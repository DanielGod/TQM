package tqm.bianfeng.com.tqm.CustomView;

import android.content.Context;
import android.os.CountDownTimer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import tqm.bianfeng.com.tqm.R;

/**
 * Created by johe on 2017/5/13.
 */

public class ShowDialogAndLoading {

    public static class Show{
        public static ShowDialogAndLoading showDialogAndLoading=new ShowDialogAndLoading();
    }

    public interface Linsener{
        public void showBefore();
        public void showAfter();
    }

    Linsener mLinsener;

    public void setmLinsener(Linsener mLinsener) {
        this.mLinsener = mLinsener;
    }

    Context mContext;



    public void showBeforeDialog(Context context,String title,String countent,String btn1,String btn2){
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(countent)
                .setCancelText(btn1)
                .setConfirmText(btn2)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        mLinsener.showBefore();
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
    public void showAfterDialog(Context context,String title,String countent,String btn1) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(countent)
                .setConfirmText(btn1)
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mLinsener.showAfter();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    SweetAlertDialog pDialog;
    int i=6;
    CountDownTimer countDownTimer;
    public void showLoading(String txt,Context context){
        mContext=context;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(txt);
        pDialog.show();
        pDialog.setCancelable(false);
        countDownTimer = new CountDownTimer(1000 * 100, 1000) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i % 6) {
                    case 0:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.success_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                i = 6;
            }
        }.start();
    }
    public void stopLoaading(){
        if(pDialog!=null&&countDownTimer!=null){
            pDialog.dismiss();
            countDownTimer.onFinish();
        }
    }


}
