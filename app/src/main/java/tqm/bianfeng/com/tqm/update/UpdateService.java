package tqm.bianfeng.com.tqm.update;

/**
 * Created by johe on 2016/12/29.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.main.MainActivity;
import tqm.bianfeng.com.tqm.update.Presenter.IUpdatePresenter;
import tqm.bianfeng.com.tqm.update.Presenter.IUpdatePresenterImpl;
import tqm.bianfeng.com.tqm.update.View.IUpdateService;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 不要忘记注册，在mainfest文件中
 */
public class UpdateService extends Service implements IUpdateService{


    //文件存储
    private File updateDir = null;

    IUpdatePresenter iUpdatePresenter;

    //通知栏

    //通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        iUpdatePresenter=new IUpdatePresenterImpl(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iUpdatePresenter.close();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取传值
        UpdateMsg updateMsg=new UpdateMsg();
        updateMsg.setUpdateContent(intent.getStringExtra("getUpdateContent"));
        updateMsg.setVersionCode(intent.getStringExtra("getVersionCode"));
        updateMsg.setUpdateUrl(intent.getStringExtra("getVersionUrl"));
        //创建下载后保存文件夹
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            //文件夹路径
            updateDir = new File(Environment.getExternalStorageDirectory(),UpdateInformation.downloadDir);
        }
        //没有则创建文件夹
        if (!updateDir.exists()) {
            updateDir.mkdir();
        }
        //设置下载过程中，点击通知栏，回到主界面
        updateIntent = new Intent(this, MainActivity.class);
        updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
        //下载通知
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(this.getResources().getString(R.string.app_name)+"正在更新")
                .setContentText(updateMsg.getUpdateContent())
                .setContentIntent(updatePendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());
        Log.i("gqf","开始下载");
        //Log.i("gqf","path"+updateDir.getAbsolutePath()+"/"+getResources().getString(R.string.app_name)+".apk");
        iUpdatePresenter.download(updateDir.getAbsolutePath()+"/"+getResources().getString(R.string.app_name)+".apk",updateMsg);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    //通知栏显示下载
    public void sendNotification(DownloadProgress download) {
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText(
               StringUtils.getDataSize(download.getCurrentFileSize()) + "/" +
                        StringUtils.getDataSize(download.getTotalFileSize()));
        notificationBuilder.setContentIntent(updatePendingIntent);
        notificationManager.notify(0, notificationBuilder.build());
    }
    //下载完成
    public void downloadCompleted() {
        //完成下载
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText(this.getResources().getString(R.string.app_name)+"更新完成");
        //完成后直接跳转安装界面，安装apk
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(iUpdatePresenter.getOutPutFile()), "application/vnd.android.package-archive");
        startActivity(intent);
        //也可通过点击通知跳转安装界面
        notificationBuilder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0));
        notificationManager.notify(0, notificationBuilder.build());
    }
    //提示
    public void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
    }
}

