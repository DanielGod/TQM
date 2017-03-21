package tqm.bianfeng.com.tqm.bank.bankinformations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.List;

import butterknife.BindView;
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

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;



    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private int position =0 ;
    private int pagNum =1;
    private CompositeSubscription mCompositeSubscription;

    public static RecyclerViewFragment newInstance(int position) {
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        recyclerViewFragment.setArguments(bundle);
        return recyclerViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        return view;
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
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<BankInformItem> bankInformItems) {
                        setBankInformItemAdapter(bankInformItems);

                    }
                });
        mCompositeSubscription.add(getBankInformItem_subscription);
    }
    @DebugLog
    private void setBankInformItemAdapter(List<BankInformItem> bankInformItems) {
//        BankInfromationAdapter bankInfromationAdapter = new BankInfromationAdapter(getActivity(),bankInformItems);
        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(new TestRecyclerViewAdapter(getActivity(),bankInformItems));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mCompositeSubscription = new CompositeSubscription();
        initDate(position+1, pagNum);

//        final List<Object> items = new ArrayList<>();
//
//        for (int i = 0; i < ITEM_COUNT; ++i) {
//            items.add(new Object());
//        }


        //setup materialviewpager


    }
}
