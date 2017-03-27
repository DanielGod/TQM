package tqm.bianfeng.com.tqm.update.Presenter;

import java.io.File;

import tqm.bianfeng.com.tqm.update.UpdateMsg;

/**
 * Created by johe on 2017/3/27.
 */

public interface IUpdatePresenter {

    public void download( String path, UpdateMsg updateMsg);

    public File getOutPutFile();
    public void close();
}
