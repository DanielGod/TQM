package tqm.bianfeng.com.tqm.User.release;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by johe on 2017/5/20.
 */

public class WebActionForAndroid {

    Context mContext;
    public WebActionForAndroid(Context context){
        mContext=context;
    }

    @JavascriptInterface
    public void finishActivity(){
        Log.i("gqf","finishActivity");
    }





}
