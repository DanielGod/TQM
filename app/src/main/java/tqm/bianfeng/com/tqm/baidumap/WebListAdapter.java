package tqm.bianfeng.com.tqm.baidumap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.BankDotItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.WebListViewType;

import static tqm.bianfeng.com.tqm.network.NetWork.LOAD;

/**
 * Created by wjy on 2016/11/7.
 */

public class WebListAdapter extends RecyclerView.Adapter<WebListAdapter.ViewHolder> {

    List<BankDotItem> datas;
    MyItemClickListener mItemClickListener;
    TextView title;
    TextView distanceTv;
    TextView addressTv;
    LinearLayout businessLinear;
    Button previousBtn;
    Button nextBtn;



    private Context mContext;
    private LayoutInflater mLayoutInflater;
    BankDotItem data;

    static final int TYPE_BOTTOM = 0;
    static final int TYPE_CELL = 1;


    public int getLayout() {
        return R.layout.map_listitem;
    }

    public WebListAdapter(List<BankDotItem> datas, Context mContext) {
        this.mContext = mContext;
        this.datas = datas;
        for (int i = 0; i < datas.size(); i++) {
            data = datas.get(i);
            int distance = (int) WebListActivity.GetShortDistance(WebListActivity.lnt, WebListActivity.lat, data.getLng(), data.getLat());
            data.setDistance(distance);

        }
        Collections.sort(datas, new DistanceOrder());
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        //        View view = mLayoutInflater.inflate(getLayout(), parent, false);
        switch (viewType) {
            case TYPE_BOTTOM: {
                view = mLayoutInflater.inflate(R.layout.webdot_list_bottom, parent, false);
                previousBtn = (Button) view.findViewById(R.id.previous_btn);
                nextBtn = (Button) view.findViewById(R.id.next_btn);
                return  new ViewHolder(view, mItemClickListener);
            }
            case TYPE_CELL: {
                view = mLayoutInflater.inflate(getLayout(), parent, false);
                title = (TextView) view.findViewById(R.id.title);
                distanceTv = (TextView) view.findViewById(R.id.distance_tv);
                addressTv = (TextView) view.findViewById(R.id.address_tv);
                businessLinear = (LinearLayout) view.findViewById(R.id.business_linear);
                return  new ViewHolder(view, mItemClickListener);
            }
        }
        //        return new ViewHolder(view, mItemClickListener);
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_BOTTOM: //底部View
                Log.e("Daniel", "---count---"+getItemCount());
                Log.e("Daniel", "---count2---"+(Constan.PAGECAPACITY+1));
                if (getItemCount()< (Constan.PAGECAPACITY+1))
                    nextBtn.setEnabled(false);
                else
                    nextBtn.setEnabled(true);
                if (WebListActivity.oldIndex==0)
                    previousBtn.setEnabled(false);
                else
                    previousBtn.setEnabled(true);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Daniel", "---nextBtn---");
                        EventBus.getDefault().post(new WebListViewType(1));//1:下一页
                    }
                });
                previousBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Daniel", "---previousBtn---");
                        EventBus.getDefault().post(new WebListViewType(0));//0:上一页
                    }
                });
                break;
            case TYPE_CELL:
                data = datas.get(position);
                addressTv.setText(data.getAddress());
                title.setText(data.getName());
                Log.e("Daniel", "---distance---" + data.getDistance());
                if (data.getDistance() > 999) {
                    distanceTv.setText(data.getDistance() / 1000 + "km");
                } else {
                   distanceTv.setText(data.getDistance() + "m");
                }
                int iconSize = data.getBusinessIcons().size();
                Log.e("Daniel", "---iconSize---" + iconSize);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                param.rightMargin = 5;
                for (int i = 0; i < iconSize; i++) {

                    ImageView circleImageView = new ImageView(mContext);
                    circleImageView.setLayoutParams(param);
                    Picasso.with(mContext).load(LOAD + data.getBusinessIcons().get(i)).into(circleImageView);
                    businessLinear.addView(circleImageView);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        Log.i("Daniel","---datas.size()---"+datas.size());
        return datas != null ? datas.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size())
            return TYPE_BOTTOM;
        else
            return TYPE_CELL;
    }

    public void setdatas(List<BankDotItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        MyItemClickListener mListener;

        ViewHolder(View view, MyItemClickListener listener) {
            super(view);
            rootView = view;
            this.mListener = listener;
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition());
            }

        }
    }
}
