package tqm.bianfeng.com.tqm.Institutions.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;

import static tqm.bianfeng.com.tqm.Institutions.CompanyInfoActivity.index;

/**
 * Created by johe on 2017/4/11.
 */

public class LawFirmOrInstitutionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<InstitutionItem> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;
    LawFirmOrInstitutionListAdapter lawFirmOrInstitutionListAdapter;

    List<InstitutionItem> inCollectItem;

    CompositeSubscription compositeSubscription;
    Realm realm;

    Gson gson;
    public InstitutionItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public LawFirmOrInstitutionListAdapter(Context mContext, List<InstitutionItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        realm = Realm.getDefaultInstance();
        compositeSubscription=new CompositeSubscription();
        inCollectItem=new ArrayList<>();
        gson=new Gson();
    }

    public void update(List<InstitutionItem> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }
    public void notifyData(List<InstitutionItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }
    public int getLayout() {
        return R.layout.institutions_in_list_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        //View v = mLayoutInflater.inflate(R.layout.my_order_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    public void Collect(final int position,final int id){
        String userId="";
        if(realm.where(User.class).findFirst()!=null){
            userId=realm.where(User.class).findFirst().getUserId()+"";
        }else{
            Toast.makeText(mContext,"请登录后再收藏",Toast.LENGTH_SHORT).show();
            return;
        }
        String isCollect="";
        if(datas.get(position).getIsCollect().equals("01")){
            isCollect="02";
        }else {
            isCollect="01";
        }
        lawFirmOrInstitutionListAdapter=this;
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().saveOrUpdate(id,
                "0"+(index+1),userId,isCollect)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","Throwable"+e.toString());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.i("gqf",position+"resultCode"+resultCode.toString());
                        int p=position;
                        if(resultCode.getCode()==ResultCode.SECCESS){
                            Log.i("gqf","resultCode"+datas.get(p).toString());
                            for(int i=0;i<inCollectItem.size();i++){
                                if(inCollectItem.get(i).getInstitutionId()== datas.get(position).getInstitutionId()){
                                    inCollectItem.remove(i);
                                    if(datas.get(p).getIsCollect().equals("01")){
                                        datas.get(p).setIsCollect("02");
                                        //changeView.get(i).setText("收藏");
                                    }else {
                                        datas.get(p).setIsCollect("01");
                                        //changeView.get(i).setText("已收藏");
                                    }
                                    Log.i("gqf","resultCode"+datas.get(p).toString());
                                    mItemClickListener.changePosition(position);
                                    break;
                                }
                            }

                        }
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.ininLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.OnClickListener(p);
            }
        });
        mHolder.ininNameTxt.setText(datas.get(p).getInstitutionName());
        mHolder.contactTxt.setText("电话：" + datas.get(p).getContact());
        //mHolder.profileTxt.setText("简介：" + datas.get(p).getProfile());
        mHolder.profileTxt.setVisibility(View.GONE);

        if(datas.get(p).getIsCollect()!=null) {
            if (datas.get(p).getIsCollect().equals("02")) {
                //未收藏
                mHolder.isCollectTxt.setText("收藏");
            } else {
                //已收藏
                mHolder.isCollectTxt.setText("已收藏");
            }
        }
        for(int i=0;i<inCollectItem.size();i++){
            if(inCollectItem.get(i).getInstitutionId()==datas.get(p).getInstitutionId()){
                mHolder.isCollectTxt.setText("收藏中");
            }
        }
        mHolder.isCollectTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isHave=false;
                for(int i=0;i<inCollectItem.size();i++){
                    if(inCollectItem.get(i).getInstitutionId()==datas.get(p).getInstitutionId()){
                        isHave=true;
                    }
                }
                if(!isHave){
                    if(realm.where(User.class).findFirst()!=null){
                        inCollectItem.add(datas.get(p));
                        Collect(p,datas.get(p).getInstitutionId());
                        mHolder.isCollectTxt.setText("收藏中");
                    }else{
                        Toast.makeText(mContext,"请登录后再收藏",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "正在收藏请稍后", Toast.LENGTH_SHORT).show();
                }


            }
        });
        Picasso.with(mContext).load(NetWork.LOAD + datas.get(p).getInstitutionIcon()).placeholder(R.drawable.ic_img_loading).error(R.drawable.ic_img_loading).into(mHolder.ininImg);

        mHolder.callLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realm.where(User.class).findFirst() == null) {
                    Toast.makeText(mContext, "请先登录后查看", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + datas.get(p).getContact()));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentPhone);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);
        public void changePosition(int position);

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.inin_img)
        ImageView ininImg;
        @BindView(R.id.ininName_txt)
        TextView ininNameTxt;
        @BindView(R.id.contact_txt)
        TextView contactTxt;
        @BindView(R.id.profile_txt)
        TextView profileTxt;
        @BindView(R.id.call_lin)
        LinearLayout callLin;
        @BindView(R.id.is_collect_txt)
        TextView isCollectTxt;
        @BindView(R.id.collection_lin)
        LinearLayout collectionLin;
        @BindView(R.id.inin_lin)
        LinearLayout ininLin;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

