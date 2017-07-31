package tqm.bianfeng.com.tqm.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.LoadingIndicator;
import tqm.bianfeng.com.tqm.CustomView.ObservableWebView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.CorrectOrReportActivity;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

import static tqm.bianfeng.com.tqm.R.id.city;

/**
 * Created by johe on 2017/3/15.
 */

public class DetailActivity extends BaseActivity {

    public static final String ACTIVITY_TYPE = "01";
    public static final String FINANC_TYPE = "02";
    public static final String LOAN_TYPE = "03";
    public static final String INFOR_TYPE = "04";
    public static final String LAWYER_TYPE = "05";

    public static final String BANK_INFO_TYPE="BANK_INFO_TYPE";

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1234;
    @BindView(R.id.detail_toolbar)
    RelativeLayout detailToolbar;
    @BindView(R.id.detail_web)
    ObservableWebView webView;

    public String detailType;

    public String detailTitle = "my";

    public int bankType=0;

    boolean isCollection = false;
    boolean isInCollection = false;
    public int detailId = -1;
    public String articlePath;
    @BindView(R.id.detail_back)
    ImageView detailBack;
    @BindView(R.id.comments_rel)
    RelativeLayout commentsRel;
    @BindView(R.id.icon_img)
    ImageView iconImg;
    @BindView(R.id.message_num)
    TextView messageNum;
    @BindView(R.id.icon_with_msg_rel)
    RelativeLayout iconWithMsgRel;
    @BindView(R.id.focuse_img)
    ImageView focuseImg;
    @BindView(R.id.toolbar_txt)
    TextView toolbarTxt;
    String toolbarTitle;
    @BindView(R.id.share_img)
    ImageView shareImg;
    @BindView(R.id.more_menu_img)
    ImageView moreMenuImg;
    @BindView(R.id.loading_indeiator)
    LoadingIndicator loadingIndeiator;
    private int toolBarHeight = 0;
    private int scrollHeight = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        detailType = getIntent().getStringExtra("detailType");
        detailId = getIntent().getIntExtra("detailId", -1);
        detailTitle = getIntent().getStringExtra("detailTitle");
        articlePath = getIntent().getStringExtra("articlePath");
        bankType=getIntent().getIntExtra(BANK_INFO_TYPE, -1);

        toolbarTitle = "";
        loadingIndeiator.showLoading();
        switch (detailType) {
            case "01":
                toolbarTitle = "金融活动";
                break;
            case "02":
                toolbarTitle = "银行理财";
                break;
            case "03":
                toolbarTitle = "金融贷款";
                break;
            case "04":
                if(bankType==1){
                    toolbarTitle = "银行资讯";
                }else if(bankType==2){
                    toolbarTitle = "热点资讯";
                }else if(bankType==3){
                    toolbarTitle = "金融课堂";
                }else{
                    toolbarTitle = "银行资讯";
                }
                break;
            case "05":
                toolbarTitle = "律师资料";
                break;
        }
        //setToolbar(detailToolbar, toolbarTitle);
        //setToolbar(false,false,R.color.white);

        initWebView();
        initactionASrc();
        initCollection();
        invalidateOptionsMenu();
        initToolBar();

    }

    public void initToolBar() {
        toolbarTxt.setText(toolbarTitle);
        toolbarTxt.setTextColor(getResources().getColor(R.color.font_black_1));
        StatusBarUtil.setColor(this, getResources().getColor(R.color.font_black_7));
        detailToolbar.setBackgroundResource(R.color.whitesmoke);
        setToolBarBgAlpha();
    }


    String url;
    String shareUrl;

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
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);//支持js
        settings.setSupportZoom(true);//仅支持双击缩放r
        webView.setInitialScale(57);//最小缩放等级
        webView.getSettings().setBlockNetworkImage(false);//阻止图片网络数据
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//图片加载放在最后
        webView.setVerticalScrollBarEnabled(false);//滚动条不显示
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);// 出现放大缩小提示
        webView.getSettings().setDisplayZoomControls(false);//隐藏缩放按钮
        String userId = "0";
        if (realm.where(User.class).findFirst() != null) {
            userId = realm.where(User.class).findFirst().getUserId() + "";
        }
        url = NetWork.LOAD + "/app/getDetail/" + detailType + "/" + detailId + "/" + userId+"/y";
        shareUrl=NetWork.LOAD + "/app/getDetail/" + detailType + "/" + detailId + "/"+"0" + "/n";
        Log.e("Daniel","------articlePath---"+articlePath);
        if(detailType.equals("01")){
            url = NetWork.LOAD+articlePath+"?module="+detailType+"&objId="+detailId+"&userId="+userId;
            shareUrl = NetWork.LOAD+articlePath;
        }else if ("03".equals(detailType)){//贷款详情
            Log.e(Constan.LOGTAGNAME,"detailType-city-detailId-userId:"+MainActivity.locationStr+detailId+userId);
            url = NetWork.LOAD+"/app/loan/detail?city="+MainActivity.locationStr+"&loanId="+detailId+"&userId="+userId+"&mark=y";
            shareUrl = NetWork.LOAD+"/app/loan/detail?city="+city+"&loanId="+detailId+"&userId="+userId+"&mark=n";
        }

        Log.i("gqf","getDetail"+detailTitle);
        Log.e("Daniel","------shareUrl---"+shareUrl);
        Log.e("Daniel","------url---"+url);
        webView.loadUrl(url);

        webView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                //内容滚动
                scrollHeight = dy / 2;
                if (scrollHeight > toolBarHeight) {
                    scrollHeight = toolBarHeight;
                    moreMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.img_edit_dark));
                }
                setToolBarBgAlpha();
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingIndeiator.hideLoading();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("http:") || url.startsWith("https:") ) {
                    view.loadUrl(url);
                    return false;
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        });
    }
    public class Object2 extends Object{
        //写一个js调android的方法
        //Android 4.2以上系统，通过在Java的远程方法上面声明
        // @JavascriptInterface可以解决WebView漏洞。如下面代码：
        //@JavascriptInterface一定的加上
        @android.webkit.JavascriptInterface
        public void finishActivity(final String data){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DetailActivity.this, "js调用了Native函数传递参数：" + data, Toast.LENGTH_SHORT).show();
                    //String usedcar = logTextView.getText() +  "\njs调用了Native函数传递参数：" + str;
                    //logTextView.setText(usedcar);
                }
            });
        }

    }
    public void setToolBarBgAlpha() {
        if (toolBarHeight == 0) {
            toolBarHeight = (int) getResources().getDimension(R.dimen.smallhdp);
        }
        Log.i("gqf", "setToolBarBgAlpha" + (float) scrollHeight / toolBarHeight);
        detailToolbar.getBackground().setAlpha(scrollHeight * 255 / toolBarHeight);
        toolbarTxt.setAlpha((float) scrollHeight / toolBarHeight);
    }

    public void initCollection() {
        if (realm.where(User.class).findFirst() != null) {
            Subscription subscription = NetWork.getUserService().isAttention(detailId, detailType, realm.where(User.class).findFirst().getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String s) {
                            if (s.equals("0")) {
                                isCollection = false;
                            } else {
                                isCollection = true;
                            }
                            initactionASrc();
                        }
                    });
            compositeSubscription.add(subscription);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection_article_false, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //社会化分享
        if (item.getItemId() == R.id.collection_false) {
            shareWithPermission();
        }
        return super.onOptionsItemSelected(item);
    }

    Observer observer = new Observer<ResultCode>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            isInCollection = false;
            Toast.makeText(DetailActivity.this, "网络问题，关注失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(ResultCode resultCode) {
            if (resultCode.getCode() == ResultCode.SECCESS) {
                isCollection = !isCollection;
                initactionASrc();
                toastFocuseResult();
            } else {
                Toast.makeText(DetailActivity.this, "关注失败，请重试", Toast.LENGTH_SHORT).show();
            }
            isInCollection = false;
        }
    };

    public void shareWithPermission(){

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            share();
        }
    }

    public void share() {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(detailTitle);//标题
        web.setDescription(detailTitle);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        if (bmp == null) {
            Log.i("gqf", "bmp==null");
        }
        web.setThumb(new UMImage(this, bmp
        ));  //缩略图


        new ShareAction(this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        //分享开始的回调

                    }

                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        Log.d("plat", "platform" + platform);
                        Toast.makeText(DetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        Toast.makeText(DetailActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                        Log.d("plat", "platform" + platform);
                        if (t != null) {
                            Log.d("throw", "throw:" + t.getMessage());
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        Toast.makeText(DetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                    }
                }).open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }


    //关注网络接口调用
    public void actionAFocuse() {
        //收藏当前文章
        if (realm.where(User.class).findFirst() == null) {
            Toast.makeText(this, "请登录后再关注", Toast.LENGTH_SHORT).show();
        } else {
            if (!isInCollection) {
                if (!isCollection) {
                    //关注
                    Subscription subscription = NetWork.getUserService().attention(detailId, detailType, realm.where(User.class).findFirst().getUserId(), "01")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
                    compositeSubscription.add(subscription);
                } else {
                    //取消
                    Subscription subscription = NetWork.getUserService().attention(detailId, detailType, realm.where(User.class).findFirst().getUserId(), "02")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
                    compositeSubscription.add(subscription);
                }
                isInCollection = true;
            }
        }
    }

    //关注按钮背景变换
    public void initactionASrc() {
        //if (!detailType.equals("04")) {
        if (isCollection) {
            //关注状态
            focuseImg.setImageResource(R.drawable.ic_focuse_true);
            //actionA.setTitle("已关注");
        } else {
            //未关注状态
            focuseImg.setImageResource(R.drawable.ic_focuse_false);
            //actionA.setTitle("未关注");
        }
        //ultipleActionsDown.setVisibility(View.VISIBLE);
        //actionC.setVisibility(View.GONE);
    }

    public void toastFocuseResult() {
        if (isCollection) {
            //关注成功
            Toast.makeText(this, "关注成功，请在猫舍查看", Toast.LENGTH_SHORT).show();
        } else {
            //取消关注
            Toast.makeText(this, "已取消关注", Toast.LENGTH_SHORT).show();
        }
    }


    //    @OnClick({R.id.action_a, R.id.action_b, R.id.action_c})
    //    public void onClick(View view) {
    //        switch (view.getId()) {
    //            case R.id.action_a:
    //                actionAFocuse();
    //                break;
    //            case R.id.action_b:
    //                webView.setScrollY(0);
    //                break;
    //            case R.id.action_c:
    //                webView.setScrollY(0);
    //                break;
    //        }
    //    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        detailToolbar.setAlpha(1);
        webView.destroy();
    }

    @OnClick({R.id.more_menu_img, R.id.detail_back, R.id.comments_rel, R.id.icon_with_msg_rel, R.id.focuse_img, R.id.share_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_back:
                onBackPressed();
                break;
            case R.id.comments_rel:
                break;
            case R.id.icon_with_msg_rel:
                break;
            case R.id.focuse_img:
                actionAFocuse();
                break;
            case R.id.share_img:
                shareWithPermission();
                break;
            case R.id.more_menu_img:
                //跳报界面

                Intent intent = new Intent(DetailActivity.this, CorrectOrReportActivity.class);
                intent.putExtra(CorrectOrReportActivity.objectId, detailId);
                intent.putExtra(CorrectOrReportActivity.objectModule, detailType);
                intent.putExtra(CorrectOrReportActivity.objectTitle, detailTitle);
                startActivity(intent);


                break;
        }
    }

}
