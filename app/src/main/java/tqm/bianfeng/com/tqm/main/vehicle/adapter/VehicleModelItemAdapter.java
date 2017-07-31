package tqm.bianfeng.com.tqm.main.vehicle.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.main.vehicle.bean.ChexingListBean;
import tqm.bianfeng.com.tqm.main.vehicle.fragment.VehicleEvaluationFragment;

/**
 * Created by johe on 2017/4/11.
 */

public class VehicleModelItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ChexingListBean> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    public ChexingListBean getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public VehicleModelItemAdapter(Context mContext, List<ChexingListBean> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<ChexingListBean> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<ChexingListBean> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.carseries_item;
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
        final ChexingListBean data = datas.get(p);
        mHolder.CarSeriesTv.setText(data.getCxname());
        Log.e("Daniel", "车型名：" + data.getCxname());
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleEvaluationFragment.chexingListBean = data;
                //显示 VehicleEvaluationFragment
                EventBus.getDefault().post("ToVehicleEvaluationFragment");
                //VehicleEvaluationFragment 接收车型数据


            }
        });


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
        @BindView(R.id.CarSeries_tv)
        TextView CarSeriesTv;

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

