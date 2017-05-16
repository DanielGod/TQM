package tqm.bianfeng.com.tqm.User.adapter;

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
 * Created by wjy on 2016/11/7.
 */

public class BankActivitionsAdapter extends RecyclerView.Adapter<BankActivitionsAdapter.ViewHolder> {

    List<BankActivityItem> datas;
    BankActivityItemClickListener mItemClickListener;



    private Context mContext;
    private LayoutInflater mLayoutInflater;
    BankActivityItem data;


    public int getLayout() {
        return R.layout.bank_activity_item;
    }

    public BankActivitionsAdapter(List<BankActivityItem> datas, Context mContext) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(BankActivityItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface BankActivityItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BankActivityItem data = datas.get(position);
        Picasso.with(mContext).load(NetWork.LOAD + data.getImageUrl()).placeholder(R.drawable.banklogo).into(holder.logoImg);
        holder.activityTitleTv.setText(data.getActivityTitle());
        holder.institutionNameTv.setText(data.getInstitution());
        holder.activityViewsTv.setText(data.getActivityViews() + "");
        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(null,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public void setdatas(List<BankActivityItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        BankActivityItemClickListener mListener;
        @BindView(R.id.logo_img)
        ImageView logoImg;
        @BindView(R.id.activityTitle_tv)
        TextView activityTitleTv;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.activityViews_tv)
        TextView activityViewsTv;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;

        ViewHolder(View view, BankActivityItemClickListener listener) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
            this.mListener = listener;
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }

        }
    }
}
