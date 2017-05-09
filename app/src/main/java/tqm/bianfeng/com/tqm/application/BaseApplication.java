package tqm.bianfeng.com.tqm.application;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.barryzhang.temptyview.TEmptyView;
import com.barryzhang.temptyview.TViewUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tqm.bianfeng.com.tqm.baidumap.service.LocationService;

/**
 *
 * Created by johe on 2017/1/5.
 */

public class BaseApplication extends Application {
    private static List<Activity> mList ;
    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setWeixin("wx45a97091477855d5", "b735c4f1e7dbc53b53555513c0a6c579");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        //PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        //PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        //PlatformConfig.setAlipay("2017032406383358");
        //PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        //PlatformConfig.setPinterest("1439206");
        //PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
        //PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
        //PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
        //PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");

    }
    //获取集合size
    public  int getListSize(){
        return mList.size();
    }

    /**
     * add Activity
     * @param activity
     */
    public static void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 遍历退出activity
     */
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * OnLowMemory是Android提供的API，在系统内存不足，
     * 所有后台程序（优先级为background的进程，不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //垃圾回收
        System.gc();
    }

    //Realm初始化
    public static String username;
    Realm realm;
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        //        LeakCanary.install(this);
        mList = new ArrayList<>();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).schemaVersion(2).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfig);
        Config.DEBUG = true;
        UMShareAPI.get(this);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        TEmptyView.init(TViewUtil.EmptyViewBuilder.getInstance(this)
                .setShowText(true)
                .setEmptyText("NO DATA")
                .setShowButton(true)
                .setShowIcon(true));



    }
    /**
     * 使用默认字体
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }
    /**
     * 版本对比(是否需要更新版本)
     *
     * @param newVersion
     *            服务器上获取的版本
     * @param indexVersion
     *            当前使用的版本
     * @return true：服务器上是最新版本，需要更新； false：不需要更新
     */
    public static boolean isUpdateForVersion(String newVersion, String indexVersion) {
        // boolean resultFlag = false;

        if ("".equals(newVersion) || null == newVersion
                || "null".equals(newVersion)) {
            return false;
        } else {

            String[] newNums = newVersion.split("\\.");
            String[] indexNums = indexVersion.split("\\.");


            if (newNums.length > indexNums.length) {// 服务器版本长度 比当前版本要长
                for (int i = 0; i < newNums.length; i++) {
                    //位数不够,给当前版本补零
                    int currentValue = 0;
                    if(i < indexNums.length){
                        currentValue = Integer.parseInt(indexNums[i]);
                    }

                    //Log.e("gqf","1isUpdateForVersion"+Integer.parseInt(newNums[i]));
                   // Log.e("gqf","2isUpdateForVersion"+currentValue);
                    // 服务器上同位版本数如果有一个数大于 当前的，就是最新版，要更新；否则不更新
                    if (Integer.parseInt(newNums[i]) > currentValue) {
                        return true;
                    } else if (Integer.parseInt(newNums[i]) <= currentValue) {
                        //                        return false;
                    }
                }
                return false;
            } else if (newNums.length <= indexNums.length) {
                for (int i = 0; i < indexNums.length; i++) {
                    //位数不够,给服务器版本补零
                    int newValue = 0;
                    if(i < newNums.length){
                        newValue = Integer.parseInt(newNums[i]);
                    }
                    //Log.e("gqf","1isUpdateForVersion"+Integer.parseInt(indexNums[i]));
                    //Log.e("gqf","2isUpdateForVersion"+newValue);
                    // 服务器上同位版本数如果有一个数大于 当前的，就是最新版，要更新；否则不更新
                    if (newValue > Integer.parseInt(indexNums[i])) {
                        return true;
                    } else if (newValue <= Integer.parseInt(indexNums[i])) {
                        //                        return false;
                    }
                }
                return false;
            }

        }
        return false;

    }

    // 获取当前版本的版本号
    public static String getCurrentVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.
                    getPackageName(), 0);
            Log.d("TAG", "getCurrentVersion: 版本值--》"+packageInfo.versionCode);
            Log.d("TAG", "getCurrentVersion: 版本号--》"+packageInfo.versionName);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

}
