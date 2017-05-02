package tqm.bianfeng.com.tqm.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
    @BindView(R.id.multiple_actions_down)
    FloatingActionsMenu multipleActionsDown;
    @BindView(R.id.action_a)
    FloatingActionButton actionA;
    @BindView(R.id.action_b)
    FloatingActionButton actionB;
    @BindView(R.id.action_c)
    FloatingActionButton actionC;

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

        detailToolbar.inflateMenu(R.menu.collection_article_false);
        detailToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.collection_false) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                        ActivityCompat.requestPermissions(DetailActivity.this, mPermissionList, 123);
                    }
                    share();
                }
                return false;
            }
        });

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
        initactionASrc();
        initCollection();
        invalidateOptionsMenu();
    }

    String url;

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
        settings.setJavaScriptEnabled(true);//支持js
        settings.setSupportZoom(true);//仅支持双击缩放r
        webView.setInitialScale(57);//最小缩放等级
        webView.getSettings().setBlockNetworkImage(false);//阻止图片网络数据
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//图片加载放在最后
        webView.setVerticalScrollBarEnabled(false);//滚动条不显示
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);// 出现放大缩小提示
        webView.getSettings().setDisplayZoomControls(false);//隐藏缩放按钮
        String userId="";
        if(realm.where(User.class).findFirst()!=null){
            userId=realm.where(User.class).findFirst().getUserId()+"";

        }
        url = "http://211.149.235.17:8080/tqm-web/app/getDetail/" + detailType + "/" + detailId+"/"+userId;
        webView.loadUrl(url);


    }

    public void initCollection() {
        if (realm.where(User.class).findFirst() != null && !detailType.equals("04")) {
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
            share();
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
            animation.cancel();
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
            animation.cancel();
        }
    };


    public void share() {
        UMWeb web = new UMWeb(url);
        web.setTitle(detailTitle);//标题
        web.setDescription(detailTitle);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
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

    RotateAnimation animation;

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
                refAnim();
                isInCollection = true;
            }
        }
    }

    //关注按钮背景变换
    public void initactionASrc() {
        //if (!detailType.equals("04")) {
        if (isCollection) {
            //关注状态
            actionA.setIcon(R.drawable.ic_focuse);
            actionA.setTitle("已关注");
        } else {
            //未关注状态
            actionA.setIcon(R.drawable.ic_unfocuse);
            actionA.setTitle("未关注");
        }
        multipleActionsDown.setVisibility(View.VISIBLE);
        actionC.setVisibility(View.GONE);
        //        } else {
        //            actionC.setVisibility(View.VISIBLE);
        //            multipleActionsDown.setVisibility(View.GONE);
        //        }
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

    public void refAnim() {
        if (animation == null) {
            animation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            AccelerateDecelerateInterpolator lir = new AccelerateDecelerateInterpolator();
            animation.setInterpolator(lir);
            animation.setDuration(2000);//设置动画持续时间
            animation.setRepeatCount(Animation.INFINITE);//设置重复次数
            animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        }
        actionA.setIcon(R.drawable.ic_loding_anim_img);
        actionA.startAnimation(animation);
    }

    @OnClick({R.id.action_a, R.id.action_b, R.id.action_c})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_a:
                actionAFocuse();
                break;
            case R.id.action_b:
                webView.setScrollY(0);
                break;
            case R.id.action_c:
                webView.setScrollY(0);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
