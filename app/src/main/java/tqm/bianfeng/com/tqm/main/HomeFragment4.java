package tqm.bianfeng.com.tqm.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.StringUtils;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.gongwen.marqueen.MarqueeFactory;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.CustomView.MarqueeView;
import tqm.bianfeng.com.tqm.CustomView.MyScrollview;
import tqm.bianfeng.com.tqm.CustomView.RequestPermissions;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForChooseActivity;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForStatusActivity;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.bank.bankinformations.test.NewBankInformationActivity;
import tqm.bianfeng.com.tqm.bank.bankloan.BankLoanActivity;
import tqm.bianfeng.com.tqm.bank.quickloan.BasicInformationActivity;
import tqm.bianfeng.com.tqm.bank.quickloan.SubmitInformationActivity;
import tqm.bianfeng.com.tqm.main.cardcollection.CardCollectionActivity;
import tqm.bianfeng.com.tqm.main.vehicle.VehicleEvaluationActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.LoanSearch;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwDksq;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

import static tqm.bianfeng.com.tqm.R.id.home_oneKeyLoan_linear;


public class HomeFragment4 extends BaseFragment {

    boolean isNetWork = true;
    boolean isInfo = false;
    boolean isFinaning = false;
    boolean isLoan = false;
    boolean isActivity = false;
    int scrollHeight = 0;
    int sliderHeight = 1;
        private final List<String> datas = Arrays.asList("《赋得古原草送别》", "离离原上草，一岁一枯荣。", "野火烧不尽，春风吹又生。", "远芳侵古道，晴翠接荒城。", "又送王孙去，萋萋满别情。");


    User mUser;
    @BindView(R.id.homeTop_bg_img)
    ImageView homeTopBgImg;
    @BindView(R.id.marqueeView2)
    com.gongwen.marqueen.MarqueeView marqueeView2;
    @BindView(R.id.home_myMoney_linear)
    LinearLayout homeMyMoneyLinear;
    @BindView(R.id.home_myLoan_linear)
    LinearLayout homeMyLoanLinear;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.select_more_hot_information_txt)
    TextView selectMoreHotInformationTxt;
    @BindView(R.id.select_more_bank_make_money_txt)
    TextView selectMoreBankMakeMoneyTxt;
    @BindView(R.id.bank_info_list)
    RecyclerView bankInfoList;
    @BindView(R.id.home_info_lin)
    LinearLayout homeInfoLin;
    @BindView(home_oneKeyLoan_linear)
    LinearLayout homeOneKeyLoanLinear;
    @BindView(R.id.schedule3_img)
    ImageView schedule3Img;
    @BindView(R.id.financialClass_linear)
    LinearLayout financialClassLinear;
    @BindView(R.id.quickCome_linear)
    LinearLayout quickComeLinear;
    @BindView(R.id.home_slider)
    SliderLayout homeSlider;
    @BindView(R.id.bank_loan_list)
    RecyclerView bankLoanList;
    @BindView(R.id.select_more_bankLoan_txt)
    LinearLayout selectMoreBankLoanTxt;
    @BindView(R.id.home_bank_loan_title_lin)
    LinearLayout homeBankLoanTitleLin;
    @BindView(R.id.bank_finaning_list)
    RecyclerView bankFinaningList;
    @BindView(R.id.select_more_bankFinancing_txt)
    LinearLayout selectMoreBankFinancingTxt;
    @BindView(R.id.home_bank_make_money_title_lin)
    LinearLayout homeBankMakeMoneyTitleLin;
    @BindView(R.id.bank_activitys_list)
    RecyclerView bankActivitysList;
    @BindView(R.id.select_more_bankActivitys_txt)
    LinearLayout selectMoreBankActivitysTxt;
    @BindView(R.id.home_bank_activity_title_lin)
    LinearLayout homeBankActivityTitleLin;
    @BindView(R.id.my_scrollview)
    MyScrollview myScrollview;
    @BindView(R.id.home_content_lin)
    LinearLayout homeContentLin;
    @BindView(R.id.notNot_img)
    ImageView notNotImg;
    @BindView(R.id.home_notNet_rel)
    RelativeLayout homeNotNetRel;
    private CompositeSubscription mCompositeSubscription;
    private WeakHandler mHandler = new WeakHandler();

    public static HomeFragment4 newInstance() {
        HomeFragment4 fragment = new HomeFragment4();

        return fragment;
    }
    public interface mListener {
        public void detailActivity(Intent intent);

        public void setToolBarColorBg(int a);

        public void changeActivity(Class activityClass);

        public void shouNetWorkActivity();

        public void toLogin();


    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        marqueeView2.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        marqueeView2.stopFlipping();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_top4, container, false);
        ButterKnife.bind(this, view);
        mCompositeSubscription = new CompositeSubscription();
        initImages(new ArrayList<String>());
        mUser = realm.where(User.class).findFirst();
        if (isNetWork) {
            showViewWhenNetWork(isNetWork);
        }
                final MarqueeFactory<TextView, String> marqueeFactory = new NoticeMF(getActivity());
                marqueeFactory.setOnItemClickListener(new MarqueeFactory.OnItemClickListener<TextView, String>() {
                    @Override
                    public void onItemClickListener(MarqueeFactory.ViewHolder<TextView, String> holder) {
                        Toast.makeText(getActivity(), holder.data, Toast.LENGTH_SHORT).show();
                    }
                });
                marqueeFactory.resetData(datas);
                marqueeView2.setAnimInAndOut(R.anim.right_in, R.anim.left_out);
                marqueeView2.setAnimDuration(2000);
                marqueeView2.setInterval(2500);
                marqueeView2.setAnimateFirstView(true);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        marqueeView2.startFlipping();
                    }
                });
                 marqueeView2.setMarqueeFactory(marqueeFactory);
//                marqueeView2.setMarqueeFactory(marqueeFactory2);
//                marqueeView2.startFlipping();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initScroll();
        ViewTreeObserver vto = homeTopBgImg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                homeTopBgImg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int toolBarHeight = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    toolBarHeight = (int) getResources().getDimension(R.dimen.hugehdp);
                } else {
                    toolBarHeight = (int) getResources().getDimension(R.dimen.smallhdp);
                }
                sliderHeight = homeTopBgImg.getHeight() - toolBarHeight;

            }
        });
        try {
            mListener.setToolBarColorBg(getAlph(scrollHeight, sliderHeight));
        } catch (Exception e) {

        }
    }

    public void initScroll() {
        myScrollview.setOnScollChangedListener(new MyScrollview.OnScollChangedListener() {
            @Override
            public void onScrollChanged(MyScrollview scrollView, int x, int y, int oldx, int oldy) {
//                Log.i("gqf", "x" + x + "y" + y + "oldx" + oldx + "oldy" + oldy);
                scrollHeight = y;
                if (sliderHeight != 1) {
                    if (isNetWork) {
                        try {
                            mListener.setToolBarColorBg(getAlph(scrollHeight, sliderHeight));
                        } catch (Exception e) {
                        }

                    }
                }
            }
        });
    }

    public int getAlph(int h1, int h2) {
        if (h1 > h2) {
            return 255;
        } else {
            return (int) ((float) h1 / h2 * 255);
        }
    }

    /**
     * 极速贷款
     */
    private void getBankLoanServiceItem() {
        LoanSearch loanSearch = new LoanSearch();
        loanSearch.setCity(MainActivity.locationStr);
        loanSearch.setHomeShow(Constan.HOMESHOW_TRUE);
        loanSearch.setPageSize(Constan.FIRSTPAGESIZE);
        loanSearch.setPageNum(Constan.FIRSTPAGENUM);
        String queryParams = new Gson().toJson(loanSearch);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankLoanItem(queryParams)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BankListItems<BankLoanItem>>() {
            @Override
            public void onCompleted() {

            }

            @DebugLog
            @Override
            public void onError(Throwable e) {

            }

            @DebugLog
            @Override
            public void onNext(BankListItems<BankLoanItem> bankLoanItemBankListItems) {
                Log.e("Daniel", "---首页贷款：---" + bankLoanItemBankListItems.getItem().toString());
                isLoan = true;
                if (bankLoanItemBankListItems.getItem().size()!=0){
                    homeBankLoanTitleLin.setVisibility(View.VISIBLE);
                    setBankLoanListAdapter(bankLoanItemBankListItems.getItem());
                }
                //                        getBankActivitys();
            }
        });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    HomeBankLoanListAdapter4 bankLoanAdapter;

    private void setBankLoanListAdapter(List<BankLoanItem> bankloanItems) {
        if (bankLoanAdapter == null) {
            //            bankLoanList.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
            bankLoanList.setLayoutManager(new LinearLayoutManager(getActivity()));
            bankFinaningList.setHasFixedSize(true);
            bankFinaningList.setNestedScrollingEnabled(false);
            bankLoanAdapter = new HomeBankLoanListAdapter4(getActivity(), bankloanItems);
            bankLoanList.setAdapter(bankLoanAdapter);
            bankLoanAdapter.setOnItemClickListener(new HomeBankLoanListAdapter4.HomeBankLoanClickListener() {
                @Override
                public void OnClickListener(int position) {
                    //跳转银行贷款详情
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", "03");
                    intent.putExtra("detailId", bankLoanAdapter.getItem(position).getLoanId());
                    intent.putExtra("detailTitle", bankLoanAdapter.getDataItem(position).getLoanName());
                    mListener.detailActivity(intent);
                }
            });
        } else {
            bankLoanAdapter.update(bankloanItems);
        }
    }

    HomeBankActivitysListAdapter4 homeBankActivitysListAdapter4;

    //初始化热点活动列表
    public void initBankActivityItemList(List<BankActivityItem> bankInformItems) {
        if (homeBankActivitysListAdapter4 == null) {
            bankActivitysList.setLayoutManager(new LinearLayoutManager(getActivity()));
            bankActivitysList.setHasFixedSize(true);
            bankActivitysList.setNestedScrollingEnabled(false);
            homeBankActivitysListAdapter4 = new HomeBankActivitysListAdapter4(getActivity(), bankInformItems);
            bankActivitysList.setAdapter(homeBankActivitysListAdapter4);

            homeBankActivitysListAdapter4.setOnItemClickListener(new HomeBankActivitysListAdapter4.HomeBankActivitysItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    //跳转银行咨询详情
                    Log.e(Constan.LOGTAGNAME,"跳转银行咨询详情");
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", DetailActivity.ACTIVITY_TYPE);
                    intent.putExtra("detailId", homeBankActivitysListAdapter4.getDataItem(position).getActivityId());
                    intent.putExtra("articlePath", homeBankActivitysListAdapter4.getDataItem(position).getArticlePath());
                    intent.putExtra("detailTitle", homeBankActivitysListAdapter4.getDataItem(position).getActivityTitle());
                    mListener.detailActivity(intent);
                }
            });
        } else {
            homeBankActivitysListAdapter4.update(bankInformItems);
        }

    }

    /**
     * 理财推荐
     */
    private void getBankFinaningItem() {
        LoanSearch loanSearch = new LoanSearch();
        //非首页
        loanSearch.setHomeShow(Constan.HOMESHOW_TRUE);
        //页数
        loanSearch.setPageNum(0);
        //条数
        loanSearch.setPageSize(Constan.FIRSTPAGESIZE);
        //城市
        loanSearch.setCity(MainActivity.locationStr);
        String queryParams = new Gson().toJson(loanSearch);
        Subscription getBankFinancItem_subscription = NetWork.getBankService().getBankFinancItem(queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankFinancItem>>() {
            @Override
            public void onCompleted() {

            }

            @DebugLog
            @Override
            public void onError(Throwable e) {

            }

            @DebugLog
            @Override
            public void onNext(BankListItems<BankFinancItem> bankLoanItemBankListItems) {
                isFinaning = true;
                Log.e("Daniel", "---BankFinancItem---");
                if (bankLoanItemBankListItems.getItem().size()!=0){
                    homeBankMakeMoneyTitleLin.setVisibility(View.VISIBLE);
                    setBankFinancListAdapter(bankLoanItemBankListItems.getItem());
                }

            }
        });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    HomeBankFinancingListAdapter4 bankFinancingAdapter;

    private void setBankFinancListAdapter(List<BankFinancItem> bankFinancItems) {
        if (bankFinancingAdapter == null) {
            bankFinaningList.setLayoutManager(new LinearLayoutManager(getActivity()));
            bankFinaningList.setHasFixedSize(true);
            bankFinaningList.setNestedScrollingEnabled(false);
            bankFinancingAdapter = new HomeBankFinancingListAdapter4(getActivity(), bankFinancItems);
            bankFinaningList.setAdapter(bankFinancingAdapter);
            bankFinancingAdapter.setOnItemClickListener(new HomeBankFinancingListAdapter4.HomeBankLoanClickListener() {
                @Override
                public void OnClickListener(int position) {
                    //跳转理财详情
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", "02");
                    intent.putExtra("detailId", bankFinancingAdapter.getDataItem(position).getFinancId());
                    intent.putExtra("detailTitle", bankFinancingAdapter.getDataItem(position).getProductName());
                    mListener.detailActivity(intent);
                }
            });
        } else {
            bankFinancingAdapter.update(bankFinancItems);
        }

    }

    List<BankActivityItem> bankActivityItems;

    //初始化热点资讯轮播
    public void initHotMsgList(List<BankActivityItem> data) {
        bankActivityItems = data;
        List<String> titles = new ArrayList<>();
        for (BankActivityItem bankActivityItem : bankActivityItems) {
            titles.add(bankActivityItem.getActivityTitle());
        }
        marqueeView.setNotices(titles);
        marqueeView.start();
        marqueeView.setOnItemClickListener(new com.sunfusheng.marqueeview.MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("detailType", DetailActivity.ACTIVITY_TYPE);
                intent.putExtra("detailId", bankActivityItems.get(position).getActivityId());
                intent.putExtra("articlePath", bankActivityItems.get(position).getArticlePath());
                intent.putExtra("detailTitle", bankActivityItems.get(position).getActivityTitle());
                mListener.detailActivity(intent);
            }
        });
    }
    //热门活动，热点资讯
    private void initActivityItem(final Integer activityType,Integer activityNum) {
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankActivityItem(null, null, Constan.HOMESHOW_TRUE, 0, activityNum, MainActivity.locationStr,activityType)//4-热门活动 ，2-热点资讯
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankActivityItem>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BankListItems<BankActivityItem> bankActivityItemBankListItems) {
                        if (bankActivityItemBankListItems.getItem().size()!=0){

                        if (activityType==2){
                            initHotMsgList(bankActivityItemBankListItems.getItem());
                        }else {
                            homeBankActivityTitleLin.setVisibility(View.VISIBLE);
                            initBankActivityItemList(bankActivityItemBankListItems.getItem());
                        }
                        }

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }


    public void initImagesData() {
        //获取首页轮播图
        Subscription subscription = NetWork.getUserService().getImages("02").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<String> strings) {
                initImages(strings);
            }
        });
        compositeSubscription.add(subscription);
    }

    Integer[] mSlider = {R.drawable.img_ad, R.drawable.img_ad1, R.drawable.img_ad2};

    public void initImages(List<String> strings) {
        //加载首页轮播图
        //        if (strings.size() > 0) {
        //            for (String url : strings) {
        //                Log.i("gqf", "strings" + url);
        //                DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
        //                textSliderView.image(NetWork.LOAD + url)
        //                        .setScaleType(BaseSliderView.ScaleType.Fit);
        //                homeSlider.addSlider(textSliderView);
        //            }
        //        }
        int size = mSlider.length;
        for (int i = 0; i < size; i++) {

            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            textSliderView.image(mSlider[i]).setScaleType(BaseSliderView.ScaleType.Fit);
            homeSlider.addSlider(textSliderView);
        }
    }

    //登录判断
    public boolean isLogin() {
        if (realm.where(User.class).findFirst() == null) {
            Toast.makeText(getActivity(), "请您先登录后使用该功能", Toast.LENGTH_SHORT).show();
            //跳到登录页面
            mListener.toLogin();
            return false;
        } else {
            return true;
        }
    }
    LoanNetWork mLoanNetWork;
    RequestPermissions requestPermissions;
    @OnClick({R.id.home_cardcollation_linear,R.id.vehicleEvaluation_lin,R.id.select_more_hot_information_txt, R.id.home_myMoney_linear, R.id.home_myLoan_linear, R.id.quickCome_linear, R.id.financialClass_linear, R.id.select_more_bankLoan_txt, R.id.select_more_bankActivitys_txt, R.id.select_more_bankFinancing_txt, R.id.home_oneKeyLoan_linear})
    public void onClick(View view) {
        switch (view.getId()) {
            //名片收集
            case R.id.home_cardcollation_linear:
                if (isLogin()){
                    mListener.changeActivity(CardCollectionActivity.class);
                }
                break;
            //车辆评估
            case R.id.vehicleEvaluation_lin:
                if (isLogin()){
                    mListener.changeActivity(VehicleEvaluationActivity.class);
                }
                break;
            //一键贷款
            case R.id.home_oneKeyLoan_linear:
                if (isLogin()){
                    //获取单条贷款信息
                    getLoanOne(realm.where(User.class).findFirst().getUserId());
                }

                break;
            //全部理财推荐
            case R.id.select_more_bankFinancing_txt:
                startActivity(new Intent(getActivity(),BankLoanActivity.class).putExtra("isLoan","01"));//01-理财
                //                    startActivity(new Intent(getActivity(), NewBankInformationActivity.class));
                break;
            //全部热门资讯
            case R.id.select_more_bankActivitys_txt:
                mListener.changeActivity(NewBankInformationActivity.class);
                //                    startActivity(new Intent(getActivity(), NewBankInformationActivity.class));
                break;
            //快速入驻
            case R.id.quickCome_linear:
                if (isLogin()) {
                    if (mUser == null){
                        mUser = realm.where(User.class).findFirst();
                    }
                    getShzt(mUser.getUserId());
                }
                break;
            //金融课堂
            case R.id.financialClass_linear:
                startActivity(new Intent(getActivity(), NewBankInformationActivity.class).putExtra("financialClass", "financialClass"));
                break;
            //全部热门贷款
            case R.id.select_more_bankLoan_txt:
                startActivity(new Intent(getActivity(),BankLoanActivity.class).putExtra("isLoan","02"));//02-贷款
                break;
            //我要理财
            case R.id.home_myMoney_linear:
                startActivity(new Intent(getActivity(),BankLoanActivity.class).putExtra("isLoan","01"));//01-理财
//                mListener.changeActivity(BankFinancingActivity.class);
                break;
            //我要贷款
            case R.id.home_myLoan_linear:
                startActivity(new Intent(getActivity(),BankLoanActivity.class).putExtra("isLoan","02"));//02-贷款
//                mListener.changeActivity(BankLoanActivity.class);
                break;
            //更多热点资讯
            case R.id.select_more_hot_information_txt:
                startActivity(new Intent(getActivity(), NewBankInformationActivity.class).putExtra("financialClass", "select_more_hot_information_txt"));
                break;
        }
    }

    private void getShzt(int userId) {
        Subscription subscription = NetWork.getUserService().getShzt(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Map<String, String> stringStringMap) {
                        Logger.e(stringStringMap.get("shzt"));
                        if (!StringUtils.isEmpty(stringStringMap.get("shzt"))){
                            Log.e("Daniel","快速入驻审核状态"+stringStringMap.get("shzt"));
                            //查看审核状态
                            mListener.changeActivity(ApplyForStatusActivity.class);
                        }else {
                            //跳转入驻选择界面
                            mListener.changeActivity(ApplyForChooseActivity.class);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
    private void getLoanOne(int userId) {
        Subscription subscription = NetWork.getBankService().getOne(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwDksq>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Dani", "快速贷款信息onError：" + e.toString());
                    }
                    @Override
                    public void onNext(YwDksq ywDksq) {
                        Log.e("Dani", "快速贷款信息onNext：" + ywDksq.toString());
                        if (!StringUtils.isEmpty(ywDksq.getSqzt())){
                                mListener.changeActivity(SubmitInformationActivity.class);
                            } else {
                                mListener.changeActivity(BasicInformationActivity.class);
                            }
                        }
                });
        compositeSubscription.add(subscription);
}
    //网络设置显示判断
    public void showViewWhenNetWork(boolean isNetWork) {
        Log.i("gqf", "showViewWhenNetWork" + isNetWork);
        this.isNetWork = isNetWork;
        if (homeInfoLin != null) {
            if (isNetWork) {
                homeNotNetRel.setVisibility(View.GONE);
                homeContentLin.setVisibility(View.VISIBLE);
                if (homeInfoLin.getVisibility() != View.VISIBLE) {
                    homeInfoLin.setVisibility(View.VISIBLE);
                    //                    initImagesData();
                    //请求热点资讯
                    initActivityItem(2,20);//2热点资讯
                    getBankLoanServiceItem();
                    getBankFinaningItem();
                    initActivityItem(4,Constan.FIRSTPAGESIZE);//热门活动
                }
            } else {
                homeInfoLin.setVisibility(View.INVISIBLE);
                homeNotNetRel.setVisibility(View.VISIBLE);
                homeContentLin.setVisibility(View.GONE);
                Log.i("gqf", "showViewWhenNetWork" + isNetWork);
            }
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //            if (!isInfo) {
            //                getBankLoanItem();
            //            } else if (!isFinaning) {
            //                getBankFinaningItem();
            //            } else if (!isLoan) {
            //                getBankLoanServiceItem();
            //            } else if (!isActivity) {
            //                getBankActivitys();
            //            }

            //根据滑动高度设置toolbar
            if (sliderHeight != 1) {
                try {
                    mListener.setToolBarColorBg(getAlph(scrollHeight, sliderHeight));
                } catch (Exception e) {

                }

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setTranslucentForImageView(getActivity(), 0, homeTopBgImg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}
