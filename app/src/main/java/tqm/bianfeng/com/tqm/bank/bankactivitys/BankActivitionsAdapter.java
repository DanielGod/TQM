package tqm.bianfeng.com.tqm.bank.bankactivitys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

/**
 * Created by wjy on 2016/11/7.
 */

public class BankActivitionsAdapter extends BaseAdapter {

    private Context mContext;
    List<BankActivityItem> datas;
    boolean isFistPage = false;


    public int getLayout() {
        return R.layout.bank_activity_item;
    }

    public BankActivitionsAdapter(List<BankActivityItem> datas, Context mContext,boolean isFistPage) {
        this.isFistPage =isFistPage;
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas!=null ?datas.size():0;
    }

    @Override
    public BankActivityItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @DebugLog
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bank_activity_item, parent, false);
            holder = new ViewHolder();
            holder.logoImg = (ImageView) convertView.findViewById(R.id.logo_img);
            holder.activityTitleTv = (TextView) convertView.findViewById(R.id.activityTitle_tv);
            holder.institutionNameTv = (TextView) convertView.findViewById(R.id.institutionName_tv);
            holder.activityViewsTv = (TextView) convertView.findViewById(R.id.activityViews_tv);
            holder.linearlayout = (LinearLayout) convertView.findViewById(R.id.linearlayout);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankActivityItem data = getItem(position);
        Picasso.with(mContext).load(NetWork.LOAD+data.getInstitutionIcon()).placeholder(R.drawable.banklogo).into(holder.logoImg);
        holder.activityTitleTv.setText(data.getActivityTitle());
        holder.institutionNameTv.setText(data.getInstitutionName());
        holder.activityViewsTv.setText(data.getActivityViews()+"");
        if (!isFistPage) {
            holder.linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ListItemPositioin("01",position));
                }
            });
        }
        return convertView;
    }

    public void setdatas(List<BankActivityItem> decoCompanyItemList) {
        this.datas = decoCompanyItemList;
        notifyDataSetChanged();
    }

    static class ViewHolder  {

        ImageView logoImg;
        TextView activityTitleTv;
        TextView institutionNameTv;
        TextView activityViewsTv;
        LinearLayout linearlayout;


        }
    }

