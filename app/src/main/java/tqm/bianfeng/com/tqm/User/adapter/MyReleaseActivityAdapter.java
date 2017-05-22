package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ReleaseActivityItem;
import tqm.bianfeng.com.tqm.pojo.ResultCode;

/**
 * Created by johe on 2017/5/15.
 */

public class MyReleaseActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    String inName = "";



    private Context mContext;
    private List<ReleaseActivityItem> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;
    CompositeSubscription compositeSubscription;

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
        compositeSubscription=new CompositeSubscription();
        deleteDatas=new ArrayList<>();
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
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        ReleaseActivityItem data = getDataItem(position);
        Picasso.with(mContext).load(NetWork.LOAD + data.getImageUrl()).placeholder(R.drawable.placeholder).into(mHolder.logoImg);
        mHolder.activityTitleTv.setText(data.getActivityTitle());

        Log.i("gqf","imgUrl"+NetWork.LOAD + data.getImageUrl());
        mHolder.institutionNameTv.setText(data.getInstitution());
        mHolder.activityViewsTv.setText(data.getActivityViews() + "");
        mHolder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.OnClickListener(position);
            }
        });

        if (data.getStatusCode().equals("00")) {
            //审核
            mHolder.releaseTypeImg.setImageResource(R.drawable.ic_release00);
        } else if (data.getStatusCode().equals("01")) {
            //通过
            mHolder.releaseTypeImg.setImageResource(R.drawable.ic_release01);
        } else if (data.getStatusCode().equals("02")) {
            //未通过
            mHolder.releaseTypeImg.setImageResource(R.drawable.ic_release02);
        }
        mHolder.releaseEdiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.EdiRelease(position);
            }
        });
        mHolder.releaseDelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isHave=false;
                for(ReleaseActivityItem releaseActivityItem:deleteDatas){
                    if(releaseActivityItem.getActivityId()==datas.get(position).getActivityId()){
                        isHave=true;
                    }
                }
                Log.i("gqf","isHave"+isHave);
                if(!isHave) {
                    deleteDatas.add(datas.get(position));
                    delect(position);
                }
            }
        });

    }
    List<ReleaseActivityItem> deleteDatas;
    public void delect(final int position){
        Subscription getBankFinancItem_subscription = NetWork.getUserService().deleteactivityById(datas.get(position).getActivityId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.toString());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.i("gqf","onNext"+resultCode.toString());
                        if(resultCode.getCode()==ResultCode.SECCESS){
                            for(int i=0;i<deleteDatas.size();i++){
                                if(deleteDatas.get(i).getActivityId()==datas.get(position).getActivityId()){
                                    deleteDatas.remove(i);
                                }
                            }
                            datas.remove(position);
                            MyReleaseActivityAdapter.this.notifyDataSetChanged();
                        }
                    }
                });
        compositeSubscription.add(getBankFinancItem_subscription);

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);
        public void EdiRelease(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.logo_img)
        ImageView logoImg;
        @BindView(R.id.activityTitle_tv)
        TextView activityTitleTv;
        @BindView(R.id.release_type_img)
        ImageView releaseTypeImg;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.release_edi_img)
        TextView releaseEdiImg;
        @BindView(R.id.release_delect_img)
        TextView releaseDelectImg;
        @BindView(R.id.activityViews_tv)
        TextView activityViewsTv;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}