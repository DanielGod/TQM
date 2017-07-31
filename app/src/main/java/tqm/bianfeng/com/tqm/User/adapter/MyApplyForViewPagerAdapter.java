package tqm.bianfeng.com.tqm.User.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import tqm.bianfeng.com.tqm.User.applyforactivity.MyApplyForFragment;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by wjy on 16/8/15.
 *
 */
public class MyApplyForViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> datas;

    public MyApplyForViewPagerAdapter(FragmentManager fm) {
        super(fm);
        datas=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Constan.log("测试position："+position);
        Fragment fragment = MyApplyForFragment.newInstance(position);
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
                return "入驻申请";
            case 1:
                return "贷款申请";
        }
        return super.getPageTitle(position);
    }
}
