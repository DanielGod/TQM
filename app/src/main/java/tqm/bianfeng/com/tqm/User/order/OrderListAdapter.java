package tqm.bianfeng.com.tqm.User.order;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.YwDksq;

/**
 * Created by johe on 2017/4/11.
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<YwDksq> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    public YwDksq getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public OrderListAdapter(Context mContext, List<YwDksq> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<YwDksq> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<YwDksq> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.order_list_item;
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
    public void setOnItemClickListener(MyItemClickListener listener) {
        mItemClickListener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        YwDksq data = datas.get(p);
        //贷款金额
        mHolder.orderMoneyTv.setText(data.getDkje());
        //申请人
        mHolder.orderNameTv.setText(data.getSqr());
        //联系电话
        mHolder.orderPhoneTv.setText(data.getLxdh());
        //城市
        mHolder.orderCityTv.setText(data.getSzcs());
        //贷款期限
        mHolder.orderTimeLimitTv.setText(data.getDkqx()+"");
        //贷款金额贷款用途
        mHolder.orderLoanUserTv.setText(data.getDkyt());
        //收入
        mHolder.orderYearinconeTv.setText(data.getIncome());
        //身份
        if ("01".equals(data.getZysf())){
            mHolder.orderInconeTv.setText(mContext.getApplicationContext().getResources().getString(R.string.identity_monthIncome));
            mHolder.orderIdentityTv.setText(mContext.getApplicationContext().getResources().getString(R.string.identity_officeWorker));
        }else if ("02".equals(data.getZysf())){
            mHolder.orderInconeTv.setText(mContext.getApplicationContext().getResources().getString(R.string.identity_businessIncome));
            mHolder.orderIdentityTv.setText(mContext.getApplicationContext().getResources().getString(R.string.identity_businessOwners));
        }
        else if ("03".equals(data.getZysf())){
            mHolder.orderInconeTv.setText(mContext.getApplicationContext().getResources().getString(R.string.identity_annualIncome));
            mHolder.orderIdentityTv.setText(mContext.getApplicationContext().getResources().getString(R.string.identity_soho));
        }else {
            mHolder.orderInconeTv.setText(mContext.getApplicationContext().getResources().getString(R.string.identity_annualIncome));
            mHolder.orderIdentityTv.setText(mContext.getApplicationContext().getResources().getString(R.string.identity_freelance));
        }
        //是否有房
        if ("00".equals(data.getSfyf())) {
            mHolder.isHasHouseTv.setText(mContext.getApplicationContext().getResources().getString(R.string.unhasHouseValue));
        } else {
            mHolder.isHasHouseTv.setText(mContext.getApplicationContext().getResources().getString(R.string.hasHouserValue));
        }
        //是否有车
        if ("00".equals(data.getSfyc())) {
            mHolder.isHasCarTv.setText(mContext.getApplicationContext().getResources().getString(R.string.unhasCarValue));
        } else {
            mHolder.isHasCarTv.setText(mContext.getApplicationContext().getResources().getString(R.string.hasCarValue));
        }
        //        Picasso.with(mContext).load().into(mHolder.orderStatuImg);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        @BindView(R.id.order_name_tv)
        TextView orderNameTv;
        @BindView(R.id.order_money_tv)
        TextView orderMoneyTv;
        @BindView(R.id.order_timeLimit_tv)
        TextView orderTimeLimitTv;
        @BindView(R.id.order_city_tv)
        TextView orderCityTv;
        @BindView(R.id.order_identity_tv)
        TextView orderIdentityTv;
        @BindView(R.id.order_loanUser_tv)
        TextView orderLoanUserTv;
        @BindView(R.id.order_yearincone_tv)
        TextView orderYearinconeTv;
        @BindView(R.id.isHasHouse_tv)
        TextView isHasHouseTv;
        @BindView(R.id.isHasCar_tv)
        TextView isHasCarTv;
        @BindView(R.id.order_phone_tv)
        TextView orderPhoneTv;
        @BindView(R.id.loan_listitem_cardview)
        CardView loanListitemCardview;
        @BindView(R.id.order_incone_tv)
        TextView orderInconeTv;


        ViewHolder(View view, MyItemClickListener mItemClickListener) {
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

