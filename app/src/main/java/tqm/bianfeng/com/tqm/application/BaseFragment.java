package tqm.bianfeng.com.tqm.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.CustomView.ToastType;

/**
 * Created by johe on 2017/3/14.
 */

public class BaseFragment extends Fragment{

    public Realm realm;
    public CompositeSubscription compositeSubscription;
    public ToastType toastType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm=Realm.getDefaultInstance();
        compositeSubscription=new CompositeSubscription();
        toastType=new ToastType();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        compositeSubscription.unsubscribe();
    }
}
