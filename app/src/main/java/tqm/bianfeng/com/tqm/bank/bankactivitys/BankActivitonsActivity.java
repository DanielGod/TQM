package tqm.bianfeng.com.tqm.bank.bankactivitys;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

public class BankActivitonsActivity extends AppCompatActivity {
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
    @BindView(R.id.YBJ_loding)
    ImageView YBJLoding;
    @BindView(R.id.YBJ_loding_txt)
    TextView YBJLodingTxt;

    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 1;
    private int mPagItemSize = 0;
    private List<BankActivityItem> mAllBankLoanItems;
    private BankActivitionsAdapter bankActivitionsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_financing);
        unbinder = ButterKnife.bind(this);
        mCompositeSubscription = new CompositeSubscription();
        EventBus.getDefault().register(this);
        //        initDrawLayout();
        setToolBar(getResources().getString(R.string.bankActivity));
        lodingIsFailOrSucess(1);
        initDate(pagNum, Constan.NOTPULLUP);
        initRefreshlv();

    }

    public void lodingIsFailOrSucess(int i) {
        if (i == 1) {
            //加载中
            YBJLoding.setVisibility(View.VISIBLE);
            YBJLodingTxt.setVisibility(View.VISIBLE);
            YBJLodingTxt.setText("加载中...");
            YBJLoding.setBackgroundResource(R.drawable.loding_anim_lists);
            AnimationDrawable anim = (AnimationDrawable) YBJLoding.getBackground();
            anim.start();

        } else if (i == 2) {
            //加载成功
            YBJLoding.setBackground(null);
            YBJLoding.setVisibility(View.GONE);
            YBJLodingTxt.setVisibility(View.GONE);
        } else {
            //加载失败
            YBJLoding.setVisibility(View.VISIBLE);
            YBJLodingTxt.setVisibility(View.VISIBLE);
            YBJLoding.setBackground(null);
            YBJLodingTxt.setText("加载失败，请检查网络连接");
            YBJLoding.setImageResource(R.drawable.ic_loding_fail);
        }
    }

    /**
     * 初始化下拉刷新，上拉加载
     */
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
                pagNum = 1;
                if (mAllBankLoanItems!=null){
                    mAllBankLoanItems.clear();
                }
                initDate(pagNum, Constan.NOTPULLUP);

            }

            @DebugLog
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullUpToRefresh---");
                pagNum = pagNum + 1;
                initDate(pagNum, Constan.PULLUP);

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

    private void initDate(int pagNum, final boolean pullUp) {
        Subscription getBankFinancItem_subscription = NetWork.getBankService().getBankActivityItem(Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankActivityItem>>() {
                    @DebugLog
                    @Override
                    public void onCompleted() {
                        //设置可上拉刷新和下拉刷新
                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
                        if (mPagItemSize == 0) {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);
                        } else if (mPagItemSize > 0 && mPagItemSize < Constan.PAGESIZE) {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
                        }

                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);
                        lodingIsFailOrSucess(3);

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankActivityItem> bankActivityItems) {
                        mPagItemSize = bankActivityItems.size();
                        lodingIsFailOrSucess(2);
                        if (mAllBankLoanItems == null) {
                            mAllBankLoanItems = new ArrayList<>();
                        }
                        mAllBankLoanItems.addAll(bankActivityItems);
                        if (pullUp) {
                            setAdapter(mAllBankLoanItems);
                        } else {

                            setAdapter(bankActivityItems);
                        }

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ListItemPositioin event) {
        if ("01".equals(event.getModule())) {
            Integer position = event.getPosition();
            Log.i("Daniel", "---onEventMainThread2---" + event.getPosition());
            //跳转银行活动详情
            Intent intent = new Intent(BankActivitonsActivity.this, DetailActivity.class);
            intent.putExtra("detailType", "01");
            intent.putExtra("detailId", bankActivitionsAdapter.getItem(position).getActivityId());
            startActivity(intent);
        }

    }

    private void setAdapter(List<BankActivityItem> bankActivityItems) {
        if (bankActivitionsAdapter == null) {
            bankActivitionsAdapter = new BankActivitionsAdapter(bankActivityItems, BankActivitonsActivity.this, false);
            mainPullRefreshLv.setAdapter(bankActivitionsAdapter);
        } else {
            bankActivitionsAdapter.setdatas(bankActivityItems);
        }
        mainPullRefreshLv.onRefreshComplete();
        initEdi(bankActivitionsAdapter, bankActivityItems);


    }

    /**
     * 查询
     *
     * @param bankFinancingAdapter
     * @param bankFinancItems
     */
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
                        Log.e("Daniel", "----editable.toString()---" + editable.toString());
                        Log.e("Daniel", "----decoCompanyItem.getProductName()---" + decoCompanyItem.getInstitutionName());
                        if (editable.toString().contains(decoCompanyItem.getInstitutionName())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        } else if (decoCompanyItem.getInstitutionName().contains(editable.toString())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        }
                    }
                    Log.e("Daniel", "----decoCompanyItemList.size()---" + decoCompanyItemList.size());
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
