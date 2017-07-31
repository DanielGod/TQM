package tqm.bianfeng.com.tqm.CustomView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by 王九东 on 2017/7/13.
 */

public class RequestPermissions {
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    Context mContext;

    public RequestPermissions(Activity activity) {
        this.mContext = activity;
    }

    Linsener mLinsener;
    public interface Linsener{
        public void RequestSuccess(int requestCode);
       public void RequestFailure(int requestCode);
        
    };
    public void setmLinsener(Linsener mLinsener) {
        this.mLinsener = mLinsener;
    }
    public void requestAllPermissions(){
        //所需要申请的权限数组
       String[] permissionsArray = new String[]{
               Manifest.permission.CAMERA,
               Manifest.permission.READ_EXTERNAL_STORAGE,
               Manifest.permission.READ_CONTACTS,
               Manifest.permission.READ_PHONE_STATE};
        //还需申请的权限列表
        List<String> permissionsList = new ArrayList<String>();

        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.size()>0){
            ActivityCompat.requestPermissions((Activity) mContext,
                    permissionsList.toArray(new String[permissionsList.size()]), Constan.REQUEST_CODE_ALL_PERMISSIONS);
        }


    }
    /**
     * 请求相机权限
     */
    public void requestCamera(){
            Log.e(Constan.LOGTAGNAME,"申请相机，存储权限权限");
        if(ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.CAMERA},
                    Constan.REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 请求存储权限
     */
    public void requestStorage(){
        Log.e(Constan.LOGTAGNAME,"申请存储权限权限");
        if(ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constan.REQUEST_CODE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 请求通讯录权限
     */

    public void requestContacts(){
        Log.e(Constan.LOGTAGNAME,"申请通讯录权限");
        if(ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    Constan.REQUEST_CODE_PERMISSION_CONTACTS);
        }
    }

    /**
     * 请求phone设备权限
     */

    public void requestPhoneState(){
        Log.e(Constan.LOGTAGNAME,"申请phone设备权限");
        if(ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    Constan.REQUEST_CODE_READ_PHONE_STATE);
        }
    }
}
