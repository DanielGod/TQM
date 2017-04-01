package tqm.bianfeng.com.tqm.lawhelp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;

public class HotCityGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> datas;
    Context mContext;



    private LayoutInflater layoutInflater;

    MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }


    public HotCityGridAdapter(Context context, List<String> datas) {
        this.mContext = context;
        this.datas = datas;
        this.layoutInflater = LayoutInflater.from(context);

    }

    public void updateListView(List<String> list) {
        this.datas = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_city_grid_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int arg0) {
        final ViewHolder mHolder = (ViewHolder) holder;

        mHolder.hotCityBtn.setText(datas.get(arg0));
        mHolder.hotCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myItemClickListener.OnClickListener(arg0);
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


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.hot_city_btn)
        Button hotCityBtn;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
