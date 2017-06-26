package tqm.bianfeng.com.tqm.bank.bankinformations.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.CustomView.LoadMoreView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.bank.bankactivitys.BankActivitionsAdapter;
import tqm.bianfeng.com.tqm.bank.bankinformations.EditStr;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.main.MainActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.ListItemPositioin;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;
    private int type = 0;//0：金融咨询 1：金融课堂
    private int pagNum = 0;//咨询从1开始
    private int mPagItemSize = 0;
    private CompositeSubscription mCompositeSubscription;
    List<BankInformItem> AllBankInformItems;
    private List<BankActivityItem> datas;
    private Intent intent;

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
            type = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        mCompositeSubscription = new CompositeSubscription();
        defaultLoadview.lodingIsFailOrSucess(1);
        datas = new ArrayList<>();
        Log.e("Daniel", "---datas---" );
        initDate(0, Constan.NOTPULLUP, null, null);
        return view;
    }

    LoadMoreView loadMoreTxt;
    BankActivitionsAdapter bankActivitionsAdapter;

    private void initDate(int pagNum, final boolean pullUp, String search, String gson) {
        Log.e("Daniel", "---type---" + type);
        Log.e("Daniel", "---pagNum---" + pagNum);
        if (datas.size() > 0) {
            if (loadMoreTxt != null) {
                loadMoreTxt.loadMoreViewAnim(1);
            }
        } else {
            if (loadMoreTxt != null) {
                loadMoreTxt.loadMoreViewAnim(4);
            }
        }
        if (type == 1) {
            type = 3;
        }
        Subscription getBankInformItem_subscription = NetWork.getBankService()
                .getBankActivityItem(search, gson, Constan.HOMESHOW_FALSE, pagNum, Constan.PAGESIZE, MainActivity.locationStr, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankListItems<BankActivityItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        defaultLoadview.lodingIsFailOrSucess(3);
                        if (loadMoreTxt != null) {
                            loadMoreTxt.loadMoreViewAnim(4);
                        }
                    }

                    @DebugLog
                    @Override
                    public void onNext(BankListItems<BankActivityItem> bankActivityItemBankListItems) {

                        for (BankActivityItem bankActivityItem : bankActivityItemBankListItems.getItem()) {
                            datas.add(bankActivityItem);
                        }
                        Log.e("Daniel", "---datas.size()---" + datas.size());
                        setBankInformItemAdapter(datas);
                        defaultLoadview.lodingIsFailOrSucess(2);
                        mListener.initEdi();

                        //加载更多判断
                        loadMoreTxt.doLoad(datas.size(), bankActivityItemBankListItems.getItem().size());

                    }
                });
        mCompositeSubscription.add(getBankInformItem_subscription);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ListItemPositioin event) {
        if ("01".equals(event.getModule())) {
            Integer position = event.getPosition();
            Log.i("Daniel", "---onEventMainThread2---" + event.getPosition());
            //跳转银行活动详情
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("detailType", "01");
            intent.putExtra("detailId", bankActivitionsAdapter.getItem(position).getActivityId());
            intent.putExtra("detailTitle", bankActivitionsAdapter.getItem(position).getActivityTitle());
            intent.putExtra("articlePath", bankActivitionsAdapter.getItem(position).getArticlePath());
            startActivity(intent);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EditStr event) {
        initDate(0, Constan.NOTPULLUP, event.getEditStr(), null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface mListener {
        public void detailActivity(Intent intent);

        public void initEdi();
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    TestRecyclerViewAdapter recyclerViewAdapter;
    LoadMoreWrapper mLoadMoreWrapper;
    View loadMoreView;

    private void setBankInformItemAdapter(List<BankActivityItem> bankActivityItems) {
        if (recyclerViewAdapter == null) {
            recyclerViewAdapter = new TestRecyclerViewAdapter(getActivity(), bankActivityItems);
            recyclerViewAdapter.setOnItemClickListener(new TestRecyclerViewAdapter.BankInformItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    intent = new Intent(getActivity(), DetailActivity.class);
                    //跳转银行活动详情
                    intent.putExtra("detailType", "01");
                    intent.putExtra("detailId", recyclerViewAdapter.getItem(position).getActivityId());
                    intent.putExtra("detailTitle", recyclerViewAdapter.getItem(position).getActivityTitle());
                    intent.putExtra("articlePath", recyclerViewAdapter.getItem(position).getArticlePath());
                    startActivity(intent);
                }

                @Override
                public void changePosition(int position) {
                    mLoadMoreWrapper.notifyItemChanged(position);
                }
            });
            //添加上拉加载
            mLoadMoreWrapper = new LoadMoreWrapper(recyclerViewAdapter);
            loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.default_loading, null);
            loadMoreTxt = (LoadMoreView) loadMoreView.findViewById(R.id.load_more_txt);
            loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreWrapper.setLoadMoreView(loadMoreView);
            //加载监听
            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    //在此开起加载动画，更新数据
                    Log.e("Daniel", "---onLoadMoreRequested----");
                    if (datas.size() % 10 == 0 && datas.size() != 0) {
                        pagNum = pagNum + 10;
                        initDate(pagNum, Constan.NOTPULLUP, null, null);
                    }
                }
            });
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setHasFixedSize(true);
            //Use this now
            mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
            mRecyclerView.setAdapter(mLoadMoreWrapper);
        } else {
            recyclerViewAdapter.update(bankActivityItems);
            mLoadMoreWrapper.notifyDataSetChanged();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
