package tqm.bianfeng.com.tqm.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.PhoneUtils;
import com.jaeger.library.StatusBarUtil;
import com.soundcloud.android.crop.Crop;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.CustomView.RequestPermissions;
import tqm.bianfeng.com.tqm.Dialog.BaseDialog;
import tqm.bianfeng.com.tqm.Institutions.InstitutionsInFragment;
import tqm.bianfeng.com.tqm.Institutions.SearchInstiutionsActivity;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.UserFragment;
import tqm.bianfeng.com.tqm.Util.DisplayUtil;
import tqm.bianfeng.com.tqm.Util.NetUtils;
import tqm.bianfeng.com.tqm.Util.PermissionsHelper;
import tqm.bianfeng.com.tqm.Util.PhotoGet;
import tqm.bianfeng.com.tqm.Util.SystemBarTintManager;
import tqm.bianfeng.com.tqm.application.BaseApplication;
import tqm.bianfeng.com.tqm.application.BaseApplicationLike;
import tqm.bianfeng.com.tqm.lawhelp.AllCityActivity;
import tqm.bianfeng.com.tqm.lawhelp.LawHelpFragment;
import tqm.bianfeng.com.tqm.main.location.MyLocationListener;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.LawAdd;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.address.EvenAddress;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.update.UpdateMsg;
import tqm.bianfeng.com.tqm.update.UpdateService;

import static tqm.bianfeng.com.tqm.Util.PhotoGet.REQUEST_IMAGE_CAPTURE;

public class MainActivity extends AppCompatActivity implements  UserFragment.mListener, HomeFragment4.mListener, LawHelpFragment.mListener {
    private static final String HOME_TAG = "home_flag";
    private static final String LAWHELP_TAG = "lawhelp_flag";
    private static final String INSTITUTIONSIN_TAG = "institutionsin_flag";

    private static final String CATHOME_TAG = "cathome_flag";
    private static final int CONTENT_HOME = 1;
    private static final int CONTENT_LAWHELP = 2;
    private static final int CONTENT_INSTITUTIONSIN = 3;
    private static final int CONTENT_CATHOME = 4;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    private static final int TAKEPHOTO = 1; // 拍照
    private static final int GALLERY = 2; // 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3; // 结果
    private static boolean ISUPDATEAPP = true;//本程序是否在此次开起后更新
    public static String locationStr = Constan.LOCATION;
    private String mLocationVersion;
    SharedPreferences sharedPreferences;
    CompositeSubscription compositeSubscription;
    PhotoGet photoGet;
    @BindView(R.id.bottomBar)
    BottomNavigationBar bottomBar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    BaseDialog baseDialog;
    AlertDialog.Builder alert;
    @BindView(R.id.container_lin)
    RelativeLayout containerLin;
    @BindView(R.id.net_work_lin)
    LinearLayout netWorkLin;
    @BindView(R.id.home_location_txt)
    TextView homeLocationTxt;
    @BindView(R.id.home_lin)
    RelativeLayout homeLin;

    Realm realm;
    @BindView(R.id.action_search_img)
    ImageView actionSearchImg;
    @BindView(R.id.home_login_txt)
    TextView homeLoginTxt;
    String mVersion;//本地版本
    private ContentResolver cr;
    RequestPermissions requestPermissions;
    private String channel;//渠道号
    private String IMEI="android";//设备号
    private String MAC;//mac地址
    ProgressDialog dialog;
    public LocationClient mLocationClient = null;
//    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        EventBus.getDefault().register(this);
        requestPermissions = new RequestPermissions(this);
        compositeSubscription = new CompositeSubscription();
        lawAdd=realm.where(LawAdd.class).findFirst();
        //百度地图定位
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //声明定位监听类
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myLocationListener);
        //配置定位参数
        initLocation();
        //设置底部栏
        initBottomBar();
        //版本更新
        updateApp();
        //网络判断
        initNetWork(true);
        setBelow(0);
        //获取本地版本
        mVersion= AppUtils.getAppInfo(MainActivity.this).getVersionName();
        if (Build.VERSION.SDK_INT < 23){
            Log.e("Daniel","版本号小于23");
            channel = "afwl001";
//            channel = WalleChannelReader.getChannel(getApplicationContext());
//            channel = AppUtilsBd.getChanel(getApplicationContext());
            IMEI = PhoneUtils.getPhoneIMEI(getApplicationContext());
            saveChannel(channel,IMEI);
        }else {
            //请求权限
            requestPermissions.requestAllPermissions();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenAddress evenAddress) {
        mLocationClient.stop();
        homeLocationTxt.setText(evenAddress.getCity());

        updateLawAdd(evenAddress.getCity(),evenAddress.getProvience());
    }
    LawAdd lawAdd;
    //更新lawAdd本地数据
    public void updateLawAdd(final String city, final String province){

        Log.e(Constan.LOGTAGNAME,"更新lawAdd本地数据---city"+city);
        Log.e(Constan.LOGTAGNAME,"更新lawAdd本地数据---province"+province);
        Log.e(Constan.LOGTAGNAME,"更新lawAdd本地数据---isInTransaction:"+realm.isInTransaction());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (lawAdd!=null) {
                    lawAdd.setCity(city);
                    lawAdd.setProvince(province);
                }else {
                    lawAdd = realm.createObject(LawAdd.class);
                    lawAdd.setCity(city);
                    lawAdd.setProvince(province);
                }
            }
        });
        Log.e(Constan.LOGTAGNAME,"MainActivity---lawAdd"+realm.where(LawAdd.class).findFirst());
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Daniel","requestCode："+requestCode);
        switch (requestCode) {
            case Constan.REQUEST_CODE_ALL_PERMISSIONS:
                Log.e("Daniel","获取所有权限回调：");
                for (int i = 0; i < permissions.length; i++) {
                    if ("android.permission.READ_PHONE_STATE".equals(permissions[i])
                            && (grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        IMEI = PhoneUtils.getPhoneIMEI(MainActivity.this);
                        Log.e("Daniel","获取IMEI："+IMEI);
                    }
                    if ("android.permission.ACCESS_FINE_LOCATION".equals(permissions[i])
                            && (grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        Log.e("Daniel","开始定位");
                        //开启定位
                        mLocationClient.start();
                    }
                }
                //获取设备号
                // TODO: 2017/8/3 渠道号
                channel = "afwl001";
//                                  channel = WalleChannelReader.getChannel(getApplicationContext());
                //                Toast.makeText(this, "渠道号："+channel, Toast.LENGTH_SHORT).show();
                Log.e("Daniel","获取channel："+channel);
                //保存渠道号，设备号
                saveChannel(channel, IMEI);
                break;
            case Constan.REQUEST_CODE_LOCATIONI:
                if (grantResults.length>0
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e("Daniel","开始定位");
                    //开启定位
                    mLocationClient.start();
                }
                break;
            default:
                break;
        }
    }

    private void saveChannel(String cl, String imei) {
        Log.e("Daniel","saveChannel");
        Log.e("Daniel","----channel-----"+ cl);
        Log.e("Daniel","----IMEI----"+ imei);
        //保存渠道号
        NetWork.getBankService().saveChannel(cl, imei)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Daniel","----保存设备号，渠道号异常----"+e.toString());
                        //                        countToEnter();
                    }
                    @DebugLog
                    @Override
                    public void onNext(ResultCode resultCode) {
                        Log.e("Daniel","----getMsgv----"+resultCode.getMsg());
                        //                        countToEnter();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolBarColorBg(tooleBarNowAlpha);
        //定位按钮信息更新
        if (realm.where(LawAdd.class).findFirst() != null) {
            if (!realm.where(LawAdd.class).findFirst().getCity().equals("")) {
                homeLocationTxt.setText(realm.where(LawAdd.class).findFirst().getCity());
                locationStr = realm.where(LawAdd.class).findFirst().getCity();
            } else {
                homeLocationTxt.setText(Constan.LOCATION);
                locationStr = Constan.LOCATION;
            }
        } else {
            homeLocationTxt.setText(Constan.LOCATION);
            locationStr = Constan.LOCATION;

        }
        MobclickAgent.onResume(this);
    }

    /**
     * 设置底部栏
     */

    private void initBottomBar() {
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar
                .setActiveColor(R.color.colorPrimary)
                .setInActiveColor(R.color.bottom_img)
                .setBarBackgroundColor(R.color.whitesmoke);
        bottomBar.addItem(new BottomNavigationItem(R.drawable.home, R.string.home))
                .addItem(new BottomNavigationItem(R.drawable.law_help, R.string.lawHelp))
                .addItem(new BottomNavigationItem(R.drawable.institutions_in, R.string.institutionsIn))
                .addItem(new BottomNavigationItem(R.drawable.cat_home, R.string.catHome))
                .initialise();
        setContent(CONTENT_HOME);
        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        setContent(CONTENT_HOME);
                        break;
                    case 1:
                        setContent(CONTENT_LAWHELP);
                        break;
                    case 2:
                        setContent(CONTENT_INSTITUTIONSIN);
                        break;
                    case 3:
                        setContent(CONTENT_CATHOME);
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    /**
     * 页面切换
     *
     * @param contentHome
     */
    HomeFragment4 homeFragment;
    LawHelpFragment lawHelpFragment;
    InstitutionsInFragment institutionsInFragment;
    UserFragment userFragemnt;

    public void  setContent(int contentHome) {
        switch (contentHome) {
            case CONTENT_HOME:
                String home_str = getResources().getString(R.string.home);
                toolbarTitle.setText(home_str);
                homeFragment = (HomeFragment4) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
                hideFragment(HOME_TAG);
                if (homeFragment == null) {
                    homeFragment = HomeFragment4.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, homeFragment, HOME_TAG).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().show(homeFragment).commitNow();
                }
                setBelow(0);
                break;
            case CONTENT_LAWHELP:
                String lawHelp_str = getResources().getString(R.string.lawHelp);
                toolbarTitle.setText(lawHelp_str);
                lawHelpFragment = (LawHelpFragment) getSupportFragmentManager().findFragmentByTag(LAWHELP_TAG);
                hideFragment(LAWHELP_TAG);
                if (lawHelpFragment == null) {
                    lawHelpFragment = LawHelpFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, lawHelpFragment, LAWHELP_TAG).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().show(lawHelpFragment).commitNow();
                }
                setBelow(1);
                break;
            case CONTENT_INSTITUTIONSIN:
                String institutionsIn_str = getResources().getString(R.string.institutionsIn);
                toolbarTitle.setText(institutionsIn_str);
                institutionsInFragment = (InstitutionsInFragment) getSupportFragmentManager().findFragmentByTag(INSTITUTIONSIN_TAG);
                hideFragment(INSTITUTIONSIN_TAG);
                if (institutionsInFragment == null) {
                    institutionsInFragment = InstitutionsInFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, institutionsInFragment, INSTITUTIONSIN_TAG).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().show(institutionsInFragment).commitNow();
                }
                setBelow(2);
                break;
            case CONTENT_CATHOME:
                String catHome_str = getResources().getString(R.string.catHome);
                toolbarTitle.setText(catHome_str);
                userFragemnt = (UserFragment) getSupportFragmentManager().findFragmentByTag(CATHOME_TAG);
                hideFragment(CATHOME_TAG);
                if (userFragemnt == null) {
                    userFragemnt = UserFragment.newInstance("猫舍");
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, userFragemnt, CATHOME_TAG).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().show(userFragemnt).commitNow();
                }
                setBelow(3);
                break;
        }

    }

    private void hideFragment(String tag) {
        if (homeFragment != null && tag != HOME_TAG) {
            getSupportFragmentManager().beginTransaction().hide(homeFragment).commitNow();
        }
        if (lawHelpFragment != null && tag != LAWHELP_TAG) {
            getSupportFragmentManager().beginTransaction().hide(lawHelpFragment).commitNow();
        }
        if (institutionsInFragment != null && tag != INSTITUTIONSIN_TAG) {
            getSupportFragmentManager().beginTransaction().hide(institutionsInFragment).commitNow();
        }
        if (userFragemnt != null && tag != CATHOME_TAG) {
            getSupportFragmentManager().beginTransaction().hide(userFragemnt).commitNow();
        }
    }

    public void setBelow(int index) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) containerLin.getLayoutParams();
        if (index == 0) {
            params.removeRule(RelativeLayout.BELOW);
            //toolbarTitle.setVisibility(View.GONE);
            homeLocationTxt.setVisibility(View.VISIBLE);
            Log.i("Daniel", "用户信息是否空：" + realm.where(User.class).findFirst());
            if (realm.where(User.class).findFirst() == null) {
                Log.i("Daniel", "测试登录显示" );
                homeLoginTxt.setVisibility(View.VISIBLE);
            }else {
                Log.i("Daniel", "测试登录隐藏0" );
                homeLoginTxt.setVisibility(View.INVISIBLE);
            }
        } else {
            params.addRule(RelativeLayout.BELOW, R.id.toolbar);
            if (index!=3){
                setToolBarColorBg(255);
            }else {
                setToolBarColorBg(0);
                params.removeRule(RelativeLayout.BELOW);
            }
            //toolbarTitle.setVisibility(View.VISIBLE);
            homeLocationTxt.setVisibility(View.INVISIBLE);
            Log.i("Daniel", "测试登录隐藏非零" );
            homeLoginTxt.setVisibility(View.INVISIBLE);
//            Log.i("Daniel", "测试登录" );

        }

        if (index == 2) {
            actionSearchImg.setVisibility(View.VISIBLE);
        } else {
            actionSearchImg.setVisibility(View.GONE);
        }
        containerLin.setLayoutParams(params);

    }

    public void changeActivity(
            Class activityClass) {
        startActivity(new Intent(MainActivity.this, activityClass));
    }
    public void changeUserHeadImg() {
        PermissionsHelper.verifyStoragePermissions(MainActivity.this);
        Log.i("gqf", "changeUserHeadImg");
        //修改用户头像
        if (photoGet == null) {
            photoGet = PhotoGet.getInstance();
        }
        if (baseDialog == null) {
            baseDialog = new BaseDialog(this);
        }
        photoGet.showAvatarDialog(MainActivity.this, baseDialog);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照返回
        if (resultCode == RESULT_OK) {
            Log.i("gqf", "RESULT_OK");
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Log.i("gqf", "REQUEST_IMAGE_CAPTURE");
                photoGet.beginCrop(photoGet.getmCurrentPhotoUri());

            } else if (requestCode == Crop.REQUEST_PICK) {
                Log.i("gqf", "REQUEST_PICK");
                photoGet.beginCrop(data.getData());
            }
            if (requestCode == Crop.REQUEST_CROP) {
                Log.i("gqf", "handleCrop");
                photoGet.handleCrop(resultCode, data);
                if (photoGet.getHeadFile() == null) {
                    Log.i("gqf", "getHeadFile==null");
                }
                userFragemnt.setUserHeadImg(photoGet.getHeadFile());
            }
        }
        //        else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
        //            Log.i("gqf", "REQUEST_PICK");
        //            photoGet. beginCrop(data.getData());
        //        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    //带intent的页面跳转
    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void detailActivity(Intent intent) {
        startActivity(intent);
    }

    UpdateMsg mUpdateMsg;
    MaterialDialog mDialog;
    ImageView unUpdateImg;
    ImageView updateImg;
    TextView version;
    TextView updateContent;
    Observer<UpdateMsg> observer = new Observer<UpdateMsg>() {
        @Override
        public void onCompleted() {
        }
        @Override
        public void onError(Throwable e) {
            Log.e("gqf", "updateMsg" + e.getMessage());
        }

        @Override
        public void onNext(UpdateMsg updateMsg) {
            Log.e("gqf", "更新信息" + updateMsg.toString());

//            sharedPreferences= getSharedPreferences("version",
//                    Activity.MODE_PRIVATE);
////            mLocationVersion=sharedPreferences.getString("version","");
//            if (StringUtils.isEmpty(sharedPreferences.getString("version",""))){
//                Log.e("gqf", "本地版本为空" );
//                //实例化SharedPreferences.Editor对象
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                //用putString的方法保存数据
//                editor.putString("version","0.0.1");
//                //提交当前数据
//                editor.apply();
//            }
            //与本地版本号对比

            if (BaseApplication.isUpdateForVersion(updateMsg.getVersionCode(), mVersion)) {
                //Log.i("gqf",UpdateInformation.localVersion+"updateMsg"+updateMsg.toString());
                mUpdateMsg = updateMsg;
//                if (alert == null) {
//                    alert = new AlertDialog.Builder(MainActivity.this);
//                }
                mDialog = new MaterialDialog.Builder(MainActivity.this)
                        .customView(R.layout.main_update_dialog, false).show();
                View view = mDialog.getCustomView();
                unUpdateImg = (ImageView) view.findViewById(R.id.updateDialog_unUpdate_img);
                updateImg = (ImageView) view.findViewById(R.id.updateDialog_immediatelyUpdate_img);
                updateContent = (TextView) view.findViewById(R.id.updateDialog_content_tv);
                version = (TextView) view.findViewById(R.id.updateDialog_version_tv);
                version.setText("V "+updateMsg.getVersionCode());
                updateContent.setText(updateMsg.getUpdateContent());
                updateImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            //申请WRITE_EXTERNAL_STORAGE权限
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                        } else {
                            startUpdateService(mUpdateMsg);
                        }
                    }
                });
                unUpdateImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        ISUPDATEAPP = false;
                    }
                });
//                alert.setTitle("软件升级")
//                        .setMessage(updateMsg.getUpdateContent())
//                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                //开启更新服务UpdateService
//                                //这里为了把update更好模块化，可以传一些updateService依赖的值
//                                //如布局ID，资源ID，动态获取的标题,这里以app_name为例
//                                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                        != PackageManager.PERMISSION_GRANTED) {
//                                    //申请WRITE_EXTERNAL_STORAGE权限
//                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//                                } else {
//                                    startUpdateService(mUpdateMsg);
//                                }
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                ISUPDATEAPP = false;
//                            }
//                        });
//                alert.create().show();
            }
        }
    };


    //开起后台更新服务
    public void startUpdateService(UpdateMsg updateMsg) {
//        Constan.localVersion  = mUpdateMsg.getVersionCode();
        Log.e("gqf", "更新版本："+ mUpdateMsg.getVersionCode());
        Log.e("gqf", "本地版本："+mVersion);
        Intent updateIntent = new Intent(MainActivity.this, UpdateService.class);
        updateIntent.putExtra("getUpdateContent", updateMsg.getUpdateContent());
        updateIntent.putExtra("getVersionCode", updateMsg.getVersionCode());
        updateIntent.putExtra("getVersionUrl", updateMsg.getUpdateUrl());
        startService(updateIntent);
        Toast.makeText(this, "后台下载中", Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

    //检测更新
    public void updateApp() {
        Log.e("gqf", "updateMsgupdateApp");
        if (ISUPDATEAPP && alert == null) {
            Log.e("gqf", "updateMsgupdateApp2");
            //判断本地数据库是否有版本号
            Subscription subscription = NetWork.getUserService().getVersion()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
            compositeSubscription.add(subscription);
        }
    }

    //弹出网络设置dialog
    public void shouNetWorkActivity() {
        LinearLayout netWorkLin = (LinearLayout) findViewById(R.id.net_work_lin);
        if (netWorkLin != null) {
            if (!NetUtils.isConnected(this)) {
                netWorkLin.setVisibility(View.VISIBLE);
                netWorkLin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NetUtils.openSetting(MainActivity.this);
                    }
                });

                RelativeLayout.LayoutParams labelParams = (RelativeLayout.LayoutParams) netWorkLin.getLayoutParams();
                labelParams.addRule(RelativeLayout.BELOW, toolbar.getId());
                netWorkLin.setLayoutParams(labelParams);
            } else {
                netWorkLin.setVisibility(View.GONE);
                RelativeLayout.LayoutParams labelParams = (RelativeLayout.LayoutParams) netWorkLin.getLayoutParams();
                labelParams.removeRule(RelativeLayout.BELOW);
                netWorkLin.setLayoutParams(labelParams);
            }
        }
        //        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) netWorkLin.getLayoutParams();
        //        params.addRule(RelativeLayout.BELOW, R.id.toolbar);
        //        containerLin.setLayoutParams(params);
        //        toolbar.setAlpha(1);
    }

    @Override
    public void toLogin() {
        bottomBar.selectTab(3,false);
        setContent(CONTENT_CATHOME);
    }

    //检测网络
    public void initNetWork(boolean isShowDialog) {
        Log.i("gqf", "initNetWork" + NetUtils.isConnected(this));
        if (!NetUtils.isConnected(this)) {
            if (homeFragment != null) {
                homeFragment.showViewWhenNetWork(false);
            }
        } else {
            if (homeFragment != null) {
                homeFragment.showViewWhenNetWork(true);
            }
        }
        shouNetWorkActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initNetWork(false);
        updateApp();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
        EventBus.getDefault().unregister(this);
        realm.close();
    }

    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {

            Toast.makeText(MainActivity.this, "再按一次退出铜钱猫app", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {

            //            MyConfig.clearSharePre(this, "users");
//            Toast.makeText(MainActivity.this, "还未热更新！", Toast.LENGTH_SHORT).show();
             BaseApplicationLike.exit();
//            ((BaseApplicationLike) getApplication()).exit();
        }
    }

    public void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setSystemBarColor(R.color.black);

    }

    public void setSystemBarColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(false);
//            此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(id);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.black));
            StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 100, null);
        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height = DisplayUtil.dip2px(this, getResources().getDimension(R.dimen.bigxmdp));
            toolbar.setLayoutParams(lp);
        }
    }

    int tooleBarNowAlpha = 0;

    //设置标题颜色
    public void setToolBarColorBg(int a) {
        if (toolbar != null) {
            if (NetUtils.isConnected(this)) {
                toolbar.getBackground().setAlpha(a);
                toolbarTitle.setAlpha((float) a / 255);
                tooleBarNowAlpha = a;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        //启动其他页面时修改toolbar颜色
//        toolbar.getBackground().setAlpha(255);
//        toolbarTitle.setAlpha(1);
    }

    @OnClick({R.id.home_location_txt, R.id.action_search_img,R.id.home_login_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_location_txt:
//                getContacts();
                if(ContextCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions.requestLocaltion();
                }else {
                    startActivity(new Intent(MainActivity.this, AllCityActivity.class));
                }

                break;
            case R.id.home_login_txt:
                bottomBar.selectTab(3,false);
                setContent(CONTENT_CATHOME);
                break;
            case R.id.action_search_img:
                Intent intent = new Intent(MainActivity.this, SearchInstiutionsActivity.class);
                intent.putExtra(SearchInstiutionsActivity.get_search_type, SearchInstiutionsActivity.all_search);
                startActivity(intent);
                break;
        }
    }

    private void getContacts() {
        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor=cr.query(uri,null,null,null,null);
        while(cursor.moveToNext()){
            int _id=cursor.getInt(cursor.getColumnIndex("_id"));
            String display_name=cursor.getString(cursor.getColumnIndex("display_name"));
            Log.i("test",_id+" "+display_name);
            Uri uriData=Uri.parse("content://com.android.contacts/raw_contacts/"+_id+"/data");
            Cursor cursorData=cr.query(uriData,null,null,null,null);
            while(cursorData.moveToNext()){
                String mimetype=cursorData.getString(cursorData.getColumnIndex("mimetype"));
                String data1=cursorData.getString(cursorData.getColumnIndex("data1"));
                if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                    Log.i("test","  "+mimetype+" "+data1);
                }
            }
        }
    }

}

