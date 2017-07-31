package tqm.bianfeng.com.tqm.User.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwDksq;

/**
 * Created by johe on 2017/5/10.
 */
//订单界面
public class OrderActivity extends BaseActivity {

    @BindView(R.id.release_toolbar)
    Toolbar releaseToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;
    String mOrderType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        setToolbar(releaseToolbar, "订单中心");
        defaultLoadview.lodingIsFailOrSucess(1);
        mOrderType = getIntent().getStringExtra("orderType");
            initData(mOrderType, realm.where(User.class).findFirst().getUserId());
    }

    private void initData(String mOrderType, int userId) {
        Log.e("Daniel", "订单信息请求参数：mOrderType：" + mOrderType+"userId:"+userId);
        Subscription subscription = NetWork.getUserService().getItem(mOrderType, userId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<YwDksq>>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.e("Daniel", "订单信息：" + e.toString());
                defaultLoadview.lodingIsFailOrSucess(3);
            }
            @DebugLog
            @Override
            public void onNext(List<YwDksq> ywDksqs) {
                Log.e("Daniel", "订单信息数量：" +ywDksqs.size());
                setAdapter(ywDksqs);
                defaultLoadview.lodingIsFailOrSucess(2);

            }
        });
        compositeSubscription.add(subscription);


    }

    OrderListAdapter mOrderListAdapter;
    private void setAdapter(final List<YwDksq> ywDksqs) {
        if (mOrderListAdapter == null) {
            mOrderListAdapter = new OrderListAdapter(OrderActivity.this, ywDksqs);
            mOrderListAdapter.setOnItemClickListener(new OrderListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                   startActivity(new Intent(OrderActivity.this,OrderDetailActivity.class)
                           .putExtra("viewType",mOrderType).putExtra("dksqId", ywDksqs.get(position).getId()));

                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mOrderListAdapter);
    } else {
        mOrderListAdapter.notifyData(ywDksqs);
    }

    }

}
