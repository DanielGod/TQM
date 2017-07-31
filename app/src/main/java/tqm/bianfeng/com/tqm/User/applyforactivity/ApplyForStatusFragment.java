package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwRzsq;


/**
 * Created by 王九东 on 2017/7/1.
 */

public class ApplyForStatusFragment extends BaseFragment {
    @BindView(R.id.company_name_edi)
    TextView companyNameEdi;
    @BindView(R.id.company_apply_edi)
    TextView companyApplyEdi;
    @BindView(R.id.company_user_edi)
    TextView companyUserEdi;
    @BindView(R.id.company_time_edi)
    TextView companyTimeEdi;
    @BindView(R.id.apply_for_company_msg_lin)
    LinearLayout applyForCompanyMsgLin;
    @BindView(R.id.personal_apply_edi)
    TextView personalApplyEdi;
    @BindView(R.id.personal_user_edi)
    TextView personalUserEdi;
    @BindView(R.id.personal_time_edi)
    TextView personalTimeEdi;
    @BindView(R.id.apply_for_personal_msg_lin)
    LinearLayout applyForPersonalMsgLin;
    @BindView(R.id.creditManager_name_edi)
    TextView creditManagerNameEdi;
    @BindView(R.id.creditManager_user_edi)
    TextView creditManagerUserEdi;
    @BindView(R.id.creditManager_address_edi)
    TextView creditManagerAddressEdi;
    @BindView(R.id.creditManager_time_edi)
    TextView creditManagerTimeEdi;
    @BindView(R.id.apply_for_creditManager_msg_lin)
    LinearLayout applyForCreditManagerMsgLin;
    @BindView(R.id.no_audit_view)
    View noAuditView;
    @BindView(R.id.audit_end_img)
    ImageView auditEndImg;
    @BindView(R.id.audit_end_txt)
    TextView auditEndTxt;
    @BindView(R.id.audit_end_lin)
    LinearLayout auditEndLin;
    @BindView(R.id.audit_remark_txt)
    TextView auditRemarkTxt;
    @BindView(R.id.commit)
    Button commit;

    @BindView(R.id.apply_statu_scroll)
    ScrollView applyStatuScroll;
    public static YwRzsq ywApplyEnter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_for_status, container, false);
        ButterKnife.bind(this, view);
        showDialogAndLoading = ShowDialogAndLoading.Show.showDialogAndLoading;
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {
                EventBus.getDefault().post("ApplyForStatusFragment_pay");
            }
            @Override
            public void showAfter() {
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOne(realm.where(User.class).findFirst().getUserId());
    }

    ShowDialogAndLoading showDialogAndLoading;
    //获取进度
    public void getOne(int userId) {
        Subscription subscription = NetWork.getUserService()
                .getOne(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<YwRzsq>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("gqf", "ywApplyEnter" + e.toString());
            }
            @Override
            public void onNext(YwRzsq ywAppler) {
                Log.e("Daniel", "申请进度页面信息：" + ywAppler.toString());
                applyStatuScroll.setVisibility(View.VISIBLE);
                ywApplyEnter = ywAppler;
                if (ywApplyEnter.getLxbq().equals("1001") || ywApplyEnter.getLxbq().equals("1002")) {
                    //公司申请
                    applyForCompanyMsgLin.setVisibility(View.VISIBLE);
                    companyNameEdi.setText(ywApplyEnter.getGsmc());
                    companyUserEdi.setText(ywApplyEnter.getLxr());
                    if (ywApplyEnter.getLxbq().equals("1001")) {
                        companyApplyEdi.setText("民间资本");
                    } else {
                        companyApplyEdi.setText("中介公司");
                    }
                    companyTimeEdi.setText(transferLongToDate("yyyy-MM-dd", ywApplyEnter.getCreateDate()).toString());
                } else if (ywApplyEnter.getLxbq().equals("2001") || ywApplyEnter.getLxbq().equals("2002")) {
                    //个人申请
                    applyForPersonalMsgLin.setVisibility(View.VISIBLE);
                    personalUserEdi.setText(ywApplyEnter.getLxr());
                    if (ywApplyEnter.getLxbq().equals("2001")) {
                        personalApplyEdi.setText("资方");
                    } else {
                        personalApplyEdi.setText("个人中介");
                    }
                    personalTimeEdi.setText(transferLongToDate("yyyy-MM-dd", ywApplyEnter.getCreateDate()).toString());

                } else {
                    //信贷经理申请
                    applyForCreditManagerMsgLin.setVisibility(View.VISIBLE);
                    creditManagerNameEdi.setText(ywApplyEnter.getInstitutionName());
                    creditManagerUserEdi.setText(ywApplyEnter.getLxr());
                    creditManagerAddressEdi.setText(ywApplyEnter.getProvince() + ywApplyEnter.getCity() + ywApplyEnter.getCounty());
                    creditManagerTimeEdi.setText(transferLongToDate("yyyy-MM-dd", ywApplyEnter.getCreateDate()).toString());
                }

                if (ywApplyEnter.getShzt().equals("02") || ywApplyEnter.getShzt().equals("01") || ywApplyEnter.getShzt().equals("00")) {
                    if (ywApplyEnter.getShzt().equals("00") || ywApplyEnter.getShzt().equals("02")){
                        commit.setVisibility(View.VISIBLE);
                    }else {
                        commit.setVisibility(View.GONE);
                    }
                    if (ywApplyEnter.getShzt().equals("02") || ywApplyEnter.getShzt().equals("01")){
                        auditEndLin.setVisibility(View.VISIBLE);
                        noAuditView.setVisibility(View.VISIBLE);
                        if (ywApplyEnter.getShzt().equals("02")) {
                            if (!StringUtils.isEmpty(ywApplyEnter.getShbz())){
                                auditRemarkTxt.setVisibility(View.VISIBLE);
                                auditRemarkTxt.setText(ywApplyEnter.getShbz());
                            }
                            noAuditView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            auditEndImg.setImageResource(R.drawable.ic_apply_true);
                            auditEndTxt.setText("审核未通过");
                        } else if (ywApplyEnter.getShzt().equals("01")) {
                            auditEndTxt.setText("审核通过");
                            noAuditView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            auditEndImg.setImageResource(R.drawable.ic_apply_true);
                            if (ywApplyEnter.getLxbq().equals("3001")){
                                showDialogAndLoading.showAuthSuccessDialog(getActivity(), "审核成功", "是否要在机构平台首页展示您的联系方式？", "取消", "确定");
                            }
                        }
                    }else {
                        auditEndLin.setVisibility(View.GONE);
                        noAuditView.setVisibility(View.GONE);
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
        if (ywApplyEnter.getLxbq().equals("1001") || ywApplyEnter.getLxbq().equals("1002")) {
            ApplyForActivity.APPLYFORTYPE = ApplyForActivity.APPLYFORCOMPANYTYPE;
        } else if (ywApplyEnter.getLxbq().equals("2001") || ywApplyEnter.getLxbq().equals("2002")){
            ApplyForActivity.APPLYFORTYPE = ApplyForActivity.APPLYFORPERSONALTYPE;
        }else {
            ApplyForActivity.APPLYFORTYPE = ApplyForActivity.APPLYFORCREDITTYPE;
        }
        startActivity(new Intent(getActivity(), ApplyForActivity.class));

    }


}
