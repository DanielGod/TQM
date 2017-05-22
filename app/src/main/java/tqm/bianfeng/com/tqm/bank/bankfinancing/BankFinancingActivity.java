package tqm.bianfeng.com.tqm.bank.bankfinancing;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
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
import tqm.bianfeng.com.tqm.bank.fragment.TestFilterFragment;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.AQueryParams;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.FilterEvens;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

public class BankFinancingActivity extends AppCompatActivity {
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
    @BindView(R.id.ivDeleteText)
    ImageView ivDeleteText;
    @BindView(R.id.YBJ_loding)
    ImageView YBJLoding;
    @BindView(R.id.YBJ_loding_txt)
    TextView YBJLodingTxt;

    @BindView(R.id.bankActivity_pageView_linear)
    LinearLayout bankActivityPageViewLinear;
    @BindView(R.id.bankActivity_focus_linear)
    LinearLayout bankActivityFocusLinear;
    @BindView(R.id.bankActivity_pageView_tv)
    TextView bankActivityPageViewTv;
    @BindView(R.id.bankActivity_pageView_img)
    ImageView bankActivityPageViewImg;
    @BindView(R.id.bankActivity_focus_tv)
    TextView bankActivityFocusTv;


    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 0;//每页开始的下标数
    private int mPagItemSize = 0;
    private BankFinancingAdapter bankFinancingAdapter;
    private List<BankFinancItem> mAllBankLoanItems;
    private boolean PullDown = false; //是否下拉


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_financing);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setToolBar(getResources().getString(R.string.bankFinancing));
        initRefreshlv();
        lodingIsFailOrSucess(1);
        mCompositeSubscription = new CompositeSubscription();
        initDrawLayout();
        initDate(pagNum, Constan.NOTPULLUP,null,null);
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etSearch.setFocusableInTouchMode(true);
                return false;
            }
        });

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

    private void initRefreshlv() {
        //设置刷新时显示的文本
        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);//设置模式在设置字体之前
        ILoadingLayout startLayout = mainPullRefreshLv.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel(getResources().getString(R.string.pullRefresh));
        startLayout.setRefreshingLabel(getResources().getString(R.string.loading));
        startLayout.setReleaseLabel(getResources().getString(R.string.releaseRefresh));

        ILoadingLayout endLayout = mainPullRefreshLv.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel(getResources().getString(R.string.PullLoading));
        endLayout.setRefreshingLabel(getResources().getString(R.string.loading));
        endLayout.setReleaseLabel(getResources().getString(R.string.releaseLoading));

        mainPullRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullDownToRefresh---");
                pagNum = 0;
                if (mAllBankLoanItems != null) {

                    mAllBankLoanItems.clear();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        if (isClickPageView) {
                            String queryParams = setQuerParams("financViews", Constan.DESC, "");//筛选条件 “浏览量”字段，降序
                            Log.i("Daniel", "---queryParams---" + queryParams);
                            initDate(pagNum, Constan.NOTPULLUP, null, queryParams);
                        }
                        if (isClickFocus) {
                            String queryParams = setQuerParams("atttenNum", Constan.DESC, "");//筛选条件 “关注”字段，降序
                            Log.i("Daniel", "---queryParams---" + queryParams);
                            initDate(pagNum, Constan.NOTPULLUP, null, queryParams);
                        }
                        if (!isClickPageView && !isClickFocus) {
                            initDate(pagNum, Constan.NOTPULLUP, null, null);
                        }
                    }
                }).start();


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullUpToRefresh---");
                pagNum = pagNum + Constan.PAGESIZE;
                Log.i("Daniel", "---onPullUpToRefresh---" + pagNum);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        initDate(pagNum, Constan.PULLUP, null, null);
                    }
                }).start();

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

    private void initDate(int pagNum, final boolean pullUp,String search,String json) {
        Log.e("Daniel", "---pagNum---" + pagNum);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankFinancItem(search,json, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankFinancItem>>() {
                    @Override
                    public void onCompleted() {
                        //设置可上拉刷新和下拉刷新
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
                        mainPullRefreshLv.setRefreshing(false);
                        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);
                        lodingIsFailOrSucess(3);

                    }

                    @DebugLog
                    @Override
                    public void onNext(BankListItems<BankFinancItem> bankLoanItemBankListItems) {
                        mPagItemSize = bankLoanItemBankListItems.getItem().size();
                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
                        Log.e("Daniel", "---pullUp---" + pullUp);
                        lodingIsFailOrSucess(2);
                        if (mAllBankLoanItems == null) {
                            mAllBankLoanItems = new ArrayList<>();
                        }
                        mAllBankLoanItems.addAll( bankLoanItemBankListItems.getItem());
                        if (pullUp) {
                            setAdapter(mAllBankLoanItems);
                        } else {
                            setAdapter(bankLoanItemBankListItems.getItem());
                        }

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FilterEvens event) {
        Log.i("Daniel", "---onEventMainThread---" + event.getFilterValue());
        initDate(pagNum, Constan.NOTPULLUP,null,event.getFilterValue());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread2(ListItemPositioin event) {
        if ("02".equals(event.getModule())) {
            Integer position = event.getPosition();
            Log.i("Daniel", "---onEventMainThread2---" + event.getPosition());
            //跳转银行理财详情
            Log.i("Daniel", "---getFinancId---" + bankFinancingAdapter.getItem(position).getFinancId());
            Log.i("Daniel", "---position---" + position);
            Intent intent = new Intent(BankFinancingActivity.this, DetailActivity.class);
            intent.putExtra("detailType", "02");
            intent.putExtra("detailId", bankFinancingAdapter.getItem(position).getFinancId());
            intent.putExtra("detailTitle", bankFinancingAdapter.getItem(position).getProductName());

            startActivity(intent);
        }

    }

    private void setAdapter(List<BankFinancItem> bankFinancItems) {
        if (bankFinancingAdapter == null) {
            bankFinancingAdapter = new BankFinancingAdapter(BankFinancingActivity.this, bankFinancItems, false);
            mainPullRefreshLv.setAdapter(bankFinancingAdapter);
        } else {
            bankFinancingAdapter.setdatas(bankFinancItems);
        }
        mainPullRefreshLv.onRefreshComplete();
        initEdi(bankFinancingAdapter, bankFinancItems);
    }

    public void initEdi(final BankFinancingAdapter bankFinancingAdapter, final List<BankFinancItem> bankFinancItems) {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivDeleteText.setVisibility(View.VISIBLE);
            }

            @DebugLog
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    Log.i("Daniel", "---editable.toString()---" + editable.toString());
                    initDate(0, Constan.NOTPULLUP, editable.toString(), null);
                }

            }
        });
    }

    private void initDrawLayout() {
        Fragment fragment = TestFilterFragment.newInstance(2);//1:贷款 2：；理财
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();

    }

    boolean isClickPageView = false;//点击浏览量
    boolean isClickFocus = false;//关注
    @OnClick({R.id.etSearch, R.id.ll_filter, R.id.ivDeleteText, R.id.bankActivity_pageView_linear,
            R.id.bankActivity_focus_linear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
                break;
            case R.id.ll_filter:
                drawerLayout.openDrawer(drawerContent);//筛选页
                break;
            case R.id.ivDeleteText:
                etSearch.setText("");
                ivDeleteText.setVisibility(View.GONE);
                etSearch.setFocusableInTouchMode(false);
                break;
            case R.id.bankActivity_pageView_linear:  //浏览量
                pageViewOnClick();
                break;
            case R.id.bankActivity_focus_linear:
                focusOnClick();
                break;

        }
    }

    /**
     * 点击关注
     */
    private void focusOnClick() {
        if (!isClickFocus) {
            bankActivityFocusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
            isClickFocus = true;
            bankActivityPageViewTv.setTextColor(getResources().getColor(R.color.home_hotActivity_gray9));
            bankActivityPageViewImg.setImageResource(R.drawable.asc_dark);
            isClickPageView = false;
            String queryParams = setQuerParams("atttenNum", Constan.DESC, "");//筛选条件 “关注”字段，降序
            Log.i("Daniel", "---queryParams---" + queryParams);
            initDate(0, Constan.NOTPULLUP, null, queryParams);
        } else {
            bankActivityFocusTv.setTextColor(getResources().getColor(R.color.home_hotActivity_gray9));
            isClickFocus = false;
            initDate(0, Constan.NOTPULLUP, null, null);
        }
    }

    /**
     * 点击浏览量
     */
    private void pageViewOnClick() {
        if (!isClickPageView) {
            bankActivityPageViewTv.setTextColor(getResources().getColor(R.color.colorPrimary));
            bankActivityPageViewImg.setImageResource(R.drawable.asc_light);
            isClickPageView = true;
            bankActivityFocusTv.setTextColor(getResources().getColor(R.color.home_hotActivity_gray9));
            isClickFocus = false;
            String queryParams = setQuerParams("financViews", Constan.DESC, "");//筛选条件 “浏览量”字段，降序
            Log.i("Daniel", "---queryParams---" + queryParams);
            initDate(0, Constan.NOTPULLUP, null, queryParams);
        } else {
            bankActivityPageViewTv.setTextColor(getResources().getColor(R.color.home_hotActivity_gray9));
            bankActivityPageViewImg.setImageResource(R.drawable.asc_dark);
            isClickPageView = false;
            initDate(0, Constan.NOTPULLUP, null, null);
        }
    }

    public  String order = null;  //查询字段
    public  String sort = null;   //asc:升序 desc：降序
    public  String institution = null; //查询机构名称
    private String setQuerParams(String pOrder, String pSort, String pInstitution) {
        order = pOrder;
        sort = pSort;
        institution = pInstitution;
        AQueryParams aQueryParams = new AQueryParams(order, sort, institution);
        return new Gson().toJson(aQueryParams);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
    }

}
