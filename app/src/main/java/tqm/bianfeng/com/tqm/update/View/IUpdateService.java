package tqm.bianfeng.com.tqm.update.View;

import tqm.bianfeng.com.tqm.update.DownloadProgress;

/**
 * Created by johe on 2017/3/27.
 */

public interface IUpdateService {
    public void sendNotification(DownloadProgress download );
    public void downloadCompleted();
    public void showToast(String msg);
}
