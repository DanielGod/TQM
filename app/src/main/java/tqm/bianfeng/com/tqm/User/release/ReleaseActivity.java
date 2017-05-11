package tqm.bianfeng.com.tqm.User.release;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForCompanyActivity;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForPersonalActivity;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/5/10.
 */

public class ReleaseActivity extends BaseActivity {
    @BindView(R.id.release_toolbar)
    Toolbar releaseToolbar;
    @BindView(R.id.loan_radio)
    RadioButton loanRadio;
    @BindView(R.id.activity_radio)
    RadioButton activityRadio;
    @BindView(R.id.commit)
    Button commit;

    int chooseIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        setToolbar(releaseToolbar, "发布信息");
        loanRadio.setChecked(true);

    }

    @OnClick({R.id.loan_radio, R.id.activity_radio, R.id.commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loan_radio:
                chooseIndex=0;
                break;
            case R.id.activity_radio:
                chooseIndex=1;
                break;
            case R.id.commit:
                if(chooseIndex==0){
                    startActivity(new Intent(ReleaseActivity.this,LoanReleaseActivity.class));
                }else{
                    startActivity(new Intent(ReleaseActivity.this,ActivityReleaseActivity.class));
                }
                break;
        }
    }
}
