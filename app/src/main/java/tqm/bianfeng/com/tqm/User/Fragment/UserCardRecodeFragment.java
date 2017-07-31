package tqm.bianfeng.com.tqm.User.Fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.UserCardRecodeAdapter;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.Card;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * 名片收集
 * Created by 王九东 on 2017/7/13.
 */

public class UserCardRecodeFragment extends BaseFragment {
    @BindView(R.id.userintegral_rv)
    RecyclerView userintegralRv;
    Unbinder unbinder;
    UserCardRecodeAdapter userCardRecodeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_cardrecode, container, false);
        unbinder = ButterKnife.bind(this, view);
        initdata(realm.where(User.class).findFirst().getUserId());


        return view;
    }

    private void initdata(int userId) {
        Subscription subscription = NetWork.getUserService().getCard(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Card>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(List<Card> cards) {
                        setAdapter(cards);

                    }
                });
        compositeSubscription.add(subscription);
    }

    private void setAdapter(List<Card> cards) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
