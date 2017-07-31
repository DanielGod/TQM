package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.umeng.socialize.utils.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.UserCardRecodeAdapter;
import tqm.bianfeng.com.tqm.User.adapter.UserCorrectErrorAdapter;
import tqm.bianfeng.com.tqm.User.adapter.UserIntegarlAdapter;
import tqm.bianfeng.com.tqm.User.adapter.UserTransactionRecordsAdapter;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.Card;
import tqm.bianfeng.com.tqm.pojo.PayTrans;
import tqm.bianfeng.com.tqm.pojo.Reports;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.UserPointA;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;


/**
 * 积分，名片收集，纠错，交易记录
 * Created by 王九东 on 2017/7/14.
 */

public class UserIntegralActivity extends BaseActivity {
    @BindView(R.id.release_toolbar)
    Toolbar releaseToolbar;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Integer tag;
    @BindView(R.id.userintegral_rv)
    RecyclerView userintegralRv;
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;
User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userintegral);
        ButterKnife.bind(this);
        tag = getIntent().getIntExtra("tag", -1);
        defaultLoadview.lodingIsFailOrSucess(Constan.LOADING);
        Log.e(Constan.LOGTAGNAME, "---tag---" + tag);
        Logger.i(Constan.LOGTAGNAME, tag);
        user = realm.where(User.class).findFirst();
        if (user!=null){
            initView(tag, user.getUserId());
        }else {
            Toast.makeText(this, "登录后才能查看积分", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView(Integer tag, Integer userId) {
        switch (tag) {
            case 0:
                setToolbar(releaseToolbar, "名片收集");
                getCard(userId);
                break;
            case 1:
                setToolbar(releaseToolbar, "纠错记录");
                getReports(userId);
                break;
            case 2:
                setToolbar(releaseToolbar, "交易记录");
                getPayTrans(userId);
                break;
            case 3:
                setToolbar(releaseToolbar, "积分明细");
                getUserPointItem(userId);
                break;
        }
        //        mFragmentManager = getSupportFragmentManager();//获取到fragment的管理对象
        //        setClick(tag);
    }

    private void getPayTrans(Integer userId) {
        Subscription subscription = NetWork.getUserService()
                .getPayTrans(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PayTrans>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Constan.log("交易记录异常---"+e.toString());
                defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_FAILED);
            }

            @Override
            public void onNext(List<PayTrans> payTrans) {
                Constan.log("payTrans---"+payTrans.toString());
                if (payTrans.size()==0){
                    defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_NULL);
                }else {
                    defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
                    setPayTransAdapter(payTrans);
                }


            }
        });
        compositeSubscription.add(subscription);
    }

    UserTransactionRecordsAdapter userTransactionRecordsAdapter;

    private void setPayTransAdapter(List<PayTrans> payTrans) {
        if (userTransactionRecordsAdapter == null) {
            userTransactionRecordsAdapter = new UserTransactionRecordsAdapter(UserIntegralActivity.this, payTrans);
            userintegralRv.setLayoutManager(new LinearLayoutManager(UserIntegralActivity.this));
            userintegralRv.setAdapter(userTransactionRecordsAdapter);
        } else {
            userTransactionRecordsAdapter.notifyData(payTrans);
        }

    }

    private void getReports(Integer userId) {
        Subscription subscription = NetWork.getUserService().getReports(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Reports>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_FAILED);
            }

            @Override
            public void onNext(List<Reports> reports) {

                if (reports.size()==0){
                    defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_NULL);
                }else {
                    defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
                    setUserReportsAdapter(reports);
                }

            }
        });
        compositeSubscription.add(subscription);
    }

    UserCorrectErrorAdapter userCorrectErrorAdapter;

    private void setUserReportsAdapter(List<Reports> reports) {
        if (userCorrectErrorAdapter == null) {
            userCorrectErrorAdapter = new UserCorrectErrorAdapter(UserIntegralActivity.this, reports);
            userintegralRv.setLayoutManager(new LinearLayoutManager(UserIntegralActivity.this));
            userintegralRv.setAdapter(userCorrectErrorAdapter);
        } else {
            userCorrectErrorAdapter.notifyData(reports);
        }
    }

    private void getCard(Integer userId) {
        Subscription subscription = NetWork.getUserService().getCard(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Card>>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_FAILED);
            }
            @Override
            public void onNext(List<Card> cards) {
                if (cards.size()==0){
                    defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_NULL);
                }else {
                    Logger.i(Constan.LOCATION, cards.toString());
                    defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
                    setUserCardAdapter(cards);
                }


            }
        });
        compositeSubscription.add(subscription);
    }

    UserCardRecodeAdapter userCardRecodeAdapter;

    private void setUserCardAdapter(List<Card> cards) {
        if (userCardRecodeAdapter == null) {
            userCardRecodeAdapter = new UserCardRecodeAdapter(UserIntegralActivity.this, cards);
            userintegralRv.setLayoutManager(new LinearLayoutManager(UserIntegralActivity.this));
            userintegralRv.setAdapter(userCardRecodeAdapter);
        } else {
            userCardRecodeAdapter.notifyData(cards);
        }
    }

    private void getUserPointItem(int userId) {
        Subscription subscription = NetWork.getUserService().getUserPointItem(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<UserPointA>>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_FAILED);
            }
            @Override
            public void onNext(List<UserPointA> userPointAs) {
                if (userPointAs.size()==0){
                    defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_NULL);
                }else {
                      defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
                    setUserPointAdapter(userPointAs);
                }
            }
        });
        compositeSubscription.add(subscription);
    }
    UserIntegarlAdapter userIntegralAdapter;
    private void setUserPointAdapter(List<UserPointA> userPointAs) {
        Log.e(Constan.LOGTAGNAME, "---userPointAs---" + userPointAs.toString());
        Logger.i(Constan.LOGTAGNAME, userPointAs.toString());
        if (userIntegralAdapter == null) {
            userIntegralAdapter = new UserIntegarlAdapter(UserIntegralActivity.this, userPointAs);
            userintegralRv.setLayoutManager(new LinearLayoutManager(UserIntegralActivity.this));
            userintegralRv.setAdapter(userIntegralAdapter);
        } else {
            userIntegralAdapter.notifyData(userPointAs);
        }
    }
}
