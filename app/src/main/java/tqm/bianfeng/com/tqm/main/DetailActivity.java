package tqm.bianfeng.com.tqm.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    boolean isCollection=false;
    boolean isInCollection=false;
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
        initCollection();
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

    public void initCollection(){
        if(realm.where(User.class).findFirst() != null){
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
                            if(s.equals("0")){
                                isCollection=false;
                            }else{
                                isCollection=true;
                            }
                            invalidateOptionsMenu();
                        }
                    });
            compositeSubscription.add(subscription);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if( !detailType.equals("04")) {
            if (isCollection == false ) {
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
                Toast.makeText(this,"请登录后收藏",Toast.LENGTH_SHORT).show();
            } else {
                if(!isInCollection) {
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
                    isInCollection=true;
                }
            }

        return super.onOptionsItemSelected(item);
    }
    Observer observer=new Observer<ResultCode>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            isInCollection=false;
        }

        @Override
        public void onNext(ResultCode resultCode) {
            isInCollection=false;
            isCollection=!isCollection;
            invalidateOptionsMenu();
        }
    };

}
