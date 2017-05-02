package tqm.bianfeng.com.tqm.User;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.soundcloud.android.crop.Crop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ToastType;
import tqm.bianfeng.com.tqm.Dialog.BaseDialog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.Fragment.ApplyDialogFragment;
import tqm.bianfeng.com.tqm.Util.PermissionsHelper;
import tqm.bianfeng.com.tqm.Util.PhotoGet;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithCompanyFile;

import static tqm.bianfeng.com.tqm.Util.PhotoGet.REQUEST_IMAGE_CAPTURE;

/**
 * Created by johe on 2017/4/10.
 */

public class CompanyApplyForActivity extends BaseActivity {


    @BindView(R.id.company_apply_for_toolbar)
    Toolbar companyApplyForToolbar;
    @BindView(R.id.apply_for_company_name)
    EditText applyForCompanyName;
    @BindView(R.id.apply_for_add_img_result_txt)
    TextView applyForAddImgResultTxt;
    @BindView(R.id.apply_for_add_img_txt)
    TextView applyForAddImgTxt;
    @BindView(R.id.apply_for_company_user)
    EditText applyForCompanyUser;
    @BindView(R.id.apply_for_company_phone)
    EditText applyForCompanyPhone;
    @BindView(R.id.apply_for_btn)
    Button applyForBtn;
    private static final int TAKEPHOTO = 1; // 拍照
    private static final int GALLERY = 2; // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3; // 结果
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;




    PhotoGet photoGet;
    BaseDialog baseDialog;
    AlertDialog.Builder alert;
    @BindView(R.id.YBJ_loding_txt)
    TextView YBJLodingTxt;
    @BindView(R.id.loding_lin)
    LinearLayout lodingLin;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    private File bitmap;

    boolean isApplyForSuccess=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_apply_for);
        ButterKnife.bind(this);
        try {
            setToolbar(companyApplyForToolbar, "入驻申请");
        }catch (Exception e){

        }
        applyForBtn.setEnabled(false);
        initEdi();
        YBJLodingTxt.setText("提交中...");
        lodingLin.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        YBJLodingTxt.setVisibility(View.GONE);
        toastType = new ToastType();
    }
    public void applyForError(){
        lodingLin.setVisibility(View.GONE);
        toastType.showToastWithImg(CompanyApplyForActivity.this, false, "提交失败，请稍后重试");
    }

    public void initEdi() {
        Observable<CharSequence> applyName = RxTextView.textChanges(applyForCompanyName).skip(1);
        Observable<CharSequence> area = RxTextView.textChanges(applyForCompanyUser).skip(1);
        Observable<CharSequence> detailAddress = RxTextView.textChanges(applyForCompanyPhone).skip(1);

        Observable.combineLatest(applyName, area, detailAddress, new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
                boolean Bl = !TextUtils.isEmpty(charSequence);
                boolean B2 = !TextUtils.isEmpty(charSequence2);
                boolean B3 = !TextUtils.isEmpty(charSequence3);

                if (!Bl) {
                    applyForCompanyName.setError("公司名称为空");
                }
                if (!B2) {
                    applyForCompanyUser.setText("联系人为空");
                }
                if (!B3) {
                    applyForCompanyPhone.setError("联系电话为空");
                }

                return Bl && B2 && B3;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.e("Daniel", "--------aBoolean---------" + aBoolean);
                applyForBtn.setEnabled(aBoolean);
            }
        });
    }
    protected String getAbsoluteImagePath(Uri uri)
    {
        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( uri,
                proj,       // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null);      // Order-by clause (ascending by name)


        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
    public File saveBitmapFile(Bitmap bitmap){
        File file=new File(this.getExternalCacheDir().getAbsolutePath());//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file;
    }
    public String getAbsolutePath(Uri uri) {
        if(Build.VERSION.SDK_INT >= 19){
            Log.i("gqf",uri.getLastPathSegment());
            String id = uri.getLastPathSegment();//.split(":")[1];
            Log.i("gqf2",uri.getEncodedPath());
            final String[] imageColumns = {MediaStore.Images.Media.DATA };
            final String imageOrderBy = null;
            Uri tempUri =uri;// getUri();
            Cursor imageCursor = getContentResolver().query(tempUri, imageColumns,
                    MediaStore.Images.Media._ID + "="+id, null, imageOrderBy);
            Log.i("gqf",imageCursor.getCount()+"imageCursor"+imageCursor.getColumnCount());
            if (imageCursor.moveToFirst()) {
                return imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }else{
                return null;
            }
        }else{
            String[] projection = { MediaStore.MediaColumns.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else
                return null;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 拍照返回
        if (resultCode == RESULT_OK){
            Log.i("gqf", "RESULT_OK");
            if (requestCode == REQUEST_IMAGE_CAPTURE){
                try {
                    Log.i("gqf", "REQUEST_IMAGE_CAPTURE");
                    //photoGet.beginCrop(photoGet.getmCurrentPhotoUri());
                    Uri uri = photoGet.getmCurrentPhotoUri();
                    if (uri != null) {
                        bitmap = new File(uri.getEncodedPath());
                    } else {
                        Bundle bundle = data.getExtras();
                        Bitmap map = (Bitmap) bundle.get("data");
                        bitmap = saveBitmapFile(map);
                    }
                }catch (Exception e){
                    Log.i("gqf", "Exception"+e.toString());
                }

            } else if (requestCode == Crop.REQUEST_PICK){
                Log.i("gqf", "REQUEST_PICK");
                //photoGet. beginCrop(data.getData());
                Uri uri=data.getData();
                bitmap=new File(getAbsoluteImagePath(uri));

            }else if (requestCode == Crop.REQUEST_CROP) {
                Log.i("gqf", "handleCrop");
                photoGet.handleCrop(resultCode, data);
                bitmap = photoGet.getHeadFile();
                if (photoGet.getGeadBitmap() == null) {
                    Log.i("gqf", "getGeadBitmap==null");
                } else {
                    Log.i("gqf", "getGeadBitmap");
                }
                if (photoGet.getHeadFile() == null) {
                    Toast.makeText(CompanyApplyForActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                } else {
                    applyForAddImgResultTxt.setText("已添加");
                }
            }
        }
        //        else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
        //            Log.i("gqf", "REQUEST_PICK");
        //            photoGet. beginCrop(data.getData());
        //        }
        if (bitmap == null) {
            Toast.makeText(CompanyApplyForActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
        } else {
            applyForAddImgResultTxt.setText("已添加");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadUserHeadImg(File img) {
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), img);
        Log.i("gqf", "uploadUserHeadImg");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", img.getName(), photoRequestBody);
        builder.setType(MultipartBody.FORM);

        MultipartBody mb = builder.build();
        //网络上传
        Subscription subscription = NetWork.getUserService().uploadCompanyFile(mb.part(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCodeWithCompanyFile>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", e.toString());

                    }

                    @Override
                    public void onNext(ResultCodeWithCompanyFile resultCodeWithCompanyFile) {
                        Log.i("gqf", resultCodeWithCompanyFile.toString());
                        if (resultCodeWithCompanyFile.getCode() == ResultCode.SECCESS) {
                            initApplyForAllInfo(resultCodeWithCompanyFile.getCompanyFile());
                        } else {
                            applyForError();
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    public void setCompanyImg() {
        PermissionsHelper.verifyStoragePermissions(CompanyApplyForActivity.this);
        //上传公司执照
        if (photoGet == null) {
            photoGet = PhotoGet.getInstance();
        }
        if (baseDialog == null) {
            baseDialog = new BaseDialog(this);
        }
        photoGet.showAvatarDialog(CompanyApplyForActivity.this, baseDialog);

    }

    ToastType toastType;
    ApplyDialogFragment applyDialogFragment;

    public void initApplyForAllInfo(String imgAddress) {
        Subscription subscription = NetWork.getUserService().saveEnter(applyForCompanyUser.getText().toString(), applyForCompanyName.getText().toString(), imgAddress, applyForCompanyPhone.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.i("gqf", resultCode.toString());
                        if (resultCode.getCode() == ResultCode.SECCESS) {
                            lodingLin.setVisibility(View.GONE);
                            applyDialogFragment = new ApplyDialogFragment();
                            applyDialogFragment.show(getSupportFragmentManager(), "apply_for_dialog");
                            applyDialogFragment.setmLinsener(new ApplyDialogFragment.MLinsener() {
                                @Override
                                public void OnDismiss() {
                                    onBackPressed();
                                }
                            });

                        } else {
                            applyForError();
                        }
                    }
                });
        compositeSubscription.add(subscription);

    }



    @OnClick({R.id.apply_for_add_img_txt, R.id.apply_for_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_for_add_img_txt:
                setCompanyImg();
                //点击上传图片

                break;
            case R.id.apply_for_btn:
                if (bitmap != null) {
                    lodingLin.setVisibility(View.VISIBLE);
                    //提交申请
                    uploadUserHeadImg(bitmap);
                } else {
                    Toast.makeText(CompanyApplyForActivity.this, "请上传您的公司营业执照", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
