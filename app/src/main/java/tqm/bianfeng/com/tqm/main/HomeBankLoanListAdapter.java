package tqm.bianfeng.com.tqm.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        return R.layout.loan_firstpage_listitem;

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
        if ((data.getRateMin().equals(data.getRateMax()))){
            mHolder.tv1.setText(data.getRateMin() + "%");
        }else {

            mHolder.tv1.setText(data.getRateMin() + "%"+"-"+data.getRateMax() + "%");
        }
        mHolder.tv2.setText(data.getLoanName());
        mHolder.tv3.setText(data.getLoanPeriodMin() +"-"+data.getLoanPeriodMax()+ "个月");
        mHolder.tv4.setText(data.getLoanMoneyMin()+"-"+data.getLoanMoneyMax()+ "万");

        if(isBgNull){
            mHolder.loanItemBg.setBackgroundResource(R.drawable.loan_item_shap);
        }

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
        @BindView(R.id.loan_item_bg)
        LinearLayout loanItemBg;

        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;
        @BindView(R.id.tv4)
        TextView tv4;

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

