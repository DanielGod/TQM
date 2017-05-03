package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.MyFocuseViewPagerAdapter;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/4/28.
 */

public class MyFocusActivity extends BaseActivity {

    @BindView(R.id.focuse_toolbar)
    Toolbar focuseToolbar;
    @BindView(R.id.focuse_tablayout)
    TabLayout focuseTablayout;
    @BindView(R.id.focuse_viewpager)
    ViewPager focuseViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_focus);
        ButterKnife.bind(this);
        setToolbar(focuseToolbar, "我的关注");
        MyFocuseViewPagerAdapter myFocuseViewPagerAdapter=new MyFocuseViewPagerAdapter(getSupportFragmentManager());
        focuseViewpager.setAdapter(myFocuseViewPagerAdapter);
        focuseTablayout.setupWithViewPager(focuseViewpager);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
