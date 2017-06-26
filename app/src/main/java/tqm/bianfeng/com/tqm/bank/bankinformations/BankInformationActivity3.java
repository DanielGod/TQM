package tqm.bianfeng.com.tqm.bank.bankinformations;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.bank.bankinformations.test.RecyclerViewFragment;
import tqm.bianfeng.com.tqm.network.NetWork;

public class BankInformationActivity3 extends AppCompatActivity implements RecyclerViewFragment.mListener {
    @BindView(R.id.home_slider)
    SliderLayout homeSlider;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivDeleteText)
    ImageView ivDeleteText;
    @BindView(R.id.top)
    RelativeLayout top;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CompositeSubscription mCompositeSubscription;
    private String intent;
    private int[] mSlider = {R.drawable.information_top_slider1,R.drawable.information_top_slider2,R.drawable.information_top_slider3};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_information3);
        ButterKnife.bind(this);
        mCompositeSubscription = new CompositeSubscription();
        Log.e("Daniel","----intent---"+intent);
        intent = getIntent().getStringExtra("financialClass");
//        initImagesData();
        initImages();
        setToolBar("");
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewpagerAdapter);
        tablayout.setupWithViewPager(viewpager);
        if (intent.equals("financialClass")){
            Log.e("Daniel","----intent---"+intent);
            viewpager.setCurrentItem(1);
//            tablayout.setSelected(true);
        }
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etSearch.setFocusableInTouchMode(true);
                return false;
            }
        });

    }

    @OnClick({R.id.etSearch, R.id.ivDeleteText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etSearch:
                break;

            case R.id.ivDeleteText:
                etSearch.setText("");
                ivDeleteText.setVisibility(View.GONE);
                etSearch.setFocusableInTouchMode(false);
                EventBus.getDefault().post(new EditStr(""));

                break;


        }
    }
    /**
     * 查询
     */
    public void initEdi() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @DebugLog
            @Override
            public void afterTextChanged(Editable editable) {
                if ("".equals(etSearch.getText().toString())) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
                EventBus.getDefault().post(new EditStr(editable.toString()));
//                initDate(0, Constan.NOTPULLUP, editable.toString(), null);

            }
        });
    }



    private void setToolBar(String s) {
//        toolbar.setTitle(s);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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
//                        initImages(strings);
                    }
                });
        mCompositeSubscription.add(subscription);
    }
    public void initImages() {
        //加载首页轮播图
//        if (strings.size() > 0) {
//            for (String url : strings) {
//                Log.i("gqf", "strings" + url);
//                DefaultSliderView textSliderView = new DefaultSliderView(BankInformationActivity3.this);
//                textSliderView.image(NetWork.LOAD + url)
//                        .setScaleType(BaseSliderView.ScaleType.Fit);
//                homeSlider.addSlider(textSliderView);
//            }
//        }
                int size = mSlider.length;
                for (int i = 0; i <size ; i++) {

                    DefaultSliderView textSliderView = new DefaultSliderView(BankInformationActivity3.this);
                    textSliderView.image(mSlider[i]).setScaleType(BaseSliderView.ScaleType.Fit);
                    homeSlider.addSlider(textSliderView);
                }
    }

    @Override
    public void detailActivity(Intent intent) {
        startActivity(intent);

    }


}
