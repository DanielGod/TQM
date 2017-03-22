package tqm.bianfeng.com.tqm.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;

/**
 * Created by johe on 2017/3/14.
 */

public class HomeBankActivitysListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<BankActivityItem> datas;
    private final LayoutInflater mLayoutInflater;
    private HomeBankActivitysItemClickListener mItemClickListener;


    public BankActivityItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public HomeBankActivitysListAdapter(Context mContext, List<BankActivityItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<BankActivityItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.firstpage_listitem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        //View v = mLayoutInflater.inflate(R.layout.my_order_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v, mItemClickListener);

        return viewHolder;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(HomeBankActivitysItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        BankActivityItem data = getDataItem(position);
        mHolder.logoImg.setVisibility(View.VISIBLE);
        mHolder.annualReturnTv.setVisibility(View.GONE);
        mHolder.rateNameTv.setVisibility(View.GONE);
        mHolder.loanMoneyLinear.setVisibility(View.GONE);
        mHolder.riskGradeNameLinear.setVisibility(View.GONE);
        mHolder.investmentTermLinear.setVisibility(View.GONE);
        Picasso.with(mContext).load(NetWork.LOAD + data.getInstitutionIcon()).placeholder(R.drawable.banklogo).into(mHolder.logoImg);
        mHolder.titleTv.setText(data.getActivityTitle());
        mHolder.activityViewsTv.setText(data.getActivityViews()+"");
        mHolder.institutionNameTv.setText(data.getInstitutionName());


    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface HomeBankActivitysItemClickListener {
        public void OnClickListener(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        HomeBankActivitysItemClickListener mItemClickListener;
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
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
        @BindView(R.id.loanMoney_tv)
        TextView loanMoneyTv;
        @BindView(R.id.loanMoney_linear)
        LinearLayout loanMoneyLinear;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;
        @BindView(R.id.logo_img)
        ImageView logoImg;
        @BindView(R.id.investmentTerm_linear)
        LinearLayout investmentTermLinear;


        ViewHolder(View view, HomeBankActivitysItemClickListener mItemClickListener) {
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

