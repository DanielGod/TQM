package tqm.bianfeng.com.tqm.application;

import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by johe on 2017/3/14.
 */

public class BasePresenterImpl implements BasePresenter{

    protected Realm realm;
    protected CompositeSubscription compositeSubscription;

    public BasePresenterImpl(){
        realm=Realm.getDefaultInstance();
        compositeSubscription=new CompositeSubscription();
    }

    public void onClose(){
        realm.close();
        compositeSubscription.unsubscribe();
    }

}
