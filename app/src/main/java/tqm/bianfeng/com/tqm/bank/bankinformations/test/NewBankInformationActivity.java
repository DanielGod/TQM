package tqm.bianfeng.com.tqm.bank.bankinformations.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.bank.bankinformations.ViewpagerAdapter;

public class NewBankInformationActivity extends AppCompatActivity implements RecyclerViewFragment.mListener {
    private MaterialViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bank_information);
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        final TextView logo = (TextView) findViewById(R.id.logo_white);
        setTitle("");
        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();

                }
            });
        }
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setAdapter(viewpagerAdapter);
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        logo.setText(getResources().getString(R.string.bankInfromations));
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 1:
                        logo.setText(getResources().getString(R.string.hotInfromations));
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://mpic.tiankong.com/ced/bf9/cedbf90668823a39b4da08499bbcf860/640.jpg@360h");
                    case 2:
                        logo.setText(getResources().getString(R.string.moneyInfromations));
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                }
                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());


        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void detailActivity(Intent intent) {
        startActivity(intent);
    }
}
