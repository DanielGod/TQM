package tqm.bianfeng.com.tqm.User.release;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.pojo.ReleaseLoanItem;

/**
 * Created by johe on 2017/5/16.
 */

public class ReleaseLoanFragment extends BaseFragment {

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
    @BindView(R.id.riskGradeName_tv)
    TextView riskGradeNameTv;
    @BindView(R.id.riskGradeName_linear)
    LinearLayout riskGradeNameLinear;
    @BindView(R.id.purchaseMoneyAndRiskGradeName_linear)
    LinearLayout purchaseMoneyAndRiskGradeNameLinear;
    @BindView(R.id.loanMoney_tv)
    TextView loanMoneyTv;
    @BindView(R.id.loanMoney_linear)
    LinearLayout loanMoneyLinear;
    @BindView(R.id.loanTypeName_tv)
    TextView loanTypeNameTv;
    @BindView(R.id.loanTypeName_linear)
    LinearLayout loanTypeNameLinear;
    @BindView(R.id.loanMoneyAndloanTypeName_linear)
    LinearLayout loanMoneyAndloanTypeNameLinear;
    @BindView(R.id.investmentTerm_tv)
    TextView investmentTermTv;
    @BindView(R.id.investmentTerm_linear)
    LinearLayout investmentTermLinear;
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

    ReleaseLoanItem releaseLoanItem;

    public void setReleaseLoanItem(ReleaseLoanItem releaseLoanItem) {
        this.releaseLoanItem = releaseLoanItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listitem, container, false);
        ButterKnife.bind(this, view);

        if(releaseLoanItem!=null){
            initView();
        }
        return view;
    }
    public void initView(){

    }

}
