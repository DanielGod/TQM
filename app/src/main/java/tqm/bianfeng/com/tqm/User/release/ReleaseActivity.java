package tqm.bianfeng.com.tqm.User.release;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/5/10.
 */
//发布选择界面
public class ReleaseActivity extends BaseActivity {
    @BindView(R.id.release_toolbar)
    Toolbar releaseToolbar;
    @BindView(R.id.release_loan_lin)
    LinearLayout releaseLoanLin;
    @BindView(R.id.release_activity_lin)
    LinearLayout releaseActivityLin;

    public static int release_loan_num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        setToolbar(releaseToolbar, "发布信息");

    }


    @OnClick({R.id.release_loan_lin, R.id.release_activity_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.release_loan_lin:
                if(release_loan_num<5) {
                    LoanOrActivityReleaseActivity.RELEASE_TYPE = LoanOrActivityReleaseActivity.RELEASE_LOAN_TYPE;
                    startActivity(new Intent(ReleaseActivity.this, LoanOrActivityReleaseActivity.class));
                    finish();
                }else{
                    Toast.makeText(this, "您发布的贷款信息数量已到达上限，不能再发布", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.release_activity_lin:
                //LoanOrActivityReleaseActivity.RELEASE_TYPE=LoanOrActivityReleaseActivity.RELEASE_ACTIVITY_TYPE;
                //startActivity(new Intent(ReleaseActivity.this,LoanOrActivityReleaseActivity.class));
                Intent intent=new Intent(ReleaseActivity.this,ReleaseMyActivityActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
