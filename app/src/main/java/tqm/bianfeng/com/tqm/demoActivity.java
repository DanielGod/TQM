package tqm.bianfeng.com.tqm;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by johe on 2017/4/18.
 */

public class demoActivity extends Activity {
    Context mContext;
    @BindView(R.id.load_more_txt)
    TextView loadMoreTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_loading);
        ButterKnife.bind(this);
        mContext = this;
        //Toast.makeText(this, ""+Environment.getExternalStorageDirectory().getAbsolutePath(), 0).show();

        //        loadMoreTxt.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                if (Build.VERSION.SDK_INT >= 23) {
        //                    //判断是否有这个权限
        //                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        //                        //第一请求权限被取消显示的判断，一般可以不写
        //                        if (ActivityCompat.shouldShowRequestPermissionRationale(demoActivity.this,
        //                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        //                            //Log.i("readTosdCard","我们需要这个权限给你提供存储服务");
        //                            showAlert();
        //                        } else {
        //                            //2、申请权限: 参数二：权限的数组；参数三：请求码
        //                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
        //                        }
        //                    } else {
        //                        writeToSdCard();
        //                    }
        //                } else {
        //                    writeToSdCard();
        //                }
        //
        //            }
        //        });
    }

    public void writeToSdCard() {
        loadMoreTxt.setText("开始");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                copyApkFromAssets(mContext, "test.apk", Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.apk");
            }
        });
        t.start();

    }

    private void showAlert() {
        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle("权限说明").
                setMessage("我们需要这个权限给你提供存储服务").
                setIcon(R.drawable.ic_launcher).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //2、申请权限: 参数二：权限的数组；参数三：请求码
                        ActivityCompat.requestPermissions(demoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                create();
        alertDialog.show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                loadMoreTxt.setText("结束");
                AlertDialog.Builder m = new AlertDialog.Builder(mContext)
                        .setIcon(R.drawable.ic_launcher).setMessage("是否安装？")
                        .setIcon(R.drawable.ic_launcher)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.apk"),
                                        "application/vnd.android.package-archive");
                                mContext.startActivity(intent);
                            }
                        });
                m.show();
            }
        }
    };

    public boolean copyApkFromAssets(Context context, String fileName, String path) {
        Log.i("gqf","while");
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
                Log.i("gqf","while"+i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("gqf","while"+e.toString());
        }
        Message m = new Message();
        m.what = 1;
        handler.sendMessage(m);
        return copyIsFinish;
    }

    @OnClick(R.id.load_more_txt)
    public void onClick() {
        writeToSdCard();
    }
}