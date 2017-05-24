package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
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

    ShowDialogAndLoading showDialogAndLoading;
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
        showDialogAndLoading= ShowDialogAndLoading.Show.showDialogAndLoading;
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {
                save();
            }

            @Override
            public void showAfter() {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                applyForCompanyFragment.setImgInView(data);
            }
        }

        if (requestCode == Crop.REQUEST_CROP) {
            Log.i("gqf", "handleCrop");
            if(applyForCompanyFragment!=null){
                applyForCompanyFragment.setLogo(resultCode, data);
            }
        }else if(requestCode == Crop.RESULT_ERROR){
            Log.i("gqf", "RESULT_ERROR");
        }else if(requestCode == Crop.REQUEST_PICK){
            Log.i("gqf", "REQUEST_PICK");
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

        if(ywApplyEnter==null||ywApplyEnter.getContact().equals("")){
            Toast.makeText(this, "联系电话不能为空", Toast.LENGTH_SHORT).show();

        }else{
            //提交
            showDialogAndLoading.showBeforeDialog(this,"是否提交","  ","取消","确定");
        }

    }
    int i = 6;//apk加载进度
    SweetAlertDialog pDialog;
    CountDownTimer countDownTimer;
    public void save(){
        showDialogAndLoading.showLoading("正在提交。。",this);
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
                        showDialogAndLoading.stopLoaading();
                        if(resultCode.getCode()==ResultCode.SECCESS){
                            showDialogAndLoading.showAfterDialog(ApplyForActivity.this,"提交成功","我们将于两个工作日内与您联系，请保持电话畅通","确定");
                            User user = realm.where(User.class).findFirst();
                            realm.beginTransaction();
                            user.setUserType(ywApplyEnter.getApplyType());
                            realm.copyToRealmOrUpdate(user);
                            realm.commitTransaction();
                        }
                    }
                });
        compositeSubscription.add(subscription);
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
