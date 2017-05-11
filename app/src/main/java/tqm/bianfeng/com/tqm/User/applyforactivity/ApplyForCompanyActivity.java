package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hugo.weaving.DebugLog;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.ReadJson;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.YwApplyEnter;
import tqm.bianfeng.com.tqm.pojo.address.address_model;
import tqm.bianfeng.com.tqm.pojo.result.ResultCodeWithImgPathList;

/**
 * Created by johe on 2017/5/10.
 */

public class ApplyForCompanyActivity extends BaseActivity {

    private static final int REQUEST_IMAGE = 2;
    @BindView(R.id.apply_for_company_toolbar)
    Toolbar applyForCompanyToolbar;
    @BindView(R.id.private_capital_radio)
    RadioButton privateCapitalRadio;
    @BindView(R.id.mediation_radio)
    RadioButton mediationRadio;
    int selectRadio=0;

    @BindView(R.id.company_name_edi)
    EditText companyNameEdi;
    @BindView(R.id.company_user_name_edi)
    EditText companyUserNameEdi;
    @BindView(R.id.company_phone_num_edi)
    EditText companyPhoneNumEdi;
    @BindView(R.id.company_img1)
    ImageView companyImg1;
    @BindView(R.id.company_img2)
    ImageView companyImg2;
    @BindView(R.id.company_img3)
    ImageView companyImg3;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.logo_img1)
    ImageView logoImg1;

    boolean isAddLogo = false;
    boolean isAddCompanyImg = false;
    boolean isAddPersonalImg = false;
    @BindView(R.id.upload_logo_img_txt)
    TextView uploadLogoImgTxt;
    @BindView(R.id.add_logo_img_img)
    ImageView addLogoImgImg;
    @BindView(R.id.upload_company_img_txt)
    TextView uploadCompanyImgTxt;
    @BindView(R.id.add_company_img_img)
    ImageView addCompanyImgImg;
    @BindView(R.id.upload_personal_img_txt)
    TextView uploadPersonalImgTxt;
    @BindView(R.id.personal_img1)
    ImageView personalImg1;
    @BindView(R.id.personal_img2)
    ImageView personalImg2;
    @BindView(R.id.add_personal_img_img)
    ImageView addPersonalImgImg;
    List<address_model> addressModels;
    String selectProvinces = "";
    String selectCity = "";
    String selectCounty = "";
    ReadJson rj;
    @BindView(R.id.select_address_provinces)
    AutoCompleteTextView selectAddressProvinces;
    @BindView(R.id.select_address_city)
    AutoCompleteTextView selectAddressCity;
    @BindView(R.id.select_address_County)
    AutoCompleteTextView selectAddressCounty;

    YwApplyEnter ywApplyEnter;
    List<TextView> uploadTxts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_company);
        ButterKnife.bind(this);
        setToolbar(applyForCompanyToolbar, "公司申请");
        init();
    }
    public void init(){
        privateCapitalRadio.setChecked(true);
        iniEdi();
        initaddressModels();
        mCompanyImgSelectPath=new ArrayList<>();
        mLogoSelectPath=new ArrayList<>();
        mPersonalImgSelectPath=new ArrayList<>();
        ywApplyEnter=new YwApplyEnter();
        companyImgsView = new ArrayList<>();
        companyImgsView.add(companyImg1);
        companyImgsView.add(companyImg2);
        companyImgsView.add(companyImg3);
        personalImgsView = new ArrayList<>();
        personalImgsView.add(personalImg1);
        personalImgsView.add(personalImg2);

        uploadTxts=new ArrayList<>();
        uploadTxts.add(uploadLogoImgTxt);
        uploadTxts.add(uploadCompanyImgTxt);
        uploadTxts.add(uploadPersonalImgTxt);
    }


    public void addImg() {
        MultiImageSelector multiImageSelector = MultiImageSelector.create(this)
                .showCamera(true) // show camera or not. true by default
                // max select image size, 9 by default. used width #.multi()
                .single() // single mode
                .multi(); // original select data set, used width #.multi()
        if (isAddCompanyImg) {
            multiImageSelector.origin(mCompanyImgSelectPath);
            multiImageSelector.count(3);
        } else if (isAddLogo) {
            multiImageSelector.origin(mLogoSelectPath);
            multiImageSelector.count(1);
        } else if (isAddPersonalImg) {
            multiImageSelector.origin(mPersonalImgSelectPath);
            multiImageSelector.count(2);
        }
        multiImageSelector.start(ApplyForCompanyActivity.this, REQUEST_IMAGE);
    }

    List<ImageView> companyImgsView;
    List<ImageView> personalImgsView;
    ArrayList<String> mCompanyImgSelectPath;
    ArrayList<String> mPersonalImgSelectPath;
    ArrayList<String> mLogoSelectPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                setImgInView(data);
            }
        }
    }

    public void setImgInView(Intent data) {
        if (isAddCompanyImg) {
            mCompanyImgSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            for (int i = 0; i < companyImgsView.size(); i++) {
                companyImgsView.get(i).setImageBitmap(null);
                companyImgsView.get(i).setVisibility(View.GONE);
            }
            for (int i = 0; i < mCompanyImgSelectPath.size(); i++) {
                companyImgsView.get(i).setImageBitmap(BitmapFactory.decodeFile(mCompanyImgSelectPath.get(i)));
                companyImgsView.get(i).setVisibility(View.VISIBLE);
            }
            isAddCompanyImg = false;
        } else if (isAddLogo) {
            mLogoSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            logoImg1.setImageBitmap(null);
            logoImg1.setVisibility(View.GONE);
            for (int i = 0; i < mLogoSelectPath.size(); i++) {
                logoImg1.setImageBitmap(BitmapFactory.decodeFile(mLogoSelectPath.get(i)));
                logoImg1.setVisibility(View.VISIBLE);
            }
            isAddLogo = false;
        } else if (isAddPersonalImg) {
            mPersonalImgSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            for (int i = 0; i < personalImgsView.size(); i++) {
                personalImgsView.get(i).setImageBitmap(null);
                personalImgsView.get(i).setVisibility(View.GONE);
            }
            for (int i = 0; i < mPersonalImgSelectPath.size(); i++) {
                personalImgsView.get(i).setImageBitmap(BitmapFactory.decodeFile(mPersonalImgSelectPath.get(i)));
                personalImgsView.get(i).setVisibility(View.VISIBLE);
            }
            isAddPersonalImg = false;
        }
    }

    @OnClick({R.id.private_capital_radio,R.id.mediation_radio,R.id.add_personal_img_img, R.id.upload_personal_img_txt, R.id.upload_logo_img_txt, R.id.add_logo_img_img, R.id.upload_company_img_txt, R.id.add_company_img_img, R.id.commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_personal_img_txt:
                //上传个人图片，上传后改为已上传，上传中

                if(mPersonalImgSelectPath.size()==0){
                    Toast.makeText(getApplicationContext(),"请先添加图片再上传",Toast.LENGTH_SHORT).show();
                }else{
                    uploadImg(3,mPersonalImgSelectPath);
                }

                break;
            case R.id.private_capital_radio:
                selectRadio=0;
                break;
            case R.id.mediation_radio:
                selectRadio=1;
                break;
            case R.id.add_personal_img_img:
                //添加个人图片
                isAddPersonalImg = true;
                addImg();
                break;
            case R.id.upload_logo_img_txt:
                //上传logo图片，上传后改为已上传，上传中
                if(mLogoSelectPath.size()==0){
                    Toast.makeText(getApplicationContext(),"请先添加图片再上传",Toast.LENGTH_SHORT).show();
                }else{
                    uploadImg(1,mLogoSelectPath);
                }
                break;
            case R.id.add_logo_img_img:
                //添加logo图片
                isAddLogo = true;
                addImg();
                break;
            case R.id.upload_company_img_txt:
                //上传公司图片，上传后改为已上传，上传中
                if(mCompanyImgSelectPath.size()==0){
                    Toast.makeText(getApplicationContext(),"请先添加图片再上传",Toast.LENGTH_SHORT).show();
                }else{
                    uploadImg(2,mCompanyImgSelectPath);
                }
                break;
            case R.id.add_company_img_img:
                //添加公司图片
                isAddCompanyImg = true;
                addImg();
                break;
            case R.id.commit:
                //提交
                if(selectProvinces.equals("")||selectCity.equals("")||selectCounty.equals("")){
                    Toast.makeText(getApplicationContext(),"请选择公司所在地址",Toast.LENGTH_SHORT).show();
                }else if(ywApplyEnter.getCompanyLogo()==null||ywApplyEnter.getCompanyLogo().equals("")){
                    Toast.makeText(getApplicationContext(),"请上传logo",Toast.LENGTH_SHORT).show();
                }else{
                    initDialog(1);

                }

                break;
        }
    }
    public void iniEdi(){
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(companyNameEdi).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(companyPhoneNumEdi).skip(1);
        Observable<CharSequence> CharSequence3 = RxTextView.textChanges(companyUserNameEdi).skip(1);

        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence2, CharSequence3,  new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                boolean B3 = !TextUtils.isEmpty(charSequence3);
                return Bl && B2 && B3 ;
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
                Log.e("Daniel", "---aBoolean---" + aBoolean);
                commit.setEnabled(aBoolean);
            }
        });
        compositeSubscription.add(etSc);
    }

    public void initaddressModels() {
        addressModels = new ArrayList<>();
        String json = null;
        json = getString(R.string.p_c_c_json);
        rj = ReadJson.getInstance();
        addressModels = rj.getAddressModel(json);
        initPEdi();
    }
    //上传图片1.logo 2.公司 3.个人
    public void uploadImg(final int index,List<String> imgPaths){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < imgPaths.size(); i++) {
            File f = new File(imgPaths.get(i));
            if (f != null) {
                Log.i("gqf", "File" + i);
                if (f.exists()) {
                    RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), f);
                    builder.addFormDataPart("zichifile", f.getName(), photoRequestBody);
                }
            }
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody mb = builder.build();

        uploadTxts.get(index-1).setText("上传中");
        uploadTxts.get(index-1).setEnabled(false);

        List<MultipartBody.Part> zichifile = new ArrayList<>();
        for (int i = 0; i < mb.size(); i++) {
            zichifile.add(mb.part(i));
        }



        Subscription subscription = NetWork.getUserService().uploadCompanyFile(zichifile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCodeWithImgPathList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        uploadTxts.get(index-1).setText("上传");
                        uploadTxts.get(index-1).setEnabled(true);
                        Log.i("gqf","Throwable"+e.toString());
                        Toast.makeText(getApplicationContext(),"图片尺寸过大",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResultCodeWithImgPathList strings) {
                        Log.i("gqf","onNext"+strings.toString());
                        if(strings.getCode()== ResultCode.SECCESS){
                            uploadTxts.get(index-1).setText("已上传");
                            String paths="";
                            for(int i=0;i<strings.getFiles().size();i++){
                                if(i>0){
                                    paths=paths+",";
                                }
                                paths=paths+strings.getFiles().get(i);
                            }
                            if(index==1){
                                ywApplyEnter.setCompanyLogo(paths);
                            }else if(index==2){
                                ywApplyEnter.setCompanyImage(paths);
                            }else{
                                ywApplyEnter.setCompanyImageOther(paths);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_SHORT).show();
                            uploadTxts.get(index-1).setText("上传");
                            uploadTxts.get(index-1).setEnabled(true);
                        }

                    }
                });
        compositeSubscription.add(subscription);

    }

    //初始化省
    public void initPEdi() {
        String[] user_num;
        Log.i("gqf", "addressModels" + addressModels.size());
        user_num = new String[addressModels.size()];
        for (int i = 0; i < addressModels.size(); i++) {
            user_num[i] = addressModels.get(i).getP();
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, user_num);
        selectAddressProvinces.setAdapter(autoadapter);
        selectAddressProvinces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectAddressProvinces.showDropDown();
            }
        });
        selectAddressProvinces.setInputType(InputType.TYPE_NULL);
        selectAddressProvinces.setKeyListener(null);
        selectAddressProvinces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectProvinces = addressModels.get(position).getP();
                selectAddressCity.setAdapter(null);
                initCEdi(addressModels.get(position));
                selectAddressCity.setVisibility(View.VISIBLE);
                selectAddressCounty.setVisibility(View.INVISIBLE);
                selectAddressCity.setText("");
            }
        });
    }

    public void initCEdi(final address_model am) {
        String[] user_num;
        user_num = new String[am.getCity().size()];
        for (int i = 0; i < am.getCity().size(); i++) {
            user_num[i] = am.getCity().get(i).getName();
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, user_num);
        selectAddressCity.setAdapter(autoadapter);
        selectAddressCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectAddressCity.showDropDown();
            }
        });
        selectAddressCity.setInputType(InputType.TYPE_NULL);
        selectAddressCity.setKeyListener(null);
        selectAddressCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCity = am.getCity().get(position).getName();
                selectAddressCounty.setAdapter(null);
                initCoEdi(am.getCity().get(position).getHave());
                selectAddressCounty.setVisibility(View.VISIBLE);
                selectAddressCounty.setText("");
            }
        });

    }

    String[] countys;

    public void initCoEdi(ArrayList<String> areas) {

        countys = new String[areas.size()];
        for (int i = 0; i < areas.size(); i++) {
            countys[i] = areas.get(i);
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_dropdown_item, countys);
        selectAddressCounty.setAdapter(autoadapter);
        selectAddressCounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                selectAddressCounty.showDropDown();
            }
        });
        selectAddressCounty.setInputType(InputType.TYPE_NULL);
        selectAddressCounty.setKeyListener(null);
        selectAddressCounty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCounty = countys[position];

            }
        });

    }
    int i = 6;//apk加载进度
    SweetAlertDialog pDialog;
    CountDownTimer countDownTimer;
    public void save(){
        initDialog(2);
        Gson gson=new Gson();
        ywApplyEnter.setAddress(selectProvinces+selectCity+selectCounty);
        ywApplyEnter.setApplyName(companyNameEdi.getText().toString());
        ywApplyEnter.setProposer(companyUserNameEdi.getText().toString());
        ywApplyEnter.setContact(companyPhoneNumEdi.getText().toString());
        if(selectRadio==0){
            ywApplyEnter.setApplyType("1001");
        }else{
            ywApplyEnter.setApplyType("1002");
        }
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
                        if(resultCode.getCode()==ResultCode.SECCESS){
                            initDialog(3);
                        }else{
                            showLoading(1);
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
                        }
                    })
                    .show();
        }else if(index==2){
            showLoading(0);
        }else{
            showLoading(1);
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("提交成功")
                    .setContentText("我们将在两个工作日内联系您，请保持电话畅通。")
                    .setConfirmText("确定")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            onBackPressed();
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
}
