package tqm.bianfeng.com.tqm.User.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import tqm.bianfeng.com.tqm.User.Fragment.MyCollectionLawAndCompanyFragment;

/**
 * Created by wjy on 16/8/15.
 *
 */
public class MyCollectionViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> datas;

    public MyCollectionViewPagerAdapter(FragmentManager fm) {
        super(fm);
        datas=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment= MyCollectionLawAndCompanyFragment.newInstance(position);

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "律师事务所";
            case 1:
                return "金融机构";
            case 2:
                return "民间资本";
        }
        return super.getPageTitle(position);
    }
}
