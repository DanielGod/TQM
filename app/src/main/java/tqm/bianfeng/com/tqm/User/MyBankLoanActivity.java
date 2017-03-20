package tqm.bianfeng.com.tqm.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.BankLoanAdapter;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by johe on 2017/3/13.
 */

public class MyBankLoanActivity extends BaseActivity {

    @BindView(R.id.my_bank_loan_toolbar)
    Toolbar myBankLoanToolbar;
    @BindView(R.id.my_bank_loan_list)
    RecyclerView myBankLoanList;
    List<BankLoanItem> datas;

    BankLoanAdapter bankLoanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_loan);
        ButterKnife.bind(this);
        setToolbar(myBankLoanToolbar, "我关注的银行贷款");
        initData();
    }

    public void initData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionOfBankLoan(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankLoanItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        shouNetWorkActivity();
                    }

                    @Override
                    public void onNext(List<BankLoanItem> bankLoanItems) {
                        datas = bankLoanItems;
                        Log.i("gqf", "onNext" + datas.toString());
                        initList(bankLoanItems);
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();

    }

    public void initList(List<BankLoanItem> bankLoanItems) {
        if (bankLoanAdapter == null) {
            bankLoanAdapter = new BankLoanAdapter(bankLoanItems, this);
            myBankLoanList.setLayoutManager(new LinearLayoutManager(this));
            LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lac.setDelay(1);
            myBankLoanList.setLayoutAnimation(lac);
            myBankLoanList.setAdapter(bankLoanAdapter);
            bankLoanAdapter.setOnItemClickListener(new BankLoanAdapter.BankLoanItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent = new Intent(MyBankLoanActivity.this, DetailActivity.class);
                    intent.putExtra("detailType", "03");
                    intent.putExtra("detailId", datas.get(postion).getLoanId());
                    startActivity(intent);
                }
            });
        } else {
            bankLoanAdapter.setdatas(bankLoanItems);
        }
    }

}
