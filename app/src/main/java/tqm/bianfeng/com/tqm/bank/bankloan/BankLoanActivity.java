package tqm.bianfeng.com.tqm.bank.bankloan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.CustomView.DropDownMenu;
import tqm.bianfeng.com.tqm.CustomView.LoadMoreView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.bank.bankfinancing.BankFinancingAdapter2;
import tqm.bianfeng.com.tqm.bank.fragment.TestFilterFragment;
import tqm.bianfeng.com.tqm.lawhelp.adapter.ListDropDownAdapter;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.main.MainActivity;
import tqm.bianfeng.com.tqm.pojo.LoanSearch;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.FilterEvens;
import tqm.bianfeng.com.tqm.pojo.bank.FilterLoanVaule;
import tqm.bianfeng.com.tqm.pojo.bank.LoanType;
import tqm.bianfeng.com.tqm.pojo.bank.ProductType;
import tqm.bianfeng.com.tqm.pojo.bank.RiskGrade;

import static tqm.bianfeng.com.tqm.network.NetWork.getBankService;

public class BankLoanActivity extends AppCompatActivity {

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivDeleteText)
    ImageView ivDeleteText;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;
    @BindView(R.id.drawer_content)
    FrameLayout drawerContent;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    private CompositeSubscription mCompositeSubscription;
    private Unbinder unbinder;
    private int pageNum = 0;
    private int mPagItemSize = 0;
    BankLoanAdapter2 loanAdapter;
    BankFinancingAdapter2 financingAdapter;

    public String order = null;  //查询字段
    public String sort = null;   //asc:升序 desc：降序
    private String headers[] = {"适用人群", "贷款类型", "筛选"};
    private String headers1[] = {"产品类型", "风险等级", "筛选"};
    private List<BankLoanItem> datas;
    private List<BankFinancItem> datasFinancItem;
    List<String> crowdList;
    List<String> loanTypeList;
    List<String> RiskGradesList;
    List<String> producTypeList;

    private List<View> popupViews = new ArrayList<>();
    private ListDropDownAdapter crowdAdapter;
    private ListDropDownAdapter loanTypeAdapter;
    private ListDropDownAdapter productAdapter;
    private ListDropDownAdapter RiskGradesAdapter;
    private ListDropDownAdapter baseAdapter;
    RecyclerView contentView;
    String  isLoan ;
    LoanSearch loanSearch;//筛选条件对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_myloan);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);//订阅
        mCompositeSubscription = new CompositeSubscription();

        defaultLoadview.lodingIsFailOrSucess(1);
        isLoan = getIntent().getStringExtra("isLoan");//01-理财，02-贷款
        Log.e(Constan.LOGTAGNAME,"判断是贷款还是理财"+isLoan);
        if ("01".equals(isLoan)){
            setToolBar(getResources().getString(R.string.bankFinancing));
        }else if ("02".equals(isLoan)){
            setToolBar(getResources().getString(R.string.bankloan));
        }
        //初始化搜索监听
        initEdi();
        crowdList = new ArrayList<>(); //适合人群
        crowdList.add(getResources().getString(R.string.all));
        loanTypeList = new ArrayList<>(); //贷款类型
        loanTypeList.add(getResources().getString(R.string.all));
        RiskGradesList = new ArrayList<>(); //风险等级
        RiskGradesList.add(getResources().getString(R.string.all));
        producTypeList = new ArrayList<>(); //产品类型
        producTypeList.add(getResources().getString(R.string.all));
        datas = new ArrayList<>();
        datasFinancItem = new ArrayList<>();
        if (loanSearch==null){
            loanSearch = new LoanSearch();
        }
        //初始化顶部选择器数据
        initSpecialFields();
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
    protected void onResume() {
        super.onResume();

//        pageNum = 0;
//        pullUp = Constan.NOTPULLUP;
//        initDate();
//
    }

    //获取顶部选择器条件
    public void initSpecialFields() {
        if ("02".equals(isLoan)){
            //获取贷款类型
            getLoanTypes();
        }else {
            //获取产品类型和风险等级
            getProductTypes();
        }
    }

    private void getRiskGrades() {
        Subscription getSpecialFields = getBankService().getRiskGrades()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RiskGrade>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (datasFinancItem.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                    }
                    @Override
                    public void onNext(List<RiskGrade> RiskGrades) {
                        for (RiskGrade riskGrade : RiskGrades) {
                            RiskGradesList.add(riskGrade.getRiskGradeName());
                        }
                        Log.e("Daniel", "风险等级：" + RiskGradesList.toString());
                        //初始化DropMenu
                        iniDropMenu();
                    }
                });
        mCompositeSubscription.add(getSpecialFields);

    }

    private void getProductTypes() {
        Subscription getSpecialFields = getBankService().getProductTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ProductType>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (datasFinancItem.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                    }
                    @Override
                    public void onNext(List<ProductType> ProductTypes) {
                        for (ProductType productType : ProductTypes) {
                            producTypeList.add(productType.getProductTypeName());
                        }
                        Log.e("Daniel", "产品类型：" + producTypeList.toString());
                        getRiskGrades();
                    }
                });
        mCompositeSubscription.add(getSpecialFields);
    }
    //获取贷款类型
    private void getLoanTypes() {
        Subscription getSpecialFields = getBankService().getLoanTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LoanType>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (datas.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                    }
                    @Override
                    public void onNext(List<LoanType> loanTypes) {
                        for (LoanType loanType : loanTypes) {
                            loanTypeList.add(loanType.getLoanTypeName());
                        }
                        Log.e("Daniel", "贷款类型：" + loanTypeList.toString());
                        //初始化DropMenu
                        iniDropMenu();
                    }
                });
        mCompositeSubscription.add(getSpecialFields);
    }

    //初始化选择器
    public void iniDropMenu() {
        if ("02".equals(isLoan)){
        //适用人群下拉列表
        String[] loanCrowdArray = getResources().getStringArray(R.array.loan_crowd);
        for (int i = 0; i < loanCrowdArray.length; i++) {
            crowdList.add(loanCrowdArray[i]);
        }
        final ListView crowdView = new ListView(BankLoanActivity.this);
        crowdView.setDividerHeight(0);
        crowdAdapter = new ListDropDownAdapter(BankLoanActivity.this, crowdList);
        crowdView.setAdapter(crowdAdapter);
        popupViews.add(crowdView);
        //贷款类型下拉列表
        ListView loanTypeView = new ListView(BankLoanActivity.this);
        loanTypeView.setDividerHeight(0);
        loanTypeAdapter = new ListDropDownAdapter(BankLoanActivity.this, loanTypeList);
        loanTypeView.setAdapter(loanTypeAdapter);
        popupViews.add(loanTypeView);
            //适用人群点击
        crowdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("Daniel", "选择：" + i);
                crowdAdapter.setCheckItem(i);
                //                setLawAddDistrictOrSpecialField(i == 0 ? "" : districts.get(i), 0);
                dropDownMenu.setTabText(crowdList.get(i));
                //更新数据
                //非首页
                loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
                //页数
                loanSearch.setPageNum(0);
                //条数
                loanSearch.setPageSize(Constan.PAGESIZE);
                //城市
                loanSearch.setCity(MainActivity.locationStr);
                //如果选择全部就把条件置空
                if (i==0){
                    loanSearch.setCrowd("");
                }else {
                    loanSearch.setCrowd(crowdList.get(i));
                }
                initDate(true,loanSearch);
                dropDownMenu.closeMenu();
            }
        });
            //贷款类型点击
        loanTypeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("Daniel", "选择：" + i);
                loanTypeAdapter.setCheckItem(i);
                //                setLawAddDistrictOrSpecialField(i == 0 ? "" : specialFields.get(i), 1);
                dropDownMenu.setTabText(loanTypeList.get(i));

                //更新数据
                //非首页
                loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
                //页数
                loanSearch.setPageNum(0);
                //条数
                loanSearch.setPageSize(Constan.PAGESIZE);
                //城市
                loanSearch.setCity(MainActivity.locationStr);
                //如果选择全部就把条件置空
                if (i==0){
                    loanSearch.setLoanType("");
                }else {
                    loanSearch.setLoanType(loanTypeList.get(i));
                }
                initDate(true,loanSearch);
                //                initListData(true, lawAdd.getQueryParams(), 1);
                dropDownMenu.closeMenu();
            }
        });
        }else if ("01".equals(isLoan)){
            //产品类型下拉列表
            ListView productTypeView = new ListView(BankLoanActivity.this);
            productTypeView.setDividerHeight(0);
            productAdapter = new ListDropDownAdapter(BankLoanActivity.this, producTypeList);
            productTypeView.setAdapter(productAdapter);
            popupViews.add(productTypeView);

            //风险等级下拉列表
            ListView  RiskGradesView = new ListView(BankLoanActivity.this);
            RiskGradesView.setDividerHeight(0);
            RiskGradesAdapter = new ListDropDownAdapter(BankLoanActivity.this,  RiskGradesList);
            RiskGradesView.setAdapter(RiskGradesAdapter);
            popupViews.add(RiskGradesView);
            //产品类型点击
            productTypeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    productAdapter.setCheckItem(i);
                    //                setLawAddDistrictOrSpecialField(i == 0 ? "" : districts.get(i), 0);
                    dropDownMenu.setTabText(producTypeList.get(i));
                    //更新数据
                    //非首页
                    loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
                    //页数
                    loanSearch.setPageNum(0);
                    //条数
                    loanSearch.setPageSize(Constan.PAGESIZE);
                    //城市
                    loanSearch.setCity(MainActivity.locationStr);
                    if (i==0){
                        loanSearch.setProductType("");
                    }else{

                        loanSearch.setProductType(producTypeList.get(i));
                    }
                    initDate(true,loanSearch);
                    dropDownMenu.closeMenu();
                }
            });
            //风险等级点击
            RiskGradesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RiskGradesAdapter.setCheckItem(i);
                    //                setLawAddDistrictOrSpecialField(i == 0 ? "" : specialFields.get(i), 1);
                    dropDownMenu.setTabText(RiskGradesList.get(i));
                    //更新数据
                    //非首页
                    loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
                    //页数
                    loanSearch.setPageNum(0);
                    //条数
                    loanSearch.setPageSize(Constan.PAGESIZE);
                    //城市
                    loanSearch.setCity(MainActivity.locationStr);
                    if (i==0){
                        loanSearch.setRiskGrade("");
                    }else {
                        loanSearch.setRiskGrade(RiskGradesList.get(i));
                    }

                    initDate(true,loanSearch);
                    //                initListData(true, lawAdd.getQueryParams(), 1);
                    dropDownMenu.closeMenu();
                }
            });
        }
        //筛选
        ListView districtView0 = new ListView(BankLoanActivity.this);
        districtView0.setDividerHeight(0);
        baseAdapter = new ListDropDownAdapter(BankLoanActivity.this, new ArrayList<String>());
        districtView0.setAdapter(baseAdapter);
        popupViews.add(districtView0);

        RelativeLayout contentRel = new RelativeLayout(BankLoanActivity.this);
        contentRel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //设置内容
        contentView = new RecyclerView(BankLoanActivity.this);
        //根据数据更新
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        contentView.setLayoutManager(new LinearLayoutManager(this));

        //        贷款初始化数据
        if ("02".equals(isLoan)){
            //非首页
            loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
            //页数
            loanSearch.setPageNum(0);
            //条数
            loanSearch.setPageSize(Constan.PAGESIZE);
            //城市
            loanSearch.setCity(MainActivity.locationStr);
            initDate(true,loanSearch);
            contentRel.addView(contentView);
        }else if ("01".equals(isLoan)){
            //        理财初始化数据
            //非首页
            loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
            //页数
            loanSearch.setPageNum(0);
            //条数
            loanSearch.setPageSize(Constan.PAGESIZE);
            //城市
            loanSearch.setCity(MainActivity.locationStr);
            initDate(true,loanSearch);
            contentRel.addView(contentView);
        }

        if ("02".equals(isLoan)){
            dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentRel);
        }else if ("01".equals(isLoan)){
            dropDownMenu.setDropDownMenu(Arrays.asList(headers1), popupViews, contentRel);
        }
        dropDownMenu.setOnClickLinsener(new DropDownMenu.OnClickLinsener() {
            @Override
            public boolean onClickIndexOne(int index) {
                Log.e("Daniel", "贷款：" + index);
                Log.e("Daniel", "dropDownMenu是否处于可见状态：" + dropDownMenu.isShowing());
                //根据选择器是否下拉显示无数据提示
                if (!dropDownMenu.isShowing()) {
                    defaultLoadview.lodingIsFailOrSucess(2);
                }
                //跳转地址选择页
                boolean returnB =true;
                if (index == 0) {

                } else if (index == 1) {

                } else {
                    drawerLayout.openDrawer(drawerContent);
                    dropDownMenu.closeMenu();
                }
                return returnB;
            }

            @Override
            public void onClose() {
                if ("02".equals(isLoan)) {
                    //关闭时显示无数据提示
                    if (contentView.getChildCount() == 0 || datas.size() == 0) {
                        defaultLoadview.lodingIsFailOrSucess(3);
                    }
                }else if ("01".equals(isLoan)){
                    //关闭时显示无数据提示
                    if (contentView.getChildCount() == 0 || datasFinancItem.size() == 0) {
                        defaultLoadview.lodingIsFailOrSucess(3);
                    }
                }
            }
        });
        //        initDistricts(lawAdd.getCity());
    }
    //接收筛选条件并请求数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FilterEvens event) {
        FilterLoanVaule filterValues = new Gson().fromJson(event.getFilterValue(),FilterLoanVaule.class);
        Log.i("Daniel", "---onEventMainThread---" + event.getFilterValue());
        Log.i("Daniel", "---onEventMainThread---" + isLoan);
        if (filterValues!=null){
            //非首页
            loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
            //页数
            loanSearch.setPageNum(0);
            //条数
            loanSearch.setPageSize(Constan.PAGESIZE);
            //城市
            loanSearch.setCity(MainActivity.locationStr);
            loanSearch.setInstitution(filterValues.getInstitution());
                loanSearch.setRateMin(filterValues.getRateMin());
                loanSearch.setRateMax(filterValues.getRateMax());
                loanSearch.setLoanMoneyMin(filterValues.getLoanMoneyMin());
                loanSearch.setLoanMoneyMax(filterValues.getLoanMoneyMax());
                loanSearch.setLoanPeriodMin(filterValues.getLoanPeriodMin());
                loanSearch.setLoanPeriodMax(filterValues.getLoanPerioMax());
                loanSearch.setInvestmentModel(filterValues.getInvestmentModel());
                loanSearch.setInvestmentTermMin(filterValues.getInvestmentTermMin());
                loanSearch.setInvestmentTermMax(filterValues.getInvestmentTermMax());
            initDate(true,loanSearch);
        }


    }
    //接收下载
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String str) {

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
     * 获取贷款信息
     * -----非空-----
     *
     * @param pageNum 页数
     * -----非必须-----
     * @param search 模糊查询
     * @param institution 机构
     * @param loanType 贷款类型
     * @param crowd 适合人群
     * @param rateMin 最小利率
     * @param rateMax 最大利率
     * @param loanMoneyMin 最小贷款金额
     * @param loanMoneyMax 最大贷款金额
     * @param loanPeriodMin 最小贷款期限
     * @param loanPeriodMax 最大贷款期限
     */

    boolean pullUp;
    private int nowIndex = 0;

    private LoadMoreView loadMoreTxt;//加载更多文字

    private void initDate(final boolean isRefresh,LoanSearch loanSearch) {
        //设置数据
        String queryParams = new Gson().toJson(loanSearch);
        Log.e(Constan.LOGTAGNAME, "json：" + queryParams);
        //开始加载动画
        if ("02".equals(isLoan)){
            if (datas.size() > 0) {
                if (loadMoreTxt != null) {
                    loadMoreTxt.loadMoreViewAnim(1);
                }
            } else {
                if (loadMoreTxt != null) {
                    loadMoreTxt.loadMoreViewAnim(4);
                }
            }
            //获取贷款列表
            getBankLoanItem(isRefresh,queryParams);
        }else if ("01".equals(isLoan)){
            if (datasFinancItem.size() > 0) {
                if (loadMoreTxt != null) {
                    loadMoreTxt.loadMoreViewAnim(1);
                }
            } else {
                if (loadMoreTxt != null) {
                    loadMoreTxt.loadMoreViewAnim(4);
                }
            }
            //获取理财列表
            getBankFinancingItem(isRefresh,queryParams);
        }
    }

    private void getBankFinancingItem(final boolean isRefresh, String queryParams) {
        Subscription getBankFinancItem_subscription = getBankService().getBankFinancItem(queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankFinancItem>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(Constan.LOGTAGNAME,"理财列表获取失败："+e.toString());
                        if (datasFinancItem.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                        if (loadMoreTxt != null) {
                            loadMoreTxt.loadMoreViewAnim(4);
                        }
                    }
                    @Override
                    public void onNext(BankListItems<BankFinancItem> bankFinancItemBankListItems) {
                        //判断是刷新还是加载更多
                        if (!isRefresh) {
                            pageNum++;
                        } else {
                            datasFinancItem = new ArrayList<>();
                            pageNum = 0;
                        }
                        for (BankFinancItem bankfinancItem : bankFinancItemBankListItems.getItem()) {
                            datasFinancItem.add(bankfinancItem);
                        }
                        //显示理财数据
                        Logger.i(datasFinancItem.toString());
                        Log.e(Constan.LOGTAGNAME,"理财列表："+datasFinancItem.toString());
                        initFinancItemList(datasFinancItem);

                        //加载更多判断
                        loadMoreTxt.doLoad(datasFinancItem.size(), bankFinancItemBankListItems.getItem().size());
                        //                isOnLoading = false;
                        if (datasFinancItem.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    private void initFinancItemList(List<BankFinancItem> datasFinancItem) {
        //数据有无提示判断
        if (datasFinancItem.size() == 0) {
            defaultLoadview.lodingIsFailOrSucess(3);
        } else {
            defaultLoadview.lodingIsFailOrSucess(2);
        }
        Logger.d(datasFinancItem.toString());
        Log.e("Daniel", "理财列表" + datasFinancItem.toString());
        //初始化列表
        if (financingAdapter == null) {
            financingAdapter = new BankFinancingAdapter2(BankLoanActivity.this, datasFinancItem);
            //添加上拉加载
            mLoadMoreWrapper = new LoadMoreWrapper(financingAdapter);
            loadMoreView = getLayoutInflater().inflate(R.layout.default_loading, null);
            loadMoreTxt = (LoadMoreView) loadMoreView.findViewById(R.id.load_more_txt);
            loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreWrapper.setLoadMoreView(loadMoreView);
            //加载监听
            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    //在此开起加载动画，更新数据
                    Log.e("gqf", "onLoadMoreRequested");
                    if (financingAdapter.getItemCount() % 10 == 0 && financingAdapter.getItemCount() != 0) {
                        pageNum = pageNum+Constan.PAGESIZE;
                        LoanSearch loanSearch = new LoanSearch();
                        //非首页
                        loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
                        //页数
                        loanSearch.setPageNum(pageNum);
                        //条数
                        loanSearch.setPageSize(Constan.PAGESIZE);
                        //城市
                        loanSearch.setCity(MainActivity.locationStr);
                        initDate(false,loanSearch);
                    }
                }
            });
            contentView.setLayoutManager(new LinearLayoutManager(BankLoanActivity.this));
            contentView.setAdapter(mLoadMoreWrapper);
            financingAdapter.setOnItemClickListener(new BankFinancingAdapter2.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {

                    Intent intent = new Intent(BankLoanActivity.this, DetailActivity.class);
                    intent.putExtra("detailType", "02");
                    intent.putExtra("detailId", financingAdapter.getDataItem(position).getFinancId());
                    intent.putExtra("detailTitle", financingAdapter.getDataItem(position).getProductName());
                    startActivity(intent);
                }
            });
        } else {
            financingAdapter.update(datasFinancItem);
            mLoadMoreWrapper.notifyDataSetChanged();
        }

    }

    private void getBankLoanItem(final boolean isRefresh, String queryParams) {
        Subscription getBankFinancItem_subscription = getBankService().getBankLoanItem(queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankLoanItem>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (datas.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                        if (loadMoreTxt != null) {
                            loadMoreTxt.loadMoreViewAnim(4);
                        }
                    }
                    @Override
                    public void onNext(BankListItems<BankLoanItem> bankLoanItemBankListItems) {
                        //判断是刷新还是加载更多
                        if (!isRefresh) {
                            pageNum++;
                        } else {
                            datas = new ArrayList<>();
                            pageNum = 0;
                        }
                        for (BankLoanItem bankLoanItem: bankLoanItemBankListItems.getItem()) {
                            datas.add(bankLoanItem);
                        }
                        //显示贷款数据
                        initLoanList(datas);

                        //加载更多判断
                        loadMoreTxt.doLoad(datas.size(), bankLoanItemBankListItems.getItem().size());
                        //                isOnLoading = false;
                        if (datas.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    LoadMoreWrapper mLoadMoreWrapper;
    private View loadMoreView;
    private void initLoanList(List<BankLoanItem> datas) {
        //数据有无提示判断
        if (datas.size() == 0) {
            defaultLoadview.lodingIsFailOrSucess(3);
        } else {
            defaultLoadview.lodingIsFailOrSucess(2);
        }
        Logger.d(datas.toString());
        Log.e("Daniel", "贷款列表" + datas.toString());
        Log.e("Daniel", "贷款列表个数" + datas.size());
        //初始化列表
        if (loanAdapter == null) {
            loanAdapter = new BankLoanAdapter2(BankLoanActivity.this, datas);
            //添加上拉加载
            mLoadMoreWrapper = new LoadMoreWrapper(loanAdapter);
            loadMoreView = getLayoutInflater().inflate(R.layout.default_loading, null);
            loadMoreTxt = (LoadMoreView) loadMoreView.findViewById(R.id.load_more_txt);
            loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreWrapper.setLoadMoreView(loadMoreView);
            //加载监听
            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    //在此开起加载动画，更新数据
                    Log.e("gqf", "onLoadMoreRequested");
                    if (loanAdapter.getItemCount() % 10 == 0 && loanAdapter.getItemCount() != 0) {
                        pageNum = pageNum+Constan.PAGESIZE;
                        loanSearch = new LoanSearch();
                        //非首页
                        loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
                        //页数
                        loanSearch.setPageNum(pageNum);
                        //条数
                        loanSearch.setPageSize(Constan.PAGESIZE);
                        //城市
                        loanSearch.setCity(MainActivity.locationStr);
                        initDate(false,loanSearch);
                    }
                }
            });
            contentView.setLayoutManager(new LinearLayoutManager(BankLoanActivity.this));
            contentView.setAdapter(mLoadMoreWrapper);
            loanAdapter.setOnItemClickListener(new BankLoanAdapter2.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
//                    Toast.makeText(BankLoanActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BankLoanActivity.this, DetailActivity.class);
                    intent.putExtra("detailType", "03");
                    intent.putExtra("detailId", loanAdapter.getDataItem(position).getLoanId());
                    //            intent.putExtra("detailTitle", loanAdapter.getItem(position).getLoanName());
                    startActivity(intent);
                }
            });
        } else {
            loanAdapter.update(datas);
            mLoadMoreWrapper.notifyDataSetChanged();
        }
    }

    /**
     * 初始化搜索框监听
     *
     */
    public void initEdi() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("Daniel","---beforeTextChanged----");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("Daniel","---onTextChanged----");
            }

            @DebugLog
            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("Daniel","---afterTextChanged----"+editable.toString());
                LoanSearch loanSearch = new LoanSearch();
                //非首页
                loanSearch.setHomeShow(Constan.HOMESHOW_FALSE);
                //页数
                loanSearch.setPageNum(0);
                //条数
                loanSearch.setPageSize(Constan.PAGESIZE);
                //城市
                loanSearch.setCity(MainActivity.locationStr);
                loanSearch.setSearch(editable.toString());
                initDate(true,loanSearch);

            }
        });
    }

    /**
     * 初始化抽屉控件
     */
    private void initDrawLayout() {
        Fragment fragment = null;
        if ("02".equals(isLoan)){
            fragment = TestFilterFragment.newInstance(1);//1：贷款 2：理财
        }else if ("01".equals(isLoan)){
            fragment = TestFilterFragment.newInstance(2);//1：贷款 2：理财
        }
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
