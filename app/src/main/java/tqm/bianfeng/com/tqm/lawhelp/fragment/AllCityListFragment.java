package tqm.bianfeng.com.tqm.lawhelp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tqm.bianfeng.com.tqm.CustomView.SideBar;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.application.BaseFragment;
import tqm.bianfeng.com.tqm.lawhelp.adapter.HotCityGridAdapter;
import tqm.bianfeng.com.tqm.lawhelp.adapter.allCityListAdapter;
import tqm.bianfeng.com.tqm.lawhelp.tools.InternetImpl;
import tqm.bianfeng.com.tqm.lawhelp.tools.ThreeAddTools;
import tqm.bianfeng.com.tqm.pojo.LawAdd;
import tqm.bianfeng.com.tqm.pojo.cityInfo;

/**
 * Created by johe on 2017/3/31.
 */

public class AllCityListFragment extends BaseFragment {
    ThreeAddTools threeAddTools;
    List<cityInfo> datas;
    @BindView(R.id.all_city_list)
    RecyclerView allCityList;
    @BindView(R.id.all_city_sidebar)
    SideBar allCitySidebar;
    @BindView(R.id.toast_txt)
    TextView toastTxt;

    allCityListAdapter allCityListAdapter;
    InternetImpl internet;
    LinearLayoutManager mLinearLayoutManager;
    int mIndex;
    boolean move = false;
    View headerViewl;

    LinearLayout location_lin;
    RecyclerView hot_city_list;
    Button location_btn;
    String [] hotCity={"北京","成都","重庆","广州","杭州","南京","上海","深圳","天津","武汉","洛阳"};
    String [] hotP={"北京","四川","重庆","广东","浙江","江苏","上海","广东","天津","湖北","河南"};

    List<String> hotCityDatas;
    LawAdd lawAdd;
    HotCityGridAdapter hotCityGridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_city_list, container, false);
        threeAddTools = ThreeAddTools.getTools();
        internet = InternetImpl.createInternetImpl();
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData() {
        datas = threeAddTools.readAllCity(getActivity());
        //整理数据
        datas = internet.sortingData(datas);
        //载入数据
        initList(datas);
    }

    public interface mListener {
        public void CloseActivity();
    }

    private mListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (mListener) activity;

    }

    public void initList(List<cityInfo> data) {
        //载入列表
        allCityListAdapter = new allCityListAdapter(getActivity(), data);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        allCityList.setLayoutManager(mLinearLayoutManager);
        allCityList.setAdapter(allCityListAdapter);

        //增加头部或者足部的RecyclerView
        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(allCityListAdapter);
        initHeader();
        mHeaderAndFooterWrapper.addHeaderView(headerViewl);
        allCityList.setAdapter(mHeaderAndFooterWrapper);


        allCityListAdapter.setMyItemClickListener(new allCityListAdapter.MyItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                Toast.makeText(getActivity(), datas.get(position).getCity(), Toast.LENGTH_SHORT).show();
                LawAdd lawAdd=realm.where(LawAdd.class).findFirst();
                realm.beginTransaction();
                lawAdd.setCity(datas.get(position).getCity());
                lawAdd.setProvince(datas.get(position).getProvince());
                realm.copyToRealmOrUpdate(lawAdd);
                realm.commitTransaction();
                mListener.CloseActivity();
            }
        });


        allCityList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        allCityList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n2 = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n2 && n2 < allCityList.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = allCityList.getChildAt(n2).getTop();
                        //最后的移动
                        allCityList.scrollBy(0, top);
                    }
                }
            }

        });
        allCityList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    toastTxt.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    toastTxt.setVisibility(View.INVISIBLE);
                } else {
                    toastTxt.setVisibility(View.VISIBLE);
                    cityInfo cityInfo = datas.get(mLinearLayoutManager.findFirstVisibleItemPosition());
                    //toastTxt.setText(cityInfo.getSortKey());
                    Log.i("gqf", "cityInfo" + cityInfo.getCity());
                    toastTxt.setText(cityInfo.getSortKey());
                }
                return false;
            }
        });
        allCitySidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = allCityListAdapter.getPositionForSection(s
                        .charAt(0));
                Log.i("gqf", "demo" + s.charAt(0));
                moveToPosition(position);

            }
        });
    }

    public void initHeader() {
        headerViewl = getActivity().getLayoutInflater().inflate(R.layout.all_city_list_header, null);
        location_lin = (LinearLayout) headerViewl.findViewById(R.id.location_lin);
        hot_city_list = (RecyclerView) headerViewl.findViewById(R.id.hot_city_list);
        location_btn = (Button) headerViewl.findViewById(R.id.location_btn);

        if(realm.where(LawAdd.class).findFirst()!=null) {
            lawAdd=realm.where(LawAdd.class).findFirst();
            if (!lawAdd.getCity().equals("")) {
                location_lin.setVisibility(View.VISIBLE);
                location_btn.setText(lawAdd.getCity());
            } else {
                location_lin.setVisibility(View.GONE);
            }
        }

        hotCityDatas=new ArrayList<>();
        for(String cityName:hotCity){
            hotCityDatas.add(cityName);
        }

        hotCityGridAdapter=new HotCityGridAdapter(getActivity(),hotCityDatas);
        hot_city_list.setLayoutManager(new GridLayoutManager( getActivity(),3) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
                int measuredWidth = hot_city_list.getMeasuredWidth();
                int measuredHeight = hot_city_list.getMeasuredHeight();
                int myMeasureHeight = 0;
                int count = state.getItemCount();
                for (int i = 0; i < count; i++) {
                    View view = recycler.getViewForPosition(i);
                    if (view != null) {
                        if (myMeasureHeight < measuredHeight && i % 3 == 0) {
                            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                                    getPaddingLeft() + getPaddingRight(), p.width);
                            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                                    getPaddingTop() + getPaddingBottom(), p.height);
                            view.measure(childWidthSpec, childHeightSpec);
                            myMeasureHeight += view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                        }
                        recycler.recycleView(view);
                    }
                }
                setMeasuredDimension(measuredWidth, Math.min(measuredHeight, myMeasureHeight));
            }}
            );
        hot_city_list.setAdapter(hotCityGridAdapter);

        location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.CloseActivity();
            }
        });

        hotCityGridAdapter.setMyItemClickListener(new HotCityGridAdapter.MyItemClickListener() {
            @Override
            public void OnClickListener(int position) {
                Toast.makeText(getActivity(), hotCityDatas.get(position), Toast.LENGTH_SHORT).show();
                LawAdd lawAdd=realm.where(LawAdd.class).findFirst();
                realm.beginTransaction();
                lawAdd.setCity(hotCityDatas.get(position));
                lawAdd.setProvince(hotP[position]);
                realm.copyToRealmOrUpdate(lawAdd);
                realm.commitTransaction();
                mListener.CloseActivity();
            }
        });

    }

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        mIndex = n;
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            allCityList.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = allCityList.getChildAt(n - firstItem).getTop();
            allCityList.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            allCityList.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }

    }


}
