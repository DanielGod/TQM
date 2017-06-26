package tqm.bianfeng.com.tqm.main;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.utilcode.utils.StringUtils;
import com.jaeger.library.StatusBarUtil;
import com.soundcloud.android.crop.Crop;

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
import tqm.bianfeng.com.tqm.lawhelp.AllCityActivity;
import tqm.bianfeng.com.tqm.lawhelp.LawHelpFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.LawAdd;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.update.UpdateMsg;
import tqm.bianfeng.com.tqm.update.UpdateService;

import static tqm.bianfeng.com.tqm.Util.PhotoGet.REQUEST_IMAGE_CAPTURE;

public class MainActivity extends AppCompatActivity implements UserFragment.mListener, HomeFragment3.mListener, LawHelpFragment.mListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        compositeSubscription = new CompositeSubscription();
        Window window = getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        //        try {
//            initSystemBar();
//        } catch (Exception e) {
//
//        }
//        //设置状态栏
//        setSystemBarColor(R.color.black);
        //设置底部栏
        initBottomBar();
        //版本更新
        updateApp();
        //网络判断
        initNetWork(true);
        EventBus.getDefault().register(this);
        setBelow(0);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolBarColorBg(tooleBarNowAlpha);
        //定位按钮信息更新
        Log.e("Daniel", "----LawAdd---" + realm.where(LawAdd.class).findFirst());
        if (realm.where(LawAdd.class).findFirst() != null) {
            Log.e("Daniel", "----LawAdd---" + realm.where(LawAdd.class).findFirst());
            if (!realm.where(LawAdd.class).findFirst().getCity().equals("")) {
                Log.e("Daniel", "----LawAdd---" + realm.where(LawAdd.class).findFirst());
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
    HomeFragment3 homeFragment;
    LawHelpFragment lawHelpFragment;
    InstitutionsInFragment institutionsInFragment;
    UserFragment userFragemnt;

    public void  setContent(int contentHome) {
        switch (contentHome) {
            case CONTENT_HOME:
                String home_str = getResources().getString(R.string.home);
                toolbarTitle.setText(home_str);
                homeFragment = (HomeFragment3) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
                hideFragment(HOME_TAG);
                if (homeFragment == null) {
                    homeFragment = HomeFragment3.newInstance();
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
            sharedPreferences= getSharedPreferences("version",
                    Activity.MODE_PRIVATE);
//            mLocationVersion=sharedPreferences.getString("version","");
            if (StringUtils.isEmpty(sharedPreferences.getString("version",""))){
                Log.e("gqf", "本地版本为空" );
                //实例化SharedPreferences.Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //用putString的方法保存数据
                editor.putString("version","0.0.1");
                //提交当前数据
                editor.apply();
            }
            //与本地版本号对比
            Log.e("gqf", "本地版本号" +sharedPreferences.getString("version",""));
            if (BaseApplication.isUpdateForVersion(updateMsg.getVersionCode(), sharedPreferences.getString("version",""))) {
                //Log.i("gqf",UpdateInformation.localVersion+"updateMsg"+updateMsg.toString());
                mUpdateMsg = updateMsg;
                if (alert == null) {
                    alert = new AlertDialog.Builder(MainActivity.this);
                }
                alert.setTitle("软件升级")
                        .setMessage(updateMsg.getUpdateContent())
                        .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //开启更新服务UpdateService
                                //这里为了把update更好模块化，可以传一些updateService依赖的值
                                //如布局ID，资源ID，动态获取的标题,这里以app_name为例
                                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    //申请WRITE_EXTERNAL_STORAGE权限
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                                } else {
                                    startUpdateService(mUpdateMsg);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ISUPDATEAPP = false;
                            }
                        });
                alert.create().show();
            }
        }
    };


    //开起后台更新服务
    public void startUpdateService(UpdateMsg updateMsg) {
//        Constan.localVersion  = mUpdateMsg.getVersionCode();
        if (!StringUtils.isEmpty(mUpdateMsg.getVersionCode())){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //用putString的方法保存数据
            editor.putString("version",mUpdateMsg.getVersionCode());
            //提交当前数据
            editor.apply();
        }
        Log.e("gqf", "更新版本："+ mUpdateMsg.getVersionCode());
        Log.e("gqf", "本地版本："+sharedPreferences.getString("version",""));
        Intent updateIntent = new Intent(MainActivity.this, UpdateService.class);
        updateIntent.putExtra("getUpdateContent", updateMsg.getUpdateContent());
        updateIntent.putExtra("getVersionCode", updateMsg.getVersionCode());
        updateIntent.putExtra("getVersionUrl", updateMsg.getUpdateUrl());
        startService(updateIntent);
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
            ((BaseApplication) getApplication()).exit();
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
        //启动其他页面时修改toolbar颜色
//        toolbar.getBackground().setAlpha(255);
//        toolbarTitle.setAlpha(1);
    }

    @OnClick({R.id.home_location_txt, R.id.action_search_img,R.id.home_login_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_location_txt:
                startActivity(new Intent(MainActivity.this, AllCityActivity.class));
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
}
