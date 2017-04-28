package tqm.bianfeng.com.tqm.User.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.AutoHeightLayoutManager;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.lawhelp.LawDetailActivity;
import tqm.bianfeng.com.tqm.lawhelp.adapter.LawListAdapter;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.main.HomeBankActivitysListAdapter;
import tqm.bianfeng.com.tqm.main.HomeBankFinancingListAdapter;
import tqm.bianfeng.com.tqm.main.HomeBankInfoListAdapter;
import tqm.bianfeng.com.tqm.main.HomeBankLoanListAdapter;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.LawyerItem;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by johe on 2017/4/28.
 */

public class MyBroseFragment extends BaseFragment {
    public int index = 0;
    public static String ARG_TYPE = "arg_type";
    @BindView(R.id.my_focuse_list)
    RecyclerView myFocuseList;
    @BindView(R.id.toast_txt)
    TextView toastTxt;

    public static MyBroseFragment newInstance(int position) {
        MyBroseFragment fragment = new MyBroseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_focuse, container, false);
        ButterKnife.bind(this, view);
       if(index==0){

           initActivityData();
       }else if(index==1){

           initFinancData();
       }else if(index==2){

           initLoanData();
       }

        return view;
    }
    public void initFinancData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionOfBankFinanc(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankFinancItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<BankFinancItem> bankFinancItems) {
                        BankFinancItems = bankFinancItems;
                        initFinancList(BankFinancItems);
                        toastTxt.setVisibility(View.GONE);
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void initLoanData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionOfBankLoan(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankLoanItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<BankLoanItem> bankLoanItems) {
                        BankLoanItems = bankLoanItems;
                        initLoanList(BankLoanItems);
                        toastTxt.setVisibility(View.GONE);
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void initActivityData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionOfBankActivity(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankActivityItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<BankActivityItem> bankActivityItems) {
                        BankActivityItems = bankActivityItems;
                        initActivityList(BankActivityItems);
                        toastTxt.setVisibility(View.GONE);
                    }
                });
        compositeSubscription.add(subscription);
    }
    List<BankActivityItem> BankActivityItems;
    List<BankLoanItem> BankLoanItems;
    List<BankFinancItem> BankFinancItems;
    List<LawyerItem> LawyerItems;
    List<BankInformItem> BankInformItems;


    HomeBankActivitysListAdapter bankActivitionsAdapter;
    HomeBankLoanListAdapter bankLoanAdapter;
    HomeBankFinancingListAdapter bankFinancingAdapter;
    LawListAdapter lawListAdapter;
    HomeBankInfoListAdapter homeBankInfoListAdapter;

    public void initInfoList(List<BankInformItem> datas){
        if (homeBankInfoListAdapter == null) {
            myFocuseList.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
            homeBankInfoListAdapter = new HomeBankInfoListAdapter(getActivity(), datas);
            myFocuseList.setAdapter(homeBankInfoListAdapter);
            homeBankInfoListAdapter.setOnItemClickListener(new HomeBankInfoListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    //跳转银行咨询详情
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", "04");
                    intent.putExtra("detailId", homeBankInfoListAdapter.getDataItem(position).getInformId());
                    intent.putExtra("detailTitle", homeBankInfoListAdapter.getDataItem(position).getInformTitle());
                    startActivity(intent);
                }
            });
        } else {
            homeBankInfoListAdapter.update(datas);
        }
    }

    public void initActivityList(List<BankActivityItem> datas) {
        if(bankActivitionsAdapter==null) {
            bankActivitionsAdapter = new HomeBankActivitysListAdapter(getActivity(), datas);
            bankActivitionsAdapter.setOnItemClickListener(new HomeBankActivitysListAdapter.HomeBankActivitysItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", "01");
                    intent.putExtra("detailId", bankActivitionsAdapter.getDataItem(position).getActivityId());
                    intent.putExtra("detailTitle", bankActivitionsAdapter.getDataItem(position).getActivityTitle());
                    startActivity(intent);
                }
            });
            myFocuseList.setLayoutManager(new LinearLayoutManager(getActivity()));
            myFocuseList.setAdapter(bankActivitionsAdapter);
        }else{
            bankActivitionsAdapter.update(datas);
        }
    }

    public void initLoanList(List<BankLoanItem> datas) {
        if(bankLoanAdapter==null) {
            bankLoanAdapter = new HomeBankLoanListAdapter(getActivity(), datas);
            bankLoanAdapter.setBgNull();
            bankLoanAdapter.setOnItemClickListener(new HomeBankLoanListAdapter.HomeBankLoanClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", "03");
                    intent.putExtra("detailId", bankLoanAdapter.getItem(position).getLoanId());
                    intent.putExtra("detailTitle", bankLoanAdapter.getDataItem(position).getLoanName());
                    startActivity(intent);
                }
            });
            myFocuseList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            myFocuseList.setAdapter(bankLoanAdapter);
        }else{
            bankLoanAdapter.update(datas);
        }
    }

    public void initFinancList(List<BankFinancItem> datas) {
        if(bankFinancingAdapter==null) {
            bankFinancingAdapter = new HomeBankFinancingListAdapter(getActivity(), datas);
            bankFinancingAdapter.setOnItemClickListener(new HomeBankFinancingListAdapter.HomeBankFinancingItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", "02");
                    intent.putExtra("detailId", bankFinancingAdapter.getDataItem(position).getFinancId());
                    intent.putExtra("detailTitle", bankFinancingAdapter.getDataItem(position).getProductName());
                    startActivity(intent);
                }
            });
            myFocuseList.setLayoutManager(new LinearLayoutManager(getActivity()));
            myFocuseList.setAdapter(bankFinancingAdapter);
        }else{
            bankFinancingAdapter.update(datas);
        }
    }

    public void initLawList(List<LawyerItem> datas) {
        if(lawListAdapter==null) {
            lawListAdapter = new LawListAdapter(getActivity(), datas);
            lawListAdapter.setOnItemClickListener(new LawListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(getActivity(), LawDetailActivity.class);
                    intent.putExtra("lawyer", LawyerItems.get(position).getLawyerId() + "");
                    startActivity(intent);
                }

                @Override
                public void CallClick(int position) {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15670702651"));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentPhone);
                }

                @Override
                public void CollectionClick(int position) {

                }
            });
            myFocuseList.setLayoutManager(new LinearLayoutManager(getActivity()));
            myFocuseList.setAdapter(lawListAdapter);
        }else{
            lawListAdapter.update(datas);
        }
    }
}
