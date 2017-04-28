package tqm.bianfeng.com.tqm.User.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import tqm.bianfeng.com.tqm.User.Fragment.MyFocuseFragment;

/**
 * Created by wjy on 16/8/15.
 *
 */
public class MyFocuseViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> datas;

    public MyFocuseViewPagerAdapter(FragmentManager fm) {
        super(fm);
        datas=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment= MyFocuseFragment.newInstance(position);

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "活动";
            case 1:
                return "理财";
            case 2:
                return "贷款";
            case 3:
                return "律师";
            case 4:
                return "资讯";
        }
        return super.getPageTitle(position);
    }
}
