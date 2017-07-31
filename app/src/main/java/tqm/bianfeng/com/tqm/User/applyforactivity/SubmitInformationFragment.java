package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.bank.quickloan.BasicInformationActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwDksq;

/**
 * Created by johe on 2017/4/10.
 */

public class SubmitInformationFragment extends BaseFragment {

    @BindView(R.id.submit_two_top_tv)
    TextView submitTwoTopTv;
    @BindView(R.id.submit_two_down_tv)
    TextView submitTwoDownTv;
    @BindView(R.id.submit_two_lin)
    LinearLayout submitTwoLin;
    @BindView(R.id.submit_three_lin)
    LinearLayout submitThreeLin;
    @BindView(R.id.commit)
    Button commit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_information2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取单条贷款信息
        getLoanOne(realm.where(User.class).findFirst().getUserId());
    }

    private void getLoanOne(int userId) {
        Subscription subscription = NetWork.getBankService().getOne(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<YwDksq>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Dani", "快速贷款信息onError：" + e.toString());
            }

            @Override
            public void onNext(YwDksq ywDksq) {
                if (ywDksq != null) {
                    setView(ywDksq);
                }
            }
        });
        compositeSubscription.add(subscription);
    }

    private void setView(YwDksq ywDksq) {
        Log.e("Daniel", "SubmitInformationActivity贷款审核状态：" + ywDksq.getSqzt());
        if (!StringUtils.isEmpty(ywDksq.getSqzt())) {
            if ("01".equals(ywDksq.getSqzt()) || "03".equals(ywDksq.getSqzt())) {
                commit.setVisibility(View.VISIBLE);
                if ("01".equals(ywDksq.getSqzt())) {
                    submitTwoLin.setVisibility(View.GONE);
                    submitThreeLin.setVisibility(View.GONE);
                } else if ("03".equals(ywDksq.getSqzt())) {
                    submitTwoLin.setVisibility(View.VISIBLE);
                    submitTwoTopTv.setText("信息未通过审核！");
                    submitTwoDownTv.setText(ywDksq.getShbz());
                }
            } else {
                submitThreeLin.setVisibility(View.VISIBLE);
                if ("02".equals(ywDksq.getSqzt())) {
                    submitTwoLin.setVisibility(View.VISIBLE);
                    submitTwoTopTv.setText("信息审核成功！");
                    submitTwoDownTv.setText("24小时内铜钱猫将推荐优秀业务员贷款人员联系你");
                }
                commit.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), BasicInformationActivity.class));
    }

}
