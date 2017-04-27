package tqm.bianfeng.com.tqm.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jaeger.library.StatusBarUtil;
import com.soundcloud.android.crop.Crop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.Dialog.BaseDialog;
import tqm.bianfeng.com.tqm.Institutions.InstitutionsInFragment;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.UserFragment;
import tqm.bianfeng.com.tqm.Util.DisplayUtil;
import tqm.bianfeng.com.tqm.Util.NetUtils;
import tqm.bianfeng.com.tqm.Util.PermissionsHelper;
import tqm.bianfeng.com.tqm.Util.PhotoGet;
import tqm.bianfeng.com.tqm.application.BaseApplication;
import tqm.bianfeng.com.tqm.lawhelp.LawHelpFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.update.UpdateInformation;
import tqm.bianfeng.com.tqm.update.UpdateMsg;
import tqm.bianfeng.com.tqm.update.UpdateService;

import static tqm.bianfeng.com.tqm.Util.PhotoGet.REQUEST_IMAGE_CAPTURE;

public class MainActivity extends AppCompatActivity implements UserFragment.mListener, HomeFragment.mListener, LawHelpFragment.mListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        compositeSubscription = new CompositeSubscription();
        try {
            initSystemBar();
        } catch (Exception e) {

        }

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
    HomeFragment homeFragment;
    LawHelpFragment lawHelpFragment;
    InstitutionsInFragment institutionsInFragment;
    UserFragment userFragemnt;

    public void setContent(int contentHome) {
        switch (contentHome) {
            case CONTENT_HOME:
                String home_str = getResources().getString(R.string.home);
                toolbarTitle.setText(home_str);
                homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
                hideFragment(HOME_TAG);
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
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
                setBelow(1);
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
                setBelow(1);
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
        if (index == 1) {
            params.addRule(RelativeLayout.BELOW, R.id.toolbar);
            setToolBarColorBg(1);
        } else {
            params.removeRule(RelativeLayout.BELOW);
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
            //Log.e("gqf", "updateMsg" + updateMsg.toString());
            //与本地版本号对比
            if (BaseApplication.isUpdateForVersion(updateMsg.getVersionCode(), UpdateInformation.localVersion)) {
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
            } else {
                netWorkLin.setVisibility(View.GONE);
            }
        }
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) netWorkLin.getLayoutParams();
//        params.addRule(RelativeLayout.BELOW, R.id.toolbar);
//        containerLin.setLayoutParams(params);
//        toolbar.setAlpha(1);
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
        setSystemBarColor(R.color.colorPrimaryDark);
    }

    public void setSystemBarColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //SystemBarTintManager tintManager = new SystemBarTintManager(this);
            //tintManager.setStatusBarTintEnabled(false);
            //此处可以重新指定状态栏颜色
            //tintManager.setStatusBarTintResource(id);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
            StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this,100, null);
        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height = DisplayUtil.dip2px(this, getResources().getDimension(R.dimen.bigxmdp));
            toolbar.setLayoutParams(lp);
        }
    }

    public void setToolBarColorBg(float a) {
        if (toolbar != null) {
            if(NetUtils.isConnected(this)) {
                toolbar.setAlpha(a);
            }
        }
    }
}
