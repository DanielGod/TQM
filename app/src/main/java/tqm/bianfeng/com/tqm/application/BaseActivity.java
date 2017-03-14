package tqm.bianfeng.com.tqm.application;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.CustomView.ToastType;

/**
 * Created by johe on 2017/3/14.
 */

public class BaseActivity extends AppCompatActivity {

    public Realm realm;
    public CompositeSubscription compositeSubscription;
    public ToastType toastType;
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
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }
}