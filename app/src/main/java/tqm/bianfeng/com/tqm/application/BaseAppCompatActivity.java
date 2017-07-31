package tqm.bianfeng.com.tqm.application;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.CustomView.ToastType;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.DisplayUtil;
import tqm.bianfeng.com.tqm.Util.NetUtils;
import tqm.bianfeng.com.tqm.Util.SystemBarTintManager;

/**
 * Created by johe on 2017/3/14.
 */

public class BaseAppCompatActivity extends AppCompatActivity implements InitViewInterface{

    protected Realm realm;
    protected CompositeSubscription compositeSubscription;
    protected ToastType toastType;
    protected Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        realm=Realm.getDefaultInstance();
        compositeSubscription=new CompositeSubscription();
        toastType=new ToastType();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void setToolbar(Toolbar toolbar,String msg){
        mToolbar=toolbar;
        toolbar.setTitle(msg);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
//        toolbar.setBackground(getResources().getDrawable(R.drawable.img_toolbar_bg));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setSystemBarColor(R.color.colorPrimaryDark);
    }

    public void setToolbar(boolean isHasBack,boolean isBackWrite,int textColor){
        if(isHasBack==false){
            mToolbar.setNavigationIcon(null);
        }else{
            if(isBackWrite){
                mToolbar.setNavigationIcon(R.drawable.barcode__back_arrow);
            }else{

            }
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        mToolbar.setTitleTextColor(getResources().getColor(textColor));
    }

    //弹出网络设置dialog
    public void shouNetWorkActivity() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("当前没有网络")
                .setMessage("是否跳转系统网络设置界面?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NetUtils.openSetting(BaseAppCompatActivity.this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alert.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }

    public void setNetWorkDialog(boolean isNetWork){
        try {
            LinearLayout netWorkLin=(LinearLayout)findViewById(R.id.net_work_lin);
            if(netWorkLin!=null){
                if(isNetWork){
                    netWorkLin.setVisibility(View.VISIBLE);
                    netWorkLin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NetUtils.openSetting(BaseAppCompatActivity.this);
                        }
                    });
                }else{
                    netWorkLin.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){

        }

    }
    public void setSystemBarColor(int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(id);
        }
        else{
            if(mToolbar!=null) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mToolbar.getLayoutParams();
                lp.height = DisplayUtil.dip2px(this, getResources().getDimension(R.dimen.bigxmdp));
                mToolbar.setLayoutParams(lp);
                mToolbar.setNavigationIcon(R.drawable.ic_write_back_small);
            }
        }
    }

}