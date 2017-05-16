package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.ReleaseLoanItem;
import tqm.bianfeng.com.tqm.update.StringUtils;

/**
 * Created by johe on 2017/5/15.
 */

public class MyReleaseLoanAdapter   extends RecyclerView.Adapter< MyReleaseLoanAdapter.ViewHolder> {

    List<ReleaseLoanItem> datas;
     ReleaseLoanItemClickListener mItemClickListener;



    private Context mContext;
    private LayoutInflater mLayoutInflater;
    ReleaseLoanItem data;


    public int getLayout() {
        return R.layout.my_release_loan_item;
    }

    public MyReleaseLoanAdapter( Context mContext,List<ReleaseLoanItem> datas) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener( ReleaseLoanItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface ReleaseLoanItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new  ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, final int position) {
        ReleaseLoanItem data =datas.get(position);
        holder.institutionNameLinear.setVisibility(View.VISIBLE);
        holder.investmentTermLinear.setVisibility(View.VISIBLE);
        holder.financViewsLinear.setVisibility(View.VISIBLE);
        holder.loanMoneyAndloanTypeNameLinear.setVisibility(View.VISIBLE);
        holder.annualReturnTv.setText(data.getRate() + "%");
        holder.institutionNameTv.setText(data.getInstitutionName());
        holder.titleTv.setText(data.getLoanName());
        holder.loanMoneyTv.setText("" + data.getLoanMoney().setScale(0, BigDecimal.ROUND_DOWN));
        holder.investmentTermTv.setText(data.getLoanPeriod());
        holder.financViewsTv.setText(data.getLoanViews() + "");

        if (StringUtils.isEmpty(data.getLoanTypeName())){
            holder.loanTypeNameLinear.setVisibility(View.GONE);
        }else {

            holder.loanTypeNameTv.setText(data.getLoanTypeName());
        }
        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(null,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public void setdatas(List<ReleaseLoanItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
         ReleaseLoanItemClickListener mListener;
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


        ViewHolder(View view,  ReleaseLoanItemClickListener listener) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
            this.mListener = listener;
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }

        }
    }
}
