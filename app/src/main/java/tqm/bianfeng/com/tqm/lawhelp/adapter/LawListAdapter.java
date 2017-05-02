package tqm.bianfeng.com.tqm.lawhelp.adapter;

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
import tqm.bianfeng.com.tqm.pojo.LawyerItem;

/**
 * Created by johe on 2017/3/14.
 */

public class LawListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<LawyerItem> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;


    public LawyerItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public LawListAdapter(Context mContext, List<LawyerItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<LawyerItem> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.law_list_item;
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
        mHolder.lawyerNameTxt.setText(datas.get(p).getLawyerName());
        mHolder.contactTxt.setText(datas.get(p).getInstitutionName());



        if(datas.get(p).getIsAuthorize().equals("01")){
            mHolder.isAuthorizeTxt.setText("已认证");
        }else{
            mHolder.isAuthorizeTxt.setText("未认证");
        }

        if (datas.get(p).getAvatar() != null) {
            Picasso.with(mContext).load(NetWork.LOAD + datas.get(p).getAvatar())
                    .placeholder(R.drawable.ic_user_head_img)
                    .error(R.drawable.ic_user_head_img)
                    .into(mHolder.lawyerImg);
        }
        mHolder.layerInfoLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.OnClickListener(p);
            }
        });
        mHolder.callLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.CallClick(p);
            }
        });
        mHolder.collectionLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mItemClickListener.CollectionClick(p);
            }
        });
        mHolder.collectionLin.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);

        public void CallClick(int position);

        public void CollectionClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lawyer_img)
        ImageView lawyerImg;
        @BindView(R.id.lawyerName_txt)
        TextView lawyerNameTxt;
        @BindView(R.id.contact_txt)
        TextView contactTxt;
        @BindView(R.id.call_lin)
        LinearLayout callLin;
        @BindView(R.id.collection_lin)
        LinearLayout collectionLin;
        @BindView(R.id.layer_info_lin)
        LinearLayout layerInfoLin;
        @BindView(R.id.isAuthorize_txt)
        TextView isAuthorizeTxt;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

