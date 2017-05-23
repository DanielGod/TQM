package tqm.bianfeng.com.tqm.bank.fragment;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.buttonViewEven;

import static tqm.bianfeng.com.tqm.bank.fragment.TestFilterFragment.filter_item;

/**
 * Created by wjy on 2016/11/7.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {

    List<String> datas;
    GridViewAdapterItemClickListener mItemClickListener;


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    String data;

    public GridViewAdapter() {

    }


    public int getLayout() {
        return R.layout.item_gridview_layout;
    }

    public void setmList(List<String> mList) {
        this.datas = mList;
    }

    public GridViewAdapter(Context mContext,List<String> datas) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(GridViewAdapterItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface GridViewAdapterItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        data = datas.get(position);
        holder.text.setText(data);

        if (filter_item){
            holder.text.setChecked(false);
               Log.e("Daniel", "---position----" + position);
               Log.e("Daniel", "---datas.size()----" + datas.size());
            if (position==datas.size()-1){
//                filter_item=false;
                EventBus.getDefault().post(new ClearFilter(true));//清除筛选集合
            }
        }


        holder.text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @DebugLog
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str_buttonView = buttonView.getText().toString();

                Log.i("Daniel","----str_buttonView-----"+str_buttonView);
                if (!isChecked){
                    EventBus.getDefault().post(new buttonViewEven(buttonView.getText().toString(),false));
                    holder.text.setTextColor(mContext.getResources().getColor(R.color.black));
                }else {
                    EventBus.getDefault().post(new buttonViewEven(buttonView.getText().toString(),true));
                    holder.text.setTextColor(mContext.getResources().getColor(R.color.white));

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        Log.e("Daniel", "---datas.size()----" + datas.size());
        return datas != null ? datas.size() : 0;
    }

    public void refreshAdapter() {
        notifyDataSetChanged();
    }

    public void setdatas(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        GridViewAdapterItemClickListener mListener;
        @BindView(R.id.text)
        CheckBox text;


        ViewHolder(View view, GridViewAdapterItemClickListener listener) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
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
