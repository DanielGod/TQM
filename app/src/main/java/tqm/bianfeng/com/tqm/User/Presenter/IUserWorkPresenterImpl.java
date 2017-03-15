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
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithUserHeadImg;
import tqm.bianfeng.com.tqm.pojo.User;

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
                        iLoginAndRegistered.setTextNum(myAttention.get_$01(),myAttention.get_$02(),myAttention.get_$03(),0);
                    }
                });
        compositeSubscription.add(subscription);
    }
    //头像上传
    public void uploadUserHeadImg(File img,int userId){
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), img);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("file", img.getName(), photoRequestBody);
        builder.setType(MultipartBody.FORM);

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

                    }

                    @Override
                    public void onNext(ResultCodeWithUserHeadImg resultCodeWithUserHeadImg) {
                        if(resultCodeWithUserHeadImg.getCode()== ResultCode.SECCESS){
                            User user=realm.where(User.class).findFirst();
                            realm.beginTransaction();
                            user.setUserAvatar(resultCodeWithUserHeadImg.getUserAvatar());
                            realm.copyToRealmOrUpdate(user);
                            realm.commitTransaction();
                            iLoginAndRegistered.resetUserHeadImg(true);
                        }else{
                            iLoginAndRegistered.resetUserHeadImg(false);
                        }

                    }
                });
        compositeSubscription.add(subscription);
    }

    public void onClose(){
        super.onClose();
    }
}
