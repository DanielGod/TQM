package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import me.nereo.multi_image_selector.MultiImageSelector;
import rx.functions.Action1;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by 王九东 on 2017/7/5.
 */

public class UtilityTool {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    private static final int REQUEST_IMAGE = 2;
    public static class Init{
        public static UtilityTool mUtilityTool = new UtilityTool();
    }

    //多图选择
    public void addImg(final Context context) {
        Log.e(Constan.LOGTAGNAME,"addImg() ");
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            Log.e(Constan.LOGTAGNAME,"申请WRITE_EXTERNAL_STORAGE权限");
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }else if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //申请CAMERA权限
            Log.e(Constan.LOGTAGNAME,"申请CAMERA权限");
            new RxPermissions((Activity) context)
                    .request(Manifest.permission.CAMERA)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            Log.e(Constan.LOGTAGNAME,"aBoolean");
                            if (aBoolean) {
                                setMultiImageSelector(context);
                            } else {
                                requestPermissionInfo();
                            }
                        }
                    });
        }
    }

    private void setMultiImageSelector(Context context) {
        MultiImageSelector multiImageSelector = MultiImageSelector.create(context).showCamera(true) // show camera or not. true by default
                // max select image size, 9 by default. used width #.multi()
                .single() // single mode
                .multi(); // original select data set, used width #.multi()
//            multiImageSelector.origin(mCompanyImgSelectPath);
            multiImageSelector.count(3);//选择图片个数

        multiImageSelector.start((Activity) context, REQUEST_IMAGE);
    }

    private void requestPermissionInfo() {
        new MaterialDialog.Builder(context)
                .title("请给予相关权限")
                .content("谢谢")
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        (Activity) context.finish();
                    }
                }).show();
    }
}
