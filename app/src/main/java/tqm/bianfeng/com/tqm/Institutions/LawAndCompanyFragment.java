package tqm.bianfeng.com.tqm.Institutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.CustomView.LoadMoreView;
import tqm.bianfeng.com.tqm.Institutions.adapter.LawFirmOrInstitutionListAdapter;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/4/10.
 */

public class LawAndCompanyFragment extends BaseFragment {

    public int index = 0;
    public static String ARG_TYPE = "arg_type";
    @BindView(R.id.law_or_company_list)
    RecyclerView lawOrCompanyList;
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;
    int page = 0;
    private List<InstitutionItem> datas;

    private LawFirmOrInstitutionListAdapter lawFirmOrInstitutionListAdapter;

    public static LawAndCompanyFragment newInstance(int position) {
        LawAndCompanyFragment fragment = new LawAndCompanyFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_low_and_company, container, false);
        ButterKnife.bind(this, view);
        defaultLoadview.lodingIsFailOrSucess(1);
        datas = new ArrayList<>();
        initData();


        return view;
    }

    public void initData() {
        if (datas.size() > 0) {
            if (loadMoreTxt != null) {
                loadMoreTxt.loadMoreViewAnim(1);
            }
        } else {
            if (loadMoreTxt != null) {
                loadMoreTxt.loadMoreViewAnim(4);
            }
        }
        int userId=0;
        if(realm.where(User.class).findFirst()!=null){
            userId=realm.where(User.class).findFirst().getUserId();
        }
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getInstitutionItem("0" + (index + 1),userId, page + 1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<InstitutionItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        defaultLoadview.lodingIsFailOrSucess(3);
                        if(loadMoreTxt!=null){
                            loadMoreTxt.loadMoreViewAnim(4);
                        }

                    }

                    @Override
                    public void onNext(List<InstitutionItem> institutionItems) {
                        for (InstitutionItem institutionItem : institutionItems) {
                            datas.add(institutionItem);
                        }

                        Log.i("gqf", "institutionItems" + institutionItems.toString());
                        page++;
                        initList(datas);
                        defaultLoadview.lodingIsFailOrSucess(2);

                        //加载更多判断
                        loadMoreTxt.doLoad(datas.size(),institutionItems.size());
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }

    LoadMoreWrapper mLoadMoreWrapper;
    View loadMoreView;
    LoadMoreView loadMoreTxt;
    Intent intent;
    public void initList(List<InstitutionItem> institutionItems) {
        if (lawFirmOrInstitutionListAdapter == null) {
            lawFirmOrInstitutionListAdapter = new LawFirmOrInstitutionListAdapter(getActivity(), institutionItems);
            lawFirmOrInstitutionListAdapter.setOnItemClickListener(new LawFirmOrInstitutionListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    intent=new Intent(getActivity(),CompanyInfoActivity.class);
                    intent.putExtra("InstitutionId",datas.get(position).getInstitutionId());
                    CompanyInfoActivity.index=2;
                    EventBus.getDefault().post(intent);
                }

                @Override
                public void changePosition(int position) {
                    mLoadMoreWrapper.notifyItemChanged(position);
                }
            });
            //添加上拉加载
            mLoadMoreWrapper = new LoadMoreWrapper(lawFirmOrInstitutionListAdapter);
            loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.default_loading, null);
            loadMoreTxt = (LoadMoreView) loadMoreView.findViewById(R.id.load_more_txt);
            loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreWrapper.setLoadMoreView(loadMoreView);
            //加载监听,
            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    //在此开起加载动画，更新数据
                    Log.e("gqf", "onLoadMoreRequested");
                    if(datas.size()%10==0&&datas.size()!=0){
                        initData();
                    }
                }
            });
            lawOrCompanyList.setLayoutManager(new LinearLayoutManager(getActivity()));
            lawOrCompanyList.setAdapter(mLoadMoreWrapper);
        } else {
            lawFirmOrInstitutionListAdapter.update(institutionItems);
            mLoadMoreWrapper.notifyDataSetChanged();
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
