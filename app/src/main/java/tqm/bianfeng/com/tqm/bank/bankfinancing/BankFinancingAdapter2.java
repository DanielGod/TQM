package tqm.bianfeng.com.tqm.bank.bankfinancing;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.StringUtil;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by johe on 2017/4/11.
 */

public class BankFinancingAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BankFinancItem> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    public BankFinancItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public BankFinancingAdapter2(Context mContext, List<BankFinancItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<BankFinancItem> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<BankFinancItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.activity_financing_listitem;
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
        BankFinancItem data = datas.get(p);
        mHolder.titleTv.setText(data.getProductName());
        mHolder.institutionNameTv.setText(data.getInstitution());
        Log.e(Constan.LOGTAGNAME,"利率"+data.getAnnualReturn());
        mHolder.annualReturnTv.setText(data.getAnnualReturn() + "%");
        mHolder.investmentTermTv.setText(data.getInvestmentTerm());
        Log.e("Daniel", "理财机构icon：" + NetWork.LOAD + data.getInstitutionIcon());
        Picasso.with(mContext).load(NetWork.LOAD + data.getInstitutionIcon() + Constan.IMGURL)
                .placeholder(R.drawable.logo).into(mHolder.institutionIconImg);
        if (!StringUtil.isBlank(data.getPurchaseMoney())) {
            mHolder.purchaseMoneyTv.setText(data.getPurchaseMoney());
        } else {
            mHolder.purchaseMoneyTv.setText("0 元");
        }
//
//        if (!isFistPage) {
//            mHolder.loanListitemCardview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EventBus.getDefault().post(new ListItemPositioin("02", position));
//                }
//            });
//        }
        if ("02".equals(data.getPresaleStatus()))
            mHolder.presaleStatusImg.setImageResource(R.drawable.img_financ_ys);
        if ("01".equals(data.getPresaleStatus()))
            mHolder.presaleStatusImg.setImageResource(R.drawable.img_financ_ks);
        if ("03".equals(data.getPresaleStatus()))
            mHolder.presaleStatusImg.setImageResource(R.drawable.img_financ_ts);


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
        @BindView(R.id.presaleStatus_img)
        ImageView presaleStatusImg;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.purchaseMoney_lab)
        TextView purchaseMoneyLab;
        @BindView(R.id.purchaseMoney_tv)
        TextView purchaseMoneyTv;
        @BindView(R.id.investmentTerm_lab)
        TextView investmentTermLab;
        @BindView(R.id.investmentTerm_tv)
        TextView investmentTermTv;
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.institutionIcon_img)
        ImageView institutionIconImg;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.loan_listitem_cardview)
        CardView loanListitemCardview;


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

