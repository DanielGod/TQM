package tqm.bianfeng.com.tqm.application;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by 王九东 on 2017/7/28.
 */

public class Base2Application extends TinkerApplication {

    public Base2Application() {
        super(ShareConstants.TINKER_ENABLE_ALL, "tqm.bianfeng.com.tqm.application.BaseApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        Bugly.init(getApplicationContext(), "5f0b7de4c2", true);
//    }
}
