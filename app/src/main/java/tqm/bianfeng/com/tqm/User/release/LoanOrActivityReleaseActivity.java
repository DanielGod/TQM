package tqm.bianfeng.com.tqm.User.release;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.CustomView.LoadingIndicator;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.ImageFilePath;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/5/10.
 */

public class LoanOrActivityReleaseActivity extends BaseActivity {

    public static final String RELEASE_ID = "release_id";

    public static int RELEASE_LOAN_TYPE = 1;
    public static int RELEASE_ACTIVITY_TYPE = 2;
    public static int RELEASE_TYPE = 0;

    @BindView(R.id.activity_release_toolbar)
    Toolbar activityReleaseToolbar;

    int releaseId = 0;
    @BindView(R.id.webview)
    WebView webView;

    String url;
    @BindView(R.id.indicator)
    LoadingIndicator indicator;

    String type;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_activity_release);
        ButterKnife.bind(this);
        String title = "";
        if (RELEASE_TYPE == RELEASE_LOAN_TYPE) {
            title = "贷款信息发布";
            type="loan";
        } else {
            title = "活动信息发布";
            type="activity";
        }
        releaseId = getIntent().getIntExtra(RELEASE_ID, 0);


        setToolbar(activityReleaseToolbar,title);
        indicator.showLoading();
        initData();
    }

    public void initData() {
        url= NetWork.LOAD+"/app/"+type+"/"+releaseId+"/"+realm.where(User.class).findFirst().getUserId();
        Log.i("gqf","url"+url);
        //url="http://www.baidu.com";
        initWebView();
    }
//    public void  initWebView(){
//        webView.load(url,null);
//
//    }

    public void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setAppCacheEnabled(true);//设置启动缓存
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存模式
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//4.4以下版本自适应页面大小 不能左右滑动
        //        1.NARROW_COLUMNS：可能的话使所有列的宽度不超过屏幕宽度
        //        2.NORMAL：正常显示不做任何渲染
        //        3.SINGLE_COLUMN：把所有内容放大webview等宽的一列中
        settings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        settings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        settings.setTextZoom(100);//字体大小
        settings.setSupportZoom(true);//仅支持双击缩放r
        webView.setInitialScale(57);//最小缩放等级
        webView.getSettings().setBlockNetworkImage(false);//阻止图片网络数据
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//图片加载放在最后
        webView.setVerticalScrollBarEnabled(false);//滚动条不显示
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);// 出现放大缩小提示
        webView.getSettings().setDisplayZoomControls(false);//隐藏缩放按钮
        webView.loadUrl(url);
        //webView.loadDataWithBaseURL(url,getNewContent(IMAGE3),"text/html", "UTF-8", null);
        webView.setWebChromeClient(mWebChromeClient);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                indicator.hideLoading();

            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub

                Log.i("gqf", "shouldOverrideUrlLoading..." + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }
    private String getNewContent(String htmltext){

        Document doc= Jsoup.parse(htmltext);
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }

        return doc.toString();
    }
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private ValueCallback<Uri[]> mFilePathCallback;

    private String mCameraPhotoPath;

    //在sdcard卡创建缩略图  
    //createImageFileInSdcard  
    @SuppressLint("SdCardPath")
    private File createImageFile() {
        //mCameraPhotoPath="/mnt/sdcard/tmp.png";  
        File file=new File(Environment.getExternalStorageDirectory()+"/","tmp.png");
        mCameraPhotoPath=file.getAbsolutePath();
        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {


        // android 5.0 这里需要使用android5.0 sdk  
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {

            Log.d("gqf", "onShowFileChooser");
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }


            mFilePathCallback = filePathCallback;

            /**
             Open Declaration   String android.provider.MediaStore.ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE" 
             Standard Intent action that can be sent to have the camera application capture an image and return it.  
             The caller may pass an extra EXTRA_OUTPUT to control where this image will be written. If the EXTRA_OUTPUT is not present, then a small sized image is returned as a Bitmap object in the extra field. This is useful for applications that only need a small image. If the EXTRA_OUTPUT is present, then the full-sized image will be written to the Uri value of EXTRA_OUTPUT. As of android.os.Build.VERSION_CODES.LOLLIPOP, this uri can also be supplied through android.content.Intent.setClipData(ClipData). If using this approach, you still must supply the uri through the EXTRA_OUTPUT field for compatibility with old applications. If you don't set a ClipData, it will be copied there for you when calling Context.startActivity(Intent). 
             See Also:EXTRA_OUTPUT 
             标准意图，被发送到相机应用程序捕获一个图像，并返回它。通过一个额外的extra_output控制这个图像将被写入。如果extra_output是不存在的， 
             那么一个小尺寸的图像作为位图对象返回。如果extra_output是存在的，那么全尺寸的图像将被写入extra_output URI值。 
             */
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go  
                File photoFile = null;
                try {
                    //设置MediaStore.EXTRA_OUTPUT路径,相机拍照写入的全路径  
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (Exception ex) {
                    // Error occurred while creating the File  
                    Log.e("WebViewSetting", "Unable to create Image File", ex);
                }

                // Continue only if the File was successfully created  
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    System.out.println(mCameraPhotoPath);
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");
            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
                System.out.println(takePictureIntent);
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;
        }



        //The undocumented magic method override  
        //Eclipse will swear at you if you try to put @Override here  
        // For Android 3.0+  
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Log.d("gqf", "openFileChooser1");

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            LoanOrActivityReleaseActivity.this.startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);

        }

        // For Android 3.0+  
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Log.d("gqf", "openFileChooser2");
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            LoanOrActivityReleaseActivity.this.startActivityForResult(
                    Intent.createChooser(i, "Image Chooser"),
                    FILECHOOSER_RESULTCODE);
        }

        //For Android 4.1  
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.d("gqf", "openFileChooser3");

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            LoanOrActivityReleaseActivity.this.startActivityForResult(Intent.createChooser(i, "Image Chooser"), LoanOrActivityReleaseActivity.FILECHOOSER_RESULTCODE);

        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("gqf", "onActivityResult");

        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = data == null || resultCode != RESULT_OK ? null
                    : data.getData();
            if (result != null) {
                String imagePath = ImageFilePath.getPath(this, result);
                if (!TextUtils.isEmpty(imagePath)) {
                    result = Uri.parse("file:///" + imagePath);
                }
            }
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == INPUT_FILE_REQUEST_CODE && mFilePathCallback != null) {
            // 5.0的回调  
            Uri[] results = null;

            // Check that the response is a good one  
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo  
                    if (mCameraPhotoPath != null) {
                        Log.d("camera_photo_path", mCameraPhotoPath);
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    Log.d("camera_dataString", dataString);
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }
}
