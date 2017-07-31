package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.PayTrans;

/**
 * Created by johe on 2017/4/11.
 */

public class UserTransactionRecordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static Context mContext;


    private List<PayTrans> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    public PayTrans getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public UserTransactionRecordsAdapter(Context mContext, List<PayTrans> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public void update(List<PayTrans> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<PayTrans> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.user_transactionrecords;
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
        PayTrans data = datas.get(p);
        mHolder.userTransactionRecordsJynrTv.setText(data.getJynr());
        if ("01".equals(data.getJylx())) {
            mHolder.userTransactionRecordsJyrqTv.setText(TimeUtils.milliseconds2String(data.getJyrq()));
        } else if ("02".equals(data.getJylx())) {
            mHolder.userTransactionRecordsJyrqTv.setText(data.getSxrq());
        }
        mHolder.userTransactionRecordsJyjeTv.setText("¥"+data.getJyje());
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
        @BindView(R.id.userTransactionRecords_jynr_tv)
        TextView userTransactionRecordsJynrTv;
        @BindView(R.id.userTransactionRecords_jyje_tv)
        TextView userTransactionRecordsJyjeTv;
        @BindView(R.id.userTransactionRecords_jyrq_tv)
        TextView userTransactionRecordsJyrqTv;

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

