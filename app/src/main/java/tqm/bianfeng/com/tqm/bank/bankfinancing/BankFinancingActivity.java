package tqm.bianfeng.com.tqm.bank.bankfinancing;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.TextView;

import com.google.gson.Gson;

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
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.CustomView.LoadMoreView;
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

import static tqm.bianfeng.com.tqm.R.id.ivDeleteText;

public class BankFinancingActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_content)
    FrameLayout drawerContent;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;

    TextView bankActivityFocusTv;


    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pagNum = 0;//每页开始的下标数
    private int mPagItemSize = 0;
    private BankFinancingAdapter bankFinancingAdapter;
    private List<BankFinancItem> mAllBankLoanItems;
    private boolean PullDown = false; //是否下拉
    private List<BankFinancItem> datas;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_financing);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setToolBar(getResources().getString(R.string.bankFinancing));
//        mainPullRefreshLv.setDividerDrawable(getResources().getDrawable(R.drawable.img_heard_dark));
        mCompositeSubscription = new CompositeSubscription();
        initDrawLayout();
        defaultLoadview.lodingIsFailOrSucess(1);
        initDate(0, Constan.NOTPULLUP,null,null);
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etSearch.setFocusableInTouchMode(true);
                return false;
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
    LoadMoreView loadMoreTxt;
    private void initDate(int pagNum, final boolean pullUp,String search,String json) {
        Log.e("Daniel", "---pagNum---" + pagNum);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankFinancItem(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankFinancItem>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        defaultLoadview.lodingIsFailOrSucess(3);

                    }

                    @DebugLog
                    @Override
                    public void onNext(BankListItems<BankFinancItem> bankLoanItemBankListItems) {
                        defaultLoadview.lodingIsFailOrSucess(2);
                        mPagItemSize = bankLoanItemBankListItems.getItem().size();
                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
                        Log.e("Daniel", "---pullUp---" + pullUp);
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

        } else {
            bankFinancingAdapter.setdatas(bankFinancItems);
        }
        initEdi(bankFinancingAdapter, bankFinancItems);
    }

    public void initEdi(final BankFinancingAdapter bankFinancingAdapter, final List<BankFinancItem> bankFinancItems) {
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

                initDate(0, Constan.NOTPULLUP, editable.toString(), null);

            }
        });
    }

    private void initDrawLayout() {
        Fragment fragment = TestFilterFragment.newInstance(2);//1:贷款 2：；理财 3：活动
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();

    }

    boolean isClickPageView = false;//点击浏览量
    boolean isClickFocus = false;//关注
    @OnClick({R.id.etSearch,  ivDeleteText}
         )
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
                break;

            case ivDeleteText:
                etSearch.setText("");

                etSearch.setFocusableInTouchMode(false);
                initDate(0, Constan.NOTPULLUP, null, null);
                break;
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
