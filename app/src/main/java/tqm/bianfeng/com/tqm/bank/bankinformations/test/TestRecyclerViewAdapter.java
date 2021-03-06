package tqm.bianfeng.com.tqm.bank.bankinformations.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder> {
    BankInformItemClickListener mItemClickListener;
    List<BankActivityItem> datas;
    Context mContext;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;



    public TestRecyclerViewAdapter(Context mContext, List<BankActivityItem> datas) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    public BankActivityItem getItem(int position) {
        return datas.get(position);
    }
    public void update(List<BankActivityItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public interface BankInformItemClickListener {
        public void OnClickListener(int position);
        public void changePosition(int position);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(BankInformItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bankinformation_item, parent, false);

        //        switch (viewType) {
        //            case TYPE_HEADER: {
        //                view = LayoutInflater.from(parent.getContext())
        //                        .inflate(R.layout.list_item_card_big, parent, false);
        //                return new RecyclerView.ViewHolder(view) {
        //                };
        //            }
        //            case TYPE_CELL: {
        //                view = LayoutInflater.from(parent.getContext())
        //                        .inflate(R.layout.list_item_card_small, parent, false);
        //                return new RecyclerView.ViewHolder(view) {
        //                };
        //            }
        //        }
        return new ViewHolder(view, mItemClickListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //        switch (getItemViewType(position)) {
        //            case TYPE_HEADER:
        //                break;
        //            case TYPE_CELL:
        //                break;
        //        }
        ViewHolder viewHolder = (ViewHolder) holder;

        BankActivityItem data = datas.get(position);
        viewHolder.TitleTv.setText(data.getActivityTitle());
        viewHolder.institutionNameTv.setText(data.getInstitution());
        Picasso.with(mContext).load(NetWork.LOAD+data.getImageUrl()).placeholder(R.drawable.hotactivity_2).into( viewHolder.bankinformationImg);
        viewHolder.ViewsTv.setText(data.getActivityViews() + "");
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.Title_tv)
        TextView TitleTv;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.bankinformation_img)
        ImageView bankinformationImg;
        @BindView(R.id.Views_tv)
        TextView ViewsTv;

        BankInformItemClickListener mItemClickListener;

        ViewHolder(View view, BankInformItemClickListener mItemClickListener) {
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