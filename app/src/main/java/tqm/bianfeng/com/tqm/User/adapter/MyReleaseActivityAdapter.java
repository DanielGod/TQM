package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ReleaseActivityItem;

/**
 * Created by johe on 2017/5/15.
 */

public class MyReleaseActivityAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    String inName = "";
    private Context mContext;
    private List<ReleaseActivityItem> datas;
    private final LayoutInflater mLayoutInflater;
    private  MyItemClickListener mItemClickListener;

    public void setInName(String name) {
        inName = name;
    }
    public ReleaseActivityItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public MyReleaseActivityAdapter(Context mContext, List<ReleaseActivityItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<ReleaseActivityItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.my_release_activity_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        //View v = mLayoutInflater.inflate(R.layout.my_order_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new  ViewHolder(v);

        return viewHolder;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener( MyItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        ReleaseActivityItem data = getDataItem(position);
        mHolder.logoImg.setVisibility(View.VISIBLE);
        mHolder.annualReturnTv.setVisibility(View.GONE);
        mHolder.rateNameTv.setVisibility(View.GONE);
        mHolder.loanMoneyLinear.setVisibility(View.GONE);
        mHolder.riskGradeNameLinear.setVisibility(View.GONE);
        mHolder.investmentTermLinear.setVisibility(View.GONE);
        Picasso.with(mContext).load(NetWork.LOAD + data.getImageUrl()).placeholder(R.drawable.ic_img_loading).into(mHolder.logoImg);
        mHolder.titleTv.setText(data.getActivityTitle());
        mHolder.activityViewsTv.setText(data.getActivityViews() + "");
        mHolder.institutionNameTv.setText(data.getInstitutionName());
        if (!inName.equals("")) {
            mHolder.institutionName2Tv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.institutionName2_tv)
        TextView institutionName2Tv;
        @BindView(R.id.institutionName_linear)
        LinearLayout institutionNameLinear;
        @BindView(R.id.activityViews_tv)
        TextView activityViewsTv;
        @BindView(R.id.activityViews_linear)
        LinearLayout activityViewsLinear;
        @BindView(R.id.riskGradeName_tv)
        TextView riskGradeNameTv;
        @BindView(R.id.riskGradeName_linear)
        LinearLayout riskGradeNameLinear;
        @BindView(R.id.investmentTerm_tv)
        TextView investmentTermTv;
        @BindView(R.id.loanMoney_tv)
        TextView loanMoneyTv;
        @BindView(R.id.loanMoney_linear)
        LinearLayout loanMoneyLinear;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;
        @BindView(R.id.logo_img)
        ImageView logoImg;
        @BindView(R.id.investmentTerm_linear)
        LinearLayout investmentTermLinear;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}