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
import com.jakewharton.rxbinding.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func4;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.ReadJson;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwDksq;
import tqm.bianfeng.com.tqm.pojo.address.address_model;

/**
 * Created by Daniel on 2017/6/23.
 */

public class BasicInformationActivity extends BaseActivity {

    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.quickLoan_choiceProvince_atv)
    AutoCompleteTextView quickLoanChoiceProvinceAtv;
    @BindView(R.id.quickLoan_choiceIdentity_atv)
    AutoCompleteTextView quickLoanChoiceIdentityAtv;
    String[] mStrArray_identity;
    String[] mStrArray_city;
    @BindView(R.id.quickLoan_monthMoney_linear)
    LinearLayout quickLoanMonthMoneyLinear;
    @BindView(R.id.quickLoan_companyAddress_linear)
    LinearLayout quickLoanCompanyAddressLinear;
    @BindView(R.id.quickLoan_companyMoney_linear)
    LinearLayout quickLoanCompanyMoneyLinear;
    @BindView(R.id.quickLoan_persionMoney_linear)
    LinearLayout quickLoanPersionMoneyLinear;
    @BindView(R.id.phone_num_edi)
    EditText phoneNumEdi;
    @BindView(R.id.company_user_name_edi)
    EditText companyUserNameEdi;
    @BindView(R.id.quick_loan_money_edi)
    EditText quickLoanMoneyEdi;
    @BindView(R.id.loanTerm_et)
    EditText loanTermEt;
    @BindView(R.id.quickLoan_monthMoney_et)
    EditText quickLoanMonthMoneyEt;
    @BindView(R.id.quickLoan_companyAddress_et)
    EditText quickLoanCompanyAddressEt;
    @BindView(R.id.quickLoan_companyMoney_et)
    EditText quickLoanCompanyMoneyEt;
    @BindView(R.id.quickLoan_persionMoney_et)
    EditText quickLoanPersionMoneyEt;
    //    @BindView(R.id.default_loadview)
    //    DefaultLoadView defaultLoadview;

    String identityFlag;
    public static YwDksq mYwDksp;
    @BindView(R.id.quickLoan_choiceCity_atv)
    AutoCompleteTextView quickLoanChoiceCityAtv;
    @BindView(R.id.loanUse_et)
    EditText loanUseEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_quick_loan_basiz);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setToolbar(toolbar, "快速贷款");
        //        defaultLoadview.lodingIsFailOrSucess(1);
        //获取单条贷款信息
        getOne();
        //解析城市，初始化省份
        initaddressModels();
        mStrArray_identity = getResources().getStringArray(R.array.identity);
        //身份选择
        setATVData(quickLoanChoiceIdentityAtv, mStrArray_identity);
        quickLoanChoiceIdentityAtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //全部隐藏
                setViewGone();
                switch (position) {
                    case 0:
                        identityFlag = "01";
                        quickLoanMonthMoneyLinear.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        identityFlag = "02";
                        quickLoanCompanyAddressLinear.setVisibility(View.VISIBLE);
                        quickLoanCompanyMoneyLinear.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        identityFlag = "03";
                        quickLoanPersionMoneyLinear.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        identityFlag = "04";
                        quickLoanPersionMoneyLinear.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });
        iniEdi();
    }

    //初始化省
    public void initPEdi() {
        String[] user_num;
        Log.i("gqf", "addressModels" + addressModels.size());
        user_num = new String[addressModels.size()];
        for (int i = 0; i < addressModels.size(); i++) {
            user_num[i] = addressModels.get(i).getP();
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(BasicInformationActivity.this, R.layout.simple_dropdown_item, user_num);
        quickLoanChoiceProvinceAtv.setAdapter(autoadapter);
        quickLoanChoiceProvinceAtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                quickLoanChoiceProvinceAtv.showDropDown();
            }
        });
        quickLoanChoiceProvinceAtv.setInputType(InputType.TYPE_NULL);
        quickLoanChoiceProvinceAtv.setKeyListener(null);
        quickLoanChoiceProvinceAtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //                selectProvinces = addressModels.get(position).getP();
                quickLoanChoiceCityAtv.setAdapter(null);
                //初始化市
                initCEdi(addressModels.get(position));
                quickLoanChoiceCityAtv.setVisibility(View.VISIBLE);
                quickLoanChoiceCityAtv.setText("");
            }
        });
    }

    /**
     * 初始化市
     *
     * @param am
     */
    public void initCEdi(final address_model am) {
        String[] user_num;
        user_num = new String[am.getCity().size()];
        for (int i = 0; i < am.getCity().size(); i++) {
            user_num[i] = am.getCity().get(i).getName();
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(BasicInformationActivity.this, R.layout.simple_dropdown_item, user_num);
        quickLoanChoiceCityAtv.setAdapter(autoadapter);
        quickLoanChoiceCityAtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                quickLoanChoiceCityAtv.showDropDown();
            }
        });
        quickLoanChoiceCityAtv.setInputType(InputType.TYPE_NULL);
        quickLoanChoiceCityAtv.setKeyListener(null);

    }

    public void iniEdi() {
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(phoneNumEdi).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(companyUserNameEdi).skip(1);
        Observable<CharSequence> CharSequence3 = RxTextView.textChanges(quickLoanMoneyEdi).skip(1);
        Observable<CharSequence> CharSequence4 = RxTextView.textChanges(loanTermEt).skip(1);

        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence2, CharSequence3, CharSequence4, new Func4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                boolean B3 = !TextUtils.isEmpty(charSequence3);
                boolean B4 = !TextUtils.isEmpty(charSequence4);
                return Bl && B2 && B3 && B4;
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


    public void getOne() {
        Log.e("Dani", "申请人Id：" + realm.where(User.class).findFirst().getUserId());
        Subscription subscription = NetWork.getBankService().getOne(realm.where(User.class).findFirst().getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<YwDksq>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Dani", "快速贷款信息onError：" + e.toString());
                //                        defaultLoadview.lodingIsFailOrSucess(3);
            }

            @Override
            public void onNext(YwDksq ywDksq) {
                Log.e("Dani", "快速贷款信息onNext：" + ywDksq.toString());
                if (ywDksq != null) {
                    mYwDksp = ywDksq;
                    setYwDksqView();
                }
                //                        defaultLoadview.lodingIsFailOrSucess(2);

            }
        });
        compositeSubscription.add(subscription);
    }


    private void setViewGone() {
        quickLoanMonthMoneyLinear.setVisibility(View.GONE);
        quickLoanCompanyAddressLinear.setVisibility(View.GONE);
        quickLoanCompanyMoneyLinear.setVisibility(View.GONE);
        quickLoanPersionMoneyLinear.setVisibility(View.GONE);
    }

    private void setATVData(final AutoCompleteTextView atv, String[] mStrArray) {
        Log.e("gqf", "下拉框mStrArray：" + mStrArray.length);
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(BasicInformationActivity.this, R.layout.simple_dropdown_item, mStrArray);
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

    List<address_model> addressModels;
    ReadJson rj;

    public void initaddressModels() {
        String json = null;
        json = getString(R.string.p_c_c_json);
        rj = ReadJson.getInstance();
        addressModels = rj.getAddressModel(json);
        //初始化省
        initPEdi();
        //        initPEdi();
        //        setATVData(quickLoanChoiceCityAtv, mStrArray_city);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String str) {
        if ("FINISH".equals(str)) {
            finish();
        }
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        if (StringUtils.isEmpty(quickLoanChoiceCityAtv.getText().toString()) || StringUtils.isEmpty(quickLoanChoiceProvinceAtv.getText().toString())) {
            Toast.makeText(this, "请选择城市！", Toast.LENGTH_SHORT).show();
        } else if (StringUtils.isEmpty(quickLoanChoiceIdentityAtv.getText().toString())) {
            Toast.makeText(this, "请选择身份！", Toast.LENGTH_SHORT).show();
        } else if (!StringUtils.isEmpty(identityFlag) && "01".equals(identityFlag) && StringUtils.isEmpty(quickLoanMonthMoneyEt.getText().toString())) {
            Toast.makeText(this, "请输入您的月薪！", Toast.LENGTH_SHORT).show();
        } else if (!StringUtils.isEmpty(identityFlag) && "02".equals(identityFlag) && (StringUtils.isEmpty(quickLoanCompanyAddressEt.getText().toString()) || StringUtils.isEmpty(quickLoanCompanyMoneyEt.getText().toString()))) {
            Toast.makeText(this, "请输入您的公司信息！", Toast.LENGTH_SHORT).show();
        } else if (!StringUtils.isEmpty(identityFlag) && "03".equals(identityFlag) && StringUtils.isEmpty(quickLoanPersionMoneyEt.getText().toString())) {
            Toast.makeText(this, "请输入您的收入！", Toast.LENGTH_SHORT).show();
        } else {
            if (mYwDksp == null) {
                mYwDksp = new YwDksq();
            }
            mYwDksp.setLxdh(phoneNumEdi.getText().toString());
            mYwDksp.setSqr(companyUserNameEdi.getText().toString());
            mYwDksp.setSzcs(quickLoanChoiceCityAtv.getText().toString());
            mYwDksp.setDkje(quickLoanMoneyEdi.getText().toString());
            mYwDksp.setDkyt(loanUseEt.getText().toString());
            if (!StringUtils.isEmpty(loanTermEt.getText().toString())) {

                mYwDksp.setDkqx(Integer.parseInt(loanTermEt.getText().toString()));
            }
            mYwDksp.setZysf(identityFlag);
            if ("01".equals(identityFlag)) {
                mYwDksp.setIncome(quickLoanMonthMoneyEt.getText().toString());

            } else if ("02".equals(identityFlag)) {
                mYwDksp.setIncome(quickLoanCompanyMoneyEt.getText().toString());
                mYwDksp.setGsmc(quickLoanCompanyAddressEt.getText().toString());
            } else {
                mYwDksp.setIncome(quickLoanPersionMoneyEt.getText().toString());
            }
            Intent intent = new Intent(BasicInformationActivity.this, AssetInformationActivity.class);
            startActivity(intent);
        }

    }

    private void setYwDksqView() {
        identityFlag = mYwDksp.getZysf();
        setViewGone();
        if ("01".equals(identityFlag)) {
            quickLoanMonthMoneyLinear.setVisibility(View.VISIBLE);
        } else if ("02".equals(identityFlag)) {
            quickLoanCompanyAddressLinear.setVisibility(View.VISIBLE);
            quickLoanCompanyMoneyLinear.setVisibility(View.VISIBLE);
        } else {
            quickLoanPersionMoneyLinear.setVisibility(View.VISIBLE);
        }

        if ("01".equals(identityFlag)) {
            quickLoanMonthMoneyEt.setText(mYwDksp.getIncome());
            //            quickLoanChoiceIdentityAtv.setText(mStrArray_identity[0]);

        } else if ("02".equals(identityFlag)) {
            quickLoanCompanyMoneyEt.setText(mYwDksp.getIncome());
            quickLoanCompanyAddressEt.setText(mYwDksp.getGsmc());
            //            quickLoanChoiceIdentityAtv.setText(mStrArray_identity[1]);
        } else if ("03".equals(identityFlag)) {
            quickLoanPersionMoneyEt.setText(mYwDksp.getIncome());
            //            quickLoanChoiceIdentityAtv.setText(mStrArray_identity[2]);
        } else {
            quickLoanPersionMoneyEt.setText(mYwDksp.getIncome());
            //            quickLoanChoiceIdentityAtv.setText(mStrArray_identity[3]);
        }

        phoneNumEdi.setText(mYwDksp.getLxdh());
        companyUserNameEdi.setText(mYwDksp.getSqr());
        //        quickLoanChoiceCityAtv.setText(mYwDksp.getSzcs());
        quickLoanMoneyEdi.setText(mYwDksp.getDkje());
        loanTermEt.setText(mYwDksp.getDkqx() == null ? "" : mYwDksp.getDkqx() + "");
    }

}
