package tqm.bianfeng.com.tqm.User.release;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.pojo.ReleaseLoanItem;

/**
 * Created by johe on 2017/5/16.
 */
//发布贷款进度顶部
public class ReleaseLoanFragment extends BaseFragment {


    ReleaseLoanItem releaseLoanItem;
    @BindView(R.id.annualReturn_tv)
    TextView annualReturnTv;
    @BindView(R.id.rateName_tv)
    TextView rateNameTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.purchaseMoney_tv)
    TextView purchaseMoneyTv;
    @BindView(R.id.purchaseMoney_linear)
    LinearLayout purchaseMoneyLinear;
    @BindView(R.id.investmentTerm_tv)
    TextView investmentTermTv;
    @BindView(R.id.investmentTerm_linear)
    LinearLayout investmentTermLinear;
    @BindView(R.id.purchaseMoneyAndRiskGradeName_linear)
    LinearLayout purchaseMoneyAndRiskGradeNameLinear;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.institutionName_tv)
    TextView institutionNameTv;
    @BindView(R.id.institutionName_linear)
    LinearLayout institutionNameLinear;
    @BindView(R.id.financViews_tv)
    TextView financViewsTv;
    @BindView(R.id.financViews_linear)
    LinearLayout financViewsLinear;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;

    public void setReleaseLoanItem(ReleaseLoanItem releaseLoanItem) {
        this.releaseLoanItem = releaseLoanItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.loan_listitem, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (releaseLoanItem != null) {
            initView();
        }
    }

    public void initView() {
         annualReturnTv.setText( releaseLoanItem.getRateMin() + "%" + "-" +  releaseLoanItem.getRateMax() + "%");
         institutionNameTv.setText( releaseLoanItem.getInstitution());
         titleTv.setText( releaseLoanItem.getLoanName());
         purchaseMoneyTv.setText( releaseLoanItem.getLoanMoneyMin() + "-" +  releaseLoanItem.getLoanMoneyMax() + "万");
         investmentTermTv.setText( releaseLoanItem.getLoanPeriodMin() + "-" +  releaseLoanItem.getLoanPeriodMax() + "个月");
         financViewsTv.setText( releaseLoanItem.getLoanViews() + "");

         linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(releaseLoanItem.getStatusCode().equals("01")) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", DetailActivity.LOAN_TYPE);
                    intent.putExtra("detailId", releaseLoanItem.getLoanId());
                    intent.putExtra("detailTitle", releaseLoanItem.getLoanName());
                    startActivity(intent);
                }else{
                    LoanOrActivityReleaseActivity.RELEASE_TYPE=LoanOrActivityReleaseActivity.RELEASE_LOAN_TYPE;
                    Intent intent=new Intent(getActivity(),LoanOrActivityReleaseActivity.class);
                    intent.putExtra(LoanOrActivityReleaseActivity.RELEASE_ID,releaseLoanItem.getLoanId());
                    startActivity(intent);
                }
            }
        });
    }

}
