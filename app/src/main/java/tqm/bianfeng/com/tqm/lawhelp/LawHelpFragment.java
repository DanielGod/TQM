package tqm.bianfeng.com.tqm.lawhelp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.DropDownMenu;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.lawhelp.adapter.LawListAdapter;
import tqm.bianfeng.com.tqm.lawhelp.adapter.ListDropDownAdapter;
import tqm.bianfeng.com.tqm.lawhelp.tools.ThreeAddTools;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.LawAdd;
import tqm.bianfeng.com.tqm.pojo.LawyerItem;

public class LawHelpFragment extends BaseFragment {


    @BindView(R.id.dropDownMenu)
    tqm.bianfeng.com.tqm.CustomView.DropDownMenu dropDownMenu;
    @BindView(R.id.YBJ_loding)
    ImageView YBJLoding;
    @BindView(R.id.YBJ_loding_txt)
    TextView YBJLodingTxt;

    private List<View> popupViews = new ArrayList<>();

    private LawAdd lawAdd;

    private String headers[] = {"城市", "县区", "擅长领域"};
    private String specialField;//所选法律类型
    private String city = "";
    private List<String> districts;
    private List<String> specialFields;
    private ListDropDownAdapter districtAdapter;
    private ListDropDownAdapter lawAdapter;
    private List<LawyerItem> datas;
    private int nowIndex = 0;
    private LawListAdapter lawListAdapter;
    private View loadMoreView;
    private ThreeAddTools threeAddTools;
    private TextView loadMoreTxt;//加载更多文字

    public static LawHelpFragment newInstance() {
        LawHelpFragment fragment = new LawHelpFragment();
        return fragment;
    }

    public interface mListener {
        public void changeActivity(
                Class activityClass);

        public void detailActivity(Intent intent);
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_law_help, container, false);
        ButterKnife.bind(this, view);
        lodingIsFailOrSucess(1);
        threeAddTools = ThreeAddTools.getTools();
        districts = new ArrayList<>();
        specialFields = new ArrayList<>();
        datas = new ArrayList<>();
        //初始化顶部选择器数据
        initSpecialFields();
        return view;
    }

    public void initTopTxt() {
        //初始化查找数据
        lawAdd = realm.where(LawAdd.class).findFirst();
        if (lawAdd == null) {
            lawAdd = new LawAdd();
            lawAdd.setId(1);
            realm.beginTransaction();
            realm.insertOrUpdate(lawAdd);
            realm.commitTransaction();

        } else {
            realm.beginTransaction();
            Log.i("gqf", "lawAdd" + lawAdd.toString());
            //更新顶部选择器文字
            if (!lawAdd.getCity().equals("")) {
                headers[0] = lawAdd.getCity();
                city = lawAdd.getCity();
            }
            if (!lawAdd.getDistrict().equals("")) {
                lawAdd.setDistrict("");
            }
            if (!lawAdd.getSpecialField().equals("")) {
                lawAdd.setSpecialField("");
            }
            realm.copyToRealmOrUpdate(lawAdd);
            realm.commitTransaction();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //根据所选城市加载区县
        if (lawAdd != null) {
            if (!lawAdd.getCity().equals("")) {
                initDistricts(lawAdd.getCity());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dropDownMenu.isWork()) {
            lawAdd = realm.where(LawAdd.class).findFirst();
            if (!lawAdd.getCity().equals("")) {
                if (!city.equals(lawAdd.getCity()) || (city.equals("") && !lawAdd.getCity().equals(""))) {
                    city = lawAdd.getCity();
                    dropDownMenu.setTabTxtByIndex(city, 0);
                    initDistricts(lawAdd.getCity());
                    initListData(true, lawAdd.getQueryParams(), 1);
                }
            }else if(lawAdd.getCity().equals("")){
                city = lawAdd.getCity();
                dropDownMenu.setTabTxtByIndex("城市", 0);
                initDistricts(lawAdd.getCity());
                initListData(true, lawAdd.getQueryParams(), 1);
            }
        }
    }

    public void lodingIsFailOrSucess(int i) {
        if (i == 1) {
            //加载中
            YBJLoding.setVisibility(View.VISIBLE);
            YBJLodingTxt.setVisibility(View.VISIBLE);
            YBJLodingTxt.setText("加载中...");
            YBJLoding.setBackgroundResource(R.drawable.loding_anim_lists);
            AnimationDrawable anim = (AnimationDrawable) YBJLoding.getBackground();
            anim.start();
        } else if (i == 2) {
            //加载成功
            YBJLoding.setBackground(null);
            YBJLoding.setVisibility(View.GONE);
            YBJLodingTxt.setVisibility(View.GONE);
        }else if(i==3){
            //没有数据
            YBJLoding.setVisibility(View.VISIBLE);
            YBJLodingTxt.setVisibility(View.VISIBLE);
            YBJLoding.setBackground(null);
            YBJLodingTxt.setText("当前条件没有查询到数据");
            YBJLoding.setImageResource(R.drawable.ic_no_city);
            Animation myAnimation= AnimationUtils.loadAnimation(getActivity(),R.anim.dd_mask_in);
            YBJLoding.setAnimation(myAnimation);
            YBJLodingTxt.setAnimation(myAnimation);
            myAnimation.start();
        }
        else {
            //加载失败
            YBJLoding.setVisibility(View.VISIBLE);
            YBJLodingTxt.setVisibility(View.VISIBLE);
            YBJLoding.setBackground(null);
            YBJLodingTxt.setText("加载失败，请检查网络连接");
            YBJLoding.setImageResource(R.drawable.ic_loding_fail);
        }
    }

    RecyclerView contentView;

    //获取数据
    public void initListData(final boolean isRefresh, String queryParams, int index) {
        Log.e("gqf", index + "queryParams" + queryParams);
        //开始加载动画
        loadMoreViewAnim(1);
        Subscription getBankFinancItem_subscription = NetWork.getLawService().getLawyerItem(queryParams, index, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LawyerItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<LawyerItem> lawyerItems) {
                        //判断是刷新还是加载更多
                        if (!isRefresh) {
                            nowIndex++;
                        } else {
                            datas = new ArrayList<>();
                            nowIndex = 1;
                        }
                        for (LawyerItem lawyerItem : lawyerItems) {
                            datas.add(lawyerItem);
                        }

                        //显示数据
                        initList(datas);

                        //加载更多判断
                        if (datas.size() < 10) {
                            //隐藏
                            loadMoreViewAnim(4);
                        } else if (datas.size() > 10 && lawyerItems.size() < 10) {
                            //没有更多
                            loadMoreViewAnim(3);
                        } else {
                            //加载完成
                            loadMoreViewAnim(2);
                        }
                    }
                });
        compositeSubscription.add(getBankFinancItem_subscription);
    }

    LoadMoreWrapper mLoadMoreWrapper;

    public void initList(List<LawyerItem> lawyerItems) {
        //数据有无提示判断
        if(lawyerItems.size()==0){
            lodingIsFailOrSucess(3);
        }else{
            lodingIsFailOrSucess(2);
        }
        Log.e("gqf", "lawyerItems" + lawyerItems.toString());
        //初始化列表
        if (lawListAdapter == null) {
            lawListAdapter = new LawListAdapter(getActivity(), lawyerItems);
            //自定义点击
            lawListAdapter.setOnItemClickListener(new LawListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(getActivity(), LawDetailActivity.class);
                    intent.putExtra("lawyer", datas.get(position).getLawyerId() + "");
                    mListener.detailActivity(intent);
                }

                @Override
                public void CallClick(int position) {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15670702651"));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mListener.detailActivity(intentPhone);
                }

                @Override
                public void CollectionClick(int position) {

                }
            });
            //添加上拉加载
            mLoadMoreWrapper = new LoadMoreWrapper(lawListAdapter);
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
                    if (lawListAdapter.getItemCount() % 10 == 0 && lawListAdapter.getItemCount() != 0) {
                        initListData(false, lawAdd.getQueryParams(), nowIndex + 1);
                    }
                }
            });
            contentView.setLayoutManager(new LinearLayoutManager(getActivity()));
            contentView.setAdapter(mLoadMoreWrapper);
        } else {
            lawListAdapter.update(lawyerItems);
            mLoadMoreWrapper.notifyDataSetChanged();
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

    //获取顶部选择器条件
    public void initSpecialFields() {
        Subscription getSpecialFields = NetWork.getLawService().getSpecialFields()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        specialFields = strings;
                        specialFields.add(0, "不限");
                        Log.e("gqf", "specialFields" + specialFields.toString());
                        iniDropMenu();
                    }
                });
        compositeSubscription.add(getSpecialFields);
    }


    //根据所选城市加载县区
    public void initDistricts(String city) {
        districts = threeAddTools.getDistrictsByCity(getActivity(), city);
        if(districts.size()>0){
            districts.set(0, "全部");
        }
        Log.i("gqf", city + "districts" + districts.toString());
        districtAdapter.update(districts);
    }

    //更新本地存储
    public void setLawAddDistrictOrSpecialField(String data,int type){
        realm.beginTransaction();
        if(type==0){
            lawAdd.setDistrict(data);
        }else{
            lawAdd.setSpecialField(data);
        }
        realm.copyToRealmOrUpdate(lawAdd);
        realm.commitTransaction();
    }

    //初始化选择器
    public void iniDropMenu() {

         ListView districtView0 = new ListView(getActivity());
        districtView0.setDividerHeight(0);
        districtAdapter = new ListDropDownAdapter(getActivity(), new ArrayList<String>());
        districtView0.setAdapter(districtAdapter);

        //县区下拉列表
         ListView districtView = new ListView(getActivity());
        districtView.setDividerHeight(0);
        districtAdapter = new ListDropDownAdapter(getActivity(), districts);
        districtView.setAdapter(districtAdapter);

        //律师条件下拉列表
         ListView lawView = new ListView(getActivity());
        lawView.setDividerHeight(0);
        lawAdapter = new ListDropDownAdapter(getActivity(), specialFields);
        lawView.setAdapter(lawAdapter);

        popupViews.add(districtView0);
        popupViews.add(districtView);
        popupViews.add(lawView);
        districtView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                districtAdapter.setCheckItem(i);
                setLawAddDistrictOrSpecialField(i == 0 ? "" : districts.get(i),0);
                dropDownMenu.setTabText(i == 0 ? headers[1] : districts.get(i));
                dropDownMenu.closeMenu();
                //更新数据
                initListData(true, lawAdd.getQueryParams(), 1);
            }
        });
        lawView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lawAdapter.setCheckItem(i);
                setLawAddDistrictOrSpecialField(i == 0 ? "" : specialFields.get(i),1);
                dropDownMenu.setTabText(i == 0 ? headers[2] : specialFields.get(i));
                dropDownMenu.closeMenu();
                //更新数据
                initListData(true, lawAdd.getQueryParams(), 1);
            }
        });

        //设置内容
        contentView = new RecyclerView(getActivity());
        //根据数据更新
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        contentView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initTopTxt();
        //初始化数据
        Log.e("gqf", lawAdd.getQueryParams());
        initListData(true, lawAdd.getQueryParams(), nowIndex + 1);
        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
        dropDownMenu.setOnClickLinsener(new DropDownMenu.OnClickLinsener() {
            @Override
            public void onClickIndexOne(int index) {
                //跳转地址选择页
                if (index == 0) {
                    mListener.changeActivity(AllCityActivity.class);
                } else {
                    if (lawAdd.getCity().equals("")) {
                        Toast.makeText(getActivity(), "没有对应县区数据，请先选择城市", Toast.LENGTH_SHORT).show();
                        dropDownMenu.closeMenu();
                    }
                }

                //根据选择器是否下拉显示无数据提示
                if(dropDownMenu.isShowing()) {
                    lodingIsFailOrSucess(2);
                }else{
                    if(datas.size()==0){
                        lodingIsFailOrSucess(3);
                    }
                }
            }

            @Override
            public void onClose() {
                //关闭时显示无数据提示
                if(datas.size()==0){
                    lodingIsFailOrSucess(3);
                }
            }
        });

        initDistricts(lawAdd.getCity());
    }


    @Override
    public void onDetach() {
        super.onDetach();
        compositeSubscription.unsubscribe();
    }


}
