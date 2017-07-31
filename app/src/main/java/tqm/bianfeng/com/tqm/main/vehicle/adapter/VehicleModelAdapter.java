package tqm.bianfeng.com.tqm.main.vehicle.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.main.vehicle.bean.ChexingListBean;
import tqm.bianfeng.com.tqm.main.vehicle.bean.DataBean;

/**
 * Created by johe on 2017/4/11.
 */

public class VehicleModelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static Context mContext;
    private List<DataBean> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;
    List<ChexingListBean> vehicleModelList;

    public DataBean getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public VehicleModelAdapter(Context mContext, List<DataBean> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public void update(List<DataBean> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<DataBean> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.vehiclechioce_list_item;
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
        DataBean data = datas.get(p);
        vehicleModelList =  datas.get(p).getChexing_list();
        Log.e("Daniel","年份："+data.getPyear());
        mHolder.vehicleChioceTopTv.setText(data.getPyear()+"");
        if ( mHolder.vehicleModelItemAdapter==null) {
            mHolder.vehicleChiocePpRecycle.setLayoutManager(new LinearLayoutManager(mContext));
            mHolder.vehicleModelItemAdapter = new VehicleModelItemAdapter(mContext,vehicleModelList);
            mHolder.vehicleChiocePpRecycle.setAdapter( mHolder.vehicleModelItemAdapter);
        }else {
            mHolder.vehicleModelItemAdapter.notifyData(vehicleModelList);
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
        @BindView(R.id.vehicleChioce_top_tv)
        TextView vehicleChioceTopTv;
        @BindView(R.id.vehicleChioce_pp_recycle)
        RecyclerView vehicleChiocePpRecycle;
        VehicleModelItemAdapter vehicleModelItemAdapter;
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

