package tqm.bianfeng.com.tqm.bank.bankactivitys;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
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
    private int mPagItemSize = 0 ;



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

        //设置刷新时显示的文本
        ILoadingLayout startLayout = mainPullRefreshLv.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel("正在下拉刷新...");
        startLayout.setRefreshingLabel("正在刷新...");
        startLayout.setReleaseLabel("放开以刷新");


        ILoadingLayout endLayout = mainPullRefreshLv.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel("正在上拉刷新...");
        endLayout.setRefreshingLabel("加载中...");
        endLayout.setReleaseLabel("放开以刷新");

        mainPullRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @DebugLog
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullDownToRefresh---");
                initDate(1);

            }
            @DebugLog
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullDownToRefresh---");
                if (mPagItemSize>Constan.PAGESIZE){
                    pagNum = pagNum + 1;
                    initDate(pagNum);
                }else {
                    mainPullRefreshLv.onRefreshComplete();
                }

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
                    @DebugLog
                    @Override
                    public void onCompleted() {
                        //设置可上拉刷新和下拉刷新
                        Log.e("Daniel","---mPagItemSize---"+mPagItemSize);
                        if (mPagItemSize>Constan.PAGESIZE){
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
                        }else {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankActivityItem> bankActivityItems) {
                        mPagItemSize= bankActivityItems.size();
                        setAdapter(bankActivityItems);

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }
    private void setAdapter(List<BankActivityItem> bankActivityItems) {
        BankActivitionsAdapter bankActivitionsAdapter = new BankActivitionsAdapter(bankActivityItems,BankActivitonsActivity.this);
        mainPullRefreshLv.setAdapter(bankActivitionsAdapter);
        initEdi(bankActivitionsAdapter,bankActivityItems);
        mainPullRefreshLv.onRefreshComplete();

    }

    public void initEdi(final BankActivitionsAdapter bankFinancingAdapter, final List<BankActivityItem> bankFinancItems) {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @DebugLog
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    List<BankActivityItem> decoCompanyItemList = new ArrayList();
                    for (BankActivityItem decoCompanyItem : bankFinancItems) {
                        Log.e("Daniel","----editable.toString()---"+editable.toString());
                        Log.e("Daniel","----decoCompanyItem.getProductName()---"+decoCompanyItem.getInstitutionName());
                        if (editable.toString().contains(decoCompanyItem.getInstitutionName())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        } else if (decoCompanyItem.getInstitutionName().contains(editable.toString())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        }
                    }
                    Log.e("Daniel","----decoCompanyItemList.size()---"+decoCompanyItemList.size());
                    bankFinancingAdapter.setdatas(decoCompanyItemList);
                } else {
                    bankFinancingAdapter.setdatas(bankFinancItems);
                }

            }
        });
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
                break;
        }
    }
}
