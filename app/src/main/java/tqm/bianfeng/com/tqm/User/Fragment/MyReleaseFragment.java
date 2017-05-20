package tqm.bianfeng.com.tqm.User.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.User.adapter.MyReleaseActivityAdapter;
import tqm.bianfeng.com.tqm.User.adapter.MyReleaseLoanAdapter;
import tqm.bianfeng.com.tqm.User.release.LoanOrActivityReleaseActivity;
import tqm.bianfeng.com.tqm.User.release.ReleaseMyActivityActivity;
import tqm.bianfeng.com.tqm.User.release.ReleaseProgressActivity;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.ReleaseActivityItem;
import tqm.bianfeng.com.tqm.pojo.ReleaseLoanItem;
import tqm.bianfeng.com.tqm.pojo.User;

/**
 * Created by johe on 2017/5/10.
 */

public class MyReleaseFragment extends BaseFragment{
    public int index = 0;
    public static String ARG_TYPE = "arg_type";
    @BindView(R.id.my_focuse_list)
    RecyclerView myFocuseList;
    @BindView(R.id.toast_txt)
    TextView toastTxt;

    public static boolean isRefresh=false;
    private static final String TYPE="getMyAttentionItem";
    Gson gson;

    MyReleaseActivityAdapter myReleaseActivityAdapter;
    MyReleaseLoanAdapter myReleaseLoanAdapter;


    public static MyReleaseFragment newInstance(int position) {
        MyReleaseFragment fragment = new MyReleaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_focuse, container, false);
        ButterKnife.bind(this, view);
        gson=new Gson();
        isRefresh=false;
        if(index==1){
            initActivityData();
        }else{
            initLoanData();
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(isRefresh){
            isRefresh=false;
            if(index==1){
                initActivityData();
            }else{
                initLoanData();
            }
        }
    }

    public void initLoanData(){
        Subscription getBankFinancItem_subscription = NetWork.getUserService().getReleaseLoanItem(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ReleaseLoanItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","Throwable"+e.toString());
                    }

                    @Override
                    public void onNext(List<ReleaseLoanItem> releaseLoanItems) {
                        Log.i("gqf","onNext"+releaseLoanItems.toString());
                        if(releaseLoanItems.size()>0){
                            toastTxt.setVisibility(View.GONE);
                        }
                        initLoanAdapter(releaseLoanItems);
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }
    public void initActivityData(){
        Subscription getBankFinancItem_subscription = NetWork.getUserService().getBankActivityItem(realm.where(User.class).findFirst().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ReleaseActivityItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("gqf","Throwable"+e.toString());
                    }

                    @Override
                    public void onNext(List<ReleaseActivityItem> releaseActivityItems) {
                        Log.i("gqf","onNext"+releaseActivityItems.toString());
                        if(releaseActivityItems.size()>0){
                            toastTxt.setVisibility(View.GONE);
                        }
                        initActivityAdapter(releaseActivityItems);
                    }
                });

        compositeSubscription.add(getBankFinancItem_subscription);
    }
    public void initLoanAdapter(List<ReleaseLoanItem> releaseLoanItems){
        if(myReleaseLoanAdapter==null){
            myReleaseLoanAdapter=new MyReleaseLoanAdapter(getActivity(),releaseLoanItems);
            myReleaseLoanAdapter.setOnItemClickListener(new MyReleaseLoanAdapter.ReleaseLoanItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent=new Intent(getActivity(), ReleaseProgressActivity.class);
                    intent.putExtra(ReleaseProgressActivity.RELEASE_TYPE,ReleaseProgressActivity.loan_type);
                    intent.putExtra(ReleaseProgressActivity.RELEASE_JSON,gson.toJson(myReleaseLoanAdapter.getDatas().get(postion)));
                    startActivity(intent);

                }

                @Override
                public void EdiRelease(int position) {
                    LoanOrActivityReleaseActivity.RELEASE_TYPE=LoanOrActivityReleaseActivity.RELEASE_LOAN_TYPE;
                    Intent intent=new Intent(getActivity(),LoanOrActivityReleaseActivity.class);
                    intent.putExtra(LoanOrActivityReleaseActivity.RELEASE_ID,myReleaseLoanAdapter.getDatas().get(position).getLoanId());
                    startActivity(intent);
                }
            });
            initList(myReleaseLoanAdapter);
        }else{
            myReleaseLoanAdapter.setdatas(releaseLoanItems);
        }

    }
    public void initActivityAdapter(List<ReleaseActivityItem> releaseActivityItems){
        if(myReleaseActivityAdapter==null){
            myReleaseActivityAdapter=new MyReleaseActivityAdapter(getActivity(),releaseActivityItems);
            myReleaseActivityAdapter.setOnItemClickListener(new MyReleaseActivityAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent=new Intent(getActivity(), ReleaseProgressActivity.class);
                    intent.putExtra(ReleaseProgressActivity.RELEASE_TYPE,ReleaseProgressActivity.activity_type);
                    intent.putExtra(ReleaseProgressActivity.RELEASE_JSON,gson.toJson(myReleaseActivityAdapter.getDataItem(position)));
                    startActivity(intent);
                }

                @Override
                public void EdiRelease(int position) {
//                    LoanOrActivityReleaseActivity.RELEASE_TYPE=LoanOrActivityReleaseActivity.RELEASE_ACTIVITY_TYPE;
//                    Intent intent=new Intent(getActivity(),LoanOrActivityReleaseActivity.class);
//                    intent.putExtra(LoanOrActivityReleaseActivity.RELEASE_ID,myReleaseActivityAdapter.getDataItem(position).getActivityId());
//                    startActivity(intent);
                    Intent intent=new Intent(getActivity(),ReleaseMyActivityActivity.class);
                    intent.putExtra(ReleaseMyActivityActivity.ACTIVITY_ID,myReleaseActivityAdapter.getDataItem(position).getActivityId());
                    startActivity(intent);
                }
            });
            initList(myReleaseActivityAdapter);
        }else{
            myReleaseActivityAdapter.update(releaseActivityItems);
        }

    }
    public void initList(RecyclerView.Adapter adapter){

        myFocuseList.setLayoutManager(new LinearLayoutManager(getActivity()));
        myFocuseList.setAdapter(adapter);

    }



}
