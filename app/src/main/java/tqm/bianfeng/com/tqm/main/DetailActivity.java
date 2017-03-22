package tqm.bianfeng.com.tqm.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/3/15.
 */

public class DetailActivity extends BaseActivity {


    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.detail_web)
    WebView webView;

    public String detailType;

    public String detailTitle = "my";

    boolean isCollection = false;
    boolean isInCollection = false;
    public int detailId = -1;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        detailType = getIntent().getStringExtra("detailType");
        detailId = getIntent().getIntExtra("detailId", -1);
        detailTitle = getIntent().getStringExtra("detailTitle");
        String toolbarTitle = "";
        switch (detailType) {
            case "01":
                toolbarTitle = "银行活动";
                break;
            case "02":
                toolbarTitle = "银行理财";
                break;
            case "03":
                toolbarTitle = "银行贷款";
                break;
            case "04":
                toolbarTitle = "银行资讯";
                break;
        }
        setToolbar(detailToolbar, toolbarTitle);
        initWebView();
        initCollection();
    }

    String url;

    public void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        settings.setTextZoom(100);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setInitialScale(57);
        webView.getSettings().setBlockNetworkImage(false);

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);// 设置缩放
        webView.getSettings().setDisplayZoomControls(false);/*
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        webView.setWebViewClient(new CustomWebViewClient());*/
        url = "http://211.149.235.17:8080/tqm-web/app/getDetail/" + detailType + "/" + detailId;
        webView.loadUrl(url);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

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
                            invalidateOptionsMenu();
                        }
                    });
            compositeSubscription.add(subscription);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!detailType.equals("04")) {
            if (isCollection == false) {
                getMenuInflater().inflate(R.menu.collection_article_false, menu);
            } else {
                getMenuInflater().inflate(R.menu.collection_article_true, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //收藏当前文章
        if (realm.where(User.class).findFirst() == null) {
            Toast.makeText(this, "请登录后收藏", Toast.LENGTH_SHORT).show();
        } else {
            if (!isInCollection) {
                if (item.getItemId() == R.id.collection_false) {
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
        return super.onOptionsItemSelected(item);
    }

    Observer observer = new Observer<ResultCode>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            isInCollection = false;
        }

        @Override
        public void onNext(ResultCode resultCode) {
            isInCollection = false;
            isCollection = !isCollection;
            invalidateOptionsMenu();
        }
    };

    @OnClick(R.id.fab)
    public void onClick() {

        refAnim();
    }

    public void share(){
        UMWeb web = new UMWeb(url);
        web.setTitle(detailTitle);//标题
        web.setDescription(detailTitle);
        //web.setThumb(thumb);  //缩略图
        //        web.setDescription("my description");//描述
        new ShareAction(this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.ALIPAY, SHARE_MEDIA.DINGTALK)
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
    RotateAnimation animation;
    //关注网络接口调用
    public void fabFocuse(){
        //收藏当前文章
        if (realm.where(User.class).findFirst() == null) {
            Toast.makeText(this, "请登录后再关注", Toast.LENGTH_SHORT).show();
        } else {
            if (!isInCollection) {
                //关注
            }else{
                //取消关注
            }
        }
    }
    //关注按钮背景变换
    public void initFabSrc(){
        if(isInCollection){
            //关注状态
        }else{
            //未关注状态
        }
    }

    public void refAnim() {
        if(animation==null) {
            animation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            AccelerateDecelerateInterpolator lir = new AccelerateDecelerateInterpolator();
            animation.setInterpolator(lir);
            animation.setDuration(2000);//设置动画持续时间
            animation.setRepeatCount(Animation.INFINITE);//设置重复次数
            animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        }
        fab.setImageResource(R.drawable.ic_loding_anim_img);
        fab.startAnimation(animation);
    }

}
