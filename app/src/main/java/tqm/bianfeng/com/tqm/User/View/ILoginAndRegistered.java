package tqm.bianfeng.com.tqm.User.View;

import java.io.File;

/**
 * Created by johe on 2017/3/13.
 */

public interface ILoginAndRegistered {
    public void loginOrRegisteredResult(int toastType,boolean isSuccess,String msg);
    public void setUserHeadImg(File bitmap);
    public void resetUserHeadImg(boolean isChange);
    public void shouNetWorkActivity();
}
