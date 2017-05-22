package tqm.bianfeng.com.tqm.capital;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.LoadMoreView;
import tqm.bianfeng.com.tqm.CustomView.LoadingIndicator;
import tqm.bianfeng.com.tqm.Institutions.CompanyInfoActivity;
import tqm.bianfeng.com.tqm.Institutions.SearchInstiutionsActivity;
import tqm.bianfeng.com.tqm.Institutions.adapter.LawFirmOrInstitutionListAdapter;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/5/13.
 */

public class PrivateCapitalActivity extends BaseActivity {

    @BindView(R.id.private_capital_toolbar)
    Toolbar privateCapitalToolbar;
    @BindView(R.id.private_capital_list)
    RecyclerView privateCapitalList;
    @BindView(R.id.indicator)
    LoadingIndicator indicator;
    @BindView(R.id.no_search_txt)
    TextView noSearchTxt;
    @BindView(R.id.action_search_img)
    ImageView actionSearchImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_capital);
        ButterKnife.bind(this);
        setToolbar(privateCapitalToolbar, "");
        //        privateCapitalToolbar.inflateMenu(R.menu.search_menu);
        //        privateCapitalToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        //            @Override
        //            public boolean onMenuItemClick(MenuItem item) {
        //                if (item.getItemId() == R.id.search) {
        //                    startActivity(new Intent(PrivateCapitalActivity.this, SearchInstiutionsActivity.class));
        //                }
        //                return false;
        //            }
        //        });
        datas = new ArrayList<>();
        noSearchTxt.setVisibility(View.GONE);
        initData();
    }

    LawFirmOrInstitutionListAdapter lawFirmOrInstitutionListAdapter;
    List<InstitutionItem> datas;

    boolean isMore = false;
    int page = 1;

    public void initData() {
        indicator.showLoading();
        if (datas.size() > 0) {
            if (loadMoreTxt != null) {
                loadMoreTxt.loadMoreViewAnim(1);
            }
        } else {
            if (loadMoreTxt != null) {
                loadMoreTxt.loadMoreViewAnim(4);
            }
        }
        int userId = 0;
        if (realm.where(User.class).findFirst() != null) {
            userId = realm.where(User.class).findFirst().getUserId();
        }
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getInstitutionItem("03", userId, page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<InstitutionItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf", "onError" + e.toString());
                        indicator.hideLoading();
                        showSearchResult();
                        if (loadMoreTxt != null) {
                            loadMoreTxt.loadMoreViewAnim(4);
                        }
                    }

                    @Override
                    public void onNext(List<InstitutionItem> institutionItems) {
                        if (!isMore) {
                            datas = new ArrayList<InstitutionItem>();
                        } else {
                            isMore = false;
                        }

                        for (InstitutionItem institutionItem : institutionItems) {
                            datas.add(institutionItem);
                        }
                        Log.i("gqf", "institutionItems" + institutionItems.toString());
                        initList();
                        showSearchResult();
                        indicator.hideLoading();

                        //加载更多判断
                        loadMoreTxt.doLoad(datas.size(), institutionItems.size());
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }

    public void showSearchResult() {
        if (datas.size() == 0) {
            noSearchTxt.setVisibility(View.VISIBLE);
        } else {
            noSearchTxt.setVisibility(View.GONE);
        }
    }


    LoadMoreWrapper mLoadMoreWrapper;
    View loadMoreView;
    LoadMoreView loadMoreTxt;
    Intent intent;

    public void initList() {
        if (lawFirmOrInstitutionListAdapter == null) {
            lawFirmOrInstitutionListAdapter = new LawFirmOrInstitutionListAdapter(this, datas);
            lawFirmOrInstitutionListAdapter.setOnItemClickListener(new LawFirmOrInstitutionListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    CompanyInfoActivity.index = 3;
                    intent = new Intent(PrivateCapitalActivity.this, CompanyInfoActivity.class);
                    intent.putExtra("InstitutionId", datas.get(position).getInstitutionId());
                    startActivity(intent);
                }

                @Override
                public void changePosition(int position) {
                    mLoadMoreWrapper.notifyItemChanged(position);
                }
            });
            //添加上拉加载
            mLoadMoreWrapper = new LoadMoreWrapper(lawFirmOrInstitutionListAdapter);
            loadMoreView = getLayoutInflater().inflate(R.layout.default_loading, null);
            loadMoreTxt = (LoadMoreView) loadMoreView.findViewById(R.id.load_more_txt);
            loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreWrapper.setLoadMoreView(loadMoreView);
            //加载监听
            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    //在此开起加载动画，更新数据
                    Log.e("gqf", "onLoadMoreRequested");
                    if (datas.size() % 10 == 0 && datas.size() != 0) {
                        page++;
                        isMore = true;
                        initData();
                    }
                }
            });
            privateCapitalList.setLayoutManager(new LinearLayoutManager(this));
            privateCapitalList.setAdapter(mLoadMoreWrapper);
        } else {
            lawFirmOrInstitutionListAdapter.update(datas);
            mLoadMoreWrapper.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.action_search_img)
    public void onClick() {
        Intent intent=new Intent(PrivateCapitalActivity.this, SearchInstiutionsActivity.class);
        intent.putExtra(SearchInstiutionsActivity.get_search_type,SearchInstiutionsActivity.capital_search);
        startActivity(intent);
    }
}
