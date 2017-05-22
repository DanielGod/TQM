package tqm.bianfeng.com.tqm.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by wjy on 16/10/13.
 */

public class AppUtils {
    public static String getChanel(Context context){
        String channel = "nnn";
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            channel= applicationInfo.metaData.getString("WEIJY_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
