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
import tqm.bianfeng.com.tqm.User.adapter.BankFinancingAdapter;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;

/**
 * Created by johe on 2017/3/13.
 */

public class MyBankMakeMoneyActivity extends BaseActivity {

    @BindView(R.id.my_bank_make_money_toolbar)
    Toolbar myBankMakeMoneyToolbar;
    @BindView(R.id.my_bank_make_money_list)
    RecyclerView myBankMakeMoneyList;

    List<BankFinancItem> datas;
    BankFinancingAdapter bankFinancingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_make_money);
        ButterKnife.bind(this);
        setToolbar(myBankMakeMoneyToolbar,"我关注的银行理财");
        initData();
    }

    public void initData(){
        Subscription subscription = NetWork.getUserService().getMyAttentionOfBankFinanc(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankFinancItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<BankFinancItem> bankFinancItems) {
                        datas=bankFinancItems;
                        Log.i("gqf","onNext"+datas.toString());
                        initList(bankFinancItems);
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void initList(List<BankFinancItem> bankFinancItems){
        if(bankFinancingAdapter==null){
            bankFinancingAdapter=new BankFinancingAdapter(bankFinancItems,this);
            myBankMakeMoneyList.setLayoutManager(new LinearLayoutManager(this));
            LayoutAnimationController lac=new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lac.setDelay(1);
            myBankMakeMoneyList.setLayoutAnimation(lac);
            myBankMakeMoneyList.setAdapter(bankFinancingAdapter);
            bankFinancingAdapter.setOnItemClickListener(new BankFinancingAdapter.BankFinancingItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent=new Intent(MyBankMakeMoneyActivity.this,DetailActivity.class);
                    intent.putExtra("detailType","02");
                    intent.putExtra("detailId",datas.get(postion).getFinancId());
                    startActivity(intent);
                }
            });
        }else{
            bankFinancingAdapter.setdatas(bankFinancItems);
        }
    }

}
