package tqm.bianfeng.com.tqm.User.Presenter;

import android.os.Handler;
import android.os.Looper;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.User.View.ILoginAndRegistered;
import tqm.bianfeng.com.tqm.application.BasePresenterImpl;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithUser;

/**
 * Created by johe on 2017/3/13.
 */

public class ILoginRegisterPresenterImpl extends BasePresenterImpl implements ILoginRegisterPresenter{
    ILoginAndRegistered iLoginAndRegistered;
    Handler handler;
    String oldCode="";

    public void setOldCode(boolean isGet) {
        if(isGet){
            //网络获取验证码


        }else{
            oldCode="";
        }
    }
    public ILoginRegisterPresenterImpl(ILoginAndRegistered iLoginAndRegistered) {
        super();
        this.iLoginAndRegistered = iLoginAndRegistered;
        handler = new Handler(Looper.getMainLooper());
    }
    public void loginOrRegister(String phone,String code){
        if(oldCode.equals("")||!oldCode.equals(code)){
            //验证码错误
            iLoginAndRegistered.loginOrRegisteredResult(false,"验证码不正确");
        }else{
            //网络验证
            Subscription subscription = NetWork.getUserService().register(phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCodeWithUser>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ResultCodeWithUser resuleCodeWithUser) {
                            if(resuleCodeWithUser.getCode()== ResultCode.SECCESS){

                                realm.beginTransaction();
                                realm.insertOrUpdate(resuleCodeWithUser.getUser());
                                realm.commitTransaction();
                                iLoginAndRegistered.loginOrRegisteredResult(true,"注册成功");
                            }else{
                                iLoginAndRegistered.loginOrRegisteredResult(false,"注册失败");
                            }
                        }
                    });
            compositeSubscription.add(subscription);
        }
    }

    public void onClose(){
        super.onClose();
    }



}
