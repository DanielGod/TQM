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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwRzsq;
import tqm.bianfeng.com.tqm.pojo.result.ResultCodeWithImgPathList;

/**
 * Created by johe on 2017/5/12.
 */
//个人申请
public class ApplyForPersonalFragment extends BaseFragment {
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    private static final int REQUEST_IMAGE = 2;
    private static final int REQUEST_PERSION_IMAGE = 3;
    YwRzsq ywApplyEnter;
    @BindView(R.id.private_capital_radio)
    RadioButton privateCapitalRadio;
    @BindView(R.id.mediation_radio)
    RadioButton mediationRadio;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.phone_num_edi)
    EditText phoneNumEdi;
    //    @BindView(R.id.id_num_edi)
    //    EditText idNumEdi;
    @BindView(R.id.upload_personal_img_txt)
    TextView uploadPersonalImgTxt;
    int selectRadio = 0;
    @BindView(R.id.user_first_name)
    EditText userFirstName;
    @BindView(R.id.add_personal_img_img)
    ImageView addPersonalImgImg;

    ArrayList<String> mPersonalImgSelectPath;
    @BindView(R.id.personal_img1)
    ImageView personalImg1;
    @BindView(R.id.personal_img2)
    ImageView personalImg2;

    @OnClick({R.id.private_capital_radio, R.id.mediation_radio, R.id.add_personal_img_img, R.id.upload_personal_img_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.private_capital_radio:
                selectRadio = 0;
                break;
            case R.id.mediation_radio:
                selectRadio = 1;
                break;
            case R.id.add_personal_img_img:
                //添加个人图片
                addImg();
                break;
            case R.id.upload_personal_img_txt:
                //上传个人图片，上传后改为已上传，上传中
                if (mPersonalImgSelectPath.size() == 0) {
                    Toast.makeText(getActivity(), "请先添加图片再上传", Toast.LENGTH_SHORT).show();
                } else {

                    uploadImg(mPersonalImgSelectPath);
                }
                break;
        }
    }
    public interface mListener {
        public void setCommitBtn(boolean is);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_for_personal, container, false);
        ButterKnife.bind(this, view);
        phoneNumEdi.setText(realm.where(User.class).findFirst().getUserPhone());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ywApplyEnter = new YwRzsq();
        privateCapitalRadio.setChecked(true);
        mPersonalImgSelectPath = new ArrayList<>();
        personalImgsView = new ArrayList<>();
        personalImgsView.add(personalImg1);
        personalImgsView.add(personalImg2);
        iniEdi();
    }
    List<ImageView> personalImgsView;

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

    public void addImg() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            MultiImageSelector multiImageSelector = MultiImageSelector.create(getActivity())
                    .showCamera(true) // show camera or not. true by default
                    // max select image size, 9 by default. used width #.multi()
                    .single() // single mode
                    .multi(); // original select data set, used width #.multi()
            multiImageSelector.origin(mPersonalImgSelectPath);
            multiImageSelector.count(2);
            multiImageSelector.start(getActivity(), REQUEST_PERSION_IMAGE);
        }
    }


    //上传图片1.logo 2.公司 3.个人
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


        Subscription subscription = NetWork.getUserService().uploadCompanyFile(zichifile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCodeWithImgPathList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        uploadPersonalImgTxt.setText("上传");
                        uploadPersonalImgTxt.setEnabled(true);
                        Log.i("Daniel", "个人申请上传图片失败" + e.toString());
                        Toast.makeText(getActivity(), "图片尺寸过大", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResultCodeWithImgPathList strings) {
                        Log.i("Daniel", "个人申请上传图片成功" + strings.toString());
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
                            //                            }
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

    public void iniEdi() {
        Observable<CharSequence> CharSequence1 = RxTextView.textChanges(userName).skip(1);
        //        Observable<CharSequence> CharSequence2 = RxTextView.textChanges(idNumEdi).skip(1);
        Observable<CharSequence> CharSequence3 = RxTextView.textChanges(userFirstName).skip(1);
        Subscription etSc = Observable.combineLatest(CharSequence1, CharSequence3, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence3) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B3 = !TextUtils.isEmpty(charSequence3);
                return Bl && B3;
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

    public YwRzsq getYwApplyEnter() {
        if (StringUtils.isEmpty(phoneNumEdi.getText().toString())){
            Toast.makeText(getActivity(),"请填写联系电话！",Toast.LENGTH_SHORT).show();
            return null;
        }else if(StringUtils.isEmpty(ywApplyEnter.getGrmp())){
            Toast.makeText(getActivity(),"请上传个人名片！",Toast.LENGTH_SHORT).show();
            return null;
        }else {
            if (selectRadio == 0) {
                ywApplyEnter.setLxbq("2001");
            } else {
                ywApplyEnter.setLxbq("2002");
            }
            ywApplyEnter.setGsmc(userFirstName.getText().toString());
            ywApplyEnter.setLxr(userName.getText().toString());
            ywApplyEnter.setLxdh(phoneNumEdi.getText().toString());
            ywApplyEnter.setSqlx("02");//02-个人
            if(selectRadio==0){
                ywApplyEnter.setLxbq("2001");
            }else{
                ywApplyEnter.setLxbq("2002");
            }
            //        ywApplyEnter.setIdCard(idNumEdi.getText().toString());

        }

        return ywApplyEnter;
    }

    public void setYwApplyEnter(YwRzsq data) {
        ywApplyEnter = data;

        if (ywApplyEnter.getLxbq().equals("2001")) {
            privateCapitalRadio.setChecked(true);
        } else {
            mediationRadio.setChecked(true);
        }
        ywApplyEnter.setShzt("00");
        userFirstName.setText(ywApplyEnter.getGsmc());
        userName.setText(ywApplyEnter.getLxr());
        phoneNumEdi.setText(ywApplyEnter.getLxdh());
        //        idNumEdi.setText(ywApplyEnter.getIdCard());


    }
}
