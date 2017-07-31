package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.order.Pay;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwRzsq;

import static tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForStatusFragment.ywApplyEnter;


/**
 * Created by 王九东 on 2017/7/1.
 */

public class ApplyForPayFragment extends BaseFragment {


    YwRzsq mYwRzsq;
    @BindView(R.id.creditManager_user_edi)
    TextView creditManagerUserEdi;
    @BindView(R.id.creditManager_institutions_edi)
    TextView creditManagerInstitutionsEdi;
    @BindView(R.id.creditManager_phone_edi)
    TextView creditManagerPhoneEdi;
    @BindView(R.id.creditManager_address_edi)
    TextView creditManagerAddressEdi;
    @BindView(R.id.apply_for_creditManager_msg_lin)
    LinearLayout applyForCreditManagerMsgLin;
    @BindView(R.id.apply_statu_scroll)
    ScrollView applyStatuScroll;

    Unbinder mBinder;
    @BindView(R.id.payMoney_left_lin)
    LinearLayout payMoneyLeftLin;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.payMoney_med_lin)
    LinearLayout payMoneyMedLin;
    @BindView(R.id.payMoney_right_lin)
    LinearLayout payMoneyRightLin;
    @BindView(R.id.totalPay_tv)
    TextView totalPayTv;
    @BindView(R.id.commitPay_btn)
    Button commitPayBtn;
    @BindView(R.id.payBottom_lin)
    LinearLayout payBottomLin;

    String mPayFlag = "00";
    @BindView(R.id.payWayChoice_cb)
    CheckBox payWayChoiceCb;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_for_pay, container, false);
        mBinder = ButterKnife.bind(this, view);
        mYwRzsq = ywApplyEnter;
        initView();

        payWayChoiceCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    commitPayBtn.setEnabled(true);
                else
                    commitPayBtn.setEnabled(false);

            }
        });

        return view;
    }

    private void initView() {
        creditManagerPhoneEdi.setText(ywApplyEnter.getLxdh());
        creditManagerInstitutionsEdi.setText(ywApplyEnter.getInstitutionName());
        creditManagerUserEdi.setText(ywApplyEnter.getLxr());
        creditManagerAddressEdi.setText(ywApplyEnter.getProvince() + ywApplyEnter.getCity() + ywApplyEnter.getCounty()); setBg(R.id.payMoney_left_lin);
        commitPayBtn.setEnabled(false);
        setBg(R.id.payMoney_left_lin);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinder.unbind();
    }

    @OnClick({R.id.payMoney_left_lin, R.id.payMoney_med_lin, R.id.payMoney_right_lin, R.id.commitPay_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.payMoney_left_lin:
                setBg(R.id.payMoney_left_lin);
                break;
            case R.id.payMoney_med_lin:
                setBg(R.id.payMoney_med_lin);
                break;
            case R.id.payMoney_right_lin:
                setBg(R.id.payMoney_right_lin);
                break;
            case R.id.commitPay_btn:
                dhOrderPay();
                break;
        }
    }

    private void dhOrderPay() {
        Subscription subscription = NetWork.getPayService().dhOrderPay(mPayFlag, realm.where(User.class).findFirst().getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Daniel", "平台展示支付错误：" + e.toString());

            }

            @Override
            public void onNext(Map<String, String> map) {

                Log.e("Daniel", "平台展示支付成功：" + map.get("orderInfo"));
                if (!StringUtils.isEmpty(map.get("orderInfo"))) {
                    new Pay(getActivity(), map.get("orderInfo")).payV2();
                }
            }
        });
        compositeSubscription.add(subscription);
    }

    private void setBg(int flag) {
        payBottomLin.setVisibility(View.VISIBLE);
        payMoneyLeftLin.setBackground(null);
        payMoneyMedLin.setBackground(null);
        payMoneyRightLin.setBackground(null);
        switch (flag) {
            case R.id.payMoney_left_lin:
                mPayFlag = "01";
                payMoneyLeftLin.setBackground(getActivity().getResources().getDrawable(R.drawable.radio_red_border_shap));
                totalPayTv.setText("120");
                break;
            case R.id.payMoney_med_lin:
                mPayFlag = "02";
                payMoneyMedLin.setBackground(getActivity().getResources().getDrawable(R.drawable.radio_red_border_shap));
                totalPayTv.setText("80");
                break;
            case R.id.payMoney_right_lin:
                mPayFlag = "03";
                payMoneyRightLin.setBackground(getActivity().getResources().getDrawable(R.drawable.radio_red_border_shap));
                totalPayTv.setText("50");
                break;
        }

    }
}
