package tqm.bianfeng.com.tqm.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

import static tqm.bianfeng.com.tqm.R.id.rateName_tv;

/**
 * Created by johe on 2017/3/14.
 */

public class HomeBankFinancingListAdapter4 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<BankFinancItem> datas;
    private final LayoutInflater mLayoutInflater;
    private HomeBankLoanClickListener mItemClickListener;
    boolean isFistPage = false;


    public BankFinancItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public HomeBankFinancingListAdapter4(Context mContext, List<BankFinancItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        this.isFistPage = isFistPage;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<BankFinancItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.activity_loan_listitem4;

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
        BankFinancItem data = getItem(position);
        Log.e(Constan.LOGTAGNAME,"利率"+data.getAnnualReturn());
        mHolder.annualReturnTv.setText(data.getAnnualReturn()  + "%");
        mHolder.institutionNameTv.setText(data.getInstitution());
        mHolder.titleTv.setText(data.getProductName());
        mHolder.rateNameTv.setText("预期年化");
//        Picasso.with(mContext).load(NetWork.LOAD + "/getImg/" + data.getInstitutionIcon() + Constan.IMGURL).placeholder(R.drawable.placeholder).into(mHolder.bankfinancingimg);
    }

    boolean isBgNull = false;

    public void setBgNull() {
        isBgNull = true;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public BankFinancItem getItem(int position) {
        return datas.get(position);
    }

    public interface HomeBankLoanClickListener {
        public void OnClickListener(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private HomeBankLoanClickListener mItemClickListener;
        @BindView(R.id.logo_img)
        ImageView logoImg;
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;


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

