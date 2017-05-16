package tqm.bianfeng.com.tqm.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.MyCollectionViewPagerAdapter;
import tqm.bianfeng.com.tqm.application.BaseActivity;


/**
 * Created by johe on 2017/4/28.
 */

public class MyCollectionActivity extends BaseActivity {
    @BindView(R.id.collection_toolbar)
    Toolbar collectionToolbar;
    @BindView(R.id.collection_tablayout)
    TabLayout collectionTablayout;
    @BindView(R.id.collection_viewpager)
    ViewPager collectionViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        ButterKnife.bind(this);
        setToolbar(collectionToolbar, "我的收藏");
        MyCollectionViewPagerAdapter myCollectionViewPagerAdapter=new MyCollectionViewPagerAdapter(getSupportFragmentManager());
        collectionViewpager.setAdapter(myCollectionViewPagerAdapter);
        collectionTablayout.setupWithViewPager(collectionViewpager);
        EventBus.getDefault().register(this);
    }
    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void detailActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
