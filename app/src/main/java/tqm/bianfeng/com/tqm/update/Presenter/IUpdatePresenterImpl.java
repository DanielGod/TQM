package tqm.bianfeng.com.tqm.update.Presenter;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.application.BasePresenterImpl;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.network.exception.CustomizeException;
import tqm.bianfeng.com.tqm.update.DownloadProgress;
import tqm.bianfeng.com.tqm.update.DownloadProgressListener;
import tqm.bianfeng.com.tqm.update.FileUtils;
import tqm.bianfeng.com.tqm.update.StringUtils;
import tqm.bianfeng.com.tqm.update.UpdateMsg;
import tqm.bianfeng.com.tqm.update.View.IUpdateService;

/**
 * Created by johe on 2017/3/27.
 */

public class IUpdatePresenterImpl extends BasePresenterImpl implements IUpdatePresenter{

    IUpdateService iUpdateService;
    File outputFile;
    int downloadCount = 0;
    public IUpdatePresenterImpl(IUpdateService i){
        super();
        iUpdateService=i;
    }
    public File getOutPutFile(){
        return outputFile;
    }
    public void download(String path, UpdateMsg updateMsg){
        //下载文件名，地址
        outputFile = new File(path);
        //删除已有
        if (outputFile.exists()) {
            outputFile.delete();
        }
        String baseUrl = StringUtils.getHostName(NetWork.LOAD+updateMsg.getUpdateUrl());
        Log.e("gqf","baseUrl"+baseUrl);
        //开起下载
        //        new DownloadAPI(baseUrl, listener)
        //                .downloadAPK(NetWork.LOAD+updateMsg.getUpdateUrl(), outputFile, new Subscriber() {
        //            @Override
        //            public void onCompleted() {
        //                downloadCompleted();
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                e.printStackTrace();
        //                //downloadCompleted);
        //                Toast.makeText(getApplicationContext(),"下载失败", Toast.LENGTH_SHORT).show();
        //            }
        //
        //            @Override
        //            public void onNext(Object o) {
        //            }
        //        });
        Subscription subscription = NetWork.getUpdateService(baseUrl,new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                //下载进度监听
                int progress = (int) ((bytesRead * 100) / contentLength);
                if ((downloadCount == 0) || progress > downloadCount) {
                    DownloadProgress download = new DownloadProgress();
                    download.setTotalFileSize(contentLength);
                    download.setCurrentFileSize(bytesRead);
                    download.setProgress(progress);
                    iUpdateService.sendNotification(download);
                }
            }
        })
                .download(NetWork.LOAD+updateMsg.getUpdateUrl())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        try {
                            FileUtils.writeFile(inputStream, outputFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new CustomizeException(e.getMessage(), e);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        iUpdateService.downloadCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //downloadCompleted);
                        iUpdateService.showToast("下载失败");
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
        compositeSubscription.add(subscription);
    }
    public void close(){
        super.onClose();
    }
}
