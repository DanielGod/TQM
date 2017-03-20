package tqm.bianfeng.com.tqm.User.View;

import java.io.File;

/**
 * Created by johe on 2017/3/13.
 */

public interface ILoginAndRegistered {
    public void loginOrRegisteredResult(int toastType,boolean isSuccess,String msg);
    public void setTextNum(int num1,int num2,int num3,int num4);
    public void setUserHeadImg(File bitmap);
    public void resetUserHeadImg(boolean isChange);
    public void shouNetWorkActivity();
}
