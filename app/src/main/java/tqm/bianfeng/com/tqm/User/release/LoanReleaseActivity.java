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

public class LoanReleaseActivity extends BaseActivity {

    @BindView(R.id.loan_release_toolbar)
    Toolbar loanReleaseToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_release);
        ButterKnife.bind(this);
        setToolbar(loanReleaseToolbar, "贷款信息发布");


    }

}
