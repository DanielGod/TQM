package tqm.bianfeng.com.tqm.Institutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
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
import tqm.bianfeng.com.tqm.Institutions.adapter.LawFirmOrInstitutionListAdapter;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/5/10.
 */

public class SearchInstiutionsActivity extends BaseActivity {

    public static final String all_search="00";
    public static final String capital_search="03";
    public static final String get_search_type="get_search_type";
    public String search_type;

    @BindView(R.id.toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.search_city_edi)
    EditText searchCityEdi;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.search_recyclerView)
    RecyclerView searchRecyclerView;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.search_btn)
    Button searchBtn;
    @BindView(R.id.no_search_txt)
    TextView noSearchTxt;
    @BindView(R.id.indicator)
    LoadingIndicator indicator;

    public String searchName;
    boolean isMore=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_instiutions);
        ButterKnife.bind(this);
        //setToolbar(searchToolbar, "搜索机构");
        //searchToolbar.
        search_type=getIntent().getStringExtra(get_search_type);
        searchToolbar.setTitle("搜索机构");
        searchToolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);
        searchToolbar.setTitleTextColor(getResources().getColor(R.color.font_black_1));
        searchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        indicator.hide();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.font_black_7));
        datas=new ArrayList<>();
    }

    LawFirmOrInstitutionListAdapter lawFirmOrInstitutionListAdapter;
    List<InstitutionItem> datas;

    int page=1;
    public void initData(String search) {
        searchName=search;
        searchBtn.setEnabled(false);
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
        noSearchTxt.setVisibility(View.GONE);
        int userId=0;
        if(realm.where(User.class).findFirst()!=null){
            userId=realm.where(User.class).findFirst().getUserId();
        }
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().searchInstitutionItem(searchName,userId,search_type,page,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<InstitutionItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        searchBtn.setEnabled(true);
                        indicator.hideLoading();
                        showSearchResult();
                        if(loadMoreTxt!=null){
                            loadMoreTxt.loadMoreViewAnim(4);
                        }
                    }

                    @Override
                    public void onNext(List<InstitutionItem> institutionItems) {
                        if(!isMore){
                            datas=new ArrayList<InstitutionItem>();
                        }else{
                            isMore=false;
                        }

                        for(InstitutionItem institutionItem:institutionItems){
                            datas.add(institutionItem);
                        }
                        Log.i("gqf", "institutionItems" + institutionItems.toString());
                        initList();
                        searchBtn.setEnabled(true);
                        showSearchResult();
                        indicator.hideLoading();
                        if(datas.size()==0){
                            noSearchTxt.setVisibility(View.VISIBLE);
                        }

                        //加载更多判断
                        loadMoreTxt.doLoad(datas.size(),institutionItems.size());

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
                    intent=new Intent(SearchInstiutionsActivity.this,CompanyInfoActivity.class);
                    intent.putExtra("InstitutionId",datas.get(position).getInstitutionId());
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
                    if(datas.size()%10==0&&datas.size()!=0){
                        page++;
                        isMore=true;
                        initData(searchName);
                    }
                }
            });
            searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            searchRecyclerView.setAdapter(mLoadMoreWrapper);
        } else {
            lawFirmOrInstitutionListAdapter.update(datas);
            mLoadMoreWrapper.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.search_btn)
    public void onClick() {
        if (searchCityEdi.getText().toString().equals("")) {
            Toast.makeText(this, "搜索名称不能为空", Toast.LENGTH_SHORT).show();

        } else {
            initList();
            initData(searchCityEdi.getText().toString());
        }
    }
}
