package tqm.bianfeng.com.tqm.bank.bankinformations.test;

import android.app.Activity;
import android.content.Intent;
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
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;
//    @BindView(R.id.recyclerView)
//    PullToRefreshListView mainPullRefreshLv;


        @BindView(R.id.recyclerView)
        RecyclerView mRecyclerView;

    private int position = 0;
    private int pagNum = 1;
    private int mPagItemSize = 0;
    private CompositeSubscription mCompositeSubscription;

    public static RecyclerViewFragment newInstance(int position) {
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
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
        ButterKnife.bind(this, view);
        return view;
    }

//    private void initRefreshlv() {
//        //设置刷新时显示的文本
//        ILoadingLayout startLayout = mainPullRefreshLv.getLoadingLayoutProxy(true, false);
//        startLayout.setPullLabel("正在下拉刷新...");
//        startLayout.setRefreshingLabel("正在刷新...");
//        startLayout.setReleaseLabel("放开以刷新");
//
//
//        ILoadingLayout endLayout = mainPullRefreshLv.getLoadingLayoutProxy(false, true);
//        endLayout.setPullLabel("正在上拉刷新...");
//        endLayout.setRefreshingLabel("加载中...");
//        endLayout.setReleaseLabel("放开以刷新");
//
//        mainPullRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                Log.i("Daniel", "---onPullDownToRefresh---");
//                initDate(null, 1);
//
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                Log.i("Daniel", "---onPullDownToRefresh---");
//                if (mPagItemSize > Constan.PAGESIZE) {
//                    pagNum = pagNum + 1;
//                    initDate(null, pagNum);
//                } else {
//                    mainPullRefreshLv.onRefreshComplete();
//                }
//            }
//        });
//
//    }

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
//                        Log.e("Daniel", "---mPagItemSize---" + mPagItemSize);
//                        if (mPagItemSize > Constan.PAGESIZE) {
//                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);
//                        } else {
//                            mainPullRefreshLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//                        }
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

    public interface mListener {
        public void detailActivity(Intent intent);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

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
        final TestRecyclerViewAdapter recyclerViewAdapter = new TestRecyclerViewAdapter(getActivity(), bankInformItems);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(new TestRecyclerViewAdapter.BankInformItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                //跳转银行贷款详情
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("detailType", "04");
                intent.putExtra("detailId", recyclerViewAdapter.getItem(position).getInformId());
                mListener.detailActivity(intent);
            }
        });

//        mainPullRefreshLv.setAdapter(new BankInfromationAdapter(getActivity(), bankInformItems));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mCompositeSubscription = new CompositeSubscription();
//        initRefreshlv();
        initDate(position + 1, pagNum);

        //        final List<Object> items = new ArrayList<>();
        //
        //        for (int i = 0; i < ITEM_COUNT; ++i) {
        //            items.add(new Object());
        //        }


        //setup materialviewpager


    }
}
