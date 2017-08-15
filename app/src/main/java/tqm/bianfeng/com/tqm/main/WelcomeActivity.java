package tqm.bianfeng.com.tqm.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.AppConstants;
import tqm.bianfeng.com.tqm.Util.NetUtils;
import tqm.bianfeng.com.tqm.Util.SpUtils;
import tqm.bianfeng.com.tqm.featureguide.global.WelcomeGuideActivity;
import tqm.bianfeng.com.tqm.network.NetWork;

public class WelcomeActivity extends Activity {

    @BindView(R.id.startPage_img)
    ImageView startPageImg;
    @BindView(R.id.activity_time_tv)
    TextView activityTimeTv;
    @BindView(R.id.activity_start_page)
    RelativeLayout activityStartPage;


    private CompositeSubscription mCompositeSubscription;
    private static final int TIMETOCOUNT = 3;


    public final static int REQUEST_READ_PHONE_STATE = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        ScreenUtils.hideStatusBar(this);
        setContentView(R.layout.activity_welcome);
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//        //获取权限
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
//        }
            mCompositeSubscription = new CompositeSubscription();
            ButterKnife.bind(this);
//            channel = AppUtilsBd.getChanel(getApplicationContext());
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
                                countToEnter();
                            }

                            @Override
                            public void onNext(List<String> strings) {
                                Log.e("gqf","onNext"+strings.toString());
                                //Picasso.with(WelcomeActivity.this).load(NetWork.LOAD+strings.get(0)).error(R.drawable.qidongye).into(startPageImg);
                                countToEnter();
                            }
                        });
                mCompositeSubscription.add(subscription);
//                //获取设备号，保存渠道号
//                IMEI = PhoneUtils.getPhoneIMEI(getApplicationContext());
//                saveChannel();
            }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_READ_PHONE_STATE:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//
//                    //获取设备号，保存渠道号
//                    IMEI = PhoneUtils.getPhoneIMEI(getApplicationContext());
//                    saveChannel();
//                }
//                break;
//
//            default:
//                break;
//        }
//    }

//    private void requestPhone() {
//        Log.e("Daniel","requestPhone");
//        //动态获取权限
//        new RxPermissions(this)
//                .request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                .subscribe(new Action1<Boolean>() {
//                    @DebugLog
//                    @Override
//                    public void call(Boolean aBoolean) {
//                        if (aBoolean) {
//                            IMEI = PhoneUtils.getPhoneIMEI(getApplicationContext());
//                            saveChannel();
//                        } else {
//                            requestPermissionInfo();
//                        }
//                    }
//                });
//    }



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
