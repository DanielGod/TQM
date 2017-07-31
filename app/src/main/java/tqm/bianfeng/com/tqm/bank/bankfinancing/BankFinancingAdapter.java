package tqm.bianfeng.com.tqm.bank.bankfinancing;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.StringUtil;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

/**
 * Created by wjy on 2016/11/7.
 */

public class BankFinancingAdapter extends BaseAdapter {

    List<BankFinancItem> datas;

    private Context mContext;
    boolean isFistPage = false;

    public BankFinancingAdapter(Context mContext, List<BankFinancItem> datas, boolean isFistPage) {
        this.datas = datas;
        this.mContext = mContext;
        this.isFistPage = isFistPage;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public BankFinancItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @DebugLog
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_financing_listitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankFinancItem data = getItem(position);

        holder.titleTv.setText(data.getProductName());
        holder.institutionNameTv.setText(data.getInstitution());
        Log.e(Constan.LOGTAGNAME,"利率"+data.getAnnualReturn());
        holder.annualReturnTv.setText(data.getAnnualReturn() + "%");
        holder.investmentTermTv.setText(data.getInvestmentTerm());
        Log.e("Daniel", "理财机构icon：" + NetWork.LOAD + data.getInstitutionIcon());
        Picasso.with(mContext).load(NetWork.LOAD + data.getInstitutionIcon() + Constan.IMGURL)
                .placeholder(R.drawable.logo).into(holder.institutionIconImg);
        if (!StringUtil.isBlank(data.getPurchaseMoney())) {
            holder.purchaseMoneyTv.setText(data.getPurchaseMoney());
        } else {
            holder.purchaseMoneyTv.setText("0 元");
        }

        if (!isFistPage) {
            holder.loanListitemCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ListItemPositioin("02", position));
                }
            });
        }
        if ("02".equals(data.getPresaleStatus()))
            holder.presaleStatusImg.setImageResource(R.drawable.img_financ_ys);
        if ("01".equals(data.getPresaleStatus()))
            holder.presaleStatusImg.setImageResource(R.drawable.img_financ_ks);
        if ("03".equals(data.getPresaleStatus()))
            holder.presaleStatusImg.setImageResource(R.drawable.img_financ_ts);


        return convertView;
    }

    @DebugLog
    public void setdatas(List<BankFinancItem> decoCompanyItemList) {
        this.datas = decoCompanyItemList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
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
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.loan_listitem_cardview)
        CardView loanListitemCardview;
        @BindView(R.id.institutionIcon_img)
        ImageView institutionIconImg;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}

