package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/3/13.
 */

public class MyBankActivityActivity extends BaseActivity {


    @BindView(R.id.my_bank_activity_toolbar)
    Toolbar myBankActivityToolbar;
    @BindView(R.id.my_bank_activity_list)
    RecyclerView myBankActivityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_activity);
        ButterKnife.bind(this);
        setToolbar(myBankActivityToolbar,"我关注的银行活动");
    }


}
