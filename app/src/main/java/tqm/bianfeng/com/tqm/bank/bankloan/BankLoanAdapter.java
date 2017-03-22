package tqm.bianfeng.com.tqm.bank.bankloan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankLoanItem data = getItem(position);
        holder.institutionNameLinear.setVisibility(View.VISIBLE);
        holder.investmentTermLinear.setVisibility(View.VISIBLE);
        holder.financViewsLinear.setVisibility(View.VISIBLE);
        holder.loanMoneyAndloanTypeNameLinear.setVisibility(View.VISIBLE);
        holder.annualReturnTv.setText(data.getRate() + "%");
        holder.institutionNameTv.setText(data.getInstitutionName());
        holder.titleTv.setText(data.getLoanName());
        holder.loanMoneyTv.setText("" + data.getLoanMoney().setScale(0, BigDecimal.ROUND_DOWN));
        holder.investmentTermTv.setText(data.getLoanPeriod());
        holder.financViewsTv.setText(data.getLoanViews() + "");
        holder.loanTypeNameTv.setText(data.getLoanTypeName());
        if (!isFistPage) {
            holder.linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ListItemPositioin(position));
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
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.loanTypeName_tv)
        TextView loanTypeNameTv;
        @BindView(R.id.loanTypeName_linear)
        LinearLayout loanTypeNameLinear;
        @BindView(R.id.riskGradeName_tv)
        TextView riskGradeNameTv;
        @BindView(R.id.riskGradeName_linear)
        LinearLayout riskGradeNameLinear;
        @BindView(R.id.loanMoney_tv)
        TextView loanMoneyTv;
        @BindView(R.id.loanMoney_linear)
        LinearLayout loanMoneyLinear;
        @BindView(R.id.purchaseMoney_tv)
        TextView purchaseMoneyTv;
        @BindView(R.id.purchaseMoney_linear)
        LinearLayout purchaseMoneyLinear;
        @BindView(R.id.investmentTerm_tv)
        TextView investmentTermTv;
        @BindView(R.id.investmentTerm_linear)
        LinearLayout investmentTermLinear;
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
        @BindView(R.id.loanMoneyAndloanTypeName_linear)
        LinearLayout loanMoneyAndloanTypeNameLinear;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
