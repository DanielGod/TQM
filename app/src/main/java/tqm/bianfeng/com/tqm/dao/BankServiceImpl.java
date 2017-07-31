package tqm.bianfeng.com.tqm.dao;

import android.util.Log;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.application.BasePresenterImpl;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.YwDksq;
import tqm.bianfeng.com.tqm.service.Bank;

/**
 * Created by 王九东 on 2017/7/5.
 */

public class BankServiceImpl extends BasePresenterImpl implements Bank {
    YwDksq mYwDksq;
    public YwDksq getOne(Integer userId) {
        Subscription subscription = NetWork.getBankService().getOne(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwDksq>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Dani", "快速贷款信息onError：" + e.toString());
                    }
                    @Override
                    public void onNext(YwDksq ywDksq) {
                        mYwDksq = ywDksq;
                    }
                });
        compositeSubscription.add(subscription);
        return mYwDksq;
    }
}
