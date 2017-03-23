package tqm.bianfeng.com.tqm.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
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
import tqm.bianfeng.com.tqm.CustomView.AutoHeightLayoutManager;
import tqm.bianfeng.com.tqm.CustomView.MarqueeView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.bank.bankactivitys.BankActivitonsActivity;
import tqm.bianfeng.com.tqm.bank.bankfinancing.BankFinancingActivity;
import tqm.bianfeng.com.tqm.bank.bankinformations.test.NewBankInformationActivity;
import tqm.bianfeng.com.tqm.bank.bankloan.BankLoanActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;


public class HomeFragment extends BaseFragment {

    @BindView(R.id.home_slider)
    SliderLayout homeSlider;
    @BindView(R.id.home_bank_activity_lin)
    LinearLayout homeBankActivityLin;
    @BindView(R.id.home_bank_make_money_lin)
    LinearLayout homeBankMakeMoneyLin;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.home_bank_loan_lin)
    LinearLayout homeBankLoanLin;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.home_select_branches_lin)
    LinearLayout homeSelectBranchesLin;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.select_more_hot_information_txt)
    TextView selectMoreHotInformationTxt;
    @BindView(R.id.select_more_bank_make_money_txt)
    TextView selectMoreBankMakeMoneyTxt;
    HomeBankInfoListAdapter homeBankInfoListAdapter;
    @BindView(R.id.bank_info_list)
    RecyclerView bankInfoList;

    @BindView(R.id.bank_activitys_list)
    RecyclerView bankActivitysList;


    @BindView(R.id.bank_finaning_list)
    RecyclerView bankFinaningList;
    @BindView(R.id.bank_loan_list)
    RecyclerView bankLoanList;
    boolean isNetWork = true;
    @BindView(R.id.home_bank_loan_title_lin)
    LinearLayout homeBankLoanTitleLin;
    @BindView(R.id.home_bank_activity_title_lin)
    LinearLayout homeBankActivityTitleLin;
    @BindView(R.id.home_info_lin)
    LinearLayout homeInfoLin;
    @BindView(R.id.home_bank_make_money_title_lin)
    LinearLayout homeBankMakeMoneyTitleLin;
    private CompositeSubscription mCompositeSubscription;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }


    public interface mListener {
        public void detailActivity(Intent intent);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_top, container, false);
        ButterKnife.bind(this, view);
        mCompositeSubscription = new CompositeSubscription();

        if (isNetWork) {
            showViewWhenNetWork(isNetWork);
        }
        return view;
    }


    /**
     * 银行贷款
     */
    private void getBankLoanServiceItem() {
        homeBankLoanTitleLin.setVisibility(View.VISIBLE);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankLoanItem(null, Constan.HOMESHOW_TRUE, Constan.FIRSTPAGENUM, Constan.FIRSTPAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankLoanItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankLoanItem> bankloanItems) {
                        Log.e("Daniel", "---BankLoanItem---");
                        setBankLoanListAdapter(bankloanItems);
                        getBankActivitys();
                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    private void setBankLoanListAdapter(final List<BankLoanItem> bankloanItems) {
        bankLoanList.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
        final HomeBankLoanListAdapter bankLoanAdapter = new HomeBankLoanListAdapter(getActivity(), bankloanItems);
        bankLoanList.setAdapter(bankLoanAdapter);
        bankLoanAdapter.setOnItemClickListener(new HomeBankLoanListAdapter.HomeBankLoanClickListener() {
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
    }

    /**
     * 银行活动
     */
    private void getBankActivitys() {
        homeBankActivityTitleLin.setVisibility(View.VISIBLE);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankActivityItem(Constan.HOMESHOW_TRUE, Constan.FIRSTPAGENUM, Constan.FIRSTPAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankActivityItem>>() {
                    @DebugLog
                    @Override
                    public void onCompleted() {
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankActivityItem> bankActivityItems) {
                        Log.e("Daniel", "---bankActivityItems.size()---" + bankActivityItems.size());
                        setBankActivitysListAdapter(bankActivityItems);

                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);

    }

    private void setBankActivitysListAdapter(List<BankActivityItem> bankActivityItems) {

        bankActivitysList.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
        final HomeBankActivitysListAdapter homeBankActivitysListAdapter = new HomeBankActivitysListAdapter(getActivity(), bankActivityItems);
        bankActivitysList.setAdapter(homeBankActivitysListAdapter);
        homeBankActivitysListAdapter.setOnItemClickListener(new HomeBankActivitysListAdapter.HomeBankActivitysItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                //跳转银行活动详情
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("detailType", "01");
                intent.putExtra("detailId", homeBankActivitysListAdapter.getDataItem(position).getActivityId());
                intent.putExtra("detailTitle", homeBankActivitysListAdapter.getDataItem(position).getActivityTitle());
                mListener.detailActivity(intent);
            }
        });

    }


    /**
     * 银行理财
     */
    private void getBankFinaningItem() {
        homeBankMakeMoneyTitleLin.setVisibility(View.VISIBLE);
        Subscription getBankFinancItem_subscription = NetWork.getBankService()
                .getBankFinancItem(null, Constan.HOMESHOW_TRUE, Constan.FIRSTPAGENUM, Constan.FIRSTPAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankFinancItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankFinancItem> bankFinancItems) {
                        Log.e("Daniel", "---BankFinancItem---");
                        setBankFinancListAdapter(bankFinancItems);
                        getBankLoanServiceItem();
                    }
                });
        mCompositeSubscription.add(getBankFinancItem_subscription);
    }

    private void setBankFinancListAdapter(List<BankFinancItem> bankFinancItems) {
        bankFinaningList.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
        final HomeBankFinancingListAdapter bankFinancingAdapter = new HomeBankFinancingListAdapter(getActivity(), bankFinancItems);
        bankFinaningList.setAdapter(bankFinancingAdapter);
        bankFinancingAdapter.setOnItemClickListener(new HomeBankFinancingListAdapter.HomeBankFinancingItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                //跳转银行理财详情
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("detailType", "02");
                intent.putExtra("detailId", bankFinancingAdapter.getDataItem(position).getFinancId());
                intent.putExtra("detailTitle", bankFinancingAdapter.getDataItem(position).getProductName());
                mListener.detailActivity(intent);
            }
        });

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getBankLoanItem() {
        //银行资讯
        Subscription subscription = NetWork.getUserService().getBankInformItem(0, "01", 1, 3)
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
                        Log.e("gqf", "bankInformItems" + bankInformItems.toString());
                        initBankLoanItemList(bankInformItems);
                        getBankFinaningItem();
                    }
                });
        compositeSubscription.add(subscription);
    }

    //初始化银行资讯列表
    public void initBankLoanItemList(List<BankInformItem> bankInformItems) {
        if (homeBankInfoListAdapter == null) {
            bankInfoList.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
            homeBankInfoListAdapter = new HomeBankInfoListAdapter(getActivity(), bankInformItems);
            bankInfoList.setAdapter(homeBankInfoListAdapter);
            homeBankInfoListAdapter.setOnItemClickListener(new HomeBankInfoListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    //跳转银行咨询详情
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", "04");
                    intent.putExtra("detailId", homeBankInfoListAdapter.getDataItem(position).getInformId());
                    intent.putExtra("detailTitle", homeBankInfoListAdapter.getDataItem(position).getInformTitle());
                    mListener.detailActivity(intent);
                }
            });
        } else {
            homeBankInfoListAdapter.update(bankInformItems);
        }

    }

    //初始化热点资讯轮播
    public void initHotMsgList(List<BankInformItem> bankInformItems) {
        List<String> titles = new ArrayList<>();
        for (BankInformItem bankInformItem : bankInformItems) {
            titles.add(bankInformItem.getInformTitle());
        }
        marqueeView.setNotices(titles);
        marqueeView.start();
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

    public void initImages(List<String> strings) {
        //加载首页轮播图
        if (strings.size() > 0) {
            for (String url : strings) {
                DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                textSliderView.image(NetWork.LOAD + url)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                homeSlider.addSlider(textSliderView);
            }
        }
    }


    @OnClick({R.id.select_more_bankFinancing_txt, R.id.select_more_bankLoan_txt, R.id.select_more_bankActivitys_txt,
            R.id.home_bank_activity_lin, R.id.home_bank_loan_lin, R.id.home_bank_make_money_lin, R.id.select_more_bank_make_money_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_more_bankFinancing_txt:
                startActivity(new Intent(getActivity(), BankFinancingActivity.class));
                break;
            case R.id.select_more_bankLoan_txt:
                startActivity(new Intent(getActivity(), BankLoanActivity.class));
                break;
            case R.id.select_more_bankActivitys_txt:
                startActivity(new Intent(getActivity(), BankActivitonsActivity.class));
                break;
            case R.id.home_bank_activity_lin:
                startActivity(new Intent(getActivity(), BankActivitonsActivity.class));
                break;
            case R.id.home_bank_loan_lin:
                startActivity(new Intent(getActivity(), BankLoanActivity.class));
                break;
            case R.id.home_bank_make_money_lin:
                startActivity(new Intent(getActivity(), BankFinancingActivity.class));
                break;
            case R.id.select_more_bank_make_money_txt:
                startActivity(new Intent(getActivity(), NewBankInformationActivity.class));
                break;
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
                    initImagesData();
                    getBankLoanItemHot();
                    getBankLoanItem();
                }
            } else {
                homeInfoLin.setVisibility(View.INVISIBLE);
                Log.i("gqf", "showViewWhenNetWork" + isNetWork);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}
