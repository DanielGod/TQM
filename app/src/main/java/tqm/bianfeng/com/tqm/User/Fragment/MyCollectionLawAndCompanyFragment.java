package tqm.bianfeng.com.tqm.User.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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
import tqm.bianfeng.com.tqm.Institutions.CompanyInfoActivity;
import tqm.bianfeng.com.tqm.Institutions.adapter.LawFirmOrInstitutionListAdapter;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/4/10.
 */

public class MyCollectionLawAndCompanyFragment extends BaseFragment {

    public int index = 0;
    public static String ARG_TYPE = "arg_type";
    @BindView(R.id.law_or_company_list)
    RecyclerView lawOrCompanyList;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.YBJ_loding_txt)
    TextView YBJLodingTxt;

    int page = 0;
    private List<InstitutionItem> datas;

    private LawFirmOrInstitutionListAdapter lawFirmOrInstitutionListAdapter;

    public static MyCollectionLawAndCompanyFragment newInstance(int position) {
        MyCollectionLawAndCompanyFragment fragment = new MyCollectionLawAndCompanyFragment();
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
        lodingIsFailOrSucess(1);
        datas = new ArrayList<>();
        //initData();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void initData() {
        if (datas.size() > 0) {
            loadMoreViewAnim(1);
        }
        Subscription getBankFinancItem_subscription = NetWork.getInstitutionService().getCollectInstitutionItem("0" + (index + 1), realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<InstitutionItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        lodingIsFailOrSucess(3);
                        loadMoreViewAnim(4);
                    }

                    @Override
                    public void onNext(List<InstitutionItem> institutionItems) {
                        datas=new ArrayList<InstitutionItem>();
                        for (InstitutionItem institutionItem : institutionItems) {
                            datas.add(institutionItem);
                        }


                        Log.i("gqf", "institutionItems" + institutionItems.toString());
                        page++;
                        initList(datas);
                        lodingIsFailOrSucess(2);
                        loadMoreViewAnim(4);
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }

    LoadMoreWrapper mLoadMoreWrapper;
    View loadMoreView;
    TextView loadMoreTxt;
    Intent intent;
    public void initList(List<InstitutionItem> institutionItems) {
        if (lawFirmOrInstitutionListAdapter == null) {
            lawFirmOrInstitutionListAdapter = new LawFirmOrInstitutionListAdapter(getActivity(), institutionItems);
            lawFirmOrInstitutionListAdapter.setOnItemClickListener(new LawFirmOrInstitutionListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    intent=new Intent(getActivity(),CompanyInfoActivity.class);
                    intent.putExtra("InstitutionId",datas.get(position).getInstitutionId());
                    CompanyInfoActivity.index=index;
                    EventBus.getDefault().post(intent);
                }
            });
            //添加上拉加载
            mLoadMoreWrapper = new LoadMoreWrapper(lawFirmOrInstitutionListAdapter);
            loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.default_loading, null);
            loadMoreTxt = (TextView) loadMoreView.findViewById(R.id.load_more_txt);
            loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreWrapper.setLoadMoreView(loadMoreView);
            //加载监听
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

    public void lodingIsFailOrSucess(int i) {
        if (i == 1) {
            //加载中
            animationView.setVisibility(View.VISIBLE);
            YBJLodingTxt.setVisibility(View.VISIBLE);
            YBJLodingTxt.setText("加载中...");
            //开始动画
            boolean inPlaying = animationView.isAnimating();
            if (!inPlaying) {
                animationView.setProgress(0f);
                animationView.playAnimation();
            }
        } else if (i == 2) {
            //加载成功
            //借书动画
            animationView.cancelAnimation();
            animationView.setVisibility(View.GONE);
            YBJLodingTxt.setVisibility(View.GONE);
        } else if (i == 3) {
            //没有数据
            animationView.setVisibility(View.GONE);
            YBJLodingTxt.setVisibility(View.VISIBLE);
            YBJLodingTxt.setText("没有查询到数据");
            //YBJLoding.setImageResource(R.drawable.ic_no_city);
            YBJLodingTxt.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_no_city), null, null);
            Animation myAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.dd_mask_in);
            animationView.setAnimation(myAnimation);
            YBJLodingTxt.setAnimation(myAnimation);
            myAnimation.start();
        } else {

        }
    }

    public void loadMoreViewAnim(int statu) {
        //操作加载更多动画
        if (loadMoreTxt != null) {
            loadMoreTxt.setVisibility(View.VISIBLE);
            switch (statu) {
                case 1://动画开始
                    loadMoreTxt.setText("加载中...");
                    break;
                case 2://加载完成恢复初始状态
                    loadMoreTxt.setText("加载更多");
                    break;
                case 3://没有更多
                    loadMoreTxt.setText("没有更多");
                    break;
                case 4://不显示
                    loadMoreTxt.setVisibility(View.GONE);
                    break;
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
