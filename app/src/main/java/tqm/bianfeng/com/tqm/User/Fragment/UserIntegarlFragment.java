package tqm.bianfeng.com.tqm.User.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import tqm.bianfeng.com.tqm.User.adapter.UserIntegarlAdapter;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.UserPointA;

/**
 * 积分
 * Created by 王九东 on 2017/7/13.
 */

public class UserIntegarlFragment extends BaseFragment {
    @BindView(R.id.userintegral_rv)
    RecyclerView userintegralRv;
    Unbinder unbinder;
    UserIntegarlAdapter userIntegralAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_integar, container, false);
        unbinder = ButterKnife.bind(this, view);
        getUserPointItem(realm.where(User.class).findFirst().getUserId());

        return view;
    }
    private void getUserPointItem(int userId) {
        Subscription subscription = NetWork.getUserService().getUserPointItem(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserPointA>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(List<UserPointA> userPointAs) {
                        setAdapter(userPointAs);

                    }
                });
        compositeSubscription.add(subscription);
    }

    private void setAdapter(List<UserPointA> userPointAs) {
        if (userIntegralAdapter==null) {
            userIntegralAdapter = new UserIntegarlAdapter(mContext,userPointAs);
            userintegralRv.setLayoutManager(new LinearLayoutManager(mContext));
            userintegralRv.setAdapter(userIntegralAdapter);
        }else {
            userIntegralAdapter.notifyData(userPointAs);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
