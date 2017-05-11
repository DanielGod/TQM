package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/5/10.
 */

public class ApplyForChooseActivity extends BaseActivity {

    @BindView(R.id.apply_for_choose_toolbar)
    Toolbar applyForChooseToolbar;
    @BindView(R.id.choose_company_apply_lin)
    LinearLayout chooseCompanyApplyLin;
    @BindView(R.id.choose_personal_apply_lin)
    LinearLayout choosePersonalApplyLin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_choose);
        ButterKnife.bind(this);
        setToolbar(applyForChooseToolbar, "入驻申请");

    }




    @OnClick({R.id.choose_company_apply_lin, R.id.choose_personal_apply_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_company_apply_lin:
                startActivity(new Intent(ApplyForChooseActivity.this, ApplyForCompanyActivity.class));
                break;
            case R.id.choose_personal_apply_lin:
                startActivity(new Intent(ApplyForChooseActivity.this, ApplyForPersonalActivity.class));
                break;
        }
    }
}
