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
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
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


    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 1;
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
        initDate(null, pagNum, Constan.NOTPULLUP);
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
                pagNum = 1;
                if (mAllBankLoanItems != null) {

                    mAllBankLoanItems.clear();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        initDate(null, pagNum, Constan.NOTPULLUP);
                    }
                }).start();


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.i("Daniel", "---onPullUpToRefresh---");
                pagNum = pagNum + 1;
                Log.i("Daniel", "---onPullUpToRefresh---" + pagNum);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        initDate(null, pagNum, Constan.PULLUP);
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

    private void initDate(String json, int pagNum, final boolean pullUp) {
        Log.e("Daniel", "---pagNum---" + pagNum);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankFinancItem(json, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankFinancItem>>() {
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
                        mainPullRefreshLv.setMode(PullToRefreshBase.Mode.DISABLED);
                        lodingIsFailOrSucess(3);

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankFinancItem> bankFinancItems) {
                        mPagItemSize = bankFinancItems.size();
                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
                        Log.e("Daniel", "---pullUp---" + pullUp);
                        lodingIsFailOrSucess(2);
                        if (mAllBankLoanItems == null) {
                            mAllBankLoanItems = new ArrayList<>();
                        }
                        mAllBankLoanItems.addAll(bankFinancItems);
                        if (pullUp) {
                            setAdapter(mAllBankLoanItems);
                        } else {
                            setAdapter(bankFinancItems);
                        }

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FilterEvens event) {
        Log.i("Daniel", "---onEventMainThread---" + event.getFilterValue());
        initDate(event.getFilterValue(), pagNum, Constan.NOTPULLUP);

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
                    List<BankFinancItem> decoCompanyItemList = new ArrayList();
                    for (BankFinancItem decoCompanyItem : bankFinancItems) {
                        Log.e("Daniel", "----editable.toString()---" + editable.toString());
                        Log.e("Daniel", "----decoCompanyItem.getProductName()---" + decoCompanyItem.getProductName());
                        if (editable.toString().contains(decoCompanyItem.getProductName())) {
                            decoCompanyItemList.add(decoCompanyItem);
                        } else if (decoCompanyItem.getProductName().contains(editable.toString())) {
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

    private void initDrawLayout() {
        Fragment fragment = new FilterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("departmentName", "");
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();

    }


    @OnClick({R.id.etSearch, R.id.ll_filter, R.id.ivDeleteText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
//                etSearch.setFocusableInTouchMode(true);
                break;
            case R.id.ll_filter:
                drawerLayout.openDrawer(drawerContent);
                break;
            case R.id.ivDeleteText:
                etSearch.setText("");
                ivDeleteText.setVisibility(View.GONE);
                etSearch.setFocusableInTouchMode(false);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mCompositeSubscription.unsubscribe();
    }

}
