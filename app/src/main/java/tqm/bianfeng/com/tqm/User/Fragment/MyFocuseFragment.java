package tqm.bianfeng.com.tqm.User.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.AutoHeightLayoutManager;
import tqm.bianfeng.com.tqm.CustomView.IntentToDetail;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.BankLoanAdapter;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.lawhelp.adapter.LawListAdapter;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.main.HomeBankActivitysListAdapter;
import tqm.bianfeng.com.tqm.main.HomeBankFinancingListAdapter;
import tqm.bianfeng.com.tqm.main.HomeBankInfoListAdapter;
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
//我的关注
public class MyFocuseFragment extends BaseFragment {
    public int index = 0;
    public static String ARG_TYPE = "arg_type";
    @BindView(R.id.my_focuse_list)
    RecyclerView myFocuseList;
    @BindView(R.id.toast_txt)
    TextView toastTxt;

    private static final String TYPE="getMyAttentionItem";
    Gson gson;
    IntentToDetail intentToDetail ;


    public static MyFocuseFragment newInstance(int position) {
        MyFocuseFragment fragment = new MyFocuseFragment();
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
        intentToDetail = IntentToDetail.newInstance.intentToDetail;
        //initData();
        //initData();
        return view;
    }
    public void initData(){
        if(index==0){
            initActivityData();
        }else if(index==1){
            initFinancData();
        }else if(index==2){
            initLoanData();
        }else if(index==3){
            initInfoData();
        }else if(index==4){
            initLawyerData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    List<BankActivityItem> BankActivityItems;
    List<BankLoanItem> BankLoanItems;
    List<BankFinancItem> BankFinancItems;
    List<LawyerItem> LawyerItems;
    List<BankInformItem> BankInformItems;

    public void initFinancData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionItem02(TYPE,realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankFinancItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","BankLoanItems"+e.toString());
                    }

                    @Override
                    public void onNext(List<BankFinancItem> bankFinancItems) {
                        BankFinancItems = bankFinancItems;

                        if(BankFinancItems.size()!=0){
                            toastTxt.setVisibility(View.GONE);
                        }else{
                            toastTxt.setVisibility(View.VISIBLE);
                            BankFinancItems=new ArrayList<BankFinancItem>();
                        }
                        initFinancList(BankFinancItems);
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void initInfoData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionItem04(TYPE,realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankInformItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","BankLoanItems"+e.toString());
                    }

                    @Override
                    public void onNext(List<BankInformItem> bankInformItems) {
                        BankInformItems = bankInformItems;

                        if(BankInformItems.size()!=0){
                            toastTxt.setVisibility(View.GONE);
                        }else{
                            toastTxt.setVisibility(View.VISIBLE);
                            BankInformItems=new ArrayList<BankInformItem>();
                        }
                        initInfoList(BankInformItems);
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void initLawyerData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionItem05(TYPE,realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LawyerItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","BankLoanItems"+e.toString());
                    }

                    @Override
                    public void onNext(List<LawyerItem> lawyerItems) {
                        LawyerItems = lawyerItems;

                        if(LawyerItems.size()!=0){
                            toastTxt.setVisibility(View.GONE);
                        }else{
                            toastTxt.setVisibility(View.VISIBLE);
                            LawyerItems=new ArrayList<LawyerItem>();
                        }
                        initLawList(LawyerItems);
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void initLoanData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionItem03(TYPE,realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankLoanItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","BankLoanItems"+e.toString());
                    }

                    @Override
                    public void onNext(List<BankLoanItem> bankLoanItems) {
                        BankLoanItems = bankLoanItems;

                        if(BankLoanItems.size()!=0){
                            toastTxt.setVisibility(View.GONE);
                        }else{
                            toastTxt.setVisibility(View.VISIBLE);
                            BankLoanItems=new ArrayList<BankLoanItem>();
                        }
                        Log.i("gqf","BankLoanItems"+BankLoanItems.toString());
                        initLoanList(BankLoanItems);
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void initActivityData() {
        Subscription subscription = NetWork.getUserService().getMyAttentionItem01(TYPE,realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankActivityItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","BankLoanItems"+e.toString());
                    }

                    @Override
                    public void onNext(List<BankActivityItem> bankActivityItems) {
                        BankActivityItems = bankActivityItems;

                        if(BankActivityItems.size()!=0){
                            toastTxt.setVisibility(View.GONE);
                        }else{
                            toastTxt.setVisibility(View.VISIBLE);
                            BankActivityItems=new ArrayList<BankActivityItem>();
                        }
                        initActivityList(BankActivityItems);
                    }
                });
        compositeSubscription.add(subscription);
    }


    HomeBankActivitysListAdapter bankActivitionsAdapter;
    //HomeBankLoanListAdapter bankLoanAdapter;
    BankLoanAdapter bankLoanAdapter;
    HomeBankFinancingListAdapter bankFinancingAdapter;
    LawListAdapter lawListAdapter;
    HomeBankInfoListAdapter homeBankInfoListAdapter;

    public void initInfoList(List<BankInformItem> datas) {
        if (homeBankInfoListAdapter == null) {
            myFocuseList.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
            homeBankInfoListAdapter = new HomeBankInfoListAdapter(getActivity(), datas);
            myFocuseList.setAdapter(homeBankInfoListAdapter);
            homeBankInfoListAdapter.setOnItemClickListener(new HomeBankInfoListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    //跳转银行咨询详情
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", DetailActivity.INFOR_TYPE);
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
        if (bankActivitionsAdapter == null) {
            bankActivitionsAdapter = new HomeBankActivitysListAdapter(getActivity(), datas);
            bankActivitionsAdapter.setOnItemClickListener(new HomeBankActivitysListAdapter.HomeBankActivitysItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    startActivity(intentToDetail.ActivityToDetail(
                            getActivity(),
                            bankActivitionsAdapter.getDataItem(position).getActivityId(),
                            bankActivitionsAdapter.getDataItem(position).getArticlePath(),
                            bankActivitionsAdapter.getDataItem(position).getActivityTitle()
                    ));
                }
            });
            myFocuseList.setLayoutManager(new LinearLayoutManager(getActivity()));
            myFocuseList.setAdapter(bankActivitionsAdapter);
        } else {
            bankActivitionsAdapter.update(datas);
        }
    }

//    public void initLoanList(List<BankLoanItem> datas) {
//        if (bankLoanAdapter == null) {
//            bankLoanAdapter = new HomeBankLoanListAdapter(getActivity(), datas);
//            bankLoanAdapter.setBgNull();
//            bankLoanAdapter.setOnItemClickListener(new HomeBankLoanListAdapter.HomeBankLoanClickListener() {
//                @Override
//                public void OnClickListener(int position) {
//                    Intent intent = new Intent(getActivity(), DetailActivity.class);
//                    intent.putExtra("detailType", "03");
//                    intent.putExtra("detailId", bankLoanAdapter.getItem(position).getLoanId());
//                    intent.putExtra("detailTitle", bankLoanAdapter.getDataItem(position).getLoanName());
//                    startActivity(intent);
//                }
//            });
//            myFocuseList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//            myFocuseList.setAdapter(bankLoanAdapter);
//        } else {
//            bankLoanAdapter.update(datas);
//        }
//    }
public void initLoanList(List<BankLoanItem> datas) {
    if (bankLoanAdapter == null) {
        bankLoanAdapter = new tqm.bianfeng.com.tqm.User.adapter.BankLoanAdapter(getActivity(), datas);
        // bankLoanAdapter.setBgNull();
        bankLoanAdapter.setOnItemClickListener(new tqm.bianfeng.com.tqm.User.adapter.BankLoanAdapter.BankLoanItemClickListener() {
            @Override
            public void onItemClick(View view,int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("detailType", DetailActivity.LOAN_TYPE);
                intent.putExtra("detailId", BankLoanItems.get(position).getLoanId());
                intent.putExtra("detailTitle", BankLoanItems.get(position).getLoanName());
                startActivity(intent);
            }
        });
        myFocuseList.setLayoutManager(new LinearLayoutManager(getActivity()));
        myFocuseList.setAdapter(bankLoanAdapter);
    } else {
        bankLoanAdapter.setdatas(datas);
    }
}

    public void initFinancList(List<BankFinancItem> datas) {
        if (bankFinancingAdapter == null) {
            bankFinancingAdapter = new HomeBankFinancingListAdapter(getActivity(), datas);
            bankFinancingAdapter.setOnItemClickListener(new HomeBankFinancingListAdapter.HomeBankFinancingItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", DetailActivity.FINANC_TYPE);
                    intent.putExtra("detailId", bankFinancingAdapter.getDataItem(position).getFinancId());
                    intent.putExtra("detailTitle", bankFinancingAdapter.getDataItem(position).getProductName());
                    startActivity(intent);
                }
            });
            myFocuseList.setLayoutManager(new LinearLayoutManager(getActivity()));
            myFocuseList.setAdapter(bankFinancingAdapter);
        } else {
            bankFinancingAdapter.update(datas);
        }
    }

    public void initLawList(List<LawyerItem> datas) {
        if (lawListAdapter == null) {
            lawListAdapter = new LawListAdapter(getActivity(), datas);
            lawListAdapter.setInMyFocuse(true);
            lawListAdapter.setOnItemClickListener(new LawListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailId", LawyerItems.get(position).getLawyerId());
                    intent.putExtra("detailTitle", LawyerItems.get(position).getLawyerName());
                    intent.putExtra("detailType", DetailActivity.LAWYER_TYPE);
                    startActivity(intent);
                }

                @Override
                public void CallClick(int position) {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lawListAdapter.getDataItem(position)));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentPhone);
                }

                @Override
                public void CollectionClick(int position) {

                }
                @Override
                public void changePosition(int position) {
                    lawListAdapter.notifyItemChanged(position);
                }
            });
            myFocuseList.setLayoutManager(new LinearLayoutManager(getActivity()));
            myFocuseList.setAdapter(lawListAdapter);
        } else {
            lawListAdapter.update(datas);
            lawListAdapter.notifyDataSetChanged();
        }
    }

}
