package tqm.bianfeng.com.tqm.User.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.DksqVO;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/5/10.
 */
//订单详情界面
public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.release_toolbar)
    Toolbar releaseToolbar;
    @BindView(R.id.orderDeatil_getInformation_lin)
    LinearLayout orderDeatilGetInformationLin;
    @BindView(R.id.orderDeatil_getTime_tv)
    TextView orderDeatilGetTimeTv;
    @BindView(R.id.orderDeatil_getWay_tv)
    TextView orderDeatilGetWayTv;
    @BindView(R.id.orderDeatil_assetInformation_lin)
    LinearLayout orderDeatilAssetInformationLin;
    @BindView(R.id.orderDeatil_phone_tv)
    TextView orderDeatilPhoneTv;
    @BindView(R.id.orderDeatil_name_tv)
    TextView orderDeatilNameTv;
    @BindView(R.id.orderDeatil_loanNum_tv)
    TextView orderDeatilLoanNumTv;
    @BindView(R.id.orderDeatil_loanCity_tv)
    TextView orderDeatilLoanCityTv;
    @BindView(R.id.orderDeatil_loanTimeLimit_tv)
    TextView orderDeatilLoanTimeLimitTv;
    @BindView(R.id.orderDeatil_personalformation_lin)
    LinearLayout orderDeatilPersonalformationLin;
    @BindView(R.id.orderDeatil_professionalIdentity_tv)
    TextView orderDeatilProfessionalIdentityTv;
    @BindView(R.id.orderDeatil_monthlySalary_tv)
    TextView orderDeatilMonthlySalaryTv;
    @BindView(R.id.orderDeatil_loanInformation_lin)
    LinearLayout orderDeatilLoanInformationLin;
    @BindView(R.id.orderDeatil_assetInformation_tv)
    TextView orderDeatilAssetInformationTv;
    @BindView(R.id.orderDeatil_isSocialSecurity_tv)
    TextView orderDeatilIsSocialSecurityTv;
    @BindView(R.id.orderDeatil_house_tv)
    TextView orderDeatilHouseTv;
    @BindView(R.id.orderDeatil_houseValuse_tv)
    TextView orderDeatilHouseValuseTv;
    @BindView(R.id.orderDeatil_car_tv)
    TextView orderDeatilCarTv;
    @BindView(R.id.orderDeatil_carValuse_tv)
    TextView orderDeatilCarValuseTv;
    @BindView(R.id.orderDeatil_companyName_tv)
    TextView orderDeatilCompanyNameTv;
    @BindView(R.id.orderDeatil_companyName_lin)
    LinearLayout orderDeatilCompanyNameLin;
    @BindView(R.id.orderDeati_income_tv)
    TextView orderDeatiIncomeTv;
    @BindView(R.id.commit)
    Button commit;
    String mViewType;
    String mDksqId;
    User mUser;
    DksqVO mDksqVO;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @BindView(R.id.houseValue_lin)
    LinearLayout houseValueLin;
    @BindView(R.id.carValue_lin)
    LinearLayout carValueLin;
    @BindView(R.id.orderDeatil_loanUse_tv)
    TextView orderDeatilLoanUseTv;

    ShowDialogAndLoading showDialogAndLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        setToolbar(releaseToolbar, "订单中心");
        showDialogAndLoading = ShowDialogAndLoading.Show.showDialogAndLoading;
        mUser = realm.where(User.class).findFirst();
        mDksqId = getIntent().getStringExtra("dksqId");
        mViewType = getIntent().getStringExtra("viewType");//01-未领取，02-已领取
        showDialogAndLoading.showLoading("加载中",OrderDetailActivity.this);

        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {
            }
            @Override
            public void showAfter() {
                Logger.d(paySuccess);
                if (paySuccess)
                //跳转至领取订单页面
                   startActivity(new Intent(OrderDetailActivity.this,OrderActivity.class).putExtra("orderType","02"));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("onResume");
        if (mUser != null) {
            initData(mUser.getUserId(), mDksqId, mViewType);
        }
    }
    boolean paySuccess = false;//是否付过费
    //获取订单详情
    private void initData(int userId, String mDksqId, String mViewType) {
        Log.e("Dani", "mViewType：" + mViewType);
        Subscription subscription = NetWork.getUserService().getDksqVO(userId, mDksqId, mViewType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DksqVO>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.e("Dani", "订单详情信息onError：" + e.toString());
                showDialogAndLoading.stopLoaading();
                showDialogAndLoading.showFailureDialog(OrderDetailActivity.this,"通知","获取订单失败！");
                onBackPressed();
            }
            // TODO: 2017/7/5 对话框
            @Override
            public void onNext(DksqVO dksqVO) {
                Log.e("Dani", "订单详情信息onNext：" + dksqVO.toString());
                if (dksqVO != null) {
                    mDksqVO = dksqVO;
                    showDialogAndLoading.stopLoaading();
                    if (paySuccess)
                    showDialogAndLoading.showAfterDialog(OrderDetailActivity.this,"领取成功","恭喜您！领取成功。","确定");
                    else
                        InitView(dksqVO);
                }


            }
        });
        compositeSubscription.add(subscription);
    }

    private String transferLongToDate(String dateFormat, Long millSec) {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Date date = new Date(millSec);

        return sdf.format(date);
    }

    private void InitView(DksqVO dksqVO) {
        Log.e("Dani", "领取信息："+dksqVO.getIsReceive());
        //领取信息 0-未领取 1-领取
        if ("0".equals(dksqVO.getIsReceive())) {
            orderDeatilGetInformationLin.setVisibility(View.GONE);
            commit.setVisibility(View.VISIBLE);
        } else if ("1".equals(dksqVO.getIsReceive())) {
            orderDeatilGetInformationLin.setVisibility(View.VISIBLE);
            commit.setVisibility(View.GONE);
            orderDeatilGetTimeTv.setText(transferLongToDate("yyyy-MM-dd", dksqVO.getLqrq()).toString());
            if ("01".equals(dksqVO.getLqfs())) {
                orderDeatilGetWayTv.setText("免费");
            } else {
                orderDeatilGetWayTv.setText("付费");
            }
        }
        //贷款信息
        Logger.i(dksqVO.getSqr()+dksqVO.getLxdh());
        orderDeatilPhoneTv.setText(dksqVO.getLxdh());
        orderDeatilNameTv.setText(dksqVO.getSqr());
        orderDeatilLoanNumTv.setText(dksqVO.getDkje());
        orderDeatilLoanCityTv.setText(dksqVO.getSzcs());
        orderDeatilLoanTimeLimitTv.setText(dksqVO.getDkqx() + "");
        orderDeatilLoanUseTv.setText(dksqVO.getDkyt());
        if ("01".equals(dksqVO.getZysf())) {
            orderDeatilProfessionalIdentityTv.setText("上班族");
            orderDeatiIncomeTv.setText("税后月薪");
        } else if ("02".equals(dksqVO.getZysf())) {
            orderDeatilProfessionalIdentityTv.setText("企业主");
            orderDeatilCompanyNameLin.setVisibility(View.VISIBLE);
            orderDeatilCompanyNameTv.setText(dksqVO.getGsmc());
            orderDeatiIncomeTv.setText("公司流水");

        } else if ("03".equals(dksqVO.getZysf())) {
            orderDeatilProfessionalIdentityTv.setText("个体户");
            orderDeatiIncomeTv.setText("收入");
        } else {
            orderDeatilProfessionalIdentityTv.setText("自由职业");
            orderDeatiIncomeTv.setText("收入");
        }
        orderDeatilMonthlySalaryTv.setText(dksqVO.getIncome());

        if ("00".equals(dksqVO.getSfjj())) {
            orderDeatilAssetInformationTv.setText("否");
        } else {
            orderDeatilAssetInformationTv.setText("是");
        }
        if ("00".equals(dksqVO.getSfjnsb())) {
            orderDeatilIsSocialSecurityTv.setText("否");
        } else {
            orderDeatilIsSocialSecurityTv.setText("是");
        }
        if ("00".equals(dksqVO.getSfyf())) {
            orderDeatilHouseTv.setText("无房产");
            houseValueLin.setVisibility(View.GONE);
        } else {
            orderDeatilHouseTv.setText("有房产");
            houseValueLin.setVisibility(View.VISIBLE);
        }
        orderDeatilHouseValuseTv.setText(dksqVO.getFcgz());
        if ("00".equals(dksqVO.getSfyc())) {
            orderDeatilCarTv.setText("无车");
            carValueLin.setVisibility(View.GONE);
        } else {
            orderDeatilCarTv.setText("有车");
            carValueLin.setVisibility(View.VISIBLE);
        }

        orderDeatilCarValuseTv.setText(dksqVO.getCcgz());


    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        isFree();

    }

    boolean wrapInScrollView = true;

    private void isFree() {
        //判断是否免费
        Subscription subscription = NetWork.getUserService().isFree(mUser.getUserPhone(), mUser.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                showDialog(s);
            }
        });
        compositeSubscription.add(subscription);
    }
    MaterialDialog mDialog;
    private void showDialog(String s) {
        mDialog = new MaterialDialog.Builder(OrderDetailActivity.this)
                .customView(R.layout.fragment_apply_dialog, wrapInScrollView).show();
        View view = mDialog.getCustomView();
        Button freeGetBtn = (Button) view.findViewById(R.id.freeGet_btn);
        ImageView demiss = (ImageView) view.findViewById(R.id.dimess);
        demiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        if ("0".equals(s)) {
            freeGetBtn.setVisibility(View.GONE);
        } else {
            freeGetBtn.setVisibility(View.VISIBLE);
        }
        Log.e("Dani", "判断是否免费：" + s);
        Button payGetBtn = (Button) view.findViewById(R.id.payGet_btn);
        payGetBtn.setText("付费" + mDksqVO.getExtractMoney() + "元领取");

        freeGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //领取
                showDialogAndLoading.showLoading("请稍等！正在领取中。。",OrderDetailActivity.this);
                mDialog.dismiss();
                saveLqmx();

            }
        });
        payGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Dani", "付费领取：");
                showDialogAndLoading.showLoading("请稍等！正在领取中。。",OrderDetailActivity.this);
                dkOrderPay(mDksqId, mUser.getUserId());

            }


        });
    }

    private void dkOrderPay(String mDksqId, int userId) {
        Subscription subscription = NetWork.getPayService().dkOrderPay(mDksqId, userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Dani", "获取付费orderInfo错误：" + e.toString());
                showDialogAndLoading.stopLoaading();
                showDialogAndLoading.showAfterDialog(OrderDetailActivity.this,"领取结果","抱歉！领取失败。","确定");

            }

            @Override
            public void onNext(Map<String, String> orderInfo) {
                Log.e("Dani", "获取付费orderInfo：" + orderInfo.get("orderInfo").toString());
                //调用支付宝
                new Pay(OrderDetailActivity.this, orderInfo.get("orderInfo")).payV2();
                mDialog.dismiss();
            }
        });
        compositeSubscription.add(subscription);

    }
    //保存免费领取
    private void saveLqmx() {
        Subscription subscription = NetWork.getUserService().saveLqmx(mDksqId, mUser.getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultCode>() {
            @DebugLog
            @Override
            public void call(ResultCode resultCode) {
                Log.e("Dani", "付费：" + resultCode.getCode());
                if (resultCode.getCode() == ResultCode.SECCESS) {
                    paySuccess = true;
                    mViewType = "02";
                    initData(mUser.getUserId(), mDksqId, mViewType);
                } else {
                    paySuccess = false;
                    showDialogAndLoading.stopLoaading();
                    showDialogAndLoading.showAfterDialog(OrderDetailActivity.this,"领取结果","抱歉！领取失败。","确定");
                }
            }
        });
        compositeSubscription.add(subscription);
    }


}
