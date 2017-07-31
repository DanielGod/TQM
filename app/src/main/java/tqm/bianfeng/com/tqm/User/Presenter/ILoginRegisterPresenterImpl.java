package tqm.bianfeng.com.tqm.User.Presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

    public ILoginRegisterPresenterImpl(ILoginAndRegistered iLoginAndRegistered) {
        super();
        this.iLoginAndRegistered = iLoginAndRegistered;
        handler = new Handler(Looper.getMainLooper());
    }
    public void setOldCode(String phone,boolean isGet) {
        if(isGet){
            //网络获取验证码
            Subscription subscription = NetWork.getUserService().shortMsg(phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            iLoginAndRegistered.loginOrRegisteredResult(0,false,"无法获取验证码，请检查网络");
                            iLoginAndRegistered.shouNetWorkActivity();
                        }

                        @Override
                        public void onNext(String s) {
                            Log.e("gqf","onNext"+s);
                            if(s==null||s.equals("")){
                                iLoginAndRegistered.loginOrRegisteredResult(0,false,"请您验证您的手机号码是否正确");
                            }else{
                                oldCode=s;
                            }
                        }
                    });
            compositeSubscription.add(subscription);
        }else{
            oldCode="";
        }
    }

    //验证并注册或登录
    public void loginOrRegister(String phone,String code,String channel){
        // TODO: 2017/7/4 验证码
        if(oldCode.equals("")||!oldCode.equals(code)){
            //验证码错误
            iLoginAndRegistered.loginOrRegisteredResult(1,false,"验证码不正确");
        }else{
            //网络验证
            Subscription subscription = NetWork.getUserService().register(phone,channel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResultCodeWithUser>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            iLoginAndRegistered.loginOrRegisteredResult(0,false,"注册或登录超时，请检查网路设置");
                            iLoginAndRegistered.shouNetWorkActivity();
                        }

                        @Override
                        public void onNext(ResultCodeWithUser resuleCodeWithUser) {
                            String tosat="";
                            if(resuleCodeWithUser.getCode()== ResultCode.SECCESS){
                                //注册
                                tosat="注册";
                            }else{
                                //登录
                                tosat="登录";
                            }
                            if(resuleCodeWithUser.getUser()!=null){
                                //重置验证码
                                oldCode="";
                                //保存用户信息
                                realm.beginTransaction();
                                realm.insertOrUpdate(resuleCodeWithUser.getUser());
                                realm.commitTransaction();
                                //页面显示
                                iLoginAndRegistered.loginOrRegisteredResult(1,true,tosat+"成功");
                            }else{
                                iLoginAndRegistered.loginOrRegisteredResult(1,true,tosat+"失败");
                            }
                        }
                    });
            compositeSubscription.add(subscription);
        }
    }


    public void onClose(){
        super.onClose();
        oldCode="";
    }



}
