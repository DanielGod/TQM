package tqm.bianfeng.com.tqm.User.Presenter;

import java.io.File;

/**
 * Created by johe on 2017/3/14.
 */

public interface IUserWorkPresenter {
    public void onClose();
    public void uploadUserHeadImg(File img,int userId);
    public void getUserMsg(String phone,String c);
    public void getAuditCode(int userId);
    public void getNum(int userId);
}
