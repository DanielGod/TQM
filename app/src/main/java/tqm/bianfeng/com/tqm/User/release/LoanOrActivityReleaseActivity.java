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

public class LoanOrActivityReleaseActivity extends BaseActivity {

    public static int RELEASE_LOAN_TYPE = 1;
    public static int RELEASE_ACTIVITY_TYPE = 2;
    public static int RELEASE_TYPE = 0;

    @BindView(R.id.activity_release_toolbar)
    Toolbar activityReleaseToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_activity_release);
        ButterKnife.bind(this);
        String title="";
        if(RELEASE_TYPE==RELEASE_LOAN_TYPE){
            title="贷款信息发布";
        }else{
            title="活动信息发布";
        }

        setToolbar(activityReleaseToolbar, "活动信息发布");


    }
}
