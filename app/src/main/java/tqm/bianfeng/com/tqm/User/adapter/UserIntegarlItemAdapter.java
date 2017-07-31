package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.socialize.utils.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.UserPointB;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by johe on 2017/4/11.
 */

public class UserIntegarlItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<UserPointB> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    public UserPointB getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public UserIntegarlItemAdapter(Context mContext, List<UserPointB> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<UserPointB> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<UserPointB> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.user_integarllist_item;
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
        final UserPointB data = datas.get(p);
        Log.e(Constan.LOGTAGNAME,"---getPoint---"+data.getPoint());
        mHolder.userIntegralDateTv.setText(data.getDate());
        mHolder.userIntegralDescTv.setText(data.getDescr());
        mHolder.userIntegralPointTv.setText(data.getPoint()+"");
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
        @BindView(R.id.userIntegral_date_tv)
        TextView userIntegralDateTv;
        @BindView(R.id.userIntegral_desc_tv)
        TextView userIntegralDescTv;
        @BindView(R.id.userIntegral_point_tv)
        TextView userIntegralPointTv;

        ViewHolder(View view, MyItemClickListener mItemClickListener) {
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

