package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.MyBroseViewPagerAdapter;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;

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
    MyBroseViewPagerAdapter myBroseViewPagerAdapter;

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

                if (item.getItemId() == R.id.clean) {
                    delectHistory();
                }


                return false;
            }
        });
        initView();
    }
    boolean isClean=false;
    public void delectHistory() {
        if(isClean==false) {
            isClean=true;
            showLoading(0);
            Subscription subscription = NetWork.getUserService().deleteHistory(realm.where(User.class).findFirst().getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            showLoading(1);
                            Toast.makeText(getApplicationContext(), "服务器问题，清除失败",Toast.LENGTH_SHORT).show();
                            Log.i("gqf","onError"+e.toString());
                        }

                        @Override
                        public void onNext(ResultCode resultCode) {
                            showLoading(1);
                            if (resultCode.getCode() == ResultCode.SECCESS) {
                                initView();
                            } else {
                                Toast.makeText(getApplicationContext(), "清除失败，请重试",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            compositeSubscription.add(subscription);
        }
    }

    public void initView() {
        myBroseViewPagerAdapter = new MyBroseViewPagerAdapter(getSupportFragmentManager());
        browseViewpager.setAdapter(myBroseViewPagerAdapter);
        browseTablayout.setupWithViewPager(browseViewpager);
    }
    SweetAlertDialog pDialog;
    CountDownTimer countDownTimer;
    int i=6;
    public void showLoading(int index) {
        if (index == 0) {
            //开始
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("清空中...");
            pDialog.show();
            pDialog.setCancelable(false);
            countDownTimer = new CountDownTimer(1000 * 100, 1000) {
                public void onTick(long millisUntilFinished) {
                    // you can change the progress bar color by ProgressHelper every 800 millis
                    i++;
                    switch (i % 6) {
                        case 0:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                            break;
                        case 1:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                            break;
                        case 2:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                        case 3:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                            break;
                        case 4:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                            break;
                        case 5:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                            break;
                        case 6:
                            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                    }
                }

                public void onFinish() {
                    i = 6;
                }
            }.start();

        } else {
            //结束
            pDialog.dismiss();
            countDownTimer.onFinish();
            isClean=false;
        }
    }

}
