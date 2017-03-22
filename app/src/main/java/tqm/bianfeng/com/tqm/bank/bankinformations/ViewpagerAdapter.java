package tqm.bianfeng.com.tqm.bank.bankinformations;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tqm.bianfeng.com.tqm.bank.bankinformations.test.RecyclerViewFragment;

/**
 * Created by wjy on 16/8/15.
 *
 */
public class ViewpagerAdapter extends FragmentPagerAdapter {
    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return RecyclerViewFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "银行资讯";
            case 1:
                return "热点资讯";
            case 2:
                return "财富资讯";
        }
        return super.getPageTitle(position);
    }
}
