package tqm.bianfeng.com.tqm.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import tqm.bianfeng.com.tqm.Util.NetUtils;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStatusBar(this);
        setContentView(R.layout.activity_welcome);
        mCompositeSubscription = new CompositeSubscription();
        ButterKnife.bind(this);
        if(!NetUtils.isConnected(this)){
            Picasso.with(WelcomeActivity.this).load(R.drawable.startpage).into(startPageImg);
            countToEnter();
        }else{
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
                            Picasso.with(WelcomeActivity.this).load(R.drawable.startpage).into(startPageImg);
                            countToEnter();
                        }

                        @Override
                        public void onNext(List<String> strings) {
                            Log.e("gqf","onNext"+strings.toString());
                            Picasso.with(WelcomeActivity.this).load(NetWork.LOAD+strings.get(0)).into(startPageImg);
                            countToEnter();
                        }
                    });
            mCompositeSubscription.add(subscription);
        }
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
