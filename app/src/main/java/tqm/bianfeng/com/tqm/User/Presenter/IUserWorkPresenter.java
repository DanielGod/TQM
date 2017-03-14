package tqm.bianfeng.com.tqm.User.Presenter;

import java.io.File;

/**
 * Created by johe on 2017/3/14.
 */

public interface IUserWorkPresenter {
    public void getMyFocusMsgNum(int userId);
    public void onClose();
    public void uploadUserHeadImg(File img,int userId);
}
