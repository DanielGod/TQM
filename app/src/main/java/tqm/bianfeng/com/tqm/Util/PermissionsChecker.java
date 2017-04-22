package tqm.bianfeng.com.tqm.Util;

/**
 * Created by johe on 2017/4/13.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import java.io.FileNotFoundException;

/**
 * 检查权限的工具类
 * <p/>
 * Created by wangchenlong on 16/1/26.
 */
public class PermissionsChecker {
    private final Context mContext;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    public static Bitmap getImageBitmap(Context context, Intent intent) {
        Bitmap bitmap=null;
        if (intent.getExtras()!=null) {
            bitmap = (Bitmap) intent.getExtras().get("data");
        }else {
            Uri uri = intent.getData();
            //LogUtil.i("aaa", uri.toString());
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return bitmap;
    }
}