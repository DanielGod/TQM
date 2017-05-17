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
import tqm.bianfeng.com.tqm.pojo.ReleaseLoanItem;
import tqm.bianfeng.com.tqm.pojo.ResultCode;

/**
 * Created by johe on 2017/5/15.
 */

public class MyReleaseLoanAdapter extends RecyclerView.Adapter<MyReleaseLoanAdapter.ViewHolder> {

    List<ReleaseLoanItem> datas;
    ReleaseLoanItemClickListener mItemClickListener;

    List<ReleaseLoanItem> deleteDatas;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    ReleaseLoanItem data;

    CompositeSubscription compositeSubscription;

    public int getLayout() {
        return R.layout.my_release_loan_item;
    }

    public List<ReleaseLoanItem> getDatas() {
        return datas;
    }

    public MyReleaseLoanAdapter(Context mContext, List<ReleaseLoanItem> datas) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
        deleteDatas=new ArrayList<>();
        compositeSubscription=new CompositeSubscription();
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(ReleaseLoanItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface ReleaseLoanItemClickListener {
        public void onItemClick(View view, int postion);
        public void EdiRelease(int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayout(), parent, false);

        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ReleaseLoanItem data = datas.get(position);
        holder.annualReturnTv.setText(data.getRateMin() + "%" + "-" + data.getRateMax() + "%");
        holder.institutionNameTv.setText(data.getInstitution());
        holder.titleTv.setText(data.getLoanName());
        holder.purchaseMoneyTv.setText(data.getLoanMoneyMin() + "-" + data.getLoanMoneyMax() + "万");
        holder.investmentTermTv.setText(data.getLoanPeriodMin() + "-" + data.getLoanPeriodMax() + "个月");
        holder.financViewsTv.setText(data.getLoanViews() + "");

        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(null, position);
            }
        });
        if (data.getStatusCode().equals("00")) {
            //审核
            holder.releaseTypeImg.setImageResource(R.drawable.ic_release00);
        } else if (data.getStatusCode().equals("01")) {
            //通过
            holder.releaseTypeImg.setImageResource(R.drawable.ic_release01);
        } else if (data.getStatusCode().equals("02")) {
            //未通过
            holder.releaseTypeImg.setImageResource(R.drawable.ic_release02);
        }
        holder.releaseEdiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.EdiRelease(position);
            }
        });
        holder.releaseDelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isHave=false;
                for(ReleaseLoanItem releaseLoanItem:deleteDatas){
                    if(releaseLoanItem.getLoanId()==datas.get(position).getLoanId()){
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
    public void delect(final int position){
        Subscription getBankFinancItem_subscription = NetWork.getUserService().deleteactivityById(datas.get(position).getLoanId())
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
                                if(deleteDatas.get(i).getLoanId()==datas.get(position).getLoanId()){
                                    deleteDatas.remove(i);
                                }
                            }
                            datas.remove(position);
                            MyReleaseLoanAdapter.this.notifyDataSetChanged();
                        }
                    }
                });
        compositeSubscription.add(getBankFinancItem_subscription);

    }
    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public void setdatas(List<ReleaseLoanItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View rootView;
        ReleaseLoanItemClickListener mListener;
        @BindView(R.id.annualReturn_tv)
        TextView annualReturnTv;
        @BindView(R.id.rateName_tv)
        TextView rateNameTv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.release_type_img)
        ImageView releaseTypeImg;
        @BindView(R.id.purchaseMoney_tv)
        TextView purchaseMoneyTv;
        @BindView(R.id.purchaseMoney_linear)
        LinearLayout purchaseMoneyLinear;
        @BindView(R.id.investmentTerm_tv)
        TextView investmentTermTv;
        @BindView(R.id.investmentTerm_linear)
        LinearLayout investmentTermLinear;
        @BindView(R.id.purchaseMoneyAndRiskGradeName_linear)
        LinearLayout purchaseMoneyAndRiskGradeNameLinear;
        @BindView(R.id.release_edi_img)
        TextView releaseEdiImg;
        @BindView(R.id.release_delect_img)
        TextView releaseDelectImg;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.institutionName_tv)
        TextView institutionNameTv;
        @BindView(R.id.institutionName_linear)
        LinearLayout institutionNameLinear;
        @BindView(R.id.financViews_tv)
        TextView financViewsTv;
        @BindView(R.id.financViews_linear)
        LinearLayout financViewsLinear;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;


        ViewHolder(View view, ReleaseLoanItemClickListener listener) {
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
