package tqm.bianfeng.com.tqm.User.release;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.CustomView.LoadingIndicator;
import tqm.bianfeng.com.tqm.R;
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


        setToolbar(activityReleaseToolbar, "活动信息发布");
        indicator.showLoading();
        initData();
    }

    public void initData() {
        url= NetWork.LOAD+"/app/"+type+"/"+releaseId+"/"+realm.where(User.class).findFirst().getUserId();
        Log.i("gqf","url"+url);
        //url="http://www.baidu.com";
        initWebView();
    }

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
        webView.loadUrl(url);
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
}
