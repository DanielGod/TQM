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

public class HomeBankActivitysListAdapter4 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<BankActivityItem> datas;
    private final LayoutInflater mLayoutInflater;
    private HomeBankActivitysItemClickListener mItemClickListener;
    String inName = "";


    public BankActivityItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public void setInName(String name) {
        inName = name;
    }

    public HomeBankActivitysListAdapter4(Context mContext, List<BankActivityItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<BankActivityItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.bank_activity_item;
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
        Picasso.with(mContext).load(NetWork.LOAD + data.getImageUrl()).placeholder(R.drawable.placeholder).into(mHolder.logoImg);
        mHolder.activityTitleTv.setText(data.getActivityTitle());
        mHolder.activityViewsTv.setText(data.getActivityViews() + "");
        mHolder.institutionNameTv.setText(data.getInstitution());


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

