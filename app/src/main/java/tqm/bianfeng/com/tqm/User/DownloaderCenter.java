package tqm.bianfeng.com.tqm.User;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by 王九东 on 2017/7/17.
 */

public class DownloaderCenter extends BaseActivity {

    @BindView(R.id.release_toolbar)
    Toolbar releaseToolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadcenter);
        ButterKnife.bind(this);
        setToolbar(releaseToolbar,"下载中心");
        setDownloadAdapter();
    }

    DownloadManager downloadManager;
    List<DownloadInfo> allTask;
    List<DownloadInfo> mDownloadingTask;
//    DownloadingAdapter adapter;
    private void setDownloadAdapter() {
        downloadManager = DownloadService.getDownloadManager();
        allTask = downloadManager.getAllTask();
        //        Log.i("Daniel","DownloadingActivity---setDownloadAdapter---allTask.size()---"+allTask.size());
        if (mDownloadingTask == null) {
            mDownloadingTask = new ArrayList<>();
        } else {
            mDownloadingTask.clear();
        }

        for (DownloadInfo downloadinfo : allTask) {
            if (downloadinfo.getState() != downloadManager.FINISH) {
                mDownloadingTask.add(downloadinfo);
            }
        }
        //        Log.i("Daniel","DownloadingActivity---setDownloadAdapter---mDownloadingTask.size()---"+mDownloadingTask.size());
//        taskKey = LocalVideosActivity.taskKey;
//        taskKey.clear();
        //        Log.i("Daniel","DownloadingActivity---setDownloadAdapter---taskKey.size()---"+taskKey.size());
        //        Log.i("Daniel","DownloadingActivity---setDownloadAdapter---taskKey.size()---"+taskKey.size());
//        adapter = new DownloadingAdapter(DownloadingActivity.this, mDownloadingTask);
//        mListView.setAdapter(adapter);

    }
}
