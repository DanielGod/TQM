package tqm.bianfeng.com.tqm.bank.bankinformations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankInformItem;

/**
 * Created by wjy on 2016/11/7.
 */

public class BankInfromationAdapter extends BaseAdapter {

    List<BankInformItem> datas;

    private Context mContext;

    public BankInfromationAdapter(Context mContext, List<BankInformItem> datas) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public BankInformItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_bankinformation_item, parent, false);
            holder = new ViewHolder();
            holder.TitleTv = (TextView) convertView.findViewById(R.id.Title_tv);
            holder.institutionNameTv = (TextView) convertView.findViewById(R.id.institutionName_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.ViewsTv = (TextView) convertView.findViewById(R.id.Views_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankInformItem data = getItem(position);
        holder.TitleTv.setText(data.getInformTitle());
        holder.institutionNameTv.setText(data.getInstitutionName());
        holder.timeTv.setText(data.getReleaseDate());
        holder.ViewsTv.setText(data.getInformViews()+"");

        return convertView;
    }

    @DebugLog
    public void setdatas(List<BankInformItem> decoCompanyItemList) {
        this.datas = decoCompanyItemList;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView TitleTv;
        TextView institutionNameTv;
        TextView timeTv;
        TextView ViewsTv;
    }

}
