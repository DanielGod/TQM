package tqm.bianfeng.com.tqm.User.release;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.MyReleaseViewPagerAdapter;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/5/10.
 */

public class MyReleaseActivity extends BaseActivity {

    @BindView(R.id.my_release_toolbar)
    Toolbar myReleaseToolbar;
    @BindView(R.id.release_tablayout)
    TabLayout releaseTablayout;
    @BindView(R.id.release_viewpager)
    ViewPager releaseViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release);
        ButterKnife.bind(this);
        setToolbar(myReleaseToolbar, "我的发布");
        MyReleaseViewPagerAdapter myFocuseViewPagerAdapter=new MyReleaseViewPagerAdapter(getSupportFragmentManager());
        releaseViewpager.setAdapter(myFocuseViewPagerAdapter);
        releaseTablayout.setupWithViewPager(releaseViewpager);

    }

}
