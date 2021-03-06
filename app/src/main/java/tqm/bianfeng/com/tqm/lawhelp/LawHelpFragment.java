package tqm.bianfeng.com.tqm.lawhelp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.CustomView.DefaultLoadView;
import tqm.bianfeng.com.tqm.CustomView.DropDownMenu;
import tqm.bianfeng.com.tqm.CustomView.LoadMoreView;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.Util.GeneralTools;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.lawhelp.adapter.LawListAdapter;
import tqm.bianfeng.com.tqm.lawhelp.adapter.ListDropDownAdapter;
import tqm.bianfeng.com.tqm.lawhelp.tools.ThreeAddTools;
import tqm.bianfeng.com.tqm.main.DetailActivity;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.LawAdd;
import tqm.bianfeng.com.tqm.pojo.LawyerItem;
import tqm.bianfeng.com.tqm.pojo.User;


public class LawHelpFragment extends BaseFragment {


    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    @BindView(R.id.default_loadview)
    DefaultLoadView defaultLoadview;

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
    private LoadMoreView loadMoreTxt;//加载更多文字

    private boolean isOnLoading = false;

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
        Log.e("Daniel", "onCreateView" );
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_law_help, container, false);
        ButterKnife.bind(this, view);
        lawAdd = realm.where(LawAdd.class).findFirst();
        defaultLoadview.lodingIsFailOrSucess(1);
        threeAddTools = ThreeAddTools.getTools();
        districts = new ArrayList<>();
        specialFields = new ArrayList<>();
        datas = new ArrayList<>();
        //初始化顶部选择器数据
        initSpecialFields();
        return view;
    }

    public void initTopTxt() {
        Log.e("Daniel", "initTopTxt----start：");
        //初始化查找数据
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (lawAdd!=null) {
                    Log.e("Daniel", "lawAdd1 " + lawAdd.getQueryParams());
                    //更新顶部选择器文字
                    if (!GeneralTools.StringUtils.isEmpty(lawAdd.getCity())) {
                        headers[0] = lawAdd.getCity();
                        headers[1] = "县区";
                        city = lawAdd.getCity();
                    }
                    if (!GeneralTools.StringUtils.isEmpty(lawAdd.getDistrict())) {
                        lawAdd.setDistrict("");
                    }
                    if (!GeneralTools.StringUtils.isEmpty(lawAdd.getSpecialField())) {
                        lawAdd.setSpecialField("");
                    }
                }else {
                    lawAdd = realm.createObject(LawAdd.class);
                    lawAdd.setId(1);
                }
                Log.e("Daniel", "lawAdd2 " + lawAdd.getQueryParams());
            }
        });

//        Log.e("Daniel", "isInTransaction："+realm.isInTransaction());
//        if (!realm.isInTransaction()){
//            realm.beginTransaction();
//        }
//
//        Log.e("Daniel", "lawAdd："+lawAdd);
//        if (lawAdd == null) {
//            lawAdd = new LawAdd();
//            lawAdd.setId(1);
////            realm.beginTransaction();
//            realm.insertOrUpdate(lawAdd);
////            realm.commitTransaction();
//        } else {
////            realm.beginTransaction();
//            Log.e("Daniel", "lawAdd1 " + lawAdd.getQueryParams());
//            //更新顶部选择器文字
//            if (!lawAdd.getCity().equals("")) {
//                headers[0] = lawAdd.getCity();
//                headers[1] = "县区";
//                city = lawAdd.getCity();
//            }
//            if (!lawAdd.getDistrict().equals("")) {
//                lawAdd.setDistrict("");
//            }
//            if (!lawAdd.getSpecialField().equals("")) {
//                lawAdd.setSpecialField("");
//            }
//            realm.copyToRealmOrUpdate(lawAdd);
//        }
//        Log.e("Daniel", "isInTransaction2："+realm.isInTransaction());
//        realm.commitTransaction();
//        Log.e("Daniel", "isInTransaction3："+realm.isInTransaction());
        Log.e("Daniel", "initTopTxt----end：");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Daniel", "onStart：");
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
        Log.e("Daniel", "onResume" );
        Log.e("Daniel", "dropDownMenu.isWork()："+dropDownMenu.isWork() );
        if (dropDownMenu.isWork()) {
            //判断条件信息是否改变，改变则重新加载列表
            lawAdd = realm.where(LawAdd.class).findFirst();
            if (!lawAdd.getCity().equals("")) {
                if (city.equals(lawAdd.getCity()) || !city.equals(lawAdd.getCity()) || (city.equals("") && !lawAdd.getCity().equals(""))) {
                    city = lawAdd.getCity();
                    dropDownMenu.setTabTxtByIndex("县区", 2);
                    dropDownMenu.setTabTxtByIndex(city, 0);
                    initDistricts(lawAdd.getCity());
                    Log.e("Daniel", "lawAdd" + lawAdd.getQueryParams());
                    initListData(true, lawAdd.getQueryParams(), 1);
                }
            } else if (lawAdd.getCity().equals("")) {
                city = lawAdd.getCity();
                dropDownMenu.setTabTxtByIndex("县区", 2);
                dropDownMenu.setTabTxtByIndex("城市", 0);
                initDistricts(lawAdd.getCity());
                initListData(true, lawAdd.getQueryParams(), 1);
            }
        }
        initTopTxt();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //判断是否因卡顿导致加载失败
            if (!isOnLoading) {
                if (specialFields == null) {
                    initSpecialFields();
                } else if (datas == null) {
                    initListData(true, lawAdd.getQueryParams(), 1);
                }
            }
            if(datas.size()==0&&lawAdd!=null){
                initListData(true, lawAdd.getQueryParams(), 1);
            }
        }
    }
    RecyclerView contentView;

    //获取数据
    public void initListData(final boolean isRefresh, String queryParams, int index) {
        Log.e("Daniel", "initListData----start：");
        isOnLoading = true;
        //开始加载动画
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
        Log.e("Daniel", index + "queryParams" + queryParams);
        Subscription getBankFinancItem_subscription = NetWork.getLawService()
                .getLawyerItem(queryParams, userId, index, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LawyerItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (datas.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                        if (loadMoreTxt != null) {
                            loadMoreTxt.loadMoreViewAnim(4);
                        }
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
                        Log.e("Daniel",  "获取律师列表：" + datas);

                        //显示数据
                        initList(datas);

                        //加载更多判断
                        loadMoreTxt.doLoad(datas.size(), lawyerItems.size());
                        isOnLoading = false;
                        if (datas.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                    }
                });
        compositeSubscription.add(getBankFinancItem_subscription);
        Log.e("Daniel", "initListData----end：");
    }

    LoadMoreWrapper mLoadMoreWrapper;

    public void initList(List<LawyerItem> lawyerItems) {
        Log.e("Daniel", "initList:lawyerItems"+lawyerItems.toString() );
        //数据有无提示判断
        if (lawyerItems.size() == 0) {
            defaultLoadview.lodingIsFailOrSucess(3);
        } else {
            defaultLoadview.lodingIsFailOrSucess(2);
        }

        //初始化列表
        if (lawListAdapter == null) {
            Log.e("Daniel", "lawListAdapter == null" );
            lawListAdapter = new LawListAdapter(getActivity(), lawyerItems);
            //自定义点击
            lawListAdapter.setOnItemClickListener(new LawListAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("detailId", datas.get(position).getLawyerId());
                    intent.putExtra("detailTitle", datas.get(position).getLawyerName());
                    intent.putExtra("detailType", DetailActivity.LAWYER_TYPE);
                    mListener.detailActivity(intent);
                }

                @Override
                public void CallClick(int position) {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + datas.get(position).getContact()));
                    intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mListener.detailActivity(intentPhone);
                }

                @Override
                public void CollectionClick(int position) {

                }

                @Override
                public void changePosition(int position) {
                    mLoadMoreWrapper.notifyItemChanged(position);
                }
            });
            //添加上拉加载
            mLoadMoreWrapper = new LoadMoreWrapper(lawListAdapter);
            loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.default_loading, null);
            loadMoreTxt = (LoadMoreView) loadMoreView.findViewById(R.id.load_more_txt);
            loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadMoreWrapper.setLoadMoreView(loadMoreView);
            //加载监听
            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    //在此开起加载动画，更新数据
                    Log.e("Daniel", "onLoadMoreRequested");
                    if (lawListAdapter.getItemCount() % 10 == 0 && lawListAdapter.getItemCount() != 0) {
                        initListData(false, lawAdd.getQueryParams(), nowIndex + 1);
                    }
                }
            });
            contentView.setLayoutManager(new LinearLayoutManager(getActivity()));
            contentView.setAdapter(mLoadMoreWrapper);
        } else {
            Log.e("Daniel", "lawListAdapter != null" );
            lawListAdapter.update(lawyerItems);
            mLoadMoreWrapper.notifyDataSetChanged();
        }
    }


    //获取顶部选择器条件
    public void initSpecialFields() {
        Log.e("Daniel", "initSpecialFields" );
        isOnLoading = true;
        Subscription getSpecialFields = NetWork.getLawService().getSpecialFields()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (datas.size() == 0) {
                            defaultLoadview.lodingIsFailOrSucess(3);
                        }
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        specialFields = strings;
                        specialFields.add(0, "不限");
                        Log.e("Daniel", "specialFields" + specialFields.toString());
                        isOnLoading = false;
                        iniDropMenu();
                    }
                });
        compositeSubscription.add(getSpecialFields);
    }


    //根据所选城市加载县区
    public void initDistricts(String city) {
        Log.e("Daniel", "initDistricts----start：");
        districts = threeAddTools.getDistrictsByCity(getActivity(), city);
        if (districts.size() > 0) {
            districts.set(0, "全部");
        }
        Log.i("Daniel", city + "districts" + districts.toString());
        //districtAdapter.update(districts);
        //县区下拉列表
        districtView = new ListView(getActivity());
        districtView.setDividerHeight(0);
        districtAdapter = new ListDropDownAdapter(getActivity(), districts);
        districtView.setAdapter(districtAdapter);
        Log.e("Daniel", "initDistricts----end：");
    }

    //更新本地存储
    public void setLawAddDistrictOrSpecialField(String data, int type) {
        realm.beginTransaction();
        if (type == 0) {
            lawAdd.setDistrict(data);
        } else {
            lawAdd.setSpecialField(data);
        }
        realm.copyToRealmOrUpdate(lawAdd);
        realm.commitTransaction();
    }

    ListView districtView;

    //初始化选择器
    public void iniDropMenu() {
        Log.e("Daniel", "iniDropMenu-----start" );
        ListView districtView0 = new ListView(getActivity());
        districtView0.setDividerHeight(0);
        districtAdapter = new ListDropDownAdapter(getActivity(), new ArrayList<String>());
        districtView0.setAdapter(districtAdapter);

//        //县区下拉列表
//        districtView = new ListView(getActivity());
//        districtView.setDividerHeight(0);
//        districtAdapter = new ListDropDownAdapter(getActivity(), districts);
//        districtView.setAdapter(districtAdapter);

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
                setLawAddDistrictOrSpecialField(i == 0 ? "" : districts.get(i), 0);
                dropDownMenu.setTabText(i == 0 ? headers[1] : districts.get(i));

                //更新数据
                initListData(true, lawAdd.getQueryParams(), 1);
                dropDownMenu.closeMenu();
            }
        });
        lawView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lawAdapter.setCheckItem(i);
                setLawAddDistrictOrSpecialField(i == 0 ? "" : specialFields.get(i), 1);
                dropDownMenu.setTabText(i == 0 ? headers[2] : specialFields.get(i));

                //更新数据
                initListData(true, lawAdd.getQueryParams(), 1);
                dropDownMenu.closeMenu();
            }
        });

        RelativeLayout contentRel = new RelativeLayout(getActivity());
        contentRel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        //设置内容
        contentView = new RecyclerView(getActivity());
        //根据数据更新
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        //new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        contentView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        initTopTxt();
        //初始化数据
        Log.e("Daniel", "getQueryParams："+lawAdd.getQueryParams());
        initListData(true, lawAdd.getQueryParams(), nowIndex + 1);
        contentRel.addView(contentView);
        Log.e("Daniel", "addView----end：");
        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentRel);
        dropDownMenu.setOnClickLinsener(new DropDownMenu.OnClickLinsener() {
            @Override
            public boolean onClickIndexOne(int index) {
                //根据选择器是否下拉显示无数据提示
                if (dropDownMenu.isShowing() || (!dropDownMenu.isShowing() && index == 1)) {
                    defaultLoadview.lodingIsFailOrSucess(2);
                }
                //跳转地址选择页
                boolean returnB = true;
                if (index == 0) {
                    mListener.changeActivity(AllCityActivity.class);
                } else if (index == 1) {
                    if (lawAdd.getCity().equals("")) {
                        Toast.makeText(getActivity(), "没有对应县区数据，请先选择城市", Toast.LENGTH_SHORT).show();
                        returnB = false;
                        dropDownMenu.closeMenu();
                    } else {

                    }
                }
                return returnB;
            }

            @Override
            public void onClose() {
                //关闭时显示无数据提示
                Log.i("Daniel", "onClose");
                if (contentView.getChildCount() == 0 || datas.size() == 0) {
                    defaultLoadview.lodingIsFailOrSucess(3);
                }
            }
        });
        initDistricts(lawAdd.getCity());
        Log.e("Daniel", "iniDropMenu-----end" );
    }


    @Override
    public void onDetach() {
        super.onDetach();
        compositeSubscription.unsubscribe();
    }


}
