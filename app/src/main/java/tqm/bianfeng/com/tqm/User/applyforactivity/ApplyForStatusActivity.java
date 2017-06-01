package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwApplyEnter;
import tqm.bianfeng.com.tqm.pojo.result.ResultWithAuditCode;

/**
 * Created by johe on 2017/5/12.
 */

public class ApplyForStatusActivity extends BaseActivity {

    @BindView(R.id.apply_for_status_toolbar)
    Toolbar applyForStatusToolbar;

    YwApplyEnter ywApplyEnter;
    @BindView(R.id.no_audit_view)
    View noAuditView;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.no_audit_lin)
    LinearLayout noAuditLin;
    @BindView(R.id.audit_end_txt)
    TextView auditEndTxt;
    @BindView(R.id.audit_remark_txt)
    TextView auditRemarkTxt;
    @BindView(R.id.audit_end_lin)
    LinearLayout auditEndLin;
    @BindView(R.id.apply_statu_scroll)
    ScrollView applyStatuScroll;
    @BindView(R.id.company_name_edi)
    TextView companyNameEdi;
    @BindView(R.id.company_apply_edi)
    TextView companyApplyEdi;
    @BindView(R.id.company_user_edi)
    TextView companyUserEdi;
    @BindView(R.id.company_time_edi)
    TextView companyTimeEdi;
    @BindView(R.id.apply_for_personal_msg_lin)
    LinearLayout applyForPersonalMsgLin;
    @BindView(R.id.personal_apply_edi)
    TextView personalApplyEdi;
    @BindView(R.id.personal_user_edi)
    TextView personalUserEdi;
    @BindView(R.id.personal_time_edi)
    TextView personalTimeEdi;
    @BindView(R.id.apply_for_company_msg_lin)
    LinearLayout applyForCompanyMsgLin;
    @BindView(R.id.audit_end_img)
    ImageView auditEndImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_status);
        ButterKnife.bind(this);
        setToolbar(applyForStatusToolbar, "申请进度");
        //getAuditCode(realm.where(User.class).findFirst().getUserId());
        getOne(realm.where(User.class).findFirst().getUserId());
    }

    public void getAuditCode(int userId) {
        Subscription subscription = NetWork.getUserService().getStatus(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultWithAuditCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultWithAuditCode resultWithAuditCode) {
                        Log.i("gqf", "onNext" + resultWithAuditCode.getCode());
                    }
                });
        compositeSubscription.add(subscription);
    }

    //获取进度
    public void getOne(int userId) {
        Subscription subscription = NetWork.getUserService().getOne(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwApplyEnter>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "ywApplyEnter" + e.toString());
                    }

                    @Override
                    public void onNext(YwApplyEnter ywAppler) {
                        Log.i("gqf", "ywApplyEnter" + ywAppler.toString());
                        applyStatuScroll.setVisibility(View.VISIBLE);

                        ywApplyEnter = ywAppler;
                        if (ywApplyEnter.getApplyType().equals("1001") || ywApplyEnter.getApplyType().equals("1002")) {
                            //公司申请
                            applyForCompanyMsgLin.setVisibility(View.VISIBLE);
                            companyNameEdi.setText(ywApplyEnter.getApplyName());
                            companyUserEdi.setText(ywApplyEnter.getProposer());
                            if (ywApplyEnter.getApplyType().equals("1001")) {
                                companyApplyEdi.setText("民间资本");
                            } else {
                                companyApplyEdi.setText("中介公司");
                            }
                            companyTimeEdi.setText(transferLongToDate("yyyy-MM-dd", ywApplyEnter.getCreateDate()).toString());


                        } else {
                            //个人申请
                            applyForPersonalMsgLin.setVisibility(View.VISIBLE);
                            personalUserEdi.setText(ywApplyEnter.getProposer());
                            if (ywApplyEnter.getApplyType().equals("2001")) {
                                personalApplyEdi.setText("资方");
                            } else {
                                personalApplyEdi.setText("个人中介");
                            }
                            personalTimeEdi.setText(transferLongToDate("yyyy-MM-dd", ywApplyEnter.getCreateDate()).toString());

                        }

                        if (ywApplyEnter.getAuditCode().equals("02") || ywApplyEnter.getAuditCode().equals("01")) {
                            auditEndLin.setVisibility(View.VISIBLE);
                            noAuditView.setVisibility(View.VISIBLE);
                            if (ywApplyEnter.getAuditCode().equals("02")) {

                                auditRemarkTxt.setText(ywApplyEnter.getAuditRemark());
                                noAuditLin.setVisibility(View.VISIBLE);
                                noAuditView.setBackgroundColor(getResources().getColor(R.color.font_black_5));
                                auditEndTxt.setText("审核未通过");

                            } else if(ywApplyEnter.getAuditCode().equals("01")) {
                                auditEndTxt.setText("审核通过");
                                noAuditView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                auditEndImg.setImageResource(R.drawable.ic_apply_true);
                            }else{

                            }

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

    @OnClick(R.id.commit)
    public void onClick() {

        if (ywApplyEnter.getApplyType().equals("1001") || ywApplyEnter.getApplyType().equals("1002")) {
            ApplyForActivity.APPLYFORTYPE = ApplyForActivity.APPLYFORCOMPANYTYPE;
        } else {
            ApplyForActivity.APPLYFORTYPE = ApplyForActivity.APPLYFORPERSONALTYPE;
        }
        startActivity(new Intent(ApplyForStatusActivity.this, ApplyForActivity.class));
        finish();
    }
}
