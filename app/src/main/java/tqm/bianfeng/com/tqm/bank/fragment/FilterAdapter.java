package tqm.bianfeng.com.tqm.bank.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.bank.QueryCondition;

/**
 * Created by wjy on 2016/11/7.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    List<QueryCondition> datas;
    QueryConditionItemClickListener mItemClickListener;
    List<String> list_str;
    Map<String,List<String>> mMap;
    private Map<String,Object> mapFilterInfo;
    private static int mPosition;



    public static Context mContext;
    private LayoutInflater mLayoutInflater;
    QueryCondition data;


    public int getLayout() {
        return R.layout.filter_item;
    }

    public FilterAdapter(List<QueryCondition> datas,  Map<String,List<String>> mMap,Context mContext) {
        this.mContext = mContext;
        this.datas = datas;
        this.mMap = mMap;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(QueryConditionItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface QueryConditionItemClickListener {
        public void onItemClick(View view, int postion);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new ViewHolder(view, mItemClickListener);
    }
    @DebugLog
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        data = datas.get(position);
        holder.titleTv.setText(data.getQueryName());
        if (!"select".equals(data.getQueryType())) {
            holder.classArrow.setVisibility(View.GONE);
        }
        Log.i("Daniel","---holder.classArrow.isChecked()----"+holder.classArrow.isChecked());
        holder.gridViewAdapter.setmList(mMap.get(data.getQueryName()));
        holder.gridViewAdapter.notifyDataSetChanged();
        holder.classArrow.setChecked(true);


        holder.classArrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    holder.recyclerView.setVisibility(View.VISIBLE);
                }else {
                    holder.recyclerView.setVisibility(View.GONE);
                }
            }
        });




//        EventBus.getDefault().post(new FilterEvens(position));


    }

    @Override
    public int getItemCount() {
        //        Log.i("Daniel","---datas.size()---"+datas.size());
        return datas != null ? datas.size() : 0;
    }
    @DebugLog
    public void setdatas(List<String> list_str) {
        this.list_str = list_str;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        QueryConditionItemClickListener mListener;
        GridViewAdapter gridViewAdapter;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.class_arrow)
        CheckBox classArrow;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;


        ViewHolder(View view, QueryConditionItemClickListener listener) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
            this.mListener = listener;
            view.setOnClickListener(this);
//            gridViewAdapter = new GridViewAdapter(mContext,list_str);
//            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
//            recyclerView.setAdapter(gridViewAdapter);


        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mPosition = getPosition();
                mListener.onItemClick(view, getPosition());
            }

        }
    }
}
