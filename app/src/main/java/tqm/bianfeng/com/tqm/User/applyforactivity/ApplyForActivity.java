package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwApplyEnter;

/**
 * Created by johe on 2017/5/12.
 */

public class ApplyForActivity extends BaseActivity implements ApplyForCompanyFragment.mListener,ApplyForPersonalFragment.mListener{
    private static final int REQUEST_IMAGE = 2;
    public static int APPLYFORCOMPANYTYPE = 1;
    public static int APPLYFORPERSONALTYPE = 2;
    public static int APPLYFORTYPE = 0;
    @BindView(R.id.apply_for_company_toolbar)
    Toolbar applyForCompanyToolbar;
    @BindView(R.id.continar)
    FrameLayout continar;
    @BindView(R.id.commit)
    Button commit;

    YwApplyEnter ywApplyEnter;
    ApplyForCompanyFragment applyForCompanyFragment;
    ApplyForPersonalFragment applyForPersonalFragment;

    int applyForId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_company_or_personal);
        ButterKnife.bind(this);
        String title="";

        if(APPLYFORTYPE==APPLYFORCOMPANYTYPE){
            applyForCompanyFragment=new ApplyForCompanyFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.continar, applyForCompanyFragment).commit();
            title="公司申请";
        }else{
            applyForPersonalFragment=new ApplyForPersonalFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.continar, applyForPersonalFragment).commit();
            title="个人申请";
        }
        setToolbar(applyForCompanyToolbar, title);
        getUserApplyMsg();
        commit.setEnabled(false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                applyForCompanyFragment.setImgInView(data);
            }
        }
    }
    public void setCommitBtn(boolean is){
        commit.setEnabled(is);
    }
    @OnClick(R.id.commit)
    public void onClick() {
        if(APPLYFORTYPE==APPLYFORCOMPANYTYPE){
            ywApplyEnter=applyForCompanyFragment.getYwApplyEnter();
        }else{
            ywApplyEnter=applyForPersonalFragment.getYwApplyEnter();
        }

        if(ywApplyEnter==null){

        }else{
            //提交
            initDialog(1);
        }

    }
    int i = 6;//apk加载进度
    SweetAlertDialog pDialog;
    CountDownTimer countDownTimer;
    public void save(){
        initDialog(2);
        Gson gson=new Gson();
        ywApplyEnter.setApplyId(applyForId);
        ywApplyEnter.setApplyUser(realm.where(User.class).findFirst().getUserId());
        Subscription subscription = NetWork.getUserService().save(gson.toJson(ywApplyEnter))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.toString());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.i("gqf","onNext"+resultCode.toString());
                        showLoading(1);
                        if(resultCode.getCode()==ResultCode.SECCESS){
                            initDialog(3);
                        }


                    }
                });
        compositeSubscription.add(subscription);
    }
    public void initDialog(int index){
        if(index==1){
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("是否提交信息")
                    .setContentText("   ")
                    .setCancelText("取消")
                    .setConfirmText("确认")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            save();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }else if(index==2){
            showLoading(0);
        }else{
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("提交成功")
                    .setContentText("我们将在两个工作日内联系您，请保持电话畅通。")
                    .setConfirmText("确定")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            ApplyForActivity.this.finish();
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }


    public void showLoading(int index) {
        if (index == 0) {
            //开始
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("提交中，请稍后");
            pDialog.show();
            pDialog.setCancelable(false);
            countDownTimer = new CountDownTimer(1000 * 100, 1000) {
                public void onTick(long millisUntilFinished) {
                    // you can change the progress bar color by ProgressHelper every 800 millis
                    i++;
                    switch (i % 6) {
                        case 0:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                            break;
                        case 1:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                            break;
                        case 2:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                        case 3:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                            break;
                        case 4:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                            break;
                        case 5:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                            break;
                        case 6:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                    }
                }

                public void onFinish() {
                    i = 6;
                }
            }.start();

        } else {
            //结束
            pDialog.dismiss();
            countDownTimer.onFinish();
        }
    }
    public void getUserApplyMsg(){
        Subscription subscription = NetWork.getUserService().getOne(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwApplyEnter>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(YwApplyEnter ywApr) {
                        Log.i("gqf","YwApplyEnter"+ywApr.toString());
                        applyForId=ywApr.getApplyId();
                        if(APPLYFORTYPE==APPLYFORCOMPANYTYPE){
                            applyForCompanyFragment.setYwApplyEnter(ywApr);
                        }else{
                            applyForPersonalFragment.setYwApplyEnter(ywApr);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
}
