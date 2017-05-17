package tqm.bianfeng.com.tqm.User.release;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ReleaseActivityItem;

/**
 * Created by johe on 2017/5/16.
 */

public class ReleaseActivityFragment extends BaseFragment {

    //firstpage_listitem


    ReleaseActivityItem releaseActivityItem;
    @BindView(R.id.logo_img)
    ImageView logoImg;
    @BindView(R.id.activityTitle_tv)
    TextView activityTitleTv;
    @BindView(R.id.institutionName_tv)
    TextView institutionNameTv;
    @BindView(R.id.activityViews_tv)
    TextView activityViewsTv;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;


    public void setReleaseActivityItem(ReleaseActivityItem releaseActivityItem) {
        this.releaseActivityItem = releaseActivityItem;
        Log.i("gqf", "setReleaseActivityItem"+releaseActivityItem.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bank_activity_item, container, false);
        ButterKnife.bind(this, view);
        Log.i("gqf", "oncreate");
        if (releaseActivityItem != null) {
            initView();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void initView() {
        Log.i("gqf", "initView");
        Picasso.with(getActivity()).load(NetWork.LOAD + releaseActivityItem.getImageUrl()).placeholder(R.drawable.banklogo).into(logoImg);
        activityTitleTv.setText(releaseActivityItem.getActivityTitle());
        Log.i("gqf", "initView");
        institutionNameTv.setText(releaseActivityItem.getInstitution());
        activityViewsTv.setText(releaseActivityItem.getActivityViews() + "");
        linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(releaseActivityItem.getStatusCode().equals("01")) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailType", DetailActivity.ACTIVITY_TYPE);
                    intent.putExtra("detailId", releaseActivityItem.getActivityId());
                    intent.putExtra("detailTitle", releaseActivityItem.getActivityTitle());
                    startActivity(intent);
                }else{
//                    LoanOrActivityReleaseActivity.RELEASE_TYPE=LoanOrActivityReleaseActivity.RELEASE_ACTIVITY_TYPE;
//                    Intent intent=new Intent(getActivity(),LoanOrActivityReleaseActivity.class);
//                    intent.putExtra(LoanOrActivityReleaseActivity.RELEASE_ID,releaseActivityItem.getActivityId());
//                    startActivity(intent);
                    Intent intent=new Intent(getActivity(),ReleaseMyActivityActivity.class);
                    intent.putExtra(ReleaseMyActivityActivity.ACTIVITY_ID,releaseActivityItem.getActivityId());
                    startActivity(intent);
                }
            }
        });
    }

}
