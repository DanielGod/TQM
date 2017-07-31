package tqm.bianfeng.com.tqm.bank.quickloan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.utils.StringUtils;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by Daniel on 2017/6/23.
 */

public class AssetInformationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.isPayMoney_atIv)
    AutoCompleteTextView isPayMoneyAtIv;
    @BindView(R.id.isSocialSecurity_atIv)
    AutoCompleteTextView isSocialSecurityAtIv;
    @BindView(R.id.houseProperty_atIv)
    AutoCompleteTextView housePropertyAtIv;

    @BindView(R.id.vehicleProperty_atIv)
    AutoCompleteTextView vehiclePropertyAtIv;

    @BindView(R.id.commit)
    Button commit;

    String[] mStrArray_booleanFlag;
    String[] mStrArray_booleanFlagHouse;
    String[] mStrArray_booleanFlagCar;
    //    YwDksq mYwDksq;
    @BindView(R.id.houseMoney_et)
    EditText houseMoneyEt;
    @BindView(R.id.vehiclePropertyMarketValue_et)
    EditText vehiclePropertyMarketValueEt;

    ShowDialogAndLoading showDialogAndLoading;
    @BindView(R.id.houseMoney_lin)
    LinearLayout houseMoneyLin;
    @BindView(R.id.vehiclePropertyMarketValue_lin)
    LinearLayout vehiclePropertyMarketValueLin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_asset_information_basiz);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setToolbar(toolbar, "快速贷款");
        Intent intent = this.getIntent();
        //        mYwDksq = (YwDksq) intent.getSerializableExtra("ywDksq");
        Log.i("Daniel", "接受基本信息：" + BasicInformationActivity.mYwDksp.toString());
        mStrArray_booleanFlag = getResources().getStringArray(R.array.boolean_flag);
        mStrArray_booleanFlagHouse = getResources().getStringArray(R.array.boolean_flag_house);
        mStrArray_booleanFlagCar = getResources().getStringArray(R.array.boolean_flag_car);
        setATVData(isPayMoneyAtIv, mStrArray_booleanFlag);

        isPayMoneyAtIv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Daniel", "测试itemClick" + position);
                //设置是否交金数据
                if (position == 0) {
                    BasicInformationActivity.mYwDksp.setSfjj("01");
                } else {
                    BasicInformationActivity.mYwDksp.setSfjj("00");
                }
            }
        });
        setATVData(isSocialSecurityAtIv, mStrArray_booleanFlag);
        isSocialSecurityAtIv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Daniel", "测试itemClick" + position);
                //设置是否缴纳社保
                if (position == 0) {
                    BasicInformationActivity.mYwDksp.setSfjnsb("01");
                } else {
                    BasicInformationActivity.mYwDksp.setSfjnsb("00");
                }
            }
        });
        setATVData(housePropertyAtIv, mStrArray_booleanFlagHouse);
        housePropertyAtIv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Daniel", "测试itemClick" + position);
                //设置是否有房
                if (position == 0) {
                    BasicInformationActivity.mYwDksp.setSfyf("01");
                    houseMoneyLin.setVisibility(View.VISIBLE);
                } else {
                    BasicInformationActivity.mYwDksp.setSfyf("00");
                    houseMoneyLin.setVisibility(View.GONE);
                }
            }
        });
        setATVData(vehiclePropertyAtIv, mStrArray_booleanFlagCar);
        vehiclePropertyAtIv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Daniel", "测试itemClick" + position);
                //设置是否有车
                if (position == 0) {
                    BasicInformationActivity.mYwDksp.setSfyc("01");
                    vehiclePropertyMarketValueLin.setVisibility(View.VISIBLE);
                } else {
                    BasicInformationActivity.mYwDksp.setSfyc("00");
                    vehiclePropertyMarketValueLin.setVisibility(View.GONE);
                }
            }
        });

        if (!StringUtils.isEmpty(BasicInformationActivity.mYwDksp.getSqzt())) {
            setYwDksqView();
        }

        showDialogAndLoading = ShowDialogAndLoading.Show.showDialogAndLoading;
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {
                save();
            }

            @Override
            public void showAfter() {
                startActivity(new Intent(AssetInformationActivity.this, SubmitInformationActivity.class));
            }
        });

        iniEdi();

    }

    private void setYwDksqView() {
        Log.e("Daniel", "isPayMoneyAtIv:" + BasicInformationActivity.mYwDksp.getSfjj());
        houseMoneyEt.setText(BasicInformationActivity.mYwDksp.getFcgz());
        vehiclePropertyMarketValueEt.setText(BasicInformationActivity.mYwDksp.getCcgz());
    }

    /**
     * 用于接受关闭页面广播
     *
     * @param str
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String str) {
        if ("FINISH".equals(str)) {
            finish();
        }
    }

    /**
     * 初始化下拉框数据
     *
     * @param atv
     * @param mStrArray
     */
    private void setATVData(final AutoCompleteTextView atv, String[] mStrArray) {
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(AssetInformationActivity.this, R.layout.simple_dropdown_item, mStrArray);
        atv.setAdapter(autoadapter);
        atv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                atv.showDropDown();
            }
        });
        atv.setInputType(InputType.TYPE_NULL);
        atv.setKeyListener(null);
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        if (StringUtils.isEmpty(isPayMoneyAtIv.getText().toString())) {
            Toast.makeText(this, "请选择是否交金！", Toast.LENGTH_SHORT).show();
        } else if (StringUtils.isEmpty(isSocialSecurityAtIv.getText().toString())) {
            Toast.makeText(this, "请选择是否缴纳社保！", Toast.LENGTH_SHORT).show();
        } else if (StringUtils.isEmpty(housePropertyAtIv.getText().toString())) {
            Toast.makeText(this, "请选择是否有房！", Toast.LENGTH_SHORT).show();
        } else if (StringUtils.isEmpty(vehiclePropertyAtIv.getText().toString())) {
            Toast.makeText(this, "请选择是否有车！", Toast.LENGTH_SHORT).show();
        } else {
            //提交
            showDialogAndLoading.showBeforeDialog(this, "是否提交", "  ", "取消", "确定");
        }

    }

    public void iniEdi() {
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(houseMoneyEt).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(vehiclePropertyMarketValueEt).skip(1);


        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence2, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                return Bl && B2;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @DebugLog
            @Override
            public void onNext(Boolean aBoolean) {
                commit.setEnabled(aBoolean);
            }
        });
        compositeSubscription.add(etSc);
    }

    public void save() {
        showDialogAndLoading.showLoading("正在提交", this);
        Gson gson = new Gson();
        BasicInformationActivity.mYwDksp.setSqrId(realm.where(User.class).findFirst().getUserId() + "");
        BasicInformationActivity.mYwDksp.setFcgz(houseMoneyEt.getText().toString());
        BasicInformationActivity.mYwDksp.setCcgz(vehiclePropertyMarketValueEt.getText().toString());
        Log.e("Daniel", "最后提交申请人信息：" + BasicInformationActivity.mYwDksp.toString());
        Subscription subscription = NetWork.getBankService().saveOrUpdate(gson.toJson(BasicInformationActivity.mYwDksp)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResultCode>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("gqf", "onError" + e.toString());
                showDialogAndLoading.stopLoaading();
            }

            @Override
            public void onNext(ResultCode resultCode) {
                Log.i("gqf", "onNext" + resultCode.toString());
                showDialogAndLoading.stopLoaading();
                if (resultCode.getCode() == ResultCode.SECCESS) {
                    showDialogAndLoading.showAfterDialog(AssetInformationActivity.this, "提交成功", "我们将于两个工作日内与您联系，请保持电话畅通", "确定");
                    User user = realm.where(User.class).findFirst();
                    realm.beginTransaction();
                    user.setLoanStatu(BasicInformationActivity.mYwDksp.getSqzt());
                    Log.i("Daniel", "贷款审核状态：" + BasicInformationActivity.mYwDksp.getSqzt());
                    realm.copyToRealmOrUpdate(user);
                    realm.commitTransaction();
                }
            }
        });
        compositeSubscription.add(subscription);
    }

}
