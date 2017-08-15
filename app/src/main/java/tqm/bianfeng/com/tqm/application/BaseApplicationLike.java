package tqm.bianfeng.com.tqm.application;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.lzy.okgo.OkGo;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by 王九东 on 2017/7/28.
 */

public class BaseApplicationLike extends DefaultApplicationLike {
    public static final String TAG = "Tinker.SampleApplicationLike";

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setWeixin("wx45a97091477855d5", "b735c4f1e7dbc53b53555513c0a6c579");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        //PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        //PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setQQZone("1106062218", "KEYc0rPU1GeCIJSh8ki");
        //PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
        //PlatformConfig.setAlipay("2017032406383358");
        //PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
        //PlatformConfig.setPinterest("1439206");
        //PlatformConfig.setKakao("e4f60e065048eb031e235c806b31c70f");
        //PlatformConfig.setDing("dingoalmlnohc0wggfedpk");
        //PlatformConfig.setVKontakte("5764965","5My6SNliAaLxEm3Lyd9J");
        //PlatformConfig.setDropbox("oz8v5apet3arcdy","h7p2pjbzkkxt02a");

    }

    public BaseApplicationLike(Application application, int tinkerFlags,
                               boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                               long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    public AssetManager getAssets(AssetManager assetManager) {
        return assetManager;
    }

//    public LocationClient mLocationClient = null;
//    public BDLocationListener myListener = new MyLocationListener();

    @Override
    public void onCreate() {
        super.onCreate();
        mList = new ArrayList<>();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(getApplication(), "41391628b8", false);
        //realm
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplication())
                .schemaVersion(2).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfig);
        Config.DEBUG = true;
        //友盟分享
        UMShareAPI.get(getApplication());
        //友盟统计场景配置
        MobclickAgent.setScenarioType(getApplication(), MobclickAgent.EScenarioType. E_UM_NORMAL);
        //日志打印
        Logger.addLogAdapter(new AndroidLogAdapter());
        //虚拟机忽略文件URI曝光
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //下载
        OkGo.init(getApplication());
        OkGo.getInstance()
                //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                .debug("OkGo");
//        //百度地图定位
//        mLocationClient = new LocationClient(getApplication());
//        //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);
//        //注册监听函数

    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);


    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    private static List<Activity> mList ;
    /**
     * 遍历退出activity
     */
    public static void exit() {
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

    /**
     * 使用默认字体
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources(getApplication().getResources());
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources(Resources resources) {
        Resources res = super.getResources(resources);
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
}
