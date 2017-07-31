package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/5/12.
 */

public class ApplyForStatusActivity extends BaseActivity {

    @BindView(R.id.apply_for_status_toolbar)
    Toolbar applyForStatusToolbar;
    @BindView(R.id.fragment)
    FrameLayout fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_status);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setToolbar(applyForStatusToolbar, "申请进度");
        setFragment();
        //        getAuditCode(realm.where(User.class).findFirst().getUserId());
    }
    ApplyForStatusFragment applyForStatusFragment;
    private void setFragment() {
        if (applyForStatusFragment==null){
            applyForStatusFragment = new ApplyForStatusFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, applyForStatusFragment).commit();
        }
    }
    ApplyForPayFragment applyForPayFragment;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String str) {
        if ("ApplyForStatusFragment_pay".equals(str)){
            if (applyForPayFragment==null){
                applyForPayFragment = new ApplyForPayFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, applyForPayFragment).commit();
            }
            setToolbar(applyForStatusToolbar, "支付");
        }
    }


}
