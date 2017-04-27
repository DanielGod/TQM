package tqm.bianfeng.com.tqm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by johe on 2017/4/18.
 */

public class demoActivity extends AppCompatActivity {
    Context mContext;
    @BindView(R.id.load_more_txt)
    TextView loadMoreTxt;

    //PoiSearch.Query query;
    //PoiSearch poiSearch;
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d("gqf", "action: " + s);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Log.d("gqf", "key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {

                Log.d("gqf", "key 验证成功! 功能可以正常使用");
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Log.d("gqf", "网络出错");
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_loading);
        ButterKnife.bind(this);
        mContext = this;

//        query = new PoiSearch.Query("银行", "", "洛阳市");
//        //keyWord表示搜索字符串，
//        //第二个参数表示POI搜索类型，二者选填其一，
//        //POI搜索类型共分为以下20种：汽车服务|汽车销售|
//        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
//        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
//        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
//        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
//        query.setPageSize(10);// 设置每页最多返回多少条poiitem
//        query.setPageNum(1);//设置查询页码
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
//            @Override
//            public void onPoiSearched(PoiResult poiResult, int i) {
//                Log.i("gqf",i+"onPoiSearched"+poiResult.getPois().toString());
//            }
//
//            @Override
//            public void onPoiItemSearched(PoiItem poiItem, int i) {
//                Log.i("gqf",i+"onPoiItemSearched"+poiItem.toString());
//            }
//        });
//        poiSearch.searchPOIAsyn();

        Log.i("gqf","onGetPoiResult");
        mPoiSearch = PoiSearch.newInstance();
        Log.i("gqf","onGetPoiResult1");
        poiListener = new OnGetPoiSearchResultListener(){
            public void onGetPoiResult(PoiResult result){
                //获取POI检索结果
                Log.i("gqf","onGetPoiResult");
                Log.i("gqf","onGetPoiResult"+result.getAllPoi().toString());
                Log.i("gqf","onGetPoiResult"+result.getAllAddr().toString());
                if (result == null
                        || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                    Toast.makeText(demoActivity.this, "未找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
            public void onGetPoiDetailResult(PoiDetailResult result){
                //获取Place详情页检索结果
                Log.i("gqf","onGetPoiDetailResult"+result.toString());
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                Log.i("gqf","onGetPoiIndoorResult"+poiIndoorResult.getmArrayPoiInfo().toString());
            }
        };
        Log.i("gqf","onGetPoiResult2");
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        Log.i("gqf","onGetPoiResult3");
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword("美食")
                .pageNum(1).pageCapacity(10));
        Log.i("gqf","onGetPoiResul4");

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(Intent.ACTION_TIME_TICK);
        iFilter.addAction( "com.lenovo.speechcamera.start");
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);


        mReceiver = new SDKReceiver();
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getAction();
                Log.d("gqf", "action: " + s);
                if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                    Log.d("gqf", "key 验证出错! 错误码 :" + intent.getIntExtra
                            (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                            +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
                } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {

                    Log.d("gqf", "key 验证成功! 功能可以正常使用");
                } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                    Log.d("gqf", "网络出错");
                }
            }
        }, iFilter);

    }
    OnGetPoiSearchResultListener poiListener;
    PoiSearch mPoiSearch;
    private SDKReceiver mReceiver;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
        unregisterReceiver(mReceiver);
    }


    @OnClick(R.id.load_more_txt)
    public void onClick() {
        Intent startIntent = new Intent();
        startIntent.putExtra("pkg", getPackageName());
        startIntent.setAction("com.lenovo.speechcamera.start");
        startIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(startIntent);
        Log.i("gqf","onGetPoiResul4");
    }

}