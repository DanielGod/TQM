package tqm.bianfeng.com.tqm.Institutions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.CoordinatorTabLayout;
import tqm.bianfeng.com.tqm.CustomView.MyViewPager;
import tqm.bianfeng.com.tqm.Institutions.adapter.MyPagerAdapter;
import tqm.bianfeng.com.tqm.Institutions.listener.LoadHeaderImagesListener;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.CorrectOrReportActivity;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.LawFirmOrInstitutionDetail;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;

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

    public static int index=-1;//1律师//2机构//3民资

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        ButterKnife.bind(this);

        InstitutionId = getIntent().getIntExtra("InstitutionId", 0);
        initHeaderRootView();

        Log.i("gqf","CompanyInfoActivity"+index);
        if(index==2||index==3){
            initCompanyInfoData(InstitutionId);
        }else{
            initLawInfoData(InstitutionId);
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
                        Log.i("gqf","data"+data.toString());
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

    public void initHeaderView() {
        headerView = getLayoutInflater().from(this).inflate(R.layout.company_info_header_view, null, true);
        infoHeaderImg=(ImageView)headerView.findViewById(R.id.info_header_img);
        titleTxt=(TextView) headerView.findViewById(R.id.title_txt);
        profileTxt=(TextView) headerView.findViewById(R.id.profile_txt);
        callLin=(LinearLayout) headerView.findViewById(R.id.call_lin);
        collectionLin=(LinearLayout) headerView.findViewById(R.id.collection_lin);
        phoneNumTxt=(TextView) headerView.findViewById(R.id.phone_num_txt);
        addressTxt=(TextView) headerView.findViewById(R.id.address_txt);
        moreProfileLin=(LinearLayout) headerView.findViewById(R.id.more_profile_lin);
        isCollectTxt=(TextView) headerView.findViewById(R.id.is_collect_txt);
        if(realm.where(User.class).findFirst()==null){
            phoneNumLin=(LinearLayout) headerView.findViewById(R.id.phone_num_lin);
            phoneNumLin.setVisibility(View.GONE);
        }
        Picasso.with(this).load(NetWork.LOAD+data.getInstitutionIcon()).placeholder(R.drawable.ic_img_loading).error(R.drawable.ic_img_loading).into(infoHeaderImg);
        titleTxt.setText(data.getInstitutionName());
        profileTxt.setText("简介："+data.getProfile());
        profileTxt.setVisibility(View.INVISIBLE);
        callLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电话
                if(realm.where(User.class).findFirst()==null){
                    Toast.makeText(CompanyInfoActivity.this, "请登录后使用", Toast.LENGTH_SHORT).show();
                }else {
                    if (data.getContact() != null) {
                        Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getContact()));
                        intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentPhone);
                    } else {

                    }
                }
            }
        });
        collectionLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收藏
                if(realm.where(User.class).findAll()!=null){
                    saveOrUpdate();
                }else{
                    Toast.makeText(getApplicationContext(), "请登录后再收藏", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(data.getIsCollect().equals("01")){
            isCollectTxt.setText("已收藏");
        }else {
            isCollectTxt.setText("未收藏");
        }


        phoneNumTxt.setText("电话："+data.getContact());
        addressTxt.setText("地址："+data.getAddress());
        moreProfileLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更多简介
                Intent intent = new Intent(CompanyInfoActivity.this,MoreProfileActivity.class);
                intent.putExtra("Profile",data.getProfile());
                startActivity(intent);
            }
        });
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
        Log.i("gqf","getLawyers"+data.getLawyers());
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
                            }else {
                                data.setIsCollect("01");
                                isCollectTxt.setText("已收藏");
                            }
                        }
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }

}
