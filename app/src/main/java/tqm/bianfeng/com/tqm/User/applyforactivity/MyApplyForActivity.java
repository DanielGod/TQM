package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.MyApplyForViewPagerAdapter;
import tqm.bianfeng.com.tqm.application.BaseAppCompatActivity;

/**
 * Created by 王九东 on 2017/7/20.
 */

public class MyApplyForActivity extends BaseAppCompatActivity  {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_applyfor);
        unbinder = ButterKnife.bind(this);
        setToolbar(toolbar,"申请进度");

        MyApplyForViewPagerAdapter myApplyForViewPagerAdapter = new MyApplyForViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(myApplyForViewPagerAdapter);
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
