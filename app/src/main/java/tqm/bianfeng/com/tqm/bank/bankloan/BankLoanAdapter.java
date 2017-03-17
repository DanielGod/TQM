package tqm.bianfeng.com.tqm.bank.bankloan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by Daniel on 2017/3/16.
 */

public class BankLoanAdapter extends BaseAdapter {
    Context mContext;
    List<BankLoanItem> datas;

    public BankLoanAdapter(Context mContext, List<BankLoanItem> datas) {
        this.mContext = mContext;
        this.datas = datas;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem, parent, false);
            holder = new ViewHolder();
            holder.annualReturnTv = (TextView) convertView.findViewById(R.id.annualReturn_tv);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.loanMoneyTv = (TextView) convertView.findViewById(R.id.loanMoney_tv);
            holder.purchaseMoneyTv = (TextView) convertView.findViewById(R.id.purchaseMoney_tv);
            holder.riskGradeNameTv = (TextView) convertView.findViewById(R.id.riskGradeName_tv);
            holder.investmentTermTv = (TextView) convertView.findViewById(R.id.investmentTerm_tv);
            holder.institutionNameTv = (TextView) convertView.findViewById(R.id.institutionName_tv);
            holder.financViewsTv = (TextView) convertView.findViewById(R.id.financViews_tv);
            holder.loanTypeNameTv = (TextView) convertView.findViewById(R.id.loanTypeName_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankLoanItem data = getItem(position);
        holder.riskGradeNameTv.setVisibility(View.GONE);
        holder.purchaseMoneyTv.setVisibility(View.GONE);
        holder.annualReturnTv.setText(data.getRate() + "");
        holder.institutionNameTv.setText(data.getInstitutionName());
        holder.titleTv.setText(data.getLoanName());
        holder.loanMoneyTv.setText("最高贷款（万）：" + data.getLoanMoney() + "");
        holder.investmentTermTv.setText("贷款期限：" + data.getLoanPeriod());
        holder.financViewsTv.setText(data.getLoanViews() + "");
        holder.loanTypeNameTv.setText("贷款类型："+data.getLoanTypeName());
        return convertView;
    }

    static class ViewHolder {
        TextView annualReturnTv;
        TextView titleTv;
        TextView loanTypeNameTv;
        TextView loanMoneyTv;
        TextView purchaseMoneyTv;
        TextView riskGradeNameTv;
        TextView investmentTermTv;
        TextView institutionNameTv;
        TextView financViewsTv;
    }
}
