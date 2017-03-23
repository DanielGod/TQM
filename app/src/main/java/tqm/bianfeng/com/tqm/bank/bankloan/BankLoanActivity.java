package tqm.bianfeng.com.tqm.bank.bankloan;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
    @BindView(R.id.YBJ_loding)
    ImageView YBJLoding;
    @BindView(R.id.YBJ_loding_txt)
    TextView YBJLodingTxt;


    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 1;
    private int mPagItemSize = 0;
    BankLoanAdapter loanAdapter;
    List<BankLoanItem> mAllBankLoanItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_financing);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);//订阅
        //        mListener = (mListener) BankLoanActivity.this;
        setToolBar(getResources().getString(R.string.bankloan));
        lodingIsFailOrSucess(1);
        mCompositeSubscription = new CompositeSubscription();
        initDrawLayout();
        initDate(null, pagNum, Constan.NOTPULLUP);
        initRefreshlv();


    }

    /**
     * 初始化资源加载动画
     * @param i
     */
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
            //下拉监听
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullDownToRefresh---");
                //下拉从首页开始加载
                pagNum = 1;
                if (mAllBankLoanItems != null) {
                    //清空预存集合
                    mAllBankLoanItems.clear();
                }
                initDate(null, pagNum, Constan.NOTPULLUP);

            }
            //上拉监听
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullUpToRefresh---");
                pagNum = pagNum + 1;
                initDate(null, pagNum, Constan.PULLUP);
            }
        });

    }

    //接收筛选条件并请求数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FilterEvens event) {
        Log.i("Daniel", "---onEventMainThread---" + event.getFilterValue());
        initDate(event.getFilterValue(), pagNum, Constan.NOTPULLUP);

    }
    //接受跳转详情广播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread2(ListItemPositioin event) {
        if ("03".equals(event.getModule())) {
            Integer position = event.getPosition();
            //跳转银行贷款详情
            Intent intent = new Intent(BankLoanActivity.this, DetailActivity.class);
            intent.putExtra("detailType", "03");
            intent.putExtra("detailId", loanAdapter.getItem(position).getLoanId());
            startActivity(intent);
        }

    }

    /**
     * 初始化titleBar
     * @param s
     */
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

    private void initDate(String json, int pagNum, final boolean pullUp) {
        Log.i("Daniel", "---pagNum---" + pagNum);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankLoanItem(json, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankLoanItem>>() {
                    @Override
                    public void onCompleted() {
                        //设置可上拉刷新和下拉刷新
                        if (mPagItemSize == 0) {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);//不能行拉
                        } else if (mPagItemSize > 0 && mPagItemSize < Constan.PAGESIZE) {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
                        }
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        lodingIsFailOrSucess(3);
                        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankLoanItem> bankloanItems) {
                        mPagItemSize = bankloanItems.size();
                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
                        Log.e("Daniel", "---pullUp---" + pullUp);
                        lodingIsFailOrSucess(2);
                        if (mAllBankLoanItems == null) {
                            mAllBankLoanItems = new ArrayList<>();
                        }
                        mAllBankLoanItems.addAll(bankloanItems);
                        if (pullUp) {
                            setAdapter(mAllBankLoanItems);
                        } else {
                            setAdapter(bankloanItems);
                        }
                        //

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    private void setAdapter(List<BankLoanItem> bankloanItems) {
        if (loanAdapter == null) {
            Log.e("Daniel", "---loanAdapter111---" + bankloanItems.size());
            loanAdapter = new BankLoanAdapter(BankLoanActivity.this, bankloanItems, false);
            mainPullRefreshLv.setAdapter(loanAdapter);
        } else {
            Log.e("Daniel", "---loanAdapter2222---" + bankloanItems.size());
            loanAdapter.setdatas(bankloanItems);
        }
        mainPullRefreshLv.onRefreshComplete();
        initEdi(loanAdapter, bankloanItems);

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

                        if (editable.toString().contains(decoCompanyItem.getLoanName())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        } else if (decoCompanyItem.getLoanName().contains(editable.toString())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        }
                    }

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
