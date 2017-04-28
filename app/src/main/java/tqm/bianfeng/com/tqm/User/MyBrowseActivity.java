package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.MyBroseViewPagerAdapter;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/4/28.
 */

public class MyBrowseActivity extends BaseActivity {
    @BindView(R.id.browse_toolbar)
    Toolbar browseToolbar;
    @BindView(R.id.browse_tablayout)
    TabLayout browseTablayout;
    @BindView(R.id.browse_viewpager)
    ViewPager browseViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browse);
        ButterKnife.bind(this);
        setToolbar(browseToolbar, "浏览记录");
        browseToolbar.inflateMenu(R.menu.browse_clean);
        browseToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {



                return false;
            }
        });
        MyBroseViewPagerAdapter myBroseViewPagerAdapter=new MyBroseViewPagerAdapter(getSupportFragmentManager());
        browseViewpager.setAdapter(myBroseViewPagerAdapter);
        browseTablayout.setupWithViewPager(browseViewpager);
    }
}
