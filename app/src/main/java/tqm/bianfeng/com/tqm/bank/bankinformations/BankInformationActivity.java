package tqm.bianfeng.com.tqm.bank.bankinformations;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.bank.bankinformations.test.RecyclerViewFragment;
import tqm.bianfeng.com.tqm.network.NetWork;

public class BankInformationActivity extends AppCompatActivity implements RecyclerViewFragment.mListener {

    @BindView(R.id.home_slider)
    SliderLayout homeSlider;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_bankInformation_tablayout)
    TabLayout mainBankInformationTablayout;
    @BindView(R.id.main_bankInformation_viewpager)
    ViewPager mainBankInformationViewpager;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivDeleteText)
    ImageView ivDeleteText;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.scrollview)
    CoordinatorLayout scrollview;
    @BindView(R.id.cotainer)
    LinearLayout cotainer;

    private CompositeSubscription mCompositeSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_information2);
        ButterKnife.bind(this);
        mCompositeSubscription = new CompositeSubscription();
        setToolBar(getResources().getString(R.string.bankInfromations));
        //        initImagesData();
        initImagesData();
        initBankInformationTablayout();

    }

    private void setToolBar(String s) {
        toolbar.setTitle(s);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mainBankInformationTablayout.setBackgroundColor(getResources().getColor(R.color.white));

    }

    private void initBankInformationTablayout() {
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        mainBankInformationViewpager.setAdapter(viewpagerAdapter);
        mainBankInformationTablayout.setupWithViewPager(mainBankInformationViewpager);
    }

    public void initImagesData() {
        //获取资讯轮播图
        Subscription subscription = NetWork.getUserService().getImages("02")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        initImages(strings);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

//    public void initImages(List<String> strings) {
//        //加载首页轮播图
//        if (strings.size() > 0) {
//            for (String url : strings) {
//                DefaultSliderView textSliderView = new DefaultSliderView(BankInformationActivity.this);
//                textSliderView.image(NetWork.LOAD + url)
//                        .setScaleType(BaseSliderView.ScaleType.Fit);
//                homeSlider.addSlider(textSliderView);
//            }
//        }
//    }

    public void initImages(List<String> strings) {
        //加载首页轮播图
        if (strings.size() > 0) {
            for (String url : strings) {
                Log.i("gqf", "strings" + url);
                DefaultSliderView textSliderView = new DefaultSliderView(BankInformationActivity.this);
                textSliderView.image(NetWork.LOAD + url)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                homeSlider.addSlider(textSliderView);
            }
        }
        //        int size = mSlider.length;
        //        for (int i = 0; i <size ; i++) {
        //
        //            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
        //            textSliderView.image(mSlider[i]).setScaleType(BaseSliderView.ScaleType.Fit);
        //            homeSlider.addSlider(textSliderView);
        //        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void detailActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void initEdi() {

    }
}
