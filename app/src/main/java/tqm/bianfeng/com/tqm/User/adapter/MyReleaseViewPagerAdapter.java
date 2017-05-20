package tqm.bianfeng.com.tqm.User.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import tqm.bianfeng.com.tqm.User.Fragment.MyReleaseFragment;

/**
 * Created by wjy on 16/8/15.
 *
 */
public class MyReleaseViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> datas;

    public MyReleaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
        datas=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment= MyReleaseFragment.newInstance(position);

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "贷款信息";
            case 1:
                return "活动信息";
        }
        return super.getPageTitle(position);
    }
}
