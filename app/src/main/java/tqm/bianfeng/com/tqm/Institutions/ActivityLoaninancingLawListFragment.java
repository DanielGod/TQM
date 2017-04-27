package tqm.bianfeng.com.tqm.Institutions;

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
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.lawhelp.LawDetailActivity;
import tqm.bianfeng.com.tqm.lawhelp.adapter.LawListAdapter;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.main.HomeBankActivitysListAdapter;
import tqm.bianfeng.com.tqm.main.HomeBankFinancingListAdapter;
import tqm.bianfeng.com.tqm.main.HomeBankLoanListAdapter;
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
            initActivityList(BankActivityItems);
            if(BankActivityItems.size()!=0){
                toastTxt.setVisibility(View.GONE);
            }
        } else if (BankLoanItems != null) {
            initLoanList(BankLoanItems);
            if(BankLoanItems.size()!=0){
                toastTxt.setVisibility(View.GONE);
            }
        } else if (BankFinancItems != null) {
            initFinancList(BankFinancItems);
            if(BankFinancItems.size()!=0){
                toastTxt.setVisibility(View.GONE);
            }
        } else if (LawyerItems != null) {
            initLawList(LawyerItems);
            if(LawyerItems.size()!=0){
                toastTxt.setVisibility(View.GONE);
            }
        }

        return view;
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
    HomeBankLoanListAdapter bankLoanAdapter;
    HomeBankFinancingListAdapter bankFinancingAdapter;
    LawListAdapter lawListAdapter;

    public void initActivityList(List<BankActivityItem> datas) {
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
        activityLoanFinancingLawList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityLoanFinancingLawList.setAdapter(bankActivitionsAdapter);
    }

    public void initLoanList(List<BankLoanItem> datas) {
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
        activityLoanFinancingLawList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        activityLoanFinancingLawList.setAdapter(bankLoanAdapter);
    }

    public void initFinancList(List<BankFinancItem> datas) {
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
        activityLoanFinancingLawList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityLoanFinancingLawList.setAdapter(bankFinancingAdapter);
    }

    public void initLawList(List<LawyerItem> datas) {
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
        activityLoanFinancingLawList.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityLoanFinancingLawList.setAdapter(lawListAdapter);
    }


}
