package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/3/13.
 */

public class MyBankLoanActivity extends BaseActivity {

    @BindView(R.id.my_bank_loan_toolbar)
    Toolbar myBankLoanToolbar;
    @BindView(R.id.my_bank_loan_list)
    RecyclerView myBankLoanList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_loan);
        ButterKnife.bind(this);
        setToolbar(myBankLoanToolbar,"我关注的银行贷款");
        //initData();
    }

    public void initData(){
        Subscription subscription = NetWork.getUserService().getMyAttentionOfBankLoan(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        compositeSubscription.add(subscription);
    }

}
