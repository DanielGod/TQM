package tqm.bianfeng.com.tqm.bank.bankloan;

import android.content.Context;
import android.support.v7.widget.CardView;
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
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

/**
 * Created by Daniel on 2017/3/16.
 */

public class BankLoanAdapter extends BaseAdapter {
    Context mContext;
    List<BankLoanItem> datas;
    boolean isFistPage = false;

    public BankLoanAdapter(Context mContext, List<BankLoanItem> datas, boolean isFistPage) {
        this.mContext = mContext;
        this.datas = datas;
        this.isFistPage = isFistPage;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public BankLoanItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_loan_listitem2, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankLoanItem data = getItem(position);
        if ((data.getRateMin()-data.getRateMax())==0){
            holder.annualReturnTv.setText(data.getRateMin() + "%");
        }else {

            holder.annualReturnTv.setText(data.getRateMin() + "%"+"-"+data.getRateMax() + "%");
        }
        holder.institutionNameTv.setText(data.getInstitution());
        holder.titleTv.setText(data.getLoanName());
        holder.purchaseMoneyTv.setText(data.getLoanMoneyMin() + "-" + data.getLoanMoneyMax() + "万");
        holder.investmentTermTv.setText(data.getLoanPeriodMin() + "-" + data.getLoanPeriodMax() + "个月");
        holder.financViewsTv.setText(data.getLoanViews() + "");
        Picasso.with(mContext).load(NetWork.LOAD+"/getImg/"+data.getInstitutionIcon()+ Constan.IMGURL)
                .placeholder(R.drawable.placeholder).into(holder.bankfinancingimg);
        if (!isFistPage) {
            holder.loanListitemCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ListItemPositioin("03", position));
                }
            });
        }
        return convertView;
    }

    public void setdatas(List<BankLoanItem> decoCompanyItemList) {
        this.datas = decoCompanyItemList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
