package tqm.bianfeng.com.tqm.CustomView;

import android.content.Context;
import android.content.Intent;

import tqm.bianfeng.com.tqm.main.DetailActivity;

/**
 * Created by 王九东 on 2017/7/9.
 */

public class IntentToDetail {

    public static class newInstance{
        public static IntentToDetail intentToDetail=new IntentToDetail();
    }

    public Intent ActivityToDetail( Context mContext,Integer detailId,String articlePath,String detailTitle){
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("detailType", DetailActivity.ACTIVITY_TYPE);
        intent.putExtra("detailId",detailId);
        intent.putExtra("articlePath", articlePath);
        intent.putExtra("detailTitle", detailTitle);
        return intent;
    }
}
