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
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.gongwen.marqueen.MarqueeFactory;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForChooseActivity;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForStatusActivity;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.bank.bankfinancing.BankFinancingActivity;
import tqm.bianfeng.com.tqm.bank.bankinformations.test.NewBankInformationActivity;
import tqm.bianfeng.com.tqm.bank.bankloan.BankLoanActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;



public class HomeFragment4 extends BaseFragment {


    boolean isNetWork = true;
    boolean isInfo = false;
    boolean isFinaning = false;
    boolean isLoan = false;
    boolean isActivity = false;
    int scrollHeight = 0;
    int sliderHeight = 1;
    @BindView(R.id.home_slider)
    SliderLayout homeSlider;
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
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.select_more_bankLoan_txt)
    TextView selectMoreBankLoanTxt;
    @BindView(R.id.bank_loan_list)
    RecyclerView bankLoanList;
    @BindView(R.id.home_bank_loan_title_lin)
    LinearLayout homeBankLoanTitleLin;
    @BindView(R.id.home_myMoney_linear)
    LinearLayout homeMyMoneyLinear;
    @BindView(R.id.home_myLoan_linear)
    LinearLayout homeMyLoanLinear;
    @BindView(R.id.quickCome_linear)
    LinearLayout quickComeLinear;
    @BindView(R.id.financialClass_linear)
    LinearLayout financialClassLinear;
    //ddddddddd
    User mUser;
    @BindView(R.id.homeTop_bg_img)
    ImageView homeTopBgImg;
    @BindView(R.id.my_scrollview)
    MyScrollview myScrollview;
    @BindView(R.id.marqueeView2)
    com.gongwen.marqueen.MarqueeView marqueeView2;
    private final List<String> datas = Arrays.asList("《赋得古原草送别》", "离离原上草，一岁一枯荣。", "野火烧不尽，春风吹又生。", "远芳侵古道，晴翠接荒城。", "又送王孙去，萋萋满别情。");
    private CompositeSubscription mCompositeSubscription;
    private WeakHandler mHandler = new WeakHandler();

    public static HomeFragment4 newInstance() {
        HomeFragment4 fragment = new HomeFragment4();

        return fragment;
    }

    public interface mListener {
        public void detailActivity(Intent intent);

        public void setToolBarColorBg(int a);

        public void changeActivity(
                Class activityClass);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        marqueeFactory.setData(datas);
        marqueeView2.setMarqueeFactory(marqueeFactory);
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
//        marqueeView2.setMarqueeFactory(marqueeFactory2);
//        marqueeView2.startFlipping();
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
                Log.i("gqf", "x" + x + "y" + y + "oldx" + oldx + "oldy" + oldy);
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
        homeBankLoanTitleLin.setVisibility(View.VISIBLE);
        Log.e("Daniel", "---MainActivity.locationStr---" + MainActivity.locationStr);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankLoanItem(null, null, Constan.HOMESHOW_TRUE, Constan.FIRSTPAGENUM, Constan.FIRSTPAGESIZE, MainActivity.locationStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankLoanItem>>() {
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
                        Log.e("Daniel", "---BankLoanItem---");
                        isLoan = true;
                        setBankLoanListAdapter(bankLoanItemBankListItems.getItem());
                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    HomeBankLoanListAdapter3 bankLoanAdapter;

    private void setBankLoanListAdapter(List<BankLoanItem> bankloanItems) {
        Log.e("Daniel", "bankloanItems.size()---" + bankloanItems.size());
        Logger.d("---bankloanItems.size()--" + bankloanItems.size());
        if (bankLoanAdapter == null) {
            bankLoanList.setLayoutManager(new LinearLayoutManager(getActivity()));
            bankLoanList.setHasFixedSize(true);
            bankLoanList.setNestedScrollingEnabled(false);
            bankLoanAdapter = new HomeBankLoanListAdapter3(getActivity(), bankloanItems, true);
            bankLoanList.setAdapter(bankLoanAdapter);
            bankLoanAdapter.setOnItemClickListener(new HomeBankLoanListAdapter3.HomeBankLoanClickListener() {
                @Override
                public void OnClickListener(int position) {
                    //跳转银行贷款详情
                    Log.d("Daniel", "点击首页贷款：" + position);
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


    @Override
    public void onDetach() {
        super.onDetach();
    }

    List<BankInformItem> bankInformItems;

    //初始化热点资讯轮播
    public void initHotMsgList(List<BankInformItem> data) {
        bankInformItems = data;
        List<String> titles = new ArrayList<>();
        for (BankInformItem bankInformItem : bankInformItems) {
            titles.add(bankInformItem.getInformTitle());
        }
        marqueeView.setNotices(titles);
        marqueeView.start();
        marqueeView.setOnItemClickListener(new com.sunfusheng.marqueeview.MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("detailType", DetailActivity.INFOR_TYPE);
                intent.putExtra("detailId", bankInformItems.get(position).getInformId());
                intent.putExtra("detailTitle", bankInformItems.get(position).getInformTitle());
                mListener.detailActivity(intent);
            }
        });
    }

    public void getBankLoanItemHot() {
        //热点资讯
        Subscription subscription = NetWork.getUserService().getBankInformItem(2, "02", 1, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankInformItem>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<BankInformItem> bankInformItems) {
                        initHotMsgList(bankInformItems);
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void initImagesData() {
        //获取首页轮播图
        Subscription subscription = NetWork.getUserService().getImages("02")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
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
            mListener.toLogin();
            return false;
        } else {
            return true;
        }
    }

    @OnClick({
            R.id.select_more_hot_information_txt, R.id.home_myMoney_linear, R.id.home_myLoan_linear, R.id.quickCome_linear,
            R.id.financialClass_linear, R.id.select_more_bankLoan_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quickCome_linear:
                if (isLogin()) {
                    if (mUser.getApplyForStatu().equals("00") || mUser.getApplyForStatu().equals("01") || mUser.getApplyForStatu().equals("02")) {
                        //查看审核状态
                        mListener.changeActivity(ApplyForStatusActivity.class);
                    } else {
                        //跳转入驻选择界面
                        mListener.changeActivity(ApplyForChooseActivity.class);
                    }
                }
                break;
            case R.id.financialClass_linear:
                startActivity(new Intent(getActivity(), NewBankInformationActivity.class).putExtra("financialClass", "financialClass"));
                break;
            case R.id.select_more_bankLoan_txt:
                startActivity(new Intent(getActivity(), BankLoanActivity.class));
                break;
            case R.id.home_myMoney_linear:
                startActivity(new Intent(getActivity(), BankFinancingActivity.class));
                break;
            case R.id.home_myLoan_linear:
                startActivity(new Intent(getActivity(), BankLoanActivity.class));
                break;
            case R.id.select_more_hot_information_txt:
                startActivity(new Intent(getActivity(), NewBankInformationActivity.class).putExtra("financialClass", "select_more_hot_information_txt"));
                //                startActivity(new Intent(getActivity(), BankInformationActivity3.class).putExtra("financialClass","select_more_hot_information_txt"));
                //                startActivity(new Intent(getActivity(), NewBankInformationActivity.class).putExtra("02", "02"));//02-热点资讯
                break;
            //            case R.id.home_select_branches_lin:
            //                startActivity(new Intent(getActivity(), WebListActivity.class));
            //                break;

        }
    }

    //网络设置显示判断
    public void showViewWhenNetWork(boolean isNetWork) {
        Log.i("gqf", "showViewWhenNetWork" + isNetWork);
        this.isNetWork = isNetWork;
        if (homeInfoLin != null) {
            if (isNetWork) {
                if (homeInfoLin.getVisibility() != View.VISIBLE) {
                    homeInfoLin.setVisibility(View.VISIBLE);
                    //                    initImagesData();
                    getBankLoanItemHot();
                    getBankLoanServiceItem();
                }
            } else {
                homeInfoLin.setVisibility(View.INVISIBLE);
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
