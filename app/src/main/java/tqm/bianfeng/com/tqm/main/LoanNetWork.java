package tqm.bianfeng.com.tqm.main;

import android.util.Log;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.YwDksq;

/**
 * Created by 王九东 on 2017/6/29.
 */

public class LoanNetWork implements LoanInterface {
    YwDksq mYwDksq;

    @Override
    public YwDksq getOne(int userId) {
       NetWork.getBankService().getOne(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwDksq>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Dani", "快速贷款信息onError：" + e.toString());
                        //                        defaultLoadview.lodingIsFailOrSucess(3);
                    }
                    @Override
                    public void onNext(YwDksq ywDksq) {
                        Log.e("Dani", "快速贷款信息onNext：" + ywDksq.toString());
                        //                        defaultLoadview.lodingIsFailOrSucess(2);
                        mYwDksq = ywDksq;
                    }
                });
        return mYwDksq;
    }

}
