package tqm.bianfeng.com.tqm.Institutions;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.lawhelp.adapter.LawListAdapter;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.main.HomeBankActivitysListAdapter;
import tqm.bianfeng.com.tqm.main.HomeBankFinancingListAdapter;
import tqm.bianfeng.com.tqm.pojo.LawyerItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by johe on 2017/4/11.
 */

public class ActivityLoaninancingLawListFragment extends BaseFragment {

    public int index = 0;
    public static String ARG_TYPE = "arg_type";
    @BindView(R.id.activity_loan_financing_law_list)
    RecyclerView activityLoanFinancingLawList;
    @BindView(R.id.toast_txt)
    TextView toastTxt;


    public static ActivityLoaninancingLawListFragment newInstance(int position) {
        ActivityLoaninancingLawListFragment fragment = new ActivityLoaninancingLawListFragment();
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
        View view = inflater.inflate(R.layout.fragment_activity_loan_financing_law_liat, container, false);
        ButterKnife.bind(this, view);
        toastTxt.setVisibility(View.VISIBLE);
        if (BankActivityItems != null) {

            if(BankActivityItems.size()!=0){
                setList(BankActivityItems.size());
                initActivityList(BankActivityItems);
                toastTxt.setVisibility(View.GONE);
            }
        } else if (BankLoanItems != null) {

            if(BankLoanItems.size()!=0){
                setList(BankLoanItems.size());
                initLoanList(BankLoanItems);
                toastTxt.setVisibility(View.GONE);
            }
        } else if (BankFinancItems != null) {

            if(BankFinancItems.size()!=0){
                setList(BankFinancItems.size());
                initFinancList(BankFinancItems);
                toastTxt.setVisibility(View.GONE);
            }
        } else if (LawyerItems != null) {

            if(LawyerItems.size()!=0){
                setList(LawyerItems.size());
                initLawList(LawyerItems);
                toastTxt.setVisibility(View.GONE);
            }
        }else{
            setList(0);
        }


        return view;
    }
    public void setList(int i){
//        if(i<=2){
//            activityLoanFinancingLawList.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
//        }else{
//            activityLoanFinancingLawList.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return false;
//                }
//            });
//        }
    }

    List<BankActivityItem> BankActivityItems;
    List<BankLoanItem> BankLoanItems;
    List<BankFinancItem> BankFinancItems;
    List<LawyerItem> LawyerItems;

    public void setActivityDatas(List<BankActivityItem> datas) {
        BankActivityItems = datas;
        if (activityLoanFinancingLawList != null) {
            initActivityList(BankActivityItems);
        }
    }

    public void setLoanDatas(List<BankLoanItem> datas) {
        BankLoanItems = datas;
        if (activityLoanFinancingLawList != null) {
            initLoanList(BankLoanItems);
        }
    }

    public void setFinancingDatas(List<BankFinancItem> datas) {
        BankFinancItems = datas;
        if (activityLoanFinancingLawList != null) {
            initFinancList(BankFinancItems);
        }
    }

    public void setLawDatas(List<LawyerItem> datas) {
        LawyerItems = datas;
        if (activityLoanFinancingLawList != null) {
            initLawList(LawyerItems);
        }
    }

    HomeBankActivitysListAdapter bankActivitionsAdapter;
    tqm.bianfeng.com.tqm.User.adapter.BankLoanAdapter bankLoanAdapter;
    HomeBankFinancingListAdapter bankFinancingAdapter;
    LawListAdapter lawListAdapter;

    public void initActivityList(List<BankActivityItem> datas) {
        Log.i("gqf","initActivityList"+datas.toString());
        bankActivitionsAdapter = new HomeBankActivitysListAdapter(getActivity(), datas);
        bankActivitionsAdapter.setInName("");
        bankActivitionsAdapter.setOnItemClickListener(new HomeBankActivitysListAdapter.HomeBankActivitysItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("detailType", DetailActivity.ACTIVITY_TYPE);
                intent.putExtra("detailId", bankActivitionsAdapter.getDataItem(position).getActivityId());
                intent.putExtra("detailTitle", bankActivitionsAdapter.getDataItem(position).getActivityTitle());
                startActivity(intent);
            }
        });
        activityLoanFinancingLawList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityLoanFinancingLawList.setAdapter(bankActivitionsAdapter);
    }

    public void initLoanList(List<BankLoanItem> datas) {
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
        //activityLoanFinancingLawList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        activityLoanFinancingLawList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityLoanFinancingLawList.setAdapter(bankLoanAdapter);
    }

    public void initFinancList(List<BankFinancItem> datas) {
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
        activityLoanFinancingLawList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityLoanFinancingLawList.setAdapter(bankFinancingAdapter);
    }

    public void initLawList(List<LawyerItem> datas) {
        lawListAdapter = new LawListAdapter(getActivity(), datas);
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
                Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lawListAdapter.getDataItem(position).getContact()));
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
        activityLoanFinancingLawList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityLoanFinancingLawList.setAdapter(lawListAdapter);
    }


}
