package tqm.bianfeng.com.tqm.User.release;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ReleaseActivityItem;

import static tqm.bianfeng.com.tqm.bank.fragment.FilterAdapter.mContext;

/**
 * Created by johe on 2017/5/16.
 */

public class ReleaseActivityFragment extends BaseFragment {
    @BindView(R.id.logo_img)
    ImageView logoImg;
    @BindView(R.id.annualReturn_tv)
    TextView annualReturnTv;
    @BindView(R.id.rateName_tv)
    TextView rateNameTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.institutionName2_tv)
    TextView institutionName2Tv;
    @BindView(R.id.institutionName_tv)
    TextView institutionNameTv;
    @BindView(R.id.institutionName_linear)
    LinearLayout institutionNameLinear;
    @BindView(R.id.activityViews_tv)
    TextView activityViewsTv;
    @BindView(R.id.activityViews_linear)
    LinearLayout activityViewsLinear;
    @BindView(R.id.riskGradeName_tv)
    TextView riskGradeNameTv;
    @BindView(R.id.riskGradeName_linear)
    LinearLayout riskGradeNameLinear;
    @BindView(R.id.investmentTerm_tv)
    TextView investmentTermTv;
    @BindView(R.id.investmentTerm_linear)
    LinearLayout investmentTermLinear;
    @BindView(R.id.loanMoney_tv)
    TextView loanMoneyTv;
    @BindView(R.id.loanMoney_linear)
    LinearLayout loanMoneyLinear;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    //firstpage_listitem


    ReleaseActivityItem releaseActivityItem;

    public void setReleaseActivityItem(ReleaseActivityItem releaseActivityItem) {
        this.releaseActivityItem = releaseActivityItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.firstpage_listitem, container, false);
        ButterKnife.bind(this, view);

        if(releaseActivityItem!=null){
            initView();
        }
        return view;
    }
    public void initView(){
        logoImg.setVisibility(View.VISIBLE);
        annualReturnTv.setVisibility(View.GONE);
        rateNameTv.setVisibility(View.GONE);
        loanMoneyLinear.setVisibility(View.GONE);
        riskGradeNameLinear.setVisibility(View.GONE);
        investmentTermLinear.setVisibility(View.GONE);
        Picasso.with(mContext).load(NetWork.LOAD + releaseActivityItem.getImageUrl()).placeholder(R.drawable.ic_img_loading).into(logoImg);
        titleTv.setText(releaseActivityItem.getActivityTitle());
        activityViewsTv.setText(releaseActivityItem.getActivityViews() + "");
        institutionNameTv.setText(releaseActivityItem.getInstitutionName());

        institutionName2Tv.setVisibility(View.GONE);
    }

}
