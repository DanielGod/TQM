package tqm.bianfeng.com.tqm.User.Presenter;

/**
 * Created by johe on 2017/3/13.
 */

public interface ILoginRegisterPresenter {

    public void setOldCode(String phone,boolean isGet) ;
    public void loginOrRegister(String phone,String code,String channel);

    public void onClose();


}
