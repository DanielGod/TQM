package tqm.bianfeng.com.tqm.capital;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.LoadMoreView;
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
    AVLoadingIndicatorView indicator;
    @BindView(R.id.no_search_txt)
    TextView noSearchTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_capital);
        ButterKnife.bind(this);
        setToolbar(privateCapitalToolbar, "民间资本");
        privateCapitalToolbar.inflateMenu(R.menu.search_menu);
        privateCapitalToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search) {
                    startActivity(new Intent(PrivateCapitalActivity.this, SearchInstiutionsActivity.class));
                }
                return false;
            }
        });
        datas=new ArrayList<>();
        noSearchTxt.setVisibility(View.GONE);
        initData();
    }

    LawFirmOrInstitutionListAdapter lawFirmOrInstitutionListAdapter;
    List<InstitutionItem> datas;

    boolean isMore = false;
    int page = 1;

    public void initData() {
        showLoading(0);
        if (datas.size() > 0) {
            if (loadMoreTxt != null) {
                loadMoreTxt.loadMoreViewAnim(1);
            }
        }
        int userId=0;
        if(realm.where(User.class).findFirst()!=null){
            userId=realm.where(User.class).findFirst().getUserId();
        }
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getInstitutionItem("03",userId, page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<InstitutionItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","onError"+e.toString());
                        showLoading(1);
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
                        showLoading(1);

                        //加载更多判断
                        if (datas.size() < 10) {
                            //隐藏
                            loadMoreTxt.loadMoreViewAnim(4);
                        } else if (datas.size() > 10 && institutionItems.size() < 10) {
                            //没有更多
                            loadMoreTxt.loadMoreViewAnim(3);
                        } else {
                            //加载完成
                            loadMoreTxt.loadMoreViewAnim(2);
                        }
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

    CountDownTimer countDownTimer;
    int progressIndex = 6;

    public void showLoading(int index) {
        if (index == 0) {
            //开始
            indicator.show();
            countDownTimer = new CountDownTimer(1000 * 100, 1000) {
                public void onTick(long millisUntilFinished) {
                    // you can change the progress bar color by ProgressHelper every 800 millis
                    progressIndex++;
                    switch (progressIndex % 6) {
                        case 0:
                            indicator.setIndicatorColor(getResources().getColor(R.color.blue_btn_bg_color));
                            break;
                        case 1:
                            indicator.setIndicatorColor(getResources().getColor(R.color.material_deep_teal_50));
                            break;
                        case 2:
                            indicator.setIndicatorColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                        case 3:
                            indicator.setIndicatorColor(getResources().getColor(R.color.material_deep_teal_20));
                            break;
                        case 4:
                            indicator.setIndicatorColor(getResources().getColor(R.color.material_blue_grey_80));
                            break;
                        case 5:
                            indicator.setIndicatorColor(getResources().getColor(R.color.warning_stroke_color));
                            break;
                        case 6:
                            indicator.setIndicatorColor(getResources().getColor(R.color.success_stroke_color));
                            break;
                    }
                }

                public void onFinish() {
                    progressIndex = 6;
                }
            }.start();

        } else {
            //结束
            indicator.hide();
            countDownTimer.onFinish();
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
                    CompanyInfoActivity.index=3;
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


}
