package tqm.bianfeng.com.tqm.main;

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
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.BankInformItem;

/**
 * Created by johe on 2017/3/14.
 */

public class HomeBankInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context mContext;
    private List<BankInformItem> datas;
    private final LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener;

    private CompositeSubscription mcompositeSubscription;

    public BankInformItem getDataItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    public HomeBankInfoListAdapter(Context mContext, List<BankInformItem> mDatas) {
        this.mContext = mContext;
        this.datas = mDatas;
        mLayoutInflater = LayoutInflater.from(mContext);
        mcompositeSubscription = new CompositeSubscription();
    }

    public void update(List<BankInformItem> mDatas) {
        this.datas = mDatas;
        this.notifyDataSetChanged();
    }

    public int getLayout() {
        return R.layout.bank_information_item;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int p) {
        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.bankInfoItemLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnClickListener(p);
                }
            }
        });
        mHolder.bankInfoTimeTxt.setText(datas.get(p).getReleaseDate());
        mHolder.bankInfoTitleTxt.setText(datas.get(p).getInformTitle());
        Picasso.with(mContext).load(NetWork.LOAD + datas.get(p).getImageUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(mHolder.bankInfoImg);

        mHolder.readNum.setText("" + datas.get(p).getInformViews());
        mHolder.companyNameTxt.setText(datas.get(p).getInstitutionName());
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface MyItemClickListener {
        public void OnClickListener(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bank_info_img)
        ImageView bankInfoImg;
        @BindView(R.id.bank_info_title_txt)
        TextView bankInfoTitleTxt;
        @BindView(R.id.bank_info_time_txt)
        TextView bankInfoTimeTxt;
        @BindView(R.id.read_num)
        TextView readNum;
        @BindView(R.id.bank_info_item_lin)
        LinearLayout bankInfoItemLin;
        @BindView(R.id.company_name_txt)
        TextView companyNameTxt;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

