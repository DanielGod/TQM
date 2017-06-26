package tqm.bianfeng.com.tqm.Institutions;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.Institutions.adapter.InstitutionsViewPagerAdapter;
import tqm.bianfeng.com.tqm.R;


public class InstitutionsInFragment extends Fragment {


    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static InstitutionsInFragment newInstance() {
        InstitutionsInFragment fragment = new InstitutionsInFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_institutions_in, container, false);
        ButterKnife.bind(this, view);
        InstitutionsViewPagerAdapter institutionsViewPagerAdapter=new InstitutionsViewPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(institutionsViewPagerAdapter);
        tablayout.setupWithViewPager(viewpager);

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
