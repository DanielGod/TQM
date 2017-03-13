package tqm.bianfeng.com.tqm.User.Presenter;

import android.os.Handler;
import android.os.Looper;

import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.User.View.ILoginAndRegistered;

/**
 * Created by johe on 2017/3/13.
 */

public class ILoginRegisterPresenterImpl implements ILoginRegisterPresenter{
    ILoginAndRegistered iLoginAndRegistered;
    Handler handler;
    String oldCode="";
    CompositeSubscription compositeSubscription;
    public void setOldCode(boolean isGet) {
        if(isGet){
            //网络获取验证码


        }else{
            oldCode="";
        }
    }
    public ILoginRegisterPresenterImpl(ILoginAndRegistered iLoginAndRegistered) {
        this.iLoginAndRegistered = iLoginAndRegistered;
        handler = new Handler(Looper.getMainLooper());
        compositeSubscription=new CompositeSubscription();
    }
    public void loginOrRegister(String phone,String code){
        if(oldCode.equals("")||!oldCode.equals(code)){
            //验证码错误
            iLoginAndRegistered.loginOrRegisteredResult(false,"验证码不正确");
        }else{
            //网络验证

            iLoginAndRegistered.loginOrRegisteredResult(false,"注册失败");

            iLoginAndRegistered.loginOrRegisteredResult(true,"注册成功");
        }
    }

    public void onClose(){
        compositeSubscription.unsubscribe();
    }
}
