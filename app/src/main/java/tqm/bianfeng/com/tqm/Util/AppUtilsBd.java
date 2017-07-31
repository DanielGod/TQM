package tqm.bianfeng.com.tqm.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

/**
 * Created by wjy on 16/10/13.
 */

public class AppUtilsBd {
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

    //检查应用程序是否安装并安装应用程序
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getMacAddress() {
        String macAddress =null;
        String str ="";
        try{
            //linux下查询网卡mac地址的命令
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir =new InputStreamReader(pp.getInputStream());
            LineNumberReader input =new LineNumberReader(ir);

            for(;null != str;) {
                str = input.readLine();
                if(str !=null) {
                    macAddress = str.trim();// 去空格
                    break;
                }
            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        return macAddress;
    }

    public static String getMacAddress2(Context context) {

        String macAddress =null;

        WifiManager wifiManager =
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null== wifiManager ?null: wifiManager.getConnectionInfo());

        macAddress = info.getMacAddress();
        return macAddress;
    }

//    private void installVoiceServiceApk() {
//
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(Intent.ACTION_VIEW);
//        String type = "application/vnd.android.package-archive";
//        AssetManager assets = ProActivity.this.getAssets();
//        try {
//            //当文件比较大的时候不能用这个方法 来读取Stream ss.read(buffer) = -1  我的apk大小为5M
//            InputStream ss = assets.open(AsrService.apk");
//                    //使用下面这个方法 没问题
//                    InputStream is = getClass().getResourceAsStream(
//                            "/assets/AsrService.apk");
//
//            FileOutputStream fos = ProActivity.this.openFileOutput(
//                    "AsrService.apk", Context.MODE_PRIVATE
//                            + Context.MODE_WORLD_READABLE);
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = is.read(buffer)) != -1) {
//                fos.write(buffer, 0, len);
//            }
//            fos.flush();
//            is.close();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        File f = new File(ProActivity.this.getFilesDir().getPath()
//                + "/AsrService.apk");
//
//        // String path = "file:///android_asset/ZXing.apk";
//        // File f = new File(path);
//        intent.setDataAndType(Uri.fromFile(f), type);
//        ProActivity.this.startActivity(intent);
//
//    }



    //检查服务是否启动
    private boolean isStartService(Context ctx) {
        ActivityManager mActivityManager = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> currentService = mActivityManager
                .getRunningServices(100);
        final String igrsClassName = "com.iflytek.asr.AsrService"; //serviceName
        boolean b = igrsBaseServiceIsStart(currentService, igrsClassName);
        return b;
    }

    private boolean igrsBaseServiceIsStart(
            List<ActivityManager.RunningServiceInfo> mServiceList,
            String className) {
        for (int i = 0; i < mServiceList.size(); i++) {
            if (className.equals(mServiceList.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
