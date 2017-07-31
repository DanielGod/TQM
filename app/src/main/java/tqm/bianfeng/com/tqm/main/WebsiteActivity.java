package tqm.bianfeng.com.tqm.main;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.CustomView.ObservableWebView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/3/15.
 */

public class WebsiteActivity extends BaseActivity {
    @BindView(R.id.detail_web)
    ObservableWebView detailWeb;
    private String mURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_detail);
        ButterKnife.bind(this);

        mURL = getIntent().getStringExtra("URL");
        Log.e("Daniel","官网地址："+mURL);
        initWebView();
    }

    public void initWebView() {
        WebSettings settings = detailWeb.getSettings();
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
        detailWeb.setInitialScale(57);//最小缩放等级
        detailWeb.getSettings().setBlockNetworkImage(false);//阻止图片网络数据
        detailWeb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//图片加载放在最后
        detailWeb.setVerticalScrollBarEnabled(false);//滚动条不显示
        detailWeb.setHorizontalScrollBarEnabled(false);
        detailWeb.getSettings().setBuiltInZoomControls(true);// 出现放大缩小提示
        detailWeb.getSettings().setDisplayZoomControls(false);//隐藏缩放按钮
        detailWeb.loadUrl(mURL);
    }

}
