package tqm.bianfeng.com.tqm.Institutions.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.Call;
import tqm.bianfeng.com.tqm.pojo.CreditManager;
import tqm.bianfeng.com.tqm.pojo.LawFirmOrInstitutionDetail;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by johe on 2017/4/11.
 */

public class CreditManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<CreditManager> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;
    private LawFirmOrInstitutionDetail lawFirmOrInstitutionDetail;

    public CreditManager getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public CreditManagerAdapter(Context mContext, List<CreditManager> mDatas, LawFirmOrInstitutionDetail lawFirmOrInstitutionDetail) {
        this.mContext = mContext;
        this.datas = mDatas;
        this.lawFirmOrInstitutionDetail = lawFirmOrInstitutionDetail;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void update(List<CreditManager> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<CreditManager> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.creditmanager_dialog_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(v, mItemClickListener);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        if (p == 0) {
            mHolder.creditManagerAddressTv.setVisibility(View.GONE);
            mHolder.creditManagerTv.setText("官方电话");
            mHolder.phoneNumTv.setText(lawFirmOrInstitutionDetail.getContact());
            mHolder.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new Call(lawFirmOrInstitutionDetail.getContact()));
                }
            });
        } else {
            final CreditManager data = datas.get(p - 1);
            Log.e(Constan.LOGTAGNAME, "认证经理信息：" + data.toString());
            mHolder.creditManagerAddressTv.setVisibility(View.VISIBLE);
            mHolder.creditManagerTv.setText(data.getLxr());
            mHolder.phoneNumTv.setText(data.getLxdh());
            mHolder.creditManagerAddressTv.setText(data.getCity() + data.getCounty() + "信贷经理:");
            mHolder.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new Call(data.getLxdh()));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size() + 1;
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        @BindView(R.id.creditManager_tv)
        TextView creditManagerTv;
        @BindView(R.id.phone_num_tv)
        TextView phoneNumTv;
        @BindView(R.id.call_btn)
        Button callBtn;
        @BindView(R.id.creditManagerAddress_tv)
        TextView creditManagerAddressTv;

        ViewHolder(View view, MyItemClickListener mItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.mItemClickListener = mItemClickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.OnClickListener(getPosition());
            }

        }
    }
}

