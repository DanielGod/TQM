package tqm.bianfeng.com.tqm.User.release;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/5/10.
 */

public class ActivityReleaseActivity extends BaseActivity {
    @BindView(R.id.activity_release_toolbar)
    Toolbar activityReleaseToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_release);
        ButterKnife.bind(this);
        setToolbar(activityReleaseToolbar, "活动信息发布");


    }
}
