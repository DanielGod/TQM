package tqm.bianfeng.com.tqm.bank.bankloan;

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
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
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
    private int pagNum = 0;
    private int mPagItemSize = 0;
    BankLoanAdapter loanAdapter;
    List<BankLoanItem> mAllBankLoanItems;
    public  String order = null;  //查询字段
    public  String sort = null;   //asc:升序 desc：降序
    public  String institution = null; //查询机构名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_financing);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);//订阅
        //        mListener = (mListener) BankLoanActivity.this;
        setToolBar(getResources().getString(R.string.bankloan));
        mCompositeSubscription = new CompositeSubscription();

        initDrawLayout();//侧滑页面
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //搜索框获取焦点
                etSearch.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRefreshlv();//刷新初始化加载
        lodingIsFailOrSucess(1);//动画
        initDate(pagNum, Constan.NOTPULLUP,null,null);
    }

    /**
     * 初始化资源加载动画
     *
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
            //下拉监听
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullDownToRefresh---");
                //下拉从首页开始加载
                pagNum = 0;
                if (mAllBankLoanItems != null) {
                    //清空预存集合
                    mAllBankLoanItems.clear();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        if (isClickPageView) {
                            String queryParams = setQuerParams("activityViews", Constan.DESC, "");//筛选条件 “浏览量”字段，降序
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

            //上拉监听
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullUpToRefresh---");
                pagNum = pagNum + Constan.PAGESIZE;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        initDate(pagNum, Constan.PULLUP,null,null);
                    }
                }).start();

            }
        });

    }

    //接收筛选条件并请求数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FilterEvens event) {
        Log.i("Daniel", "---onEventMainThread---" + event.getFilterValue());
        initDate(pagNum, Constan.NOTPULLUP,null,event.getFilterValue());

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
     *
     * @param s
     */
    private void setToolBar(String s) {
        //        toolbar.setTitle(s);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    /**
     * 初始化数据
     * @param pagNum
     * @param pullUp
     * @param search
     * @param gson
     */
    private void initDate(int pagNum, final boolean pullUp, String search, String gson) {
        Log.i("Daniel", "---pagNum---" + pagNum);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankLoanItem(search,gson, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankLoanItem>>() {
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

                    @Override
                    public void onError(Throwable e) {
                        lodingIsFailOrSucess(3);
                        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);
                    }

                    @Override
                    public void onNext(BankListItems<BankLoanItem> bankLoanItemBankListItems) {
                        mPagItemSize = bankLoanItemBankListItems.getItem().size();
                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
                        Log.e("Daniel", "---pullUp---" + pullUp);
                        lodingIsFailOrSucess(2);
                        if (mAllBankLoanItems == null) {
                            mAllBankLoanItems = new ArrayList<>();
                        }
                        mAllBankLoanItems.addAll(bankLoanItemBankListItems.getItem());
                        if (pullUp) {
                            setAdapter(mAllBankLoanItems);
                        } else {
                            setAdapter(bankLoanItemBankListItems.getItem());
                        }
                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    /**
     * 设置适配器
     * @param bankloanItems
     */
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

    /**
     * 初始化搜索框监听
     * @param loanAdapter
     * @param bankFinancItems
     */
    public void initEdi(final BankLoanAdapter loanAdapter, final List<BankLoanItem> bankFinancItems) {
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
    boolean isClickPageView = false;//点击浏览量
    boolean isClickFocus = false;//关注

    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.etSearch, R.id.ll_filter, R.id.ivDeleteText, R.id.bankActivity_pageView_linear,
            R.id.bankActivity_focus_linear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
                //                etSearch.setFocusableInTouchMode(true);
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
            String queryParams = setQuerParams("loanViews", Constan.DESC, "");//筛选条件 “浏览量”字段，降序
            Log.i("Daniel", "---queryParams---" + queryParams);
            initDate(0, Constan.NOTPULLUP, null, queryParams);
        } else {
            bankActivityPageViewTv.setTextColor(getResources().getColor(R.color.home_hotActivity_gray9));
            bankActivityPageViewImg.setImageResource(R.drawable.asc_dark);
            isClickPageView = false;
            initDate(0, Constan.NOTPULLUP, null, null);
        }
    }

    private String setQuerParams(String pOrder, String pSort, String pInstitution) {
        order = pOrder;
        sort = pSort;
        institution = pInstitution;
        AQueryParams aQueryParams = new AQueryParams(order, sort, institution);
        return new Gson().toJson(aQueryParams);
    }

    /**
     * 初始化抽屉控件
     */
    private void initDrawLayout() {
        Fragment fragment = TestFilterFragment.newInstance(1);//1：贷款 2：理财
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
    }

}
