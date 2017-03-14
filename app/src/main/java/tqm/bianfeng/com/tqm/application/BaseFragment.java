package tqm.bianfeng.com.tqm.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        realm=Realm.getDefaultInstance();
        compositeSubscription=new CompositeSubscription();
        toastType=new ToastType();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        compositeSubscription.unsubscribe();
    }
}
