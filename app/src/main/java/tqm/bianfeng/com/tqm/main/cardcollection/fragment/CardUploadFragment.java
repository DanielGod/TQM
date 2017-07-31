package tqm.bianfeng.com.tqm.main.cardcollection.fragment;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cardupload, container, false);
        ButterKnife.bind(this, view);
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

    public void setImgInView(Intent data) {
//        Log.e(Constan.LOGTAGNAME, "cardImg：" + cardImg);
//        Log.e(Constan.LOGTAGNAME, "setImgInView：" + data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT).size());
//        Log.e(Constan.LOGTAGNAME, "imagePath：" + data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT).get(0));
                cardImg.setImageBitmap(BitmapFactory.decodeFile(data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT).get(0)));
                imagePath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT).get(0);
        mImg = new File(imagePath);
//        imgPathList.add(imagePath);
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        saveVehicleCollection(mImg);
    }
    List<String> imgPathList;
    File mImg;
    private void saveVehicleCollection(File img) {

        showDialogAndLoading.showLoading("上传中。。",getActivity());

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), img);
        Log.i("gqf", "bm==null1");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", img.getName(), photoRequestBody);
        builder.setType(MultipartBody.FORM);
        Log.i("gqf", "bm==null2");
        MultipartBody mb=builder.build();
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        for (int i = 0; i < imgPaths.size(); i++) {
//            File f = new File(imgPaths.get(i));
//            if (f != null) {
//                Log.i("gqf", "File" + i);
//                if (f.exists()) {
//                    RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), f);
//                    builder.addFormDataPart("zichifile" + i, f.getName(), photoRequestBody);
//
//                }
//            }
//        }
//        builder.setType(MultipartBody.FORM);
//        MultipartBody mb = builder.build();
//
//        List<MultipartBody.Part> zichifile = new ArrayList<>();
//        for (int i = 0; i < mb.size(); i++) {
//            zichifile.add(mb.part(i));
//        }
//        ,realm.where(User.class).findFirst().getUserId()
        Subscription subscription = NetWork.getUserService()
                .saveVehicleCollection(mb.part(0), realm.where(User.class).findFirst().getUserId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResultCode>() {
            @Override
            public void onCompleted() {
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
