package tqm.bianfeng.com.tqm.baidumap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorInfo;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.AutoHeightLayoutManager;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseApplication;
import tqm.bianfeng.com.tqm.baidumap.overlayutil.PoiOverlay;
import tqm.bianfeng.com.tqm.baidumap.service.LocationService;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankDotItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.WebListViewType;

public class WebListActivity extends AppCompatActivity implements  OnGetPoiSearchResultListener {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.map_linear)
    LinearLayout mapLinear;
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private BaiduMap mBaiduMap = null;
    private List<String> suggest;
    private int loadIndex = 0;
    public static int oldIndex =0;
    public static double lat;
    public static double lnt;

    LatLng center = new LatLng(39.92235, 116.380338);
    int radius = 100;
    LatLng southwest = new LatLng(39.92235, 116.380338);
    LatLng northeast = new LatLng(39.947246, 116.414977);
    LatLngBounds searchbound = new LatLngBounds.Builder().include(southwest).include(northeast).build();

    int searchType = 0;  // 搜索的类型，在显示时区分

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);//能绘制地点
        //        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);//能返回结果

        mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
                .findFragmentById(R.id.map)))
                .getBaiduMap();

    }
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                //poi 搜索
                Log.e("Daniel", "----BDLocationListener---");
                lat= location.getLatitude();
                lnt=location.getLongitude();
                loadIndex=0;
                getPoiNearbySearchOption(loadIndex);


//                mPoiSearch.searchInCity((new PoiCitySearchOption())
//                                .city(location.getCity())
//                                .keyword(Constan.SEARCHFILTER)
//                                .pageNum(loadIndex));

            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };

    private LocationService locationService;
    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        // -----------location config ------------
        locationService = ((BaseApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebListViewType event) {
       switch (event.getType()){
           case 0:   //上一页
               oldIndex = loadIndex; //记录当前下标
               goToPreviousPage();
               break;
           case 1:   //下一页
               oldIndex = loadIndex;
               goToNextPage();
               break;
       }

    }

    /**
     * 响应城市内搜索按钮点击事件
     *
     */
//    public void searchButtonProcess(View v) {
////        searchType = 1;
////        String citystr = editCity.getText().toString();
////        String keystr = keyWorldsView.getText().toString();
////        mPoiSearch.searchInCity((new PoiCitySearchOption())
////                .city(citystr).keyword(keystr).pageNum(loadIndex));
//    }
//
    public void goToNextPage() {
        loadIndex++;
        getPoiNearbySearchOption(loadIndex);

    }
    public void goToPreviousPage() {
        loadIndex--;
        getPoiNearbySearchOption(loadIndex);

    }

    private void getPoiNearbySearchOption(int loadIndex) {

        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.keyword(Constan.SEARCHFILTER);
        option.sortType(PoiSortType.distance_from_near_to_far);
        option.location(new LatLng(lat, lnt));
        option.radius(Constan.SEARCHRADIUS);
        option.pageNum(loadIndex);
        option.pageCapacity(Constan.PAGECAPACITY);
        mPoiSearch.searchNearby(option);
    }

    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param result
     */
    List<String> mUidList;
    public void onGetPoiResult(PoiResult result) {
        if (result.getAllPoi()!=null){
            mUidList = new ArrayList<>();
            List<PoiInfo> datas = result.getAllPoi();
            int lenth = datas.size();
            for (int i = 0; i < lenth; i++) {
                Log.e("Daniel", "----onGetPoiResult---" + datas.get(i).uid);
                mUidList.add(datas.get(i).uid);
            }
            getBankDotItem(mUidList);


        }
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            loadIndex = oldIndex;
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();

            switch (searchType) {
                case 2:
                    //                    showNearbyArea(center, radius);
                    break;
                case 3:
                    //                    showBound(searchbound);
                    break;
                default:
                    break;
            }

            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void getBankDotItem(List<String> mUidList) {
        Log.e("Daniel", "----uids---"+new Gson().toJson(mUidList));
        NetWork.getBankService().getBankDotItem( new Gson().toJson(mUidList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankDotItem>>() {
                    @Override
                    public void onCompleted() {

                    }
                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }
                    @DebugLog
                    @Override
                    public void onNext(List<BankDotItem> bankDotItems) {
                        setAdapter(bankDotItems);//网点适配器
                    }
                });

    }

    private void setAdapter(List<BankDotItem> datas) {
        recycler.setLayoutManager(new AutoHeightLayoutManager(this));
        recycler.setHasFixedSize(true);
        WebListAdapter webListAdapter = new WebListAdapter(datas, WebListActivity.this);
        recycler.setAdapter(webListAdapter);
        locationService.stop();

    }
    static double DEF_PI = 3.14159265359; // PI
    static double DEF_2PI= 6.28318530712; // 2*PI
    static double DEF_PI180= 0.01745329252; // PI/180.0
    static double DEF_R =6370693.5; // radius of earth
    //适用于近距离
    public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
    //适用于远距离
    public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 求大圆劣弧与球心所夹的角(弧度)
        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0)
            distance = 1.0;
        else if (distance < -1.0)
            distance = -1.0;
        // 求大圆劣弧长度
        distance = DEF_R * Math.acos(distance);
        return distance;
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     *
     * @param result
     */
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 创建POI检索监听者
     */
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        @DebugLog
        public void onGetPoiResult(PoiResult result) {
            //获取POI检索结果
            List<PoiInfo> datas = result.getAllPoi();
            int lenth = datas.size();
            for (int i = 0; i < lenth; i++) {
                Log.e("Daniel", "----onGetPoiResult---" + datas.get(i).uid);
            }

        }

        @DebugLog
        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
            Log.e("Daniel", "----onGetPoiDetailResult---");
        }

        @DebugLog
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            List<PoiIndoorInfo> datas = poiIndoorResult.getmArrayPoiInfo();
            int lenth = datas.size();
            for (int i = 0; i < lenth; i++) {
                Log.e("Daniel", "----onGetPoiIndoorResult---" + datas.get(i).uid);
            }
        }
    };

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        Log.e("Daniel", "----onGetPoiIndoorResult1111---");
        List<PoiIndoorInfo> datas = poiIndoorResult.getmArrayPoiInfo();
        int lenth = datas.size();
        for (int i = 0; i < lenth; i++) {
            Log.e("Daniel", "----onGetPoiIndoorResult---" + datas.get(i).uid);
        }
    }

    boolean isMap = false;//地图和列表切换
    @OnClick(R.id.map_btn)
    public void onClick() {
        if (!isMap){
            recycler.setVisibility(View.GONE);
            mapLinear.setVisibility(View.VISIBLE);
            isMap = true;
        }else {
            recycler.setVisibility(View.VISIBLE);
            mapLinear.setVisibility(View.GONE);
            isMap = false;
        }
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

    /**
     * 对周边检索的范围进行绘制
     *
     * @param center
     * @param radius
     */
    public void showNearbyArea(LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
        mBaiduMap.addOverlay(ooMarker);

        OverlayOptions ooCircle = new CircleOptions().fillColor(0xCCCCCC00)
                .center(center).stroke(new Stroke(5, 0xFFFF00FF))
                .radius(radius);
        mBaiduMap.addOverlay(ooCircle);
    }

    /**
     * 对区域检索的范围进行绘制
     *
     * @param bounds
     */
    public void showBound(LatLngBounds bounds) {
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.drawable.ground_overlay);

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        bdGround.recycle();
    }
}
