package tqm.bianfeng.com.tqm.bank.bankinformations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

public class BankInformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private Integer mParam1;
    private int pagNum = 1;
    private int mPagItemSize = 0;
    private CompositeSubscription mCompositeSubscription;

    public BankInformationFragment() {
        // Required empty public constructor
    }

    @DebugLog
    public static BankInformationFragment newInstance(Integer param1) {
        BankInformationFragment fragment = new BankInformationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    @DebugLog
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }
    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank_information, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mCompositeSubscription = new CompositeSubscription();
        initDate(mParam1+1, pagNum);
    }

    @DebugLog
    @Override
    public void onStart() {
        super.onStart();
    }



    private void initDate(Integer type, int pagNum) {
        Log.e("Daniel", "---type---" + type);
        Log.e("Daniel", "---pagNum---" + pagNum);
        Subscription getBankInformItem_subscription = NetWork.getBankService()
                .getBankInformItem(type, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankInformItem>>() {
                    @Override
                    public void onCompleted() {
                        //设置可上拉刷新和下拉刷新

                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankInformItem> bankInformItems) {
                        mPagItemSize = bankInformItems.size();
                        setBankInformItemAdapter(bankInformItems);

                    }
                });
        mCompositeSubscription.add(getBankInformItem_subscription);
    }

    private void setBankInformItemAdapter(List<BankInformItem> bankInformItems) {
        BankInfromationAdapter bankInfromationAdapter = new BankInfromationAdapter(getActivity(),bankInformItems);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCompositeSubscription.unsubscribe();
    }


}
