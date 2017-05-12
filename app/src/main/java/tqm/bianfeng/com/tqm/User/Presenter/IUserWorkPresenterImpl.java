package tqm.bianfeng.com.tqm.User.Presenter;

import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.User.View.ILoginAndRegistered;
import tqm.bianfeng.com.tqm.application.BasePresenterImpl;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.MyAttention;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithUser;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithUserHeadImg;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.result.ResultWithAuditCode;

/**
 * Created by johe on 2017/3/14.
 */

public class IUserWorkPresenterImpl extends BasePresenterImpl implements IUserWorkPresenter{

    ILoginAndRegistered iLoginAndRegistered;

    public IUserWorkPresenterImpl(ILoginAndRegistered iLoginAndRegistered){
        super();
        this.iLoginAndRegistered=iLoginAndRegistered;
    }
    public void getMyFocusMsgNum(int userId){
        //网络获取关注数量
        Subscription subscription = NetWork.getUserService().getMyAttention(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyAttention>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MyAttention myAttention) {
                        Log.e("gqf","onNext"+myAttention.toString());
                    }
                });
        compositeSubscription.add(subscription);
    }
    //头像上传
    public void uploadUserHeadImg(File img,int userId){
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), img);
        Log.i("gqf", "bm==null1");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", img.getName(), photoRequestBody);
        builder.setType(MultipartBody.FORM);
        Log.i("gqf", "bm==null2");
        MultipartBody mb=builder.build();

        //网络上传
        Subscription subscription = NetWork.getUserService().uploadAvatars(mb.part(0),userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultCodeWithUserHeadImg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iLoginAndRegistered.loginOrRegisteredResult(0,false,"无法上传，请检查网络");
                        iLoginAndRegistered.shouNetWorkActivity();
                        Log.i("gqf","ResultCodeWithUserHeadImg"+e.toString());
                    }

                    @Override
                    public void onNext(ResultCodeWithUserHeadImg resultCodeWithUserHeadImg) {
                        if(resultCodeWithUserHeadImg.getCode()== ResultCode.SECCESS){

                            User user=realm.where(User.class).findFirst();
                            realm.beginTransaction();
                            user.setUserAvatar(resultCodeWithUserHeadImg.getUserAvatar());
                            realm.copyToRealmOrUpdate(user);
                            realm.commitTransaction();
                            Log.i("gqf","ResultCodeWithUserHeadImg"+user.getUserAvatar());
                            iLoginAndRegistered.resetUserHeadImg(true);
                        }else{
                            iLoginAndRegistered.resetUserHeadImg(false);
                            iLoginAndRegistered.loginOrRegisteredResult(1,true,"头像上传失败");
                            Log.i("gqf","ResultCodeWithUserHeadImg"+resultCodeWithUserHeadImg.toString());
                        }

                    }
                });
        compositeSubscription.add(subscription);
    }
    public void getUserMsg(String phone){
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
                    public void onNext(ResultCodeWithUser resultCodeWithUser) {
                        if(resultCodeWithUser.getUser()!=null) {
                            if(realm.where(User.class).findFirst()!=null) {
                                if (realm.where(User.class).findFirst().getUserId()!=resultCodeWithUser.getUser().getUserId()||
                                        !resultCodeWithUser.getUser().getUserAvatar().equals(realm.where(User.class).findFirst().getUserAvatar())) {
                                    User user = realm.where(User.class).findFirst();
                                    realm.beginTransaction();
                                    user.setUserId(resultCodeWithUser.getUser().getUserId());
                                    user.setUserAvatar(resultCodeWithUser.getUser().getUserAvatar());
                                    user.setUserType(resultCodeWithUser.getUser().getUserType());
                                    realm.copyToRealmOrUpdate(user);
                                    realm.commitTransaction();
                                    iLoginAndRegistered.resetUserHeadImg(true);
                                }
                            }
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void getAuditCode(int userId){
        Subscription subscription = NetWork.getUserService().getStatus(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultWithAuditCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultWithAuditCode resultWithAuditCode) {
                        Log.i("gqf","resultWithAuditCode"+resultWithAuditCode.toString());
                        iLoginAndRegistered.showStatus(resultWithAuditCode.getAuditCode());
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void onClose(){
        super.onClose();
    }
}
