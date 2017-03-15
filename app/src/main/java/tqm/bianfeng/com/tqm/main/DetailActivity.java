package tqm.bianfeng.com.tqm.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;

/**
 * Created by johe on 2017/3/15.
 */

public class DetailActivity extends BaseActivity {


    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.detail_web)
    WebView webView;

    public String detailType;

    public int detailId=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        detailType=getIntent().getStringExtra("detailType");
        detailId=getIntent().getIntExtra("detailId",-1);
        String toolbarTitle="";
        switch (detailType){
            case "01":
                toolbarTitle="银行活动";
                break;
            case "02":
                toolbarTitle="银行理财";
                break;
            case "03":
                toolbarTitle="银行贷款";
                break;
            case "04":
                toolbarTitle="银行资讯";
                break;
        }
        setToolbar(detailToolbar, toolbarTitle);
        initWebView();
    }
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
        webView.loadUrl("http://211.149.235.17:8080/tqm-web/app/getDetail/" + detailType+"/"+detailId);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

    }

}
