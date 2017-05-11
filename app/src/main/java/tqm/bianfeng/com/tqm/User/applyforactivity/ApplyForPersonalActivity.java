package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/5/10.
 */

public class ApplyForPersonalActivity extends BaseActivity {
    private static final int REQUEST_IMAGE = 2;
    @BindView(R.id.apply_for_company_toolbar)
    Toolbar applyForCompanyToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_personal);
        ButterKnife.bind(this);
        setToolbar(applyForCompanyToolbar, "个人申请");


    }







}
