package tqm.bianfeng.com.tqm.bank.bankfinancing;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

/**
 * Created by wjy on 2016/11/7.
 */

public class BankFinancingAdapter extends BaseAdapter {

    List<BankFinancItem> datas;

    private Context mContext;
    boolean isFistPage=false;

    public BankFinancingAdapter(Context mContext,List<BankFinancItem> datas,boolean isFistPage ) {
        this.datas = datas;
        this.mContext = mContext;
        this.isFistPage = isFistPage;
    }

    @Override
    public int getCount() {
        return datas!=null ?datas.size():0;
    }

    @Override
    public BankFinancItem getItem(int position) {
        Log.e("Daniel","----getItem()---position--"+position);
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.e("Daniel","----getItemId()---position--"+position);
        return position;
    }
    @DebugLog
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e("Daniel","----getView()---position--"+position);
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
            holder.rateName = (TextView) convertView.findViewById(R.id.rateName_tv);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearlayout);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e("Daniel","----getCount()-----"+getCount());
        BankFinancItem data = getItem(position);
        holder.loanMoneyTv.setVisibility(View.GONE);
        holder.loanTypeNameTv.setVisibility(View.GONE);
        holder.titleTv.setText(data.getProductName());
        holder.institutionNameTv.setText(data.getInstitutionName());
        holder.annualReturnTv.setText(data.getAnnualReturn()+"%");
        holder.riskGradeNameTv.setText("风险："+data.getRiskGradeName());
        holder.investmentTermTv.setText("期限："+data.getInvestmentTerm());
        holder.purchaseMoneyTv.setText("起购金额："+data.getPurchaseMoney()+"");
        holder.financViewsTv.setText(""+data.getFinancViews());
        holder.rateName.setText("预期年化");
        if (!isFistPage) {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ListItemPositioin(position));
                }
            });
        }

        return convertView;
    }
    @DebugLog
    public void setdatas(List<BankFinancItem> decoCompanyItemList) {
        this.datas = decoCompanyItemList;
        notifyDataSetChanged();
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
        TextView rateName;
        LinearLayout linearLayout;

    }
}
