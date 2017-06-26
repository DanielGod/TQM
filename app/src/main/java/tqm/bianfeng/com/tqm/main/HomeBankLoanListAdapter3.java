package tqm.bianfeng.com.tqm.main;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by johe on 2017/3/14.
 */

public class HomeBankLoanListAdapter3 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<BankLoanItem> datas;
    private final LayoutInflater mLayoutInflater;
    private HomeBankLoanClickListener mItemClickListener;
    boolean isFistPage = false;


    public BankLoanItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public HomeBankLoanListAdapter3(Context mContext, List<BankLoanItem> mDatas, boolean isFistPage) {
        this.mContext = mContext;
        this.datas = mDatas;
        this.isFistPage = isFistPage;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<BankLoanItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.activity_loan_listitem2;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent,false);
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
        BankLoanItem data = getItem(position);
        if ((data.getRateMin()-data.getRateMax())==0){
            mHolder.annualReturnTv.setText(data.getRateMin() + "%");
        }else {

            mHolder.annualReturnTv.setText(data.getRateMin() + "%"+"-"+data.getRateMax() + "%");
        }
        mHolder.institutionNameTv.setText(data.getInstitution());
        mHolder.titleTv.setText(data.getLoanName());
        mHolder.purchaseMoneyTv.setText(data.getLoanMoneyMin() + "-" + data.getLoanMoneyMax() + "万");
        mHolder.investmentTermTv.setText(data.getLoanPeriodMin() + "-" + data.getLoanPeriodMax() + "个月");
        mHolder.financViewsTv.setText(data.getLoanViews() + "");
        Logger.d(NetWork.LOAD+"/getImg/"+data.getInstitutionIcon()+ Constan.IMGURL);
        Picasso.with(mContext).load(NetWork.LOAD+"/getImg/"+data.getInstitutionIcon()+ Constan.IMGURL).placeholder(R.drawable.placeholder).into(mHolder.bankfinancingimg);
    }
    boolean isBgNull=false;
    public void setBgNull(){
        isBgNull=true;
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
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.fg_tv)
        TextView fgTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.financViews_tv)
        TextView financViewsTv;
        @BindView(R.id.bankfinancing_img)
        ImageView bankfinancingimg;
        @BindView(R.id.purchaseMoney_lab)
        TextView purchaseMoneyLab;
        @BindView(R.id.purchaseMoney_tv)
        TextView purchaseMoneyTv;
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.investmentTerm_lab)
        TextView investmentTermLab;
        @BindView(R.id.investmentTerm_tv)
        TextView investmentTermTv;
        @BindView(R.id.rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.loan_listitem_cardview)
        CardView loanListitemCardview;

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

