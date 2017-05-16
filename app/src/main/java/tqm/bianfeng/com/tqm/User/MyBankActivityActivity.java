package tqm.bianfeng.com.tqm.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.main.HomeBankActivitysListAdapter;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;

/**
 * Created by johe on 2017/3/13.
 */

public class MyBankActivityActivity extends BaseActivity {


    @BindView(R.id.my_bank_activity_toolbar)
    Toolbar myBankActivityToolbar;
    @BindView(R.id.my_bank_activity_list)
    RecyclerView myBankActivityList;

    HomeBankActivitysListAdapter bankActivitionsAdapter;
    List<BankActivityItem> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_activity);
        ButterKnife.bind(this);
        setToolbar(myBankActivityToolbar, "我关注的银行活动");
        initData();
    }

    public void initData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionOfBankActivity(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankActivityItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        shouNetWorkActivity();
                    }

                    @Override
                    public void onNext(BankListItems<BankActivityItem> bankActivityItemBankListItems) {
                        datas = bankActivityItemBankListItems.getItem();
                        Log.i("gqf", "onNext" + datas.toString());
                        initList(bankActivityItemBankListItems.getItem());
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    public void initList(List<BankActivityItem> bankActivityItems) {
        if (bankActivitionsAdapter == null) {
            bankActivitionsAdapter = new HomeBankActivitysListAdapter(this,bankActivityItems);
            myBankActivityList.setLayoutManager(new LinearLayoutManager(this));
            LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lac.setDelay(1);
            myBankActivityList.setLayoutAnimation(lac);
            myBankActivityList.setAdapter(bankActivitionsAdapter);
            bankActivitionsAdapter.setOnItemClickListener(new HomeBankActivitysListAdapter.HomeBankActivitysItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(MyBankActivityActivity.this, DetailActivity.class);
                    intent.putExtra("detailType", "01");
                    intent.putExtra("detailId", datas.get(position).getActivityId());
                    intent.putExtra("detailTitle", datas.get(position).getActivityTitle());
                    startActivity(intent);
                }
            });
        } else {
            bankActivitionsAdapter.update(bankActivityItems);
        }
    }

}
