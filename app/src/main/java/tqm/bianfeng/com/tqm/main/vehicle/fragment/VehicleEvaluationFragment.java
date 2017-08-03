package tqm.bianfeng.com.tqm.main.vehicle.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.DatePicker;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func9;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.PhoneUtils;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.main.vehicle.bean.ChexingListBean;
import tqm.bianfeng.com.tqm.main.vehicle.bean.CitysBean;
import tqm.bianfeng.com.tqm.main.vehicle.bean.Contact;
import tqm.bianfeng.com.tqm.main.vehicle.bean.ProvinceBean;
import tqm.bianfeng.com.tqm.main.vehicle.bean.ProvinceCityBean;
import tqm.bianfeng.com.tqm.main.vehicle.bean.VehicleEvaluationInformation;
import tqm.bianfeng.com.tqm.main.vehicle.bean.VehicleSubmitResultCode;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;


/**
 * Created by 王九东 on 2017/7/1.
 */

public class VehicleEvaluationFragment extends BaseFragment {

    public static ChexingListBean chexingListBean;
    public static Integer provinceId;
    public static Integer cityId;
//    public static String Vehicle_month;
//    public static Integer Vehicle_year;
    ProvinceCityBean provinceCityBean;//城市对象
    String[] provinceNameStr;//省名
    String[] cityNameStr;//市名
    List<ProvinceBean> provinceList;//省对象
    List<CitysBean> cityList;//市对象
    @BindView(R.id.vehicle_name_edi)
    EditText vehicleNameEdi;
    @BindView(R.id.vehicle_phone_edi)
    EditText vehiclePhoneEdi;
    @BindView(R.id.vehicleEvaluation_choiceProvince_atv)
    AutoCompleteTextView vehicleEvaluationChoiceProvinceAtv;
    @BindView(R.id.vehicleEvaluation_choiceCity_atv)
    AutoCompleteTextView vehicleEvaluationChoiceCityAtv;
    @BindView(R.id.vehicleEvaluation_choiceCar_tv)
    TextView vehicleEvaluationChoiceCarTv;
    @BindView(R.id.vehicle_price_edi)
    EditText vehiclePriceEdi;
    @BindView(R.id.vehicle_date_edi)
    TextView vehicleDateEdi;
    @BindView(R.id.vehicle_mileage_edi)
    EditText vehicleMileageEdi;
    @BindView(R.id.vehicle_carId_edi)
    EditText vehicleCarIdEdi;
    @BindView(R.id.commit)
    Button commit;
    ShowDialogAndLoading showDialogAndLoading;
    @BindView(R.id.vehicle_purpose_atv)
    AutoCompleteTextView vehiclePurposeAtv;
    @BindView(R.id.vehicle_carstatus_atv)
    AutoCompleteTextView vehicleCarstatusAtv;

    VehicleEvaluationInformation vehicleEvaluationInformation;
    MaterialDialog mDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_evaluation, container, false);
        unbind = ButterKnife.bind(this, view);

        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        vehicleEvaluationInformation = new VehicleEvaluationInformation();
        showDialogAndLoading = ShowDialogAndLoading.Show.showDialogAndLoading;
        //联系方式默认是注册
        vehiclePhoneEdi.setText(realm.where(User.class).findFirst().getUserPhone());
        //初始化城市数据
        loadData();

        //设置车辆用途下拉框
        setATVData(vehiclePurposeAtv, mContext.getResources().getStringArray(R.array.vehicle_purpose));
        //设置车况下拉框
        setATVData(vehicleCarstatusAtv,  mContext.getResources().getStringArray(R.array.vehicle_carstatus));
        vehicleCarstatusAtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:  vehicleEvaluationInformation.setCarstatus(1);break;
                    case 1:  vehicleEvaluationInformation.setCarstatus(2);break;
                    case 2:  vehicleEvaluationInformation.setCarstatus(3);break;
                }
            }
        });
        vehiclePurposeAtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:  vehicleEvaluationInformation.setPurpose(1);break;
                    case 1:  vehicleEvaluationInformation.setPurpose(2);break;
                    case 2:  vehicleEvaluationInformation.setPurpose(3);break;
                }
            }
        });
        vehicleEvaluationChoiceProvinceAtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provinceId = provinceList.get(position).getProID();
//                vehicleEvaluationInformation.setProvinceId(provinceList.get(position).getProID());
                cityList = provinceList.get(position).getCitys();
                cityNameStr = new String[cityList.size()];
                for (int i = 0; i < cityList.size(); i++) {
                    Log.e("Daniel", "市名：" + cityList.get(i).getCityName());
                    cityNameStr[i] = cityList.get(i).getCityName();
                }
                    vehicleEvaluationChoiceCityAtv.setAdapter(null);
                    //初始化市
                    vehicleEvaluationChoiceCityAtv.setVisibility(View.VISIBLE);
                    vehicleEvaluationChoiceCityAtv.setText("");
                //设置市下拉框
                setATVData(vehicleEvaluationChoiceCityAtv, cityNameStr);
            }
        });
        vehicleEvaluationChoiceCityAtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityId = cityList.get(position).getCityID();
//                vehicleEvaluationInformation.setCityId(cityList.get(position).getCityID());
            }
        });
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {
            }
            @Override
            public void showAfter() {
                showCustViewDialog();
//                Toast.makeText(mContext, "退出", Toast.LENGTH_SHORT).show();
            }
        });
        iniEdi();
        return view;
    }
    TextView vehiclePrice_left;
    TextView vehiclePrice_med;
    TextView vehiclePrice_right;
    TextView dialogMsg;
    private void showCustViewDialog() {
        mDialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.vehicle_submitsuccess_dialog, false).show();
        View view = mDialog.getCustomView();
        vehiclePrice_left = (TextView) view.findViewById(R.id.price1);
        vehiclePrice_med = (TextView) view.findViewById(R.id.price2);
        vehiclePrice_right = (TextView) view.findViewById(R.id.price3);
        dialogMsg = (TextView) view.findViewById(R.id.dialogMsg_tv);
        vehiclePrice_left.setText(priceRange[0]);
        vehiclePrice_med.setText(priceRange[1]);
        vehiclePrice_right.setText(priceRange[2]);
        String price = priceRange[0];
        double loadPrice = Double.parseDouble(price);
        int minPrice = (int) (loadPrice*0.7 );
        dialogMsg.setText("你可贷款的金额为："+minPrice+"~"+(int)loadPrice+"万");

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //关闭
                getActivity().onBackPressed();
            }
        });

    }


    public void iniEdi() {
        Log.e(Constan.LOGTAGNAME, "提交控制开始：" );
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(vehiclePhoneEdi).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(vehicleNameEdi).skip(1);
        Observable<CharSequence> CharSequence3 = RxTextView.textChanges(vehiclePriceEdi).skip(1);
        Observable<CharSequence> CharSequence4 = RxTextView.textChanges(vehicleEvaluationChoiceCarTv).skip(1);
        Observable<CharSequence> CharSequence5 = RxTextView.textChanges(vehiclePriceEdi).skip(1);
        Observable<CharSequence> CharSequence6 = RxTextView.textChanges(vehicleMileageEdi).skip(1);
        Observable<CharSequence> CharSequence7 = RxTextView.textChanges(vehicleCarIdEdi).skip(1);
        Observable<CharSequence> CharSequence8 = RxTextView.textChanges(vehiclePurposeAtv).skip(1);
        Observable<CharSequence> CharSequence9 = RxTextView.textChanges(vehicleCarstatusAtv).skip(1);
        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence2, CharSequence3, CharSequence4,
                CharSequence5, CharSequence6,CharSequence7,CharSequence8,CharSequence9,
                new Func9<CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, CharSequence,CharSequence,CharSequence,
                                        CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence1, CharSequence charSequence2, CharSequence charSequence3, CharSequence charSequence4, CharSequence charSequence5, CharSequence charSequence6, CharSequence charSequence7, CharSequence charSequence8, CharSequence charSequence9) {
                boolean Bl = !TextUtils.isEmpty(charSequence1);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                boolean B3 = !TextUtils.isEmpty(charSequence3);
                boolean B4 = !TextUtils.isEmpty(charSequence4);
                boolean B5 = !TextUtils.isEmpty(charSequence5);
                boolean B6 = !TextUtils.isEmpty(charSequence6);
                boolean B7 = !TextUtils.isEmpty(charSequence7);
                boolean B8 = !TextUtils.isEmpty(charSequence8);
                boolean B9 = !TextUtils.isEmpty(charSequence9);
                return Bl && B2 && B3 && B4 && B5 && B6 && B7&& B8&& B9;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.e(Constan.LOGTAGNAME, "提交控制异常：" + e.toString());
            }
            @DebugLog
            @Override
            public void onNext(Boolean aBoolean) {
                Log.e(Constan.LOGTAGNAME, "提交控制：" + aBoolean);
                commit.setEnabled(aBoolean);
            }
        });
        compositeSubscription.add(etSc);
    }

    private void loadData() {
        try {
            InputStream inputStream = getActivity().getApplication().getAssets().open("province.json");
            Log.e("Daniel", "inputStream：" + inputStream);
            provinceCityBean = new GsonBuilder().create().fromJson(new InputStreamReader(inputStream), ProvinceCityBean.class);
            Log.e("Daniel", "provinceCityBean：" + provinceCityBean);
            Log.e("Daniel", "城市条数：" + provinceCityBean.getProvince().size());
            provinceList = provinceCityBean.getProvince();
            Log.e("Daniel", "省份list：" +provinceList);
            provinceNameStr = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                provinceNameStr[i] = provinceList.get(i).getProName();
            }
            //设置省下拉框
            Constan.log("provinceNameStr:"+provinceNameStr);
            setATVData(vehicleEvaluationChoiceProvinceAtv, provinceNameStr);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setATVData(final AutoCompleteTextView atv, String[] mStrArray) {
        Log.e("gqf", "下拉框mStrArray：" + mStrArray.length);
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_item, mStrArray);
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

    Unbinder unbind;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(Constan.LOGTAGNAME, "----onResume--");
//        iniEdi();
        if (chexingListBean != null) {
            Log.e(Constan.LOGTAGNAME, "------" + chexingListBean);
            vehicleEvaluationChoiceCarTv.setText(chexingListBean.getCxname());
        }
        Log.e(Constan.LOGTAGNAME, "----onResume-getCityId-"+vehicleEvaluationInformation.getCityId());
        if (vehicleEvaluationInformation.getCityId()!=null){
            vehicleEvaluationChoiceCityAtv.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.vehicleEvaluation_choiceCar_tv, R.id.commit, R.id.vehicle_date_edi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.vehicleEvaluation_choiceCar_tv:
                //切换到品牌选择
                EventBus.getDefault().post("ToVehicleChioceFragment");
                break;
            case R.id.commit:
                if(ContextCompat.checkSelfPermission(
                        mContext, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    //当前Activity没有获得READ_CONTACTS权限时
                    Log.e(Constan.LOGTAGNAME,"Activity没有获得READ_CONTACTS权限");
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            Constan.REQUEST_CODE_PERMISSION_CONTACTS);
                }else {
                    showDialogAndLoading.showLoading("正在提交车辆信息！", getActivity());
                    int userId = realm.where(User.class).findFirst().getUserId();
                    List<HashMap<String, String>> list= PhoneUtils.getPhoneContacts(
                            getActivity());
                    List<Contact> contacts = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        Contact contact = new Contact();
                        contact.setContact(list.get(i).get("name"));
                        contact.setContactNumber(list.get(i).get("phone"));
                        contact.setUserId(userId);
                        Log.e(Constan.LOGTAGNAME, "联系人："+contact.toString());
                       contacts.add(contact);
                         Constan.mContacts=contacts;
                    }
                    setVehicleInformation();
                }

                break;
            case R.id.vehicle_date_edi:
                onYearMonthPicker();
                break;
        }
    }
    public void onYearMonthPicker() {
        Calendar c = Calendar.getInstance();
        Integer year = c.get(Calendar.YEAR);
        Integer month = c.get(Calendar.MONTH)+1;
        Integer day = c.get(Calendar.DAY_OF_MONTH);
        Log.e(Constan.LOGTAGNAME,"-year--month---day-"+year+"--"+month+"--"+day);
        DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH);
        picker.setGravity(Gravity.CENTER);
        picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));
        picker.setRangeStart(1990, 1, 1);
        picker.setRangeEnd(year, month, day);
        picker.setSelectedItem(year, month);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                vehicleEvaluationInformation.setUseddateMonth(month);
                vehicleEvaluationInformation.setUseddate(Integer.valueOf(year));
                vehicleDateEdi.setText(year+"-"+month);
//                Vehicle_month = month;
//                Vehicle_year = Integer.valueOf(year);

            }
        });
        picker.show();
    }

    String[] priceRange;
    private void setVehicleInformation() {
//        VehicleEvaluationInformation vehicleEvaluationInformation = new VehicleEvaluationInformation();
        vehicleEvaluationInformation.setPlateNumber(vehicleCarIdEdi.getText().toString());
        vehicleEvaluationInformation.setUserId(realm.where(User.class).findFirst().getUserId());
        vehicleEvaluationInformation.setContact(vehiclePhoneEdi.getText().toString());
        vehicleEvaluationInformation.setProposer(vehicleNameEdi.getText().toString());
        vehicleEvaluationInformation.setCarId(chexingListBean.getId());
        vehicleEvaluationInformation.setPrice(vehiclePriceEdi.getText().toString());
        vehicleEvaluationInformation.setProvinceId(provinceId);
        Log.e(Constan.LOGTAGNAME, "城市信息：" + cityId);
        vehicleEvaluationInformation.setCityId(cityId);
        vehicleEvaluationInformation.setMileage(vehicleMileageEdi.getText().toString());
        vehicleEvaluationInformation.setCarName(vehicleEvaluationChoiceCarTv.getText().toString());
        Log.e(Constan.LOGTAGNAME, "通讯录：" + Constan.mContacts);
        vehicleEvaluationInformation.setAddressBook(Constan.mContacts);
        String str = new Gson().toJson(vehicleEvaluationInformation);
        Log.e(Constan.LOGTAGNAME, "车辆评估提交信息：" + str);
        saveVehicleInformation(str);
    }

    private void saveVehicleInformation(String str) {
        Subscription subscription = NetWork.getUserService().saveVehicleInformation(str).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VehicleSubmitResultCode>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.e(Constan.LOGTAGNAME, "车辆评估提交信息异常：" + e.toString());
                showDialogAndLoading.stopLoaading();
            }
            @Override
            public void onNext(VehicleSubmitResultCode vehicleSubmitResultCode) {
                showDialogAndLoading.stopLoaading();
                Log.e(Constan.LOGTAGNAME, "车辆评估提交信息结果：" + vehicleSubmitResultCode.toString());
                if (vehicleSubmitResultCode.getCode() == 10001) {
                    showDialogAndLoading.showAfterDialog(getActivity(), "", "保存成功！", "确定");

                    Log.e(Constan.LOGTAGNAME, "车辆评估价格区间：" + vehicleSubmitResultCode.getEst_price_result().toString());
                    priceRange = vehicleSubmitResultCode.getEst_price_result().toString().split(",");
                } else if (vehicleSubmitResultCode.getCode() == 20001){
                    showDialogAndLoading.showFailureDialog(getActivity(),"","信息格式有误！");
                }else if (vehicleSubmitResultCode.getCode() == 10000){
                    showDialogAndLoading.showFailureDialog(getActivity(),"","评估失败！");
                }else {
                    showDialogAndLoading.showWarningDialog(getActivity(),"","此车已在系统提交过评估信息！");
                }
            }
        });
        compositeSubscription.add(subscription);
    }
}
