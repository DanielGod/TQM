package tqm.bianfeng.com.tqm.bank.bankactivitys;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

public class BankActivitonsActivity extends AppCompatActivity  {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_content)
    FrameLayout drawerContent;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.main_pull_refresh_lv)
    PullToRefreshListView mainPullRefreshLv;

    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_financing);
        unbinder = ButterKnife.bind(this);
        mCompositeSubscription = new CompositeSubscription();
//        initDrawLayout();
        setToolBar(getResources().getString(R.string.bankActivity));
        initDate(pagNum);
        initRefreshlv();
    }

    private void initRefreshlv() {
        //设置可上拉刷新和下拉刷新
        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);

        //设置刷新时显示的文本
        ILoadingLayout startLayout = mainPullRefreshLv.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel("正在下拉刷新...");
        startLayout.setRefreshingLabel("正在玩命加载中...");
        startLayout.setReleaseLabel("放开以刷新");


        ILoadingLayout endLayout = mainPullRefreshLv.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel("正在上拉刷新...");
        endLayout.setRefreshingLabel("正在玩命加载中...");
        endLayout.setReleaseLabel("放开以刷新");

        mainPullRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullDownToRefresh---");
                initDate( pagNum);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullDownToRefresh---");
                pagNum = pagNum + 1;
                initDate(pagNum);
            }
        });

    }

    private void setToolBar(String s) {
        toolbar.setTitle(s);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initDate(int pagNum) {
        Subscription getBankFinancItem_subscription = NetWork.getBankService().getBankActivityItem(Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankActivityItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankActivityItem> bankActivityItems) {
                        setAdapter(bankActivityItems);

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }
    private void setAdapter(List<BankActivityItem> bankActivityItems) {
        BankActivitionsAdapter bankActivitionsAdapter = new BankActivitionsAdapter(bankActivityItems,BankActivitonsActivity.this);
        mainPullRefreshLv.setAdapter(bankActivitionsAdapter);
        mainPullRefreshLv.onRefreshComplete();

    }
//
//    private void initDrawLayout() {
//        Fragment fragment = new FilterFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Bundle bundle = new Bundle();
//        bundle.putString("departmentName", "");
//        fragment.setArguments(bundle);
//        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
//
//    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
    }

    @OnClick({R.id.etSearch, R.id.ll_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
                etSearch.setFocusableInTouchMode(true);
                break;
            case R.id.ll_filter:
//                drawerLayout.openDrawer(drawerContent);
                break;
        }
    }
}
