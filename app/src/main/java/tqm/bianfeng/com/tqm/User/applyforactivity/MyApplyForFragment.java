package tqm.bianfeng.com.tqm.User.applyforactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.User;
import tqm.bianfeng.com.tqm.pojo.YwDksq;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by johe on 2017/4/10.
 */

public class MyApplyForFragment extends BaseFragment {

    public int index = 0;
    public static String ARG_TYPE = "arg_type";
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;

    ApplyForStatusFragment applyForStatusFragment;
    SubmitInformationFragment submitInformationFragment;

    public static MyApplyForFragment newInstance(int position) {
        MyApplyForFragment fragment = new MyApplyForFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, position);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_TYPE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_applyfor, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        defaultLoadview.lodingIsFailOrSucess(1);
        int userId = realm.where(User.class).findFirst().getUserId();
        if (index==0){
            getShzt(userId);
        }else {
            getLoanOne(userId);
        }
        return view;
    }
    private void getShzt(int userId) {
        Subscription subscription = NetWork.getUserService().getShzt(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_FAILED);
                    }
                    @Override
                    public void onNext(Map<String, String> stringStringMap) {

                        if (!StringUtils.isEmpty(stringStringMap.get("shzt"))){
                            defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
                            Log.e("Daniel","快速入驻审核状态"+stringStringMap.get("shzt"));
                            //查看审核状态
                            if (applyForStatusFragment==null){
                                applyForStatusFragment = new ApplyForStatusFragment();
                                setFragment(applyForStatusFragment);
                            }
//                            startActivity(new Intent(getActivity(), ApplyForStatusActivity.class));
                        }else {
                            //跳转入驻选择界面
//                            startActivity(new Intent(getActivity(), ApplyForChooseActivity.class));
                            defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_NULL);
                        }

                    }
                });
        compositeSubscription.add(subscription);
    }


    private void setFragment(Fragment fragment) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment).commit();
    }
    private void getLoanOne(int userId) {
        Subscription subscription = NetWork.getBankService().getOne(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<YwDksq>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_FAILED);
                        Log.e("Dani", "快速贷款信息onError：" + e.toString());
                    }
                    @Override
                    public void onNext(YwDksq ywDksq) {
                        Log.e("Dani", "快速贷款信息onNext：" + ywDksq.toString());
                        defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_SUCCESS);
                        if (!StringUtils.isEmpty(ywDksq.getSqzt())){
                                if (submitInformationFragment==null){
                                submitInformationFragment = new SubmitInformationFragment();
                                setFragment(submitInformationFragment);
                            }
//                            startActivity(new Intent(getActivity(), SubmitInformationActivity.class));
                        } else {
//                            startActivity(new Intent(ApplyForSudelChooseActivity.this, BasicInformationActivity.class));
                            defaultLoadview.lodingIsFailOrSucess(Constan.LOAD_NULL);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String str) {
        if ("ApplyForStatusFragment_pay".equals(str)){
          startActivity(new Intent(getActivity(),MyApplyForPayActivity.class));
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
