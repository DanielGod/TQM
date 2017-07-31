package tqm.bianfeng.com.tqm.User.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.StringUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForChooseActivity;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForStatusActivity;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.bank.quickloan.BasicInformationActivity;
import tqm.bianfeng.com.tqm.bank.quickloan.SubmitInformationActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwDksq;

/**
 * Created by johe on 2017/5/10.
 */
//申请进度选择页面
public class ApplyForSudelChooseActivity extends BaseActivity {

    @BindView(R.id.apply_for_choose_toolbar)
    Toolbar applyForChooseToolbar;
    @BindView(R.id.choose_company_apply_lin)
    LinearLayout chooseCompanyApplyLin;
    @BindView(R.id.choose_personal_apply_lin)
    LinearLayout choosePersonalApplyLin;
    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyfor_sudel_choose);
        ButterKnife.bind(this);
        setToolbar(applyForChooseToolbar, "申请进度");
        mUser = realm.where(User.class).findFirst();

    }
    private void getShzt(int userId) {
        Subscription subscription = NetWork.getUserService().getShzt(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Map<String, String> stringStringMap) {
                        if (!StringUtils.isEmpty(stringStringMap.get("shzt"))){
                            Log.e("Daniel","快速入驻审核状态"+stringStringMap.get("shzt"));
                            //查看审核状态
                            startActivity(new Intent(ApplyForSudelChooseActivity.this, ApplyForStatusActivity.class));
                        }else {
                            //跳转入驻选择界面
                            startActivity(new Intent(ApplyForSudelChooseActivity.this, ApplyForChooseActivity.class));
                        }

                    }
                });
        compositeSubscription.add(subscription);
    }
    private void getLoanOne(int userId) {
        Subscription subscription = NetWork.getBankService().getOne(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwDksq>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Dani", "快速贷款信息onError：" + e.toString());
                    }
                    @Override
                    public void onNext(YwDksq ywDksq) {
                        Log.e("Dani", "快速贷款信息onNext：" + ywDksq.toString());
                        if (!StringUtils.isEmpty(ywDksq.getSqzt())){
                            startActivity(new Intent(ApplyForSudelChooseActivity.this, SubmitInformationActivity.class));
                        } else {
                            startActivity(new Intent(ApplyForSudelChooseActivity.this, BasicInformationActivity.class));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
    @OnClick({R.id.choose_company_apply_lin, R.id.choose_personal_apply_lin,R.id.choose_creditManager_apply_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_company_apply_lin:
                //入驻申请
                    if (mUser == null){
                        mUser = realm.where(User.class).findFirst();
                    }
                    getShzt(mUser.getUserId());
                break;
            case R.id.choose_personal_apply_lin:
                //贷款申请
                //获取单条贷款信息
                getLoanOne(realm.where(User.class).findFirst().getUserId());
                if (mUser != null && mUser.getLoanStatu() != null && (mUser.getLoanStatu().equals("03") || mUser.getLoanStatu().equals("01"))) {
                    startActivity(new Intent(ApplyForSudelChooseActivity.this, SubmitInformationActivity.class));
                } else {
                    startActivity(new Intent(ApplyForSudelChooseActivity.this, BasicInformationActivity.class));
                }
                finish();
                break;
            case R.id.choose_creditManager_apply_lin:
//                //
//                ApplyForActivity.APPLYFORTYPE=ApplyForActivity.APPLYFORCREDITTYPE;
//                startActivity(new Intent(ApplyForSudelChooseActivity.this, ApplyForActivity.class));
//                finish();
                break;
        }
    }
}
