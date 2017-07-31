package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
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
import tqm.bianfeng.com.tqm.pojo.YwRzsq;

/**
 * Created by johe on 2017/5/12.
 */

public class ApplyForActivity extends BaseActivity implements ApplyForCreditFragment.mListener, ApplyForCompanyFragment.mListener,ApplyForPersonalFragment.mListener{
    private static final int REQUEST_IMAGE = 2;
    private static final int REQUEST_PERSION_IMAGE = 3;
    private static final int REQUEST_CREDIT_IMAGE = 4;
    public static int APPLYFORCOMPANYTYPE = 1;//公司
    public static int APPLYFORPERSONALTYPE = 2;//个人
    public static int APPLYFORCREDITTYPE = 3;//信贷
    public static int APPLYFORTYPE = 0;
    @BindView(R.id.apply_for_company_toolbar)
    Toolbar applyForCompanyToolbar;
    @BindView(R.id.continar)
    FrameLayout continar;
    @BindView(R.id.commit)
    Button commit;

    YwRzsq ywApplyEnter;
    ApplyForCompanyFragment applyForCompanyFragment;
    ApplyForPersonalFragment applyForPersonalFragment;
    ApplyForCreditFragment applyForCreditFragment;
    ShowDialogAndLoading showDialogAndLoading;
    String applyForId;
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
        }else if(APPLYFORTYPE==APPLYFORPERSONALTYPE){
            applyForPersonalFragment=new ApplyForPersonalFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.continar, applyForPersonalFragment).commit();
            title="个人申请";
        }else {
            applyForCreditFragment=new ApplyForCreditFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.continar, applyForCreditFragment).commit();
            title="信贷经理认证";
        }
        setToolbar(applyForCompanyToolbar, title);
        commit.setEnabled(false);
        showDialogAndLoading= ShowDialogAndLoading.Show.showDialogAndLoading;
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {
                    save();
            }

            @Override
            public void showAfter() {
              startActivity(new Intent(ApplyForActivity.this,ApplyForStatusActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Daniel","---onStart---");
        //获取入驻信息
        getUserApplyMsg();
    }

    @DebugLog
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Daniel","多图选择返回："+requestCode);
        Log.e("Daniel","多图选择返回data："+data);
        Log.e("Daniel","多图选择返回resultCode："+resultCode);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                applyForCompanyFragment.setImgInView(data);
            }
        }
        if (requestCode == REQUEST_PERSION_IMAGE) {
            if (resultCode == RESULT_OK) {
                applyForPersonalFragment.setImgInView(data);
            }
        }
        if (requestCode == REQUEST_CREDIT_IMAGE) {
            if (resultCode == RESULT_OK) {
                applyForCreditFragment.setImgInView(data);
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
        if (ywApplyEnter==null){
            ywApplyEnter = new YwRzsq();
        }
        if(APPLYFORTYPE==APPLYFORCOMPANYTYPE){
            //公司
            ywApplyEnter=applyForCompanyFragment.getYwApplyEnter();

        }else if(APPLYFORTYPE==APPLYFORPERSONALTYPE){
            //个人
            ywApplyEnter=applyForPersonalFragment.getYwApplyEnter();
        }else {
            //信贷经理
            ywApplyEnter=applyForCreditFragment.getYwApplyEnter();
        }
        if(ywApplyEnter!=null){
            //提交
            showDialogAndLoading.showBeforeDialog(this,"是否提交","  ","取消","确定");
        }

    }
    public void save(){
        showDialogAndLoading.showLoading("正在提交",this);
        Gson gson=new Gson();
        ywApplyEnter.setId(applyForId);
        ywApplyEnter.setSqr(realm.where(User.class).findFirst().getUserId());
        Log.e("Daniel","最后提交申请人信息："+ywApplyEnter.toString());
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
                        showDialogAndLoading.stopLoaading();
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.i("gqf","onNext"+resultCode.toString());
                        showDialogAndLoading.stopLoaading();
                        if(resultCode.getCode()==ResultCode.SECCESS){
                            showDialogAndLoading.showAfterDialog(ApplyForActivity.this,"提交成功","我们将于两个工作日内与您联系，请保持电话畅通","确定");
//                            User user = realm.where(User.class).findFirst();
//                            realm.beginTransaction();
////                            user.setApplyForType(ywApplyEnter.getLxbq());
//                            Log.i("Daniel","审核状态："+ywApplyEnter.getShzt());
//                            user.setApplyForStatu(ywApplyEnter.getShzt());
//                            realm.copyToRealmOrUpdate(user);
//                            realm.commitTransaction();
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void getUserApplyMsg(){
        Subscription subscription = NetWork.getUserService().getOne(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwRzsq>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(YwRzsq ywApr) {
                        Log.i("Daniel","获取入驻信息："+ywApr.toString());
                        applyForId=ywApr.getId();
                        if(APPLYFORTYPE==APPLYFORCOMPANYTYPE){
                            applyForCompanyFragment.setYwApplyEnter(ywApr);
                        }else if(APPLYFORTYPE==APPLYFORPERSONALTYPE){
                            applyForPersonalFragment.setYwApplyEnter(ywApr);
                        }else {
                            applyForCreditFragment.setYwApplyEnter(ywApr);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
}
