package tqm.bianfeng.com.tqm.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.bank.bankactivitys.BankActivitionsAdapter;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;

/**
 * Created by johe on 2017/3/13.
 */

public class MyBankActivityActivity extends BaseActivity {


    @BindView(R.id.my_bank_activity_toolbar)
    Toolbar myBankActivityToolbar;
    @BindView(R.id.my_bank_activity_list)
    RecyclerView myBankActivityList;

    BankActivitionsAdapter bankActivitionsAdapter;
    List<BankActivityItem> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_activity);
        ButterKnife.bind(this);
        setToolbar(myBankActivityToolbar,"我关注的银行活动");
        initData();
    }

    public void initData(){
        Subscription subscription = NetWork.getUserService().getMyAttentionOfBankActivity(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankActivityItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<BankActivityItem> bankActivityItems) {

                        datas=bankActivityItems;
                        Log.i("gqf","onNext"+datas.toString());
                        initList(bankActivityItems);
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void initList(List<BankActivityItem> bankActivityItems){
        if(bankActivitionsAdapter==null){
            bankActivitionsAdapter=new BankActivitionsAdapter(bankActivityItems,this);
            myBankActivityList.setLayoutManager(new LinearLayoutManager(this));
            myBankActivityList.setAdapter(bankActivitionsAdapter);
            bankActivitionsAdapter.setOnItemClickListener(new BankActivitionsAdapter.BankActivityItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent=new Intent(MyBankActivityActivity.this,DetailActivity.class);
                    intent.putExtra("detailType","01");
                    intent.putExtra("detailId",datas.get(postion).getActivityId());
                    startActivity(intent);
                }
            });
        }else{
            bankActivitionsAdapter.setdatas(bankActivityItems);
        }
    }

}
