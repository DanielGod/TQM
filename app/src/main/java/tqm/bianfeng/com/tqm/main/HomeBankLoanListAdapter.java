package tqm.bianfeng.com.tqm.main;

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
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by johe on 2017/3/14.
 */

public class HomeBankLoanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<BankLoanItem> datas;
    private final LayoutInflater mLayoutInflater;
    private HomeBankLoanClickListener mItemClickListener;


    public BankLoanItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public HomeBankLoanListAdapter(Context mContext, List<BankLoanItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<BankLoanItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.firstpage_listitem;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v, mItemClickListener);

        return viewHolder;
    }


    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(HomeBankLoanClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        BankLoanItem data = datas.get(position);
        mHolder.riskGradeNameLinear.setVisibility(View.GONE);
        mHolder.activityViewsLinear.setVisibility(View.GONE);
        mHolder.institutionNameLinear.setVisibility(View.GONE);
        mHolder.titleTv.setText(data.getLoanName());
        mHolder.annualReturnTv.setText(data.getRate() + "%");
        mHolder.investmentTermTv.setText(data.getLoanPeriod());
        mHolder.loanMoneyTv.setText("" + data.getLoanMoney().setScale(0, BigDecimal.ROUND_DOWN));
        mHolder.loanMoneyTv.setTextColor(mContext.getResources().getColor(R.color.blue_text));
        mHolder.rateNameTv.setText("年利率");


    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public BankLoanItem getItem(int position) {
        return datas.get(position);
    }

    public interface HomeBankLoanClickListener {
        public void OnClickListener(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private HomeBankLoanClickListener mItemClickListener;

        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.riskGradeName_tv)
        TextView riskGradeNameTv;
        @BindView(R.id.investmentTerm_tv)
        TextView investmentTermTv;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;
        @BindView(R.id.loanMoney_tv)
        TextView loanMoneyTv;
        @BindView(R.id.riskGradeName_linear)
        LinearLayout riskGradeNameLinear;
        @BindView(R.id.loanMoney_linear)
        LinearLayout loanMoneyLinear;
        @BindView(R.id.activityViews_linear)
        LinearLayout activityViewsLinear;
        @BindView(R.id.institutionName_linear)
        LinearLayout institutionNameLinear;

        ViewHolder(View view, HomeBankLoanClickListener mItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.mItemClickListener = mItemClickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.OnClickListener(getPosition());
            }
        }
    }
}

