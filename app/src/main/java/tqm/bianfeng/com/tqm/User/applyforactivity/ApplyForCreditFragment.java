package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.StringUtils;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.PhotoGet;
import tqm.bianfeng.com.tqm.Util.ReadJson;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwApplyEnter;
import tqm.bianfeng.com.tqm.pojo.YwRzsq;
import tqm.bianfeng.com.tqm.pojo.address.address_model;
import tqm.bianfeng.com.tqm.pojo.bank.Institution;
import tqm.bianfeng.com.tqm.pojo.result.ResultCodeWithImgPathList;

/**
 * Created by johe on 2017/5/12.
 */
//信贷申请
public class ApplyForCreditFragment extends BaseFragment {


    private static final int REQUEST_IMAGE = 2;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    private static final int REQUEST_CREDIT_IMAGE = 4;
    int selectRadio = 0;


    boolean isAddLogo = false;
    boolean isAddCompanyImg = false;
    boolean isAddPersonalImg = false;

    List<address_model> addressModels;
    String selectProvinces = "";
    String selectCity = "";
    String selectCounty = "";
    ReadJson rj;


    YwRzsq ywApplyEnter;
    YwApplyEnter oldYwApplyEnter;
    @BindView(R.id.company_user_name_edi)
    EditText companyUserNameEdi;

    @BindView(R.id.company_phone_num_edi)
    EditText companyPhoneNumEdi;
    @BindView(R.id.select_address_provinces)
    AutoCompleteTextView selectAddressProvinces;
    @BindView(R.id.select_address_city)
    AutoCompleteTextView selectAddressCity;
    @BindView(R.id.select_address_County)
    AutoCompleteTextView selectAddressCounty;
    @BindView(R.id.upload_personal_img_txt)
    TextView uploadPersonalImgTxt;
    @BindView(R.id.personal_img1)
    ImageView personalImg1;
    @BindView(R.id.personal_img2)
    ImageView personalImg2;
    @BindView(R.id.add_personal_img_img)
    ImageView addPersonalImgImg;
    @BindView(R.id.company_institution_atv)
    AutoCompleteTextView companyInstitutionAtv;



    public interface mListener {
        public void setCommitBtn(boolean is);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_for_credit, container, false);
        ButterKnife.bind(this, view);
        companyPhoneNumEdi.setText(realm.where(User.class).findFirst().getUserPhone());
        mInstitutionStrings = new ArrayList<>();
        mInstitutions = new ArrayList<>();
        initData();

        return view;
    }

    List<String> mInstitutionStrings;
    List<Institution> mInstitutions;

    private void initData() {
        NetWork.getBankService().getInstitutions().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Institution>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Daniel", "机构异常" + e);
            }

            @DebugLog
            @Override
            public void onNext(List<Institution> institutions) {
                Log.e("Daniel", "机构" + institutions.toString());
                mInstitutions = institutions;
                for (Institution institution : institutions) {
                    mInstitutionStrings.add(institution.getInstitutionName());
                }
                initIEdi();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        iniEdi();
        initaddressModels();
        photoGet = PhotoGet.getInstance();
        photoGet.setContext(getActivity());

        mPersonalImgSelectPath = new ArrayList<>();
        ywApplyEnter = new YwRzsq();

        personalImgsView = new ArrayList<>();
        personalImgsView.add(personalImg1);
        personalImgsView.add(personalImg2);

    }

    /**
     * 初始化选择机构
     */
    private void initIEdi() {
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_item, mInstitutionStrings);
        companyInstitutionAtv.setAdapter(autoadapter);
        companyInstitutionAtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击显示
                companyInstitutionAtv.showDropDown();
            }
        });
        companyInstitutionAtv.setInputType(InputType.TYPE_NULL);
        companyInstitutionAtv.setKeyListener(null);
        companyInstitutionAtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //                Toast.makeText(getActivity(), mInstitutionStrings.get(position)+"", Toast.LENGTH_SHORT).show();
                ywApplyEnter.setInstitutionId(mInstitutions.get(position).getInstitutionId());
            }
        });
    }


    public void addImg() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {

            MultiImageSelector multiImageSelector = MultiImageSelector.create(getActivity()).showCamera(true) // show camera or not. true by default
                    // max select image size, 9 by default. used width #.multi()
                    .single() // single mode
                    .multi(); // original select data set, used width #.multi()
            multiImageSelector.origin(mPersonalImgSelectPath);
            multiImageSelector.count(2);

            multiImageSelector.start(getActivity(), REQUEST_CREDIT_IMAGE);
        }
    }


    List<ImageView> personalImgsView;
    ArrayList<String> mPersonalImgSelectPath;
    PhotoGet photoGet;
    List<String> uploadLogoImgPath;

    public void setImgInView(Intent data) {

        mPersonalImgSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
        for (int i = 0; i < personalImgsView.size(); i++) {
            personalImgsView.get(i).setImageBitmap(null);
            personalImgsView.get(i).setVisibility(View.GONE);
        }
        for (int i = 0; i < mPersonalImgSelectPath.size(); i++) {
            personalImgsView.get(i).setImageBitmap(BitmapFactory.decodeFile(mPersonalImgSelectPath.get(i)));
            personalImgsView.get(i).setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.add_personal_img_img, R.id.upload_personal_img_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_personal_img_txt:
                //上传个人图片，上传后改为已上传，上传中
                if (mPersonalImgSelectPath.size() == 0) {
                    Toast.makeText(getActivity(), "请先添加图片再上传", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImg(mPersonalImgSelectPath);
                }

                break;
            case R.id.add_personal_img_img:
                //添加个人图片
                addImg();
                break;

        }
    }

    public void iniEdi() {
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(companyInstitutionAtv).skip(1);
        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(companyUserNameEdi).skip(1);
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
                mListener.setCommitBtn(aBoolean);
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

    /**
     * 上传图片
     *
     * @param imgPaths
     */
    public void uploadImg(List<String> imgPaths) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < imgPaths.size(); i++) {
            File f = new File(imgPaths.get(i));
            if (f != null) {
                Log.i("gqf", "File" + i);
                if (f.exists()) {
                    RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), f);
                    builder.addFormDataPart("zichifile" + i, f.getName(), photoRequestBody);
                }
            }
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody mb = builder.build();

        uploadPersonalImgTxt.setText("上传中");
        uploadPersonalImgTxt.setEnabled(false);

        List<MultipartBody.Part> zichifile = new ArrayList<>();
        for (int i = 0; i < mb.size(); i++) {
            zichifile.add(mb.part(i));
        }


        Subscription subscription = NetWork.getUserService().uploadCompanyFile(zichifile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResultCodeWithImgPathList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                uploadPersonalImgTxt.setText("上传");
                uploadPersonalImgTxt.setEnabled(true);
                Log.i("gqf", "Throwable" + e.toString());
                Toast.makeText(getActivity(), "图片尺寸过大", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResultCodeWithImgPathList strings) {
                Log.i("gqf", "onNext" + strings.toString());
                if (strings.getCode() == ResultCode.SECCESS) {
                    uploadPersonalImgTxt.setText("已上传");
                    String paths = "";
                    for (int i = 0; i < strings.getFiles().size(); i++) {
                        if (i > 0) {
                            paths = paths + ",";
                        }
                        paths = paths + strings.getFiles().get(i);
                    }
                    ywApplyEnter.setGrmp(paths);
                    uploadPersonalImgTxt.setEnabled(false);
                    addPersonalImgImg.setEnabled(false);

                    Log.i("gqf", "paths" + paths);
                } else {
                    Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
                    uploadPersonalImgTxt.setText("上传");
                    uploadPersonalImgTxt.setEnabled(true);
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
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_item, user_num);
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
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_item, user_num);
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

    /**
     * 初始化区
     */
    String[] countys;

    public void initCoEdi(ArrayList<String> areas) {
        countys = new String[areas.size()];
        for (int i = 0; i < areas.size(); i++) {
            countys[i] = areas.get(i);
        }
        ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_item, countys);
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

    /**
     * 提交申请返回对象
     *
     * @return
     */
    public YwRzsq getYwApplyEnter() {
        if (StringUtils.isEmpty(companyUserNameEdi.getText().toString())) {
            Toast.makeText(getActivity(), "请填写姓名！", Toast.LENGTH_SHORT).show();
            return null;
        } else if (StringUtils.isEmpty(companyPhoneNumEdi.getText().toString())) {
            Toast.makeText(getActivity(), "请填写联系电话！", Toast.LENGTH_SHORT).show();
            return null;
        }else if(selectProvinces.equals("")||selectCity.equals("")){
            Toast.makeText(getActivity(),"请选择公司所在地址！",Toast.LENGTH_SHORT).show();
            return null;
        } else if (ywApplyEnter.getGrmp() == null || ywApplyEnter.getGrmp().equals("")) {
            Toast.makeText(getActivity(), "请上传logo!", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            ywApplyEnter.setProvince(selectProvinces);
            ywApplyEnter.setCity(selectCity);
            ywApplyEnter.setCounty(selectCounty);
            //            ywApplyEnter.setAddress(selectProvinces+selectCity+selectCounty);
            ywApplyEnter.setLxr(companyUserNameEdi.getText().toString());
            ywApplyEnter.setLxdh(companyPhoneNumEdi.getText().toString());
            ywApplyEnter.setSqlx("03");//03-信贷经理
            ywApplyEnter.setLxbq("3001");
            Log.e("Daniel", "联系人：" + ywApplyEnter.getLxr());
            Log.e("Daniel", "联系电话：" + ywApplyEnter.getLxdh());
            Log.e("Daniel", "所在机构：" + ywApplyEnter.getInstitutionId());
            return ywApplyEnter;
        }

    }

    public void setYwApplyEnter(YwRzsq data) {
        ywApplyEnter = data;
        ywApplyEnter.setShzt("00");
        companyPhoneNumEdi.setText(ywApplyEnter.getLxdh());
        Log.e("Daniel", "信贷联系人：" + ywApplyEnter.getLxr());
        companyUserNameEdi.setText(ywApplyEnter.getLxr());
        ywApplyEnter.setGrmp("");


    }
}
