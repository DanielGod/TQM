package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by wjy on 2016/11/7.
 */

public class BankLoanAdapter extends RecyclerView.Adapter<BankLoanAdapter.ViewHolder> {

    List<BankLoanItem> datas;
    BankLoanItemClickListener mItemClickListener;



    private Context mContext;
    private LayoutInflater mLayoutInflater;
    BankLoanItem data;


    public int getLayout() {
        return R.layout.loan_listitem;
    }

    public BankLoanAdapter(Context mContext, List<BankLoanItem> datas) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(BankLoanItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface BankLoanItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BankLoanItem data = datas.get(position);


        holder.annualReturnTv.setText(data.getRateMin() + "%" + "-" + data.getRateMax() + "%");
        holder.institutionNameTv.setText(data.getInstitution());
        holder.titleTv.setText(data.getLoanName());
        holder.purchaseMoneyTv.setText(data.getLoanMoneyMin() + "-" + data.getLoanMoneyMax() + "万");
        holder.investmentTermTv.setText(data.getLoanPeriodMin() + "-" + data.getLoanPeriodMax() + "个月");
        holder.financViewsTv.setText(data.getLoanViews() + "");
        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(null, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public void setdatas(List<BankLoanItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        BankLoanItemClickListener mListener;
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

        ViewHolder(View view, BankLoanItemClickListener listener) {
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
