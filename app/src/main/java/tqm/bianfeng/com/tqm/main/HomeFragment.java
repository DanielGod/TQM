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
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.AutoHeightLayoutManager;
import tqm.bianfeng.com.tqm.CustomView.MarqueeView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.BankInformItem;


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
        getBankLoanItem();
        getBankLoanItemHot();
        initImagesData();
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getBankLoanItem() {
        //银行咨询
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
                        Log.e("gqf","bankInformItems"+bankInformItems.toString());
                        initBankLoanItemList(bankInformItems);
                    }
                });
        compositeSubscription.add(subscription);
    }

    //初始化银行资讯列表
    public void initBankLoanItemList(List<BankInformItem> bankInformItems) {
        if (homeBankInfoListAdapter == null) {
            homeBankInfoListAdapter = new HomeBankInfoListAdapter(getActivity(), bankInformItems);
            bankInfoList.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
            bankInfoList.setAdapter(homeBankInfoListAdapter);
            homeBankInfoListAdapter.setOnItemClickListener(new HomeBankInfoListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    //跳转银行咨询详情
                    Intent intent=new Intent(getActivity(),DetailActivity.class);
                    intent.putExtra("detailType","04");
                    intent.putExtra("detailId",homeBankInfoListAdapter.getDataItem(position).getInformId());
                    mListener.detailActivity(intent);
                }
            });
        }else{
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
        Subscription subscription = NetWork.getUserService().getBankInformItem(2, "01", 1, 3)
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
    public void initImagesData(){
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
    public void initImages(List<String> strings){
        //加载首页轮播图
        if(strings.size()>0) {
            for (String url : strings) {
                DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                textSliderView.image(NetWork.LOAD+url)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                homeSlider.addSlider(textSliderView);
            }
        }
    }


}
