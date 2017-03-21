package tqm.bianfeng.com.tqm.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.Dialog.BaseDialog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.UserFragment;
import tqm.bianfeng.com.tqm.Util.NetUtils;
import tqm.bianfeng.com.tqm.Util.PhotoGet;
import tqm.bianfeng.com.tqm.application.BaseApplication;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.update.UpdateInformation;
import tqm.bianfeng.com.tqm.update.UpdateMsg;
import tqm.bianfeng.com.tqm.update.UpdateService;

public class MainActivity extends AppCompatActivity implements UserFragment.mListener, HomeFragment.mListener {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        compositeSubscription = new CompositeSubscription();
        setContent(CONTENT_HOME);
        //设置底部栏
        initBottomBar();
        //版本更新
        updateApp();
        //网络判断
        initNetWork(true);
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
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                }
                setFragment(homeFragment, HOME_TAG);
                break;
            case CONTENT_LAWHELP:
                String lawHelp_str = getResources().getString(R.string.lawHelp);
                toolbarTitle.setText(lawHelp_str);
                lawHelpFragment = (LawHelpFragment) getSupportFragmentManager().findFragmentByTag(LAWHELP_TAG);
                if (lawHelpFragment == null) {
                    lawHelpFragment = LawHelpFragment.newInstance();
                }
                setFragment(lawHelpFragment, LAWHELP_TAG);
                break;
            case CONTENT_INSTITUTIONSIN:
                String institutionsIn_str = getResources().getString(R.string.institutionsIn);
                toolbarTitle.setText(institutionsIn_str);
                institutionsInFragment = (InstitutionsInFragment) getSupportFragmentManager().findFragmentByTag(INSTITUTIONSIN_TAG);
                if (institutionsInFragment == null) {
                    institutionsInFragment = InstitutionsInFragment.newInstance();
                }
                setFragment(institutionsInFragment, INSTITUTIONSIN_TAG);
                break;
            case CONTENT_CATHOME:
                String catHome_str = getResources().getString(R.string.catHome);
                toolbarTitle.setText(catHome_str);
                userFragemnt = (UserFragment) getSupportFragmentManager().findFragmentByTag(CATHOME_TAG);
                if (userFragemnt == null) {
                    userFragemnt = UserFragment.newInstance("猫舍");
                }
                setFragment(userFragemnt, CATHOME_TAG);
                break;
        }

    }

    /**
     * 设置fragment
     *
     * @param fragment
     */
    @DebugLog
    private void setFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void changeActivity(
            Class activityClass) {
        startActivity(new Intent(MainActivity.this, activityClass));
    }

    public void changeUserHeadImg() {
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
        switch (requestCode) {
            case TAKEPHOTO:
                String headIconPath = photoGet.getHeadIconPath();
                if (headIconPath != null)
                    photoGet.startPhotoZoom(Uri.fromFile(new File(headIconPath)), 150);
                break;
            case GALLERY:
                if (data != null) {
                    photoGet.startPhotoZoom(data.getData(), 150);
                }
                break;
            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    photoGet.saveImage(data);
                    userFragemnt.setUserHeadImg(photoGet.getHeadFile());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //带intent的页面跳转
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
            Log.e("gqf", "updateMsg" + updateMsg.toString());
            //与本地版本号对比
            if (BaseApplication.isUpdateForVersion(updateMsg.getVersionCode(), UpdateInformation.localVersion)) {
                // Log.i("gqf","updateMsg"+updateMsg.toString());
                mUpdateMsg = updateMsg;
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
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
        updateIntent.putExtra("getVersionUrl", updateMsg.getVersionUrl());
        startService(updateIntent);
    }

    //检测更新
    public void updateApp() {
        if (ISUPDATEAPP) {
            //判断本地数据库是否有版本号
            Subscription subscription = NetWork.getUpdateService().getVersion()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
            compositeSubscription.add(subscription);
        }
    }

    //弹出网络设置dialog
    public void shouNetWorkActivity() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("当前没有网络")
                .setMessage("是否跳转系统网络设置界面?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NetUtils.openSetting(MainActivity.this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alert.create().show();
    }

    //检测网络
    public void initNetWork(boolean isShowDialog) {
        Log.i("gqf", "initNetWork" + NetUtils.isConnected(this));
        if (!NetUtils.isConnected(this)) {
            if (isShowDialog) {
                shouNetWorkActivity();
            }
            if (homeFragment != null) {
                homeFragment.showViewWhenNetWork(false);
            }
        } else {
            if (homeFragment != null) {
                homeFragment.showViewWhenNetWork(true);
            }
        }
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
    }
}
