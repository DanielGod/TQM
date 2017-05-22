package tqm.bianfeng.com.tqm.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.utils.PhoneUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.AppUtils;
import tqm.bianfeng.com.tqm.Util.NetUtils;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;

public class WelcomeActivity extends Activity {

    @BindView(R.id.startPage_img)
    ImageView startPageImg;
    @BindView(R.id.activity_time_tv)
    TextView activityTimeTv;
    @BindView(R.id.activity_start_page)
    RelativeLayout activityStartPage;


    private CompositeSubscription mCompositeSubscription;
    private static final int TIMETOCOUNT = 3;
    private String channel;//渠道号
    private String IMEI;//设备号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStatusBar(this);
        setContentView(R.layout.activity_welcome);
        mCompositeSubscription = new CompositeSubscription();
        ButterKnife.bind(this);
        channel = AppUtils.getChanel(getApplicationContext());
//        Toast.makeText(this, "--------"+channel, Toast.LENGTH_SHORT).show();

        Picasso.with(WelcomeActivity.this).load(R.drawable.qidongye).into(startPageImg);

        if(!NetUtils.isConnected(this)){
            Picasso.with(WelcomeActivity.this).load(R.drawable.qidongye).into(startPageImg);
            countToEnter();
        }else{
            //countToEnter();
            Subscription subscription = NetWork.getUserService().getImages("01")
                    .subscribeOn(Schedulers.io())
                    .timeout(100,TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            //Picasso.with(WelcomeActivity.this).load(R.drawable.qidongye).into(startPageImg);
//                            countToEnter();
                        }

                        @Override
                        public void onNext(List<String> strings) {
                            Log.e("gqf","onNext"+strings.toString());
                            //Picasso.with(WelcomeActivity.this).load(NetWork.LOAD+strings.get(0)).error(R.drawable.qidongye).into(startPageImg);
//                            countToEnter();
                        }
                    });
            mCompositeSubscription.add(subscription);
            //获取设备号，保存渠道号
            requestPhone();



        }
    }

    private void requestPhone() {
        Log.e("Daniel","requestPhone");
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @DebugLog
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            IMEI = PhoneUtils.getPhoneIMEI(getApplicationContext());
                            saveChannel();
                        } else {
                            requestPermissionInfo();
                        }
                    }
                });
    }

    private void saveChannel() {
        Log.e("Daniel","saveChannel");
        Log.e("Daniel","----channel-----"+channel);
        Log.e("Daniel","----IMEI----"+IMEI);
        //保存渠道号
        NetWork.getBankService().saveChannel(channel,IMEI)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        countToEnter();
                    }
                    @DebugLog
                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.e("Daniel","----getMsgv----"+resultCode.getMsg());
                        countToEnter();

                    }
                });
    }

    private void requestPermissionInfo() {
        new MaterialDialog.Builder(this)
                .title("请给予相关权限")
                .content("谢谢")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        WelcomeActivity.this.finish();

                    }
                }).show();
    }

    /**
     * 倒计时
     */
    @DebugLog
    private void countToEnter() {
        Subscription subscriptionCount = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .limit(TIMETOCOUNT)
                .map(new Func1<Long, Long>() {
                    @DebugLog
                    @Override
                    public Long call(Long aLong) {
                        return TIMETOCOUNT - aLong;
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        WelcomeActivity.this.finish();
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(Long aLong) {
                        //                        activityTimeTv.setText(aLong + "秒");
                    }
                });
        mCompositeSubscription.add(subscriptionCount);

    }
}
