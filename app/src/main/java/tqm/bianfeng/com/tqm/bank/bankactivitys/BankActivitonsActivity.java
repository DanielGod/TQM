package tqm.bianfeng.com.tqm.bank.bankactivitys;

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
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.FilterEvens;
import tqm.bianfeng.com.tqm.pojo.bank.Institution;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

import static tqm.bianfeng.com.tqm.R.string.bankActivity;

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
    @BindView(R.id.ivDeleteText)
    ImageView ivDeleteText;




    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 0;
    private int mPagItemSize = 0;
    private List<BankActivityItem> mAllBankLoanItems;
    private BankActivitionsAdapter bankActivitionsAdapter;
    private boolean mIsbottom = false;//判断数据加载完毕
    public  String order = null;  //查询字段
    public  String sort = null;   //asc:升序 desc：降序
    public  String institution = null; //查询机构名称
    ArrayList<String> mDataset;//机构集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_activity);
        unbinder = ButterKnife.bind(this);
        mCompositeSubscription = new CompositeSubscription();
        EventBus.getDefault().register(this);
        initDrawLayout();
//        getBankInstitution();
//        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //                Toast.makeText(BankActivitonsActivity.this, ""+position, Toast.LENGTH_SHORT).show();
//                String queryParams = setQuerParams(null, null, mDataset.get(position));//筛选条件 “机构”字段
//                Log.i("Daniel", "---queryParams---" + queryParams);
//                initDate(0, Constan.NOTPULLUP, null, queryParams);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        setToolBar(getResources().getString(bankActivity));
        initRefreshlv();
        lodingIsFailOrSucess(1);
        initDate(0, Constan.NOTPULLUP, null, null);

        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etSearch.setFocusableInTouchMode(true);
                return false;
            }
        });

    }

    /**
     * 获取银行机构
     */
    private void getBankInstitution() {
        Subscription subscribe_getInstitutions = NetWork.getBankService().getInstitutions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Institution>>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(List<Institution> institutions) {

                        if (mDataset == null) {
                            mDataset = new ArrayList<>();
                        }
                        for (Institution institution1 : institutions) {
                            mDataset.add(institution1.getInstitutionName());
                        }
//                        niceSpinner.attachDataSource(mDataset);

                    }
                });
        mCompositeSubscription.add(subscribe_getInstitutions);

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
        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);//设置模式在设置字体之前
        ILoadingLayout endLayout = mainPullRefreshLv.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel(getResources().getString(R.string.PullLoading));
        endLayout.setRefreshingLabel(getResources().getString(R.string.loading));
        endLayout.setReleaseLabel(getResources().getString(R.string.releaseLoading));

        ILoadingLayout startLayout = mainPullRefreshLv.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel(getResources().getString(R.string.pullRefresh));
        startLayout.setRefreshingLabel(getResources().getString(R.string.loading));
        startLayout.setReleaseLabel(getResources().getString(R.string.releaseRefresh));


        mainPullRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @DebugLog
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

            @DebugLog
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullUpToRefresh---");

                pagNum = pagNum + Constan.PAGESIZE;
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FilterEvens event) {
        Log.i("Daniel", "---onEventMainThread---" + event.getFilterValue());
        initDate(pagNum, Constan.NOTPULLUP,null,event.getFilterValue());

    }
    private void initDate(int pagNum, final boolean pullUp, String search, String gson) {
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankActivityItem(search, gson, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankActivityItem>>() {
                    @Override
                    public void onCompleted() {
                        //设置可上拉刷新和下拉刷新
                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
                        if (mPagItemSize == 0) {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);
                        } else if (mPagItemSize > 0 && mPagItemSize < Constan.PAGESIZE) {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            mIsbottom = true;
                        } else {
                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);
                        lodingIsFailOrSucess(3);
                    }

                    @Override
                    public void onNext(BankListItems<BankActivityItem> bankActivityItemBankListItems) {
                        mPagItemSize = bankActivityItemBankListItems.getItem().size();
                        lodingIsFailOrSucess(2);
                        if (mAllBankLoanItems == null) {
                            mAllBankLoanItems = new ArrayList<BankActivityItem>();
                        }
                        mAllBankLoanItems.addAll(bankActivityItemBankListItems.getItem());
                        if (pullUp) {
                            setAdapter(mAllBankLoanItems);
                        } else {

                            setAdapter(bankActivityItemBankListItems.getItem());
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
            intent.putExtra("detailTitle", bankActivitionsAdapter.getItem(position).getActivityTitle());
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
                if ("".equals(etSearch.getText().toString())){
                    ivDeleteText.setVisibility(View.GONE);
                }else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
                initDate(0, Constan.NOTPULLUP, editable.toString(), null);

            }
        });
    }

    //
    private void initDrawLayout() {
        Fragment fragment = TestFilterFragment.newInstance(3);//1:贷款 2：；理财 3：活动
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
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
                initDate(0, Constan.NOTPULLUP, null, null);
                break;
            case R.id.bankActivity_pageView_linear:  //浏览量
//                if (!isClickPageView) {
//                    bankActivityPageViewTv.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    bankActivityPageViewImg.setImageResource(R.drawable.asc_light);
//                    isClickPageView = true;
//                    bankActivityFocusTv.setTextColor(getResources().getColor(R.color.home_hotActivity_gray9));
//                    isClickFocus = false;
//                    String queryParams = setQuerParams("activityViews", Constan.DESC, "");//筛选条件 “浏览量”字段，降序
//                    Log.i("Daniel", "---queryParams---" + queryParams);
//                    initDate(0, Constan.NOTPULLUP, null, queryParams);
//                } else {
//                    bankActivityPageViewTv.setTextColor(getResources().getColor(R.color.home_hotActivity_gray9));
//                    bankActivityPageViewImg.setImageResource(R.drawable.asc_dark);
//                    isClickPageView = false;
//                    initDate(0, Constan.NOTPULLUP, null, null);
//                }
                break;
            case R.id.bankActivity_focus_linear:
//                if (!isClickFocus) {
//                    bankActivityFocusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    isClickFocus = true;
//                    String queryParams = setQuerParams("atttenNum", Constan.DESC, "");//筛选条件 “关注”字段，降序
//                    Log.i("Daniel", "---queryParams---" + queryParams);
//                    initDate(0, Constan.NOTPULLUP, null, queryParams);
//                } else {
//                    bankActivityFocusTv.setTextColor(getResources().getColor(R.color.home_hotActivity_gray9));
//                    isClickFocus = false;
//                    initDate(0, Constan.NOTPULLUP, null, null);
//                }
                break;

        }
    }

    private String setQuerParams(String pOrder, String pSort, String pInstitution) {
        order = pOrder;
        sort = pSort;
        institution = pInstitution;
        AQueryParams aQueryParams = new AQueryParams(order, sort, institution);
        return new Gson().toJson(aQueryParams);
    }
}
