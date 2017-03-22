package tqm.bianfeng.com.tqm.bank.bankloan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import tqm.bianfeng.com.tqm.bank.fragment.FilterFragment;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.FilterEvens;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

public class BankLoanActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_content)
    FrameLayout drawerContent;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_pull_refresh_lv)
    PullToRefreshListView mainPullRefreshLv;
    @BindView(R.id.etSearch)
    EditText etSearch;


    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 1;
    private int mPagItemSize = 0;
    BankLoanAdapter loanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_financing);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);//订阅
//        mListener = (mListener) BankLoanActivity.this;
        setToolBar(getResources().getString(R.string.bankloan));
        mCompositeSubscription = new CompositeSubscription();
        initDrawLayout();
        initDate(null, pagNum);
        initRefreshlv();


    }
    private void initRefreshlv() {


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
                initDate(null, pagNum);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullDownToRefresh---");
                pagNum = pagNum+1;
                initDate(null, pagNum);
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FilterEvens event) {
        Log.i("Daniel", "---onEventMainThread---" + event.getFilterValue());
        initDate(event.getFilterValue(), pagNum);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread2(ListItemPositioin event) {
        Integer position = event.getPosition();
        Log.i("Daniel", "---onEventMainThread2---" + event.getPosition());
        //跳转银行贷款详情
        Log.i("Daniel", "---getFinancId---" + loanAdapter.getItem(position).getLoanId());
        Log.i("Daniel", "---position---" + position);
        Intent intent = new Intent(BankLoanActivity.this, DetailActivity.class);
        intent.putExtra("detailType", "03");
        intent.putExtra("detailId", loanAdapter.getItem(position).getLoanId());
        startActivity(intent);

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

    private void initDate(String json, int pagNum) {
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankLoanItem(json, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankLoanItem>>() {
                    @Override
                    public void onCompleted() {
                        //设置可上拉刷新和下拉刷新
                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
                        if (mPagItemSize > Constan.PAGESIZE) {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                    }
                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                    }
                    @DebugLog
                    @Override
                    public void onNext(List<BankLoanItem> bankloanItems) {
                        mPagItemSize = bankloanItems.size();
                        setAdapter(bankloanItems);
//

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    private void setAdapter(List<BankLoanItem> bankloanItems) {
        if (loanAdapter==null){
            loanAdapter = new BankLoanAdapter(BankLoanActivity.this,bankloanItems,false);
            mainPullRefreshLv.setAdapter(loanAdapter);
        }else {
            loanAdapter.setdatas(bankloanItems);
        }


        Log.i("Daniel", "---isRefreshing---"+mainPullRefreshLv.isRefreshing());
        mainPullRefreshLv.onRefreshComplete();
        initEdi(loanAdapter,bankloanItems);

    }
    public void initEdi(final BankLoanAdapter loanAdapter, final List<BankLoanItem> bankFinancItems) {
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
                    List<BankLoanItem> decoCompanyItemList = new ArrayList();
                    for (BankLoanItem decoCompanyItem : bankFinancItems) {
                        Log.e("Daniel","----editable.toString()---"+editable.toString());
                        Log.e("Daniel","----decoCompanyItem.getProductName()---"+decoCompanyItem.getLoanName());
                        if (editable.toString().contains(decoCompanyItem.getLoanName())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        } else if (decoCompanyItem.getLoanName().contains(editable.toString())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        }
                    }
                    Log.e("Daniel","----decoCompanyItemList.size()---"+decoCompanyItemList.size());
                    loanAdapter.setdatas(decoCompanyItemList);
                } else {
                    loanAdapter.setdatas(bankFinancItems);
                }

            }
        });
    }


    @OnClick({R.id.etSearch, R.id.ll_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
                etSearch.setFocusableInTouchMode(true);
                break;
            case R.id.ll_filter:
                drawerLayout.openDrawer(drawerContent);
                break;
        }
    }

    private void initDrawLayout() {
        Fragment fragment = new FilterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("departmentName", "");
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
    }

}
