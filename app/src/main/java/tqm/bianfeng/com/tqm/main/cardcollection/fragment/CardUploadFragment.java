package tqm.bianfeng.com.tqm.main.cardcollection.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.ShowDialogAndLoading;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.GeneralTools;
import tqm.bianfeng.com.tqm.Util.PhotoGet;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by 王九东 on 2017/7/13.
 */

public class CardUploadFragment extends BaseFragment {

    @BindView(R.id.commit)
    Button commit;
    String imagePath;
    ShowDialogAndLoading showDialogAndLoading;
    @BindView(R.id.card_img)
    ImageView cardImg;

    PhotoGet photoGet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cardupload, container, false);
        ButterKnife.bind(this, view);
        photoGet = PhotoGet.getInstance();
        photoGet.setContext(getActivity());
        imgPathList = new ArrayList<>();
        Log.e(Constan.LOGTAGNAME, "onCreateView：" );
        showDialogAndLoading = ShowDialogAndLoading.Show.showDialogAndLoading;
        showDialogAndLoading.setmLinsener(new ShowDialogAndLoading.Linsener() {
            @Override
            public void showBefore() {
            }
            @Override
            public void showAfter() {
                getActivity().onBackPressed();

            }
        });

        return view;
    }
    public interface mListener {
        public void setImg();
    }
    private mListener mListener;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(Constan.LOGTAGNAME, "onViewCreated：" );
        mListener.setImg();
    }
    Bitmap bitmap;
    File file;
    public void setImgInView(Intent data) {

        imagePath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT).get(0);
        //进行图片裁剪
        photoGet.beginImgCrop(imagePath);
        Constan.log("图片路径："+ imagePath);
//        file = new File(imagePath);
//        bitmap = GeneralTools.ImageUtils.getBitmap(imagePath);
//        cardImg.setImageBitmap(bitmap);
    }

    public void setLogo(int resultCode, Intent result) {
        photoGet.handleCrop(resultCode, result);
//        mLogoSelectPath.remove(0);
        if (photoGet.getHeadFile() == null) {
            Log.i("daniel", "getHeadFile==null");
        } else {
            bitmap = BitmapFactory.decodeFile(photoGet.getHeadFile().getAbsolutePath());
            file = new File(photoGet.getHeadFile().getAbsolutePath());
//            uploadLogoImgPath.add(photoGet.getHeadFile().getAbsolutePath());
            cardImg.setImageBitmap(bitmap);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //回收bitmap
        GeneralTools.ImageUtils.bitMapRecycled(bitmap);
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        if (GeneralTools.FileUtils.isFileExists(file)){
            Constan.log("图片大小："+ GeneralTools.FileUtils.getFileSize(file));
            saveVehicleCollection(file);
        }
    }
    List<String> imgPathList;

    private void saveVehicleCollection(File img) {

        showDialogAndLoading.showLoading("上传中。。",getActivity());

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), img);
        Log.i("gqf", "bm==null1");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", img.getName(), photoRequestBody);
        builder.setType(MultipartBody.FORM);
        Log.i("gqf", "bm==null2");
        MultipartBody mb=builder.build();

        Subscription subscription = NetWork.getUserService()
                .saveVehicleCollection(mb.part(0), realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
            @Override
            public void onCompleted() {
                //删除上传文件
                if (GeneralTools.FileUtils.isFileExists(file)){
                    GeneralTools.FileUtils.deleteFile(file);
                }
            }

            @Override
            public void onError(Throwable e) {
                showDialogAndLoading.stopLoaading();
                Log.e(Constan.LOGTAGNAME, "名片上传异常：" + e.toString());
            }

            @Override
            public void onNext(ResultCode resultCode) {
                showDialogAndLoading.stopLoaading();
                Log.e(Constan.LOGTAGNAME, "名片上传结果：" + resultCode.toString());
                if (resultCode.getCode() == 10001) {
                    showDialogAndLoading.showAfterDialog(getActivity(), "", "上传成功！", "确定");



                }

            }
        });
        compositeSubscription.add(subscription);
    }
}
