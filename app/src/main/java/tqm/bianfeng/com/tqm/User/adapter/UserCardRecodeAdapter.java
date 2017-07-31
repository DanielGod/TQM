package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.Card;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by johe on 2017/4/11.
 */

public class UserCardRecodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static Context mContext;

    private List<Card> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    public Card getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public UserCardRecodeAdapter(Context mContext, List<Card> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public void update(List<Card> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<Card> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.user_cardrecode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v, mItemClickListener);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        Card data = datas.get(p);
        mHolder.userCardRecodeRemarkTv.setText(data.getRemark());
        Log.e(Constan.LOGTAGNAME,NetWork.LOAD + data.getCardUrl());
        Picasso.with(mContext).load(NetWork.LOAD + data.getCardUrl()).placeholder(R.drawable.placeholder).into(mHolder.userCardRecodePicImg);
        mHolder.userCardRecodeDateTv.setText("提交时间:" + TimeUtils.milliseconds2String(data.getCreateDate()));
        if ("00".equals(data.getStatusCode())) {
            mHolder.userCardRecodeStatusCodeTv.setText("待审");
        } else if ("01".equals(data.getStatusCode())) {
            mHolder.userCardRecodeStatusCodeTv.setText("审核通过");
        } else if ("02".equals(data.getStatusCode())) {
            mHolder.userCardRecodeStatusCodeTv.setText("审核未通过");
        }

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        @BindView(R.id.userCardRecode_pic_img)
        ImageView userCardRecodePicImg;
        @BindView(R.id.userCardRecode_statusCode_tv)
        TextView userCardRecodeStatusCodeTv;
        @BindView(R.id.userCardRecode_remark_tv)
        TextView userCardRecodeRemarkTv;
        @BindView(R.id.userCardRecode_date_tv)
        TextView userCardRecodeDateTv;

        ViewHolder(View view, MyItemClickListener mItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.mItemClickListener = mItemClickListener;
            view.setOnClickListener(this);
            //            Log.e("Daniel","ViewHolder,carList："+carList.toString());

        }

        @Override

        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.OnClickListener(getPosition());
            }

        }
    }
}

