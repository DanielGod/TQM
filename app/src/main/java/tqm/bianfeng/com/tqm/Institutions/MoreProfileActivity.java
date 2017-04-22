package tqm.bianfeng.com.tqm.Institutions;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/4/12.
 */

public class MoreProfileActivity extends BaseActivity {

    @BindView(R.id.allcity_toolbar)
    Toolbar allcityToolbar;
    @BindView(R.id.more_profile_txt)
    TextView moreProfileTxt;

    String profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_profile);
        ButterKnife.bind(this);
        setToolbar(allcityToolbar,"简介");
        profile=getIntent().getStringExtra("Profile");
        moreProfileTxt.setText("              "+profile);


    }

}
