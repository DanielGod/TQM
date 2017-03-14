package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/3/13.
 */

public class MyBankLoanActivity extends BaseActivity {

    @BindView(R.id.my_bank_loan_toolbar)
    Toolbar myBankLoanToolbar;
    @BindView(R.id.my_bank_loan_list)
    RecyclerView myBankLoanList;

    private void setToolbar(String toolstr) {
        myBankLoanToolbar.setTitle(toolstr);
        myBankLoanToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myBankLoanToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        setSupportActionBar(myBankLoanToolbar);
        myBankLoanToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_loan);
        ButterKnife.bind(this);
        setToolbar("我关注的银行贷款");
    }

}
