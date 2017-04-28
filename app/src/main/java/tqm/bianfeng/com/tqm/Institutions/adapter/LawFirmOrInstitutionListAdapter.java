package tqm.bianfeng.com.tqm.Institutions.adapter;

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
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;

/**
 * Created by johe on 2017/4/11.
 */

public class LawFirmOrInstitutionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<InstitutionItem> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;


    public InstitutionItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public LawFirmOrInstitutionListAdapter(Context mContext, List<InstitutionItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<InstitutionItem> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.institutions_in_list_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        //View v = mLayoutInflater.inflate(R.layout.my_order_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }


    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.ininLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.OnClickListener(p);
            }
        });
        mHolder.ininNameTxt.setText(datas.get(p).getInstitutionName());
        mHolder.contactTxt.setText("电话：" + datas.get(p).getContact());
        mHolder.profileTxt.setText("简介：" + datas.get(p).getProfile());
        Picasso.with(mContext).load(NetWork.LOAD+datas.get(p).getInstitutionIcon()).placeholder(R.drawable.ic_img_loading).error(R.drawable.ic_img_loading).into(mHolder.ininImg);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.inin_img)
        ImageView ininImg;
        @BindView(R.id.ininName_txt)
        TextView ininNameTxt;
        @BindView(R.id.contact_txt)
        TextView contactTxt;
        @BindView(R.id.profile_txt)
        TextView profileTxt;
        @BindView(R.id.inin_lin)
        LinearLayout ininLin;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

