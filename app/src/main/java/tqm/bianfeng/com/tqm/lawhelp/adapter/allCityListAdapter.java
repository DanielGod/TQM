package tqm.bianfeng.com.tqm.lawhelp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.cityInfo;

public class allCityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<cityInfo> datas;
    Context mContext;



    private LayoutInflater layoutInflater;
    private Boolean isSearch = false;
    MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public Boolean getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(Boolean isSearch) {
        this.isSearch = isSearch;
    }

    public allCityListAdapter(Context context, List<cityInfo> datas) {
        this.mContext = context;
        this.datas = datas;
        this.layoutInflater = LayoutInflater.from(context);

    }

    public void updateListView(List<cityInfo> list) {
        this.datas = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.allcity_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int arg0) {
        final ViewHolder mHolder = (ViewHolder) holder;
        int section = getSectionForPosition(arg0);
        if (isSearch == false) {
            if (arg0 == getPositionForSection(section)) {
                mHolder.sortKey.setVisibility(View.VISIBLE);
                mHolder.sortKey.setText(datas.get(arg0).getSortKey());
            } else {
                mHolder.sortKey.setVisibility(View.GONE);
            }
        } else {
            mHolder.sortKey.setVisibility(View.GONE);
        }

        mHolder.city.setText(datas.get(arg0).getCity());
        mHolder.cortLin.setOnClickListener(new View.OnClickListener() {
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

    public int getSectionForPosition(int position) {
        return datas.get(position).getSortKey().charAt(0);
    }

    @SuppressLint("DefaultLocale")
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = datas.get(i).getSortKey();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    public Object[] getSections() {
        return null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sortKey)
        TextView sortKey;
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.cort_lin)
        LinearLayout cortLin;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
