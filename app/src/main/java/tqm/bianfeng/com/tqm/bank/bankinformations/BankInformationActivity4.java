package tqm.bianfeng.com.tqm.bank.bankinformations;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.bank.bankinformations.test.RecyclerViewFragment;

public class BankInformationActivity4 extends AppCompatActivity implements RecyclerViewFragment.mListener {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private CompositeSubscription mCompositeSubscription;
    private String intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_information4);
        ButterKnife.bind(this);
        mCompositeSubscription = new CompositeSubscription();
        Log.e("Daniel", "----intent---" + intent);
        intent = getIntent().getStringExtra("financialClass");
        //        initImagesData();

        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewpagerAdapter);
        tablayout.setupWithViewPager(viewpager);
        if (intent.equals("financialClass")) {
            Log.e("Daniel", "----intent---" + intent);
            viewpager.setCurrentItem(1);
            //            tablayout.setSelected(true);
        }


    }



    @Override
    public void detailActivity(Intent intent) {
        startActivity(intent);

    }

    @Override
    public void initEdi() {

    }


}
