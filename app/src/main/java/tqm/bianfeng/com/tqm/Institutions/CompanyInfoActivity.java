package tqm.bianfeng.com.tqm.Institutions;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.CoordinatorTabLayout;
import tqm.bianfeng.com.tqm.CustomView.MyViewPager;
import tqm.bianfeng.com.tqm.CustomView.RequestPermissions;
import tqm.bianfeng.com.tqm.Institutions.adapter.CreditManagerAdapter;
import tqm.bianfeng.com.tqm.Institutions.adapter.MyPagerAdapter;
import tqm.bianfeng.com.tqm.Institutions.listener.LoadHeaderImagesListener;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.CorrectOrReportActivity;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForChooseActivity;
import tqm.bianfeng.com.tqm.User.applyforactivity.ApplyForStatusActivity;
import tqm.bianfeng.com.tqm.Util.AutoInstall;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.main.MainActivity;
import tqm.bianfeng.com.tqm.main.WebsiteActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.Call;
import tqm.bianfeng.com.tqm.pojo.CreditManager;
import tqm.bianfeng.com.tqm.pojo.LawFirmOrInstitutionDetail;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

import static tqm.bianfeng.com.tqm.bank.fragment.FilterAdapter.mContext;

/**
 * Created by johe on 2017/4/10.
 */

public class CompanyInfoActivity extends BaseActivity {

    @BindView(R.id.vp)
    MyViewPager mViewPager;
    @BindView(R.id.coordinatortablayout)
    CoordinatorTabLayout coordinatortablayout;
    private int[] mColorArray;
    private ArrayList<Fragment> fragments;
    private String [] mCompanyTitles={"理财", "贷款", "活动"};
    private String [] mCapitalTitles={ "贷款", "活动"};
    private String [] mLawTitles={"旗下全部律师"};
    private  String[] mTitles ;

    private View headerView;

    int InstitutionId;
    LawFirmOrInstitutionDetail data;
    String apkUrl;

    public static int index=-1;//1律师//2机构//3民资
    RequestPermissions requestPermissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        requestPermissions = new RequestPermissions(this);
        downloadManager = DownloadService.getDownloadManager();//下载管理
        InstitutionId = getIntent().getIntExtra("InstitutionId", 0);
        initHeaderRootView();
        Log.i("gqf","CompanyInfoActivity"+index);
        if(index==2||index==3){
            initCompanyInfoData(InstitutionId);
        }else{
            initLawInfoData(InstitutionId);
        }
//        requestPermissions.setmLinsener(new RequestPermissions.Linsener() {
//            @Override
//            public void RequestSuccess(int requestCode) {
//                if (requestCode==Constan.REQUEST_CODE_EXTERNAL_STORAGE){
//                    Log.e(Constan.LOGTAGNAME,"RequestSuccess！");
//                    setAppDownloadImg(1);
//                    Toast.makeText(CompanyInfoActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
//                    GetRequest request = OkGo.get(apkUrl);
//                    downloadManager.addTask(data.getInstitutionName() + ".apk", apkUrl, request, new MyDownloadListener(downloadInfo));
//                }
//            }
//
//            @Override
//            public void RequestFailure(int requestCode) {
//                Log.e(Constan.LOGTAGNAME,"RequestFailure！");
//            }
//        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constan.REQUEST_CODE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e(Constan.LOGTAGNAME,"RequestSuccess！");
                    setAppDownloadImg(1);
                    Toast.makeText(CompanyInfoActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                    GetRequest request = OkGo.get(apkUrl);
                    downloadManager.addTask(data.getInstitutionName() + ".apk", apkUrl, request, new MyDownloadListener(downloadInfo));
                }
                break;
            default:
                break;
        }
    }

    public void initCompanyInfoData(int id) {
        String userId="0";
        if(realm.where(User.class).findFirst()!=null){
            userId=""+realm.where(User.class).findFirst().getUserId();
        }
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getFinanceDetail(id,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LawFirmOrInstitutionDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LawFirmOrInstitutionDetail lawFirmOrInstitutionDetail) {
                        data=lawFirmOrInstitutionDetail;
                        apkUrl = NetWork.LOAD+data.getApkUrl();
                        Log.e(Constan.LOGTAGNAME,"apkUrl:"+apkUrl);
                        initView(data);
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);

    }
    public void initLawInfoData(int id) {
        String userId="0";
        if(realm.where(User.class).findFirst()!=null){
            userId=""+realm.where(User.class).findFirst().getUserId();
        }
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getLawFirmDetail(id,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LawFirmOrInstitutionDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LawFirmOrInstitutionDetail lawFirmOrInstitutionDetail) {
                        data = lawFirmOrInstitutionDetail;
                        Log.i("gqf", "data" + data.toString());
                        initView(data);

                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);

    }


    public void initView(LawFirmOrInstitutionDetail lawFirmOrInstitutionDetail) {
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light};
        Log.i("gqf","CompanyInfoActivity"+index);
        if(index==2){
            initCompanyFragment();
        }else if(index==3){
            initCapitalFragment();
        }else{
            initLawFragment();
        }

        initViewPager();
        initHeaderView();

        coordinatortablayout.setTitle(data.getInstitutionName())
                .setBackEnable(true)
                .setHeaderView(headerView)
                .setContentScrimColorArray(mColorArray)
                .setLoadHeaderImagesListener(new LoadHeaderImagesListener() {
                    @Override
                    public void loadHeaderImages(View mHeaderView, TabLayout.Tab tab) {

                    }
                })
                .setupWithViewPager(mViewPager);

        if(index==0){
            //设置选中下划线为透明
            coordinatortablayout.setTabInColor(R.color.max_transparent);
        }
        coordinatortablayout.setmLinsener(new CoordinatorTabLayout.Linsener() {
            @Override
            public void changActivity(int id) {
                if(id==R.id.correct_report){
                    Intent intent=new Intent(CompanyInfoActivity.this, CorrectOrReportActivity.class);
                    intent.putExtra(CorrectOrReportActivity.objectId,InstitutionId);
                    intent.putExtra(CorrectOrReportActivity.objectModule,"06");
                    intent.putExtra(CorrectOrReportActivity.objectTitle,data.getInstitutionName());
                    startActivity(intent);
                }
            }
        });
    }

    ImageView infoHeaderImg;
    TextView titleTxt;
    TextView profileTxt;
    LinearLayout callLin;
    LinearLayout collectionLin;
    LinearLayout callLin1;
    LinearLayout collectionLin1;
    TextView phoneNumTxt;
    TextView addressTxt;
    LinearLayout moreProfileLin;
    LinearLayout phoneNumLin;
    TextView isCollectTxt;
    public void initHeaderRootView(){
        headerView = getLayoutInflater().from(this).inflate(R.layout.company_info_header_loading_view, null, true);
        coordinatortablayout.setTitle("加载中")
                .setBackEnable(true)
                .setHeaderView(headerView);
        setSystemBarColor(R.color.gary_dark);
        coordinatortablayout.setMyLinsener(new CoordinatorTabLayout.MyLinsener() {
            @Override
            public void openOrClose(boolean isOpen, int index) {
                if (!isOpen) {
                    setSystemBarColor(mColorArray[index]);
                } else {
                    setSystemBarColor(R.color.gary_dark);
                }
            }
            @Override
            public void onBack() {
                onBackPressed();
            }
        });
    }

    public class AppReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent == null)
            {
                return;
            }
            if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED))
            {
                Log.e(Constan.LOGTAGNAME,"安装新程序！");
                if (data!=null&&data.getApkPackage()!=null){
                    setInstall(realm.where(User.class).findFirst().getUserId(),data.getApkPackage());
                }
            }
        }
    }

    private void setInstall(int userId, String apkPackage) {
        Subscription subscription = NetWork.getInstitutionService().install(userId,apkPackage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(Constan.LOGTAGNAME,"apk安装记录保存成功！");
                    }
                });
        compositeSubscription.add(subscription);
    }

    private void getCreditManagers(String city) {
        //获取信贷经理信息
        Log.e("Daniel","机构城市"+city);
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService()
                .getCreditManagers(InstitutionId, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CreditManager>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Daniel","展示信贷经理信息条数："+e.toString());

                    }
                    @Override
                    public void onNext(List<CreditManager> creditManagers) {
                        Log.e("Daniel","展示信贷经理信息条数："+creditManagers.size());
                        if (creditManagers != null) {
                            //展示信贷经理信息
                            showCreditManagerDialog(creditManagers);
                        }
                    }
                });
        compositeSubscription.add(getBankFinancItem_subscription);
    }
    public interface mListener {

        public void toLogin();
    }
    private mListener mListener;

    public void setMListener(Activity activity) {
        mListener = (mListener) activity;

    }
    //登录判断
    public boolean isLogin() {
        if (realm.where(User.class).findFirst() == null) {
            Toast.makeText(CompanyInfoActivity.this, "请您先登录后使用该功能", Toast.LENGTH_SHORT).show();
            //跳到登录页面
            mListener.toLogin();
            return false;
        } else {
            return true;
        }
    }
    boolean wrapInScrollView = false;
    TextView phoneNumTv;
    Button setCreditManager;
    RecyclerView creditManagerRecycle;
    User mUser;
    private void showCreditManagerDialog(List<CreditManager> creditManagers) {
        final MaterialDialog mDialog = new MaterialDialog.Builder(CompanyInfoActivity.this)
                .customView(R.layout.creditmanager_recycle, wrapInScrollView).show();
        View view = mDialog.getCustomView();
        creditManagerRecycle= (RecyclerView) view.findViewById(R.id.recyclerView);
        setCreditManager = (Button) view.findViewById(R.id.setCreditManager_btn);
        if (realm.where(User.class).findFirst()==null ||"01".equals(realm.where(User.class).findFirst().getUserType()) ||"06".equals(realm.where(User.class).findFirst().getUserType())){
            setCreditManager.setVisibility(View.VISIBLE);
        }else {
            setCreditManager.setVisibility(View.GONE);
        }
        setCreditManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin()) {
                    if (mUser == null){
                        mUser = realm.where(User.class).findFirst();
                    }
                    getShzt(mUser.getUserId());
                }
            }
        });
        CreditManagerAdapter  creditManagerAdapter = new CreditManagerAdapter(CompanyInfoActivity.this,creditManagers,data);
            creditManagerRecycle.setLayoutManager(new LinearLayoutManager(this));
            creditManagerRecycle.setAdapter(creditManagerAdapter);
    }
    private void getShzt(int userId) {
        Subscription subscription = NetWork.getUserService().getShzt(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(Map<String, String> stringStringMap) {
                        if (!StringUtils.isEmpty(stringStringMap.get("shzt"))){
                            Log.e("Daniel","快速入驻审核状态"+stringStringMap.get("shzt"));
                            //查看审核状态
                            startActivity(new Intent(CompanyInfoActivity.this,ApplyForStatusActivity.class));
                        }else {
                            //跳转入驻选择界面
                            startActivity(new Intent(CompanyInfoActivity.this,ApplyForChooseActivity.class));
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread( Call call) {
        Log.e("Daniel","EvenBus");
                if(!StringUtils.isEmpty(call.getPhoneNum())){
                    Log.e("Daniel","呼叫电话："+call);
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + call.getPhoneNum()));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentPhone);
                }
    }
    /**
     * 下载监听内部类
     */
    private class MyDownloadListener extends DownloadListener {
        DownloadInfo mDownloadInfo;

        public MyDownloadListener(DownloadInfo mDownloadInfo) {
            this.mDownloadInfo = mDownloadInfo;
        }

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            Toast.makeText(CompanyInfoActivity.this, "下载完成!", Toast.LENGTH_SHORT).show();
//            setAppDownloadImg(2);
            AutoInstall.setUrl(downloadInfo.getTargetPath());
            AutoInstall.install(CompanyInfoActivity.this);
//            if (!AppUtilsBd.isInstallApp(CompanyInfoActivity.this, data.getApkPackage())){
//                setAppDownloadImg(1);
//                Toast.makeText(CompanyInfoActivity.this, "下载完成!", Toast.LENGTH_SHORT).show();
//                Log.e("Daniel","topalyPath"+ downloadInfo.getTargetPath());
//                AutoInstall.setUrl(downloadInfo.getTargetPath());
//                AutoInstall.install(CompanyInfoActivity.this);
//            }else {
//                setAppDownloadImg(3);
//            }
//            appDownloadImg.setImageDrawable(CompanyInfoActivity.this.getResources().getDrawable(R.drawable.az));
        }


        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            if (errorMsg != null)
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            setAppDownloadImg(0);

        }
    }
    private void setAppDownloadImg(int i) {
        //设置下载，下载中，安装，打开图标切换
        appDownloadImg.setVisibility(View.GONE);
        appDownloadingImg.setVisibility(View.GONE);
        appInstallImg.setVisibility(View.GONE);
        appOpenImg.setVisibility(View.GONE);
        switch (i) {
            case 0:
                appDownloadImg.setVisibility(View.VISIBLE);
                break;
            case 1:
                appDownloadingImg.setVisibility(View.VISIBLE);
                break;
            case 2:
                appInstallImg.setVisibility(View.VISIBLE);
                break;
            case 3:
                appOpenImg.setVisibility(View.VISIBLE);
                break;
        }
    }
    ImageView appLogoImg;
    TextView appTitle;
    TextView appIntegarNum;
    LinearLayout goWebsiteLin;
    LinearLayout appDownLin;
    ImageView isCollectImg;
    ImageView appDownloadImg;//立即下载
    ImageView appInstallImg;//安装
    ImageView appDownloadingImg;//下载中
    ImageView appOpenImg;//打开
    private DownloadManager downloadManager;
    DownloadInfo downloadInfo;
    public void initHeaderView() {
        headerView = getLayoutInflater().from(this).inflate(R.layout.company_info_header_view, null, true);
        //控件绑定
        findViewById();
        //设置app下载
        setApp();
        //app下载
        appDownloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions.requestStorage();

                //保存到缓存
//                AppCacheUtils.getInstance(getApplication()).put(data.getApkUrl(), data);
            }
        });
        //下载中
        appDownloadingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        //安装
        appInstallImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Daniel", "topalyPath" + downloadInfo.getTargetPath());
                AutoInstall.setUrl(downloadInfo.getTargetPath());
                AutoInstall.install(CompanyInfoActivity.this);
            }
        });
        //打开
        appOpenImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                PackageManager packageManager = CompanyInfoActivity.this.getPackageManager();
                intent = packageManager.getLaunchIntentForPackage(data.getApkPackage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
            //去官网
            goWebsiteLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转银行活动详情
                    Intent intent = new Intent(CompanyInfoActivity.this, WebsiteActivity.class);
                    intent.putExtra("URL", data.getWebsite());
                    startActivity(intent);
                }
            });
            if (realm.where(User.class).findFirst() == null) {
                phoneNumLin = (LinearLayout) headerView.findViewById(R.id.phone_num_lin);
                phoneNumLin.setVisibility(View.GONE);
            }
            Picasso.with(this).load(NetWork.LOAD + data.getInstitutionIcon()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(infoHeaderImg);
            titleTxt.setText(data.getInstitutionName());
            Log.e("Daniel", "机构业务：：" + data.getBusiness());
            profileTxt.setText("业务：" + data.getBusiness());
            profileTxt.setVisibility(View.INVISIBLE);
            callLin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //电话
                    if (realm.where(User.class).findFirst() == null) {
                        Toast.makeText(CompanyInfoActivity.this, "请登录后使用", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Daniel", "机构城市：：" + Constan.LOCATION);
                        getCreditManagers(MainActivity.locationStr);

                    }
                }
            });
            callLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //电话
                    if (realm.where(User.class).findFirst() == null) {
                        Toast.makeText(CompanyInfoActivity.this, "请登录后使用", Toast.LENGTH_SHORT).show();
                    } else {
                        if (data.getContact() != null) {
                            Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getContact()));
                            intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentPhone);
                        } else {

                        }
                    }
                }
            });
            collectionLin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //收藏
                    if (realm.where(User.class).findAll() != null) {
                        saveOrUpdate();
                    } else {
                        Toast.makeText(getApplicationContext(), "请登录后再收藏", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (data.getIsCollect().equals("01")) {
                isCollectTxt.setText("已收藏");
                isCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.j13));
            } else {
                isCollectTxt.setText("未收藏");
                isCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.j12));
            }
            phoneNumTxt.setText("电话：" + data.getContact());
            addressTxt.setText("地址：" + data.getAddress());
            moreProfileLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更多简介
                    Intent intent = new Intent(CompanyInfoActivity.this, MoreProfileActivity.class);
                    intent.putExtra("Profile", data.getProfile());
                    startActivity(intent);
                }
            });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Daniel", "---onResume---" );
        Log.e("Daniel", "---onResume---" +data);
        setappImg();
    }

    private void setappImg() {
        if (data!=null){
            downloadInfo = downloadManager.getDownloadInfo(apkUrl);
            Log.e("Daniel", "---setappImg---" +downloadInfo);
            if (downloadInfo != null) {
                Log.e("Daniel", "setappImg-getTargetPath：：" + downloadInfo.getTargetPath());
                Log.e("Daniel", "setappImg-是否安装：：" + AppUtils.isInstallApp(CompanyInfoActivity.this, data.getApkPackage()));
                if (!AppUtils.isInstallApp(CompanyInfoActivity.this, data.getApkPackage())){
                    //判断是否有安装包
                    Log.e("Daniel", "setappImg-isFileExists：：" + FileUtils.isFileExists(downloadInfo.getTargetPath()));
                    if (FileUtils.isFileExists(downloadInfo.getTargetPath()) && data.getApkPackage() != null) {
                        setAppDownloadImg(2);
                    }else {
                        setAppDownloadImg(0);
                    }
                }else {
                    setAppDownloadImg(3);
                }
            }
        }
    }

    private void setApp() {
        //app下载显示与隐藏
        if (StringUtils.isEmpty(data.getApkUrl())) {
            appDownLin.setVisibility(View.GONE);
        } else {
            Log.e("Daniel", "下载地址：：" + data.getApkUrl());
            appDownLin.setVisibility(View.VISIBLE);
            appTitle.setText(data.getInstitutionName());
            if (!StringUtils.isEmpty(data.getIntegralNum() + "")) {
                appIntegarNum.setText("下载即送" + data.getIntegralNum() + "积分");
            }
            Picasso.with(this).load(NetWork.LOAD + data.getAppLogo()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(appLogoImg);
            //获取下载任务
            setappImg();
        }
    }

    //headview控件绑定
    private void findViewById() {
        appDownloadingImg = (ImageView) headerView.findViewById(R.id.appDownloanding_img);
        appInstallImg = (ImageView) headerView.findViewById(R.id.appinstall_img);
        appOpenImg = (ImageView) headerView.findViewById(R.id.appOpen_img);
        isCollectImg = (ImageView) headerView.findViewById(R.id.isCollect_img);
        goWebsiteLin = (LinearLayout) headerView.findViewById(R.id.go_website_lin);
        appDownLin = (LinearLayout) headerView.findViewById(R.id.appDownload_lin);
        appLogoImg = (ImageView) headerView.findViewById(R.id.institution_Logo_img);
        infoHeaderImg = (ImageView) headerView.findViewById(R.id.info_header_img);
        appTitle = (TextView) headerView.findViewById(R.id.institution_Title_tv);
        appIntegarNum = (TextView) headerView.findViewById(R.id.institution_integraNum_tv);
        titleTxt = (TextView) headerView.findViewById(R.id.title_txt);
        profileTxt = (TextView) headerView.findViewById(R.id.profile_txt);
        callLin1 = (LinearLayout) headerView.findViewById(R.id.call_lin1);
        collectionLin1 = (LinearLayout) headerView.findViewById(R.id.collection_lin1);
        callLin = (LinearLayout) headerView.findViewById(R.id.call_lin);
        collectionLin = (LinearLayout) headerView.findViewById(R.id.collection_lin);
        phoneNumTxt = (TextView) headerView.findViewById(R.id.phone_num_txt);
        addressTxt = (TextView) headerView.findViewById(R.id.address_txt);
        moreProfileLin = (LinearLayout) headerView.findViewById(R.id.more_profile_lin);
        isCollectTxt = (TextView) headerView.findViewById(R.id.is_collect_txt);
        appDownloadImg = (ImageView) headerView.findViewById(R.id.appDownload_img);
    }
    public void initCompanyFragment() {
        fragments = new ArrayList<>();
        ActivityLoaninancingLawListFragment activityLoaninancingLawListFragment1 = ActivityLoaninancingLawListFragment.newInstance(1);
        ActivityLoaninancingLawListFragment activityLoaninancingLawListFragment2 = ActivityLoaninancingLawListFragment.newInstance(2);
        ActivityLoaninancingLawListFragment activityLoaninancingLawListFragment3 =ActivityLoaninancingLawListFragment.newInstance(3);
        activityLoaninancingLawListFragment3.setActivityDatas(data.getActivities());
        activityLoaninancingLawListFragment1.setFinancingDatas(data.getFinancs());
        activityLoaninancingLawListFragment2.setLoanDatas(data.getLoans());
        fragments.add(activityLoaninancingLawListFragment1);
        fragments.add(activityLoaninancingLawListFragment2);
        fragments.add(activityLoaninancingLawListFragment3);
    }
    public void initLawFragment() {
        fragments = new ArrayList<>();
        ActivityLoaninancingLawListFragment activityLoaninancingLawListFragment =ActivityLoaninancingLawListFragment.newInstance(1);
        activityLoaninancingLawListFragment.setLawDatas(data.getLawyers());
        fragments.add(activityLoaninancingLawListFragment);
    }
    public void initCapitalFragment() {
        fragments = new ArrayList<>();
        ActivityLoaninancingLawListFragment activityLoaninancingLawListFragment1 = ActivityLoaninancingLawListFragment.newInstance(1);
        ActivityLoaninancingLawListFragment activityLoaninancingLawListFragment2 = ActivityLoaninancingLawListFragment.newInstance(2);
        activityLoaninancingLawListFragment1.setLoanDatas(data.getLoans());
        activityLoaninancingLawListFragment2.setActivityDatas(data.getActivities());
        fragments.add(activityLoaninancingLawListFragment1);
        fragments.add(activityLoaninancingLawListFragment2);
    }
    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(4);
        Log.i("gqf","CompanyInfoActivity"+index);
        if(index==2){
            mTitles=mCompanyTitles;
       }else if(index==3){
            mTitles=mCapitalTitles;
        }else{
            mTitles=mLawTitles;
        }


        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                coordinatortablayout.onTouchEvent(event);

                return false;
            }
        });
    }
    public void saveOrUpdate(){
        String userId="";
        if(realm.where(User.class).findFirst()!=null){
            userId=realm.where(User.class).findFirst().getUserId()+"";
        }else{
            Toast.makeText(getApplicationContext(),"请登录后再收藏",Toast.LENGTH_SHORT).show();
            return;
        }
        String isCollect="";
        if(data.getIsCollect().equals("01")){
            isCollect="02";
        }else {
            isCollect="01";
        }


        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().saveOrUpdate(InstitutionId,
                "0"+(index+1),userId,isCollect)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        if(resultCode.getCode()==ResultCode.SECCESS){
                            if(data.getIsCollect().equals("01")){
                                data.setIsCollect("02");
                                isCollectTxt.setText("未收藏");
                                isCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.j12));
                            }else {
                                data.setIsCollect("01");
                                isCollectTxt.setText("已收藏");
                                isCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.j13));
                            }
                        }
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }

}
