package tqm.bianfeng.com.tqm.User.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.pojo.Reports;

/**
 * Created by johe on 2017/4/11.
 */

public class UserCorrectErrorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static Context mContext;
    private List<Reports> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    public Reports getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public UserCorrectErrorAdapter(Context mContext, List<Reports> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public void update(List<Reports> mDatas) {
        this.datas = mDatas;
        //this.notifyDataSetChanged();
    }

    public void notifyData(List<Reports> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.user_correcterror;
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
        Reports data = datas.get(p);
        mHolder.userCorrectErrorTitleTv.setText(data.getObjectTitle());
        mHolder.userCorrectErrorContentTv.setText(data.getContent());
        if (!StringUtils.isEmpty(data.getRemark())) {
            mHolder.remarkLin.setVisibility(View.VISIBLE);
            mHolder.userCardRecodeRemarkTv.setText(data.getRemark());
        }else {
            mHolder.remarkLin.setVisibility(View.GONE);
        }

        if ("00".equals(data.getStatusCode())) {
            mHolder.userCorrectErrorStatusCodeTv.setText("提交");
        } else if ("01".equals(data.getStatusCode())) {
            mHolder.userCorrectErrorStatusCodeTv.setText("采纳");
        } else if ("02".equals(data.getStatusCode())) {
            mHolder.userCorrectErrorStatusCodeTv.setText("不予采纳");
        }
        if ("01".equals(data.getType())) {
            mHolder.userCorrectErrorTypeTv.setText("纠错");
        } else if ("02".equals(data.getType())) {
            mHolder.userCorrectErrorTypeTv.setText("举报");
        }
        if ("01".equals(data.getObjectModule())) {
            mHolder.userCorrectErrorModuleTv.setText("金融资讯");
        } else if ("02".equals(data.getObjectModule())) {
            mHolder.userCorrectErrorModuleTv.setText("银行理财");
        } else if ("03".equals(data.getObjectModule())) {
            mHolder.userCorrectErrorModuleTv.setText("金融活动");
        } else if ("05".equals(data.getObjectModule())) {
            mHolder.userCorrectErrorModuleTv.setText("法律援助");
        } else if ("06".equals(data.getObjectModule())) {
            mHolder.userCorrectErrorModuleTv.setText("机构中心");
        }

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        @BindView(R.id.userCorrectError_title_tv)
        TextView userCorrectErrorTitleTv;
        @BindView(R.id.userCorrectError_statusCode_tv)
        TextView userCorrectErrorStatusCodeTv;
        @BindView(R.id.userCorrectError_type_tv)
        TextView userCorrectErrorTypeTv;
        @BindView(R.id.userCardRecode_remark_tv)
        TextView userCardRecodeRemarkTv;
        @BindView(R.id.userCorrectError_content_tv)
        TextView userCorrectErrorContentTv;
        @BindView(R.id.remark_lin)
        LinearLayout remarkLin;
        @BindView(R.id.userCorrectError_module_tv)
        TextView userCorrectErrorModuleTv;

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

