package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/3/13.
 */

public class MyBankMakeMoneyActivity extends BaseActivity {

    @BindView(R.id.my_bank_make_money_toolbar)
    Toolbar myBankMakeMoneyToolbar;
    @BindView(R.id.my_bank_make_money_list)
    RecyclerView myBankMakeMoneyList;

    private void setToolbar(String toolstr) {
        myBankMakeMoneyToolbar.setTitle(toolstr);
        myBankMakeMoneyToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myBankMakeMoneyToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
        setSupportActionBar(myBankMakeMoneyToolbar);
        myBankMakeMoneyToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_make_money);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        compositeSubscription = new CompositeSubscription();
        setToolbar("我关注的银行理财");
    }

}
