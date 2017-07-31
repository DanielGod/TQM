package tqm.bianfeng.com.tqm.main.vehicle.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
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
import tqm.bianfeng.com.tqm.main.vehicle.bean.Car;
import tqm.bianfeng.com.tqm.main.vehicle.bean.UsedCar;

/**
 * Created by johe on 2017/4/11.
 */

public class VehicleChioceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static Context mContext;
    private List<UsedCar> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;
    List<Car> carList;

    public UsedCar getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public VehicleChioceAdapter(Context mContext, List<UsedCar> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public void update(List<UsedCar> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<UsedCar> mDatas) {
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
        Log.e("Daniel","---onCreateViewHolder--");
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
        Log.e("Daniel","positon："+p);
        UsedCar data = datas.get(p);
        carList =  datas.get(p).getCarList();
        mHolder.vehicleChioceTopTv.setText(data.getPinName());
        Log.e("Daniel","首字母："+data.getPinName());
        if (mHolder.vehicleChioceItemAdapter==null) {
            Log.e("Daniel","vehicleChioceItemAdapter==null");
            mHolder.vehicleChiocePpRecycle.setLayoutManager(new GridLayoutManager(mContext,3));
            mHolder.vehicleChioceItemAdapter = new VehicleChioceItemAdapter(mContext,carList);
            mHolder.vehicleChiocePpRecycle.setAdapter(mHolder.vehicleChioceItemAdapter);
        }else {
            Log.e("Daniel","vehicleChioceItemAdapter!=null");
            mHolder.vehicleChioceItemAdapter.notifyData(carList);
//            notifyDataSetChanged();
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
        VehicleChioceItemAdapter vehicleChioceItemAdapter;
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

