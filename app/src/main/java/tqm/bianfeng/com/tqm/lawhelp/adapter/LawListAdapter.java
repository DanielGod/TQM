package tqm.bianfeng.com.tqm.lawhelp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import tqm.bianfeng.com.tqm.pojo.LawyerItem;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/3/14.
 */

public class LawListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    boolean isInMyFocuse = false;

    private Context mContext;
    private List<LawyerItem> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;
    List<LawyerItem> inCollectItem;
    CompositeSubscription compositeSubscription;
    Realm realm;

    public LawyerItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public LawListAdapter(Context mContext, List<LawyerItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        realm = Realm.getDefaultInstance();
        inCollectItem = new ArrayList<>();
        compositeSubscription = new CompositeSubscription();
    }

    public void update(List<LawyerItem> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public boolean isInMyFocuse() {
        return isInMyFocuse;
    }

    public void setInMyFocuse(boolean inMyFocuse) {
        isInMyFocuse = inMyFocuse;
    }

    public int getLayout() {
        return R.layout.law_list_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        //View v = mLayoutInflater.inflate(R.layout.my_order_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    public void Collect(final int position, final int id) {

        String isCollect = "";
        if (datas.get(position).getIsAttention().equals("01")) {
            isCollect = "02";
        } else {
            isCollect = "01";
        }
        Subscription getBankFinancItem_subscription = NetWork.getUserService()
                .attention(datas.get(position).getLawyerId(), "05", realm.where(User.class).findFirst().getUserId(), isCollect)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "Throwable" + e.toString());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.i("gqf", position + "resultCode" + resultCode.toString());
                        int p = position;
                        if (resultCode.getCode() == ResultCode.SECCESS) {
                            Log.i("gqf", datas.get(p).getIsAttention() + "resultCode" + datas.get(p).getIsAttention());
                            for (int i = 0; i < inCollectItem.size(); i++) {
                                if (inCollectItem.get(i).getLawyerId() == datas.get(position).getLawyerId()) {
                                    inCollectItem.remove(i);

                                    if (datas.get(p).getIsAttention().equals("01")) {
                                        datas.get(p).setIsAttention("02");
                                    } else {
                                        datas.get(p).setIsAttention("01");
                                    }
                                    if (isInMyFocuse) {
                                        datas.remove(p);
                                        LawListAdapter.this.notifyDataSetChanged();
                                    } else {
                                        mItemClickListener.changePosition(position);
                                    }
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
        mHolder.lawyerNameTxt.setText(datas.get(p).getLawyerName());
        mHolder.contactTxt.setText(datas.get(p).getInstitutionName());


        if (datas.get(p).getIsAuthorize() != null) {
            if (datas.get(p).getIsAuthorize().equals("01")) {
                mHolder.isAuthorizeTxt.setText("已认证");
            } else {
                mHolder.isAuthorizeTxt.setText("未认证");
            }
        }
        if(datas.get(p).getInstitutionName()!=null){
            mHolder.InNameTxt.setText(datas.get(p).getInstitutionName());
        }

        if (datas.get(p).getIsAttention() == null) {
            mHolder.collectionLin.setVisibility(View.INVISIBLE);
        } else {
            mHolder.collectionLin.setVisibility(View.VISIBLE);
            if (datas.get(p).getIsAttention().equals("02")) {
                //未关注
                mHolder.isCollectTxt.setText("关注");
            } else {
                //已关注
                mHolder.isCollectTxt.setText("已关注");
            }
        }
        for (int i = 0; i < inCollectItem.size(); i++) {
            if (inCollectItem.get(i).getLawyerId() == datas.get(p).getLawyerId()) {
                mHolder.isCollectTxt.setText("稍等..");
            }
        }

        if (datas.get(p).getAvatar() != null) {
            Picasso.with(mContext).load(NetWork.LOAD + datas.get(p).getAvatar())
                    .placeholder(R.drawable.ic_user_head_img)
                    .error(R.drawable.ic_user_head_img)
                    .into(mHolder.lawyerImg);
        }
        mHolder.layerInfoLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.OnClickListener(p);
            }
        });
        mHolder.callLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.CallClick(p);
            }
        });
        mHolder.collectionLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isHave = false;
                for (int i = 0; i < inCollectItem.size(); i++) {
                    if (inCollectItem.get(i).getLawyerId() == datas.get(p).getLawyerId()) {
                        isHave = true;
                    }
                }
                if (!isHave) {
                    if (realm.where(User.class).findFirst() != null) {
                        inCollectItem.add(datas.get(p));
                        Collect(p, datas.get(p).getLawyerId());
                        mHolder.isCollectTxt.setText("稍等..");
                    } else {
                        Toast.makeText(mContext, "请登录后再关注", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "正在关注请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //mHolder.collectionLin.setVisibility(View.GONE);
        String[] specialFields;
        if (datas.get(p).getSpecialField() != null) {
            if (!datas.get(p).getSpecialField().equals("")) {
                Log.i("gqf", "getSpecialField" + datas.get(p).getSpecialField());
                specialFields = datas.get(p).getSpecialField().split(",");
                for (int i = 0; i < ((specialFields.length > 3) ? 3 : specialFields.length); i++) {
                    if (i == 0) {
                        mHolder.goodAt1.setVisibility(View.VISIBLE);
                        mHolder.goodAt1.setText(specialFields[0]);
                    } else if (i == 1) {
                        mHolder.goodAt2.setVisibility(View.VISIBLE);
                        mHolder.goodAt2.setText(specialFields[1]);
                    } else {
                        mHolder.goodAt3.setVisibility(View.VISIBLE);
                        mHolder.goodAt3.setText(specialFields[2]);
                    }
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);

        public void CallClick(int position);

        public void CollectionClick(int position);

        public void changePosition(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lawyer_img)
        ImageView lawyerImg;
        @BindView(R.id.lawyerName_txt)
        TextView lawyerNameTxt;
        @BindView(R.id.InName_txt)
        TextView InNameTxt;
        @BindView(R.id.contact_txt)
        TextView contactTxt;
        @BindView(R.id.good_at1)
        TextView goodAt1;
        @BindView(R.id.good_at2)
        TextView goodAt2;
        @BindView(R.id.good_at3)
        TextView goodAt3;
        @BindView(R.id.isAuthorize_txt)
        TextView isAuthorizeTxt;
        @BindView(R.id.call_lin)
        LinearLayout callLin;
        @BindView(R.id.is_collect_txt)
        TextView isCollectTxt;
        @BindView(R.id.collection_lin)
        LinearLayout collectionLin;
        @BindView(R.id.layer_info_lin)
        LinearLayout layerInfoLin;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

