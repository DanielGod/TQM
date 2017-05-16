package tqm.bianfeng.com.tqm.bank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.FilterEvens;
import tqm.bianfeng.com.tqm.pojo.bank.FilterInfo;
import tqm.bianfeng.com.tqm.pojo.bank.FilterValues;
import tqm.bianfeng.com.tqm.pojo.bank.Institution;
import tqm.bianfeng.com.tqm.pojo.bank.ProductType;
import tqm.bianfeng.com.tqm.pojo.bank.QueryCondition;
import tqm.bianfeng.com.tqm.pojo.bank.RiskGrade;
import tqm.bianfeng.com.tqm.pojo.bank.buttonViewEven;

/**
 * 作者：chs on 2016/10/10 10:07
 * 邮箱：657083984@qq.com
 */

public class FilterFragment extends Fragment implements FilterAdapter.QueryConditionItemClickListener {
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mDrawerContent;
    private ImageView iv_back;

    private List<QueryCondition> mQueryConditions;
    private Map<String, List<Integer>> mMapFilterId;
    private Map<String, List<String>> mMapFilterName;
    public static boolean filter_item = false;
    FilterAdapter filterAdapter;
    public static List<Integer> filter_value_RiskGrade;
    public static List<Integer> filter_value_Institution;
    public static List<Integer> filter_value_ProductType;
    private FilterInfo mFilterInfo;
    private Map<String, Object> mapFilterInfo;
    private String filterType;//01:贷款 02：理财

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patrol_filter, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);//订阅
        mMapFilterName = new HashMap();
        mMapFilterId = new HashMap();
        initView(view);
        initEvent();
        initdata();

        return view;
    }

    private void initdata() {
        if (mFilterInfo == null) {
            mFilterInfo = new FilterInfo();
        }
        if (mapFilterInfo == null) {
            mapFilterInfo = new HashMap<>();
        }
        filter_value_RiskGrade = new ArrayList<>();
        filter_value_Institution = new ArrayList<>();
        filter_value_ProductType = new ArrayList<>();
        getRiskGrades();


    }

    private void getRiskGrades() {
        NetWork.getBankService().getRiskGrades()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer_RiskGrade);
    }

    private void getInstitutions() {

        NetWork.getBankService().getInstitutions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer_Institution);
    }

    private void getProductTypes() {
        NetWork.getBankService().getProductTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer_ProductType);
    }

    private void getQueryConditions() {
        NetWork.getBankService().queryConditions("02")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer_queryConditions);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    @DebugLog
    public void onEventMainThread(buttonViewEven event) {
        String value = event.getButtonView();
        if (event.isFlag()) {
            getType(value);
        } else {
            removeType(value);
        }


    }

    @DebugLog
    private boolean removeType(String value) {
        boolean bStop = false;
        if (!bStop) {
            List<RiskGrade> lRiskGrades = (List<RiskGrade>) mapFilterInfo.get("风险等级");
            for (int i = 0; i < lRiskGrades.size(); i++) {
                if (value.equals(lRiskGrades.get(i).getRiskGradeName())) {
                    filter_value_RiskGrade.remove(lRiskGrades.get(i).getRiskGradeId());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get("发行机构");
            for (int i = 0; i < lInstitution.size(); i++) {
                if (value.equals(lInstitution.get(i).getInstitutionName())) {
                    filter_value_Institution.remove(lInstitution.get(i).getInstitutionId());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<ProductType> lProductType = (List<ProductType>) mapFilterInfo.get("产品类型");
            for (int i = 0; i < lProductType.size(); i++) {
                if (value.equals(lProductType.get(i).getProductTypeName())) {
                    filter_value_ProductType.remove(lProductType.get(i).getProductTypeId());
                    return bStop = true;
                }
            }
        }
        return bStop = true;
    }


    Observer observer_queryConditions = new Observer<List<QueryCondition>>() {
        @Override
        public void onCompleted() {

        }

        @DebugLog
        @Override
        public void onError(Throwable e) {
        }

        @DebugLog
        @Override
        public void onNext(List<QueryCondition> queryConditions) {
            mQueryConditions = queryConditions;
            setAdapter(queryConditions);


        }
    };

    Observer observer_ProductType = new Observer<List<ProductType>>() {
        @Override
        public void onCompleted() {

        }

        @DebugLog
        @Override
        public void onError(Throwable e) {

        }

        @DebugLog
        @Override
        public void onNext(List<ProductType> productTypes) {
            List<Integer> list_int = new ArrayList<>();
            List<String> list_str = new ArrayList<>();
            for (ProductType productType : productTypes) {
                list_int.add(productType.getProductTypeId());
                list_str.add(productType.getProductTypeName());
            }
            mMapFilterId.put("产品类型", list_int);
            mMapFilterName.put("产品类型", list_str);

            mFilterInfo.setlProductType(productTypes);
            mapFilterInfo.put("产品类型", productTypes);

            getQueryConditions();
            //            setGridViewAdapter(list_str);

        }
    };

    @DebugLog
    private void setGridViewAdapter(List<String> lists) {
        Log.i("Daniel", "---setGridViewAdapter---1");

    }

    Observer observer_Institution = new Observer<List<Institution>>() {
        @Override
        public void onCompleted() {

        }

        @DebugLog
        @Override
        public void onError(Throwable e) {

        }

        @DebugLog
        @Override
        public void onNext(List<Institution> institutions) {
            List<Integer> list_int = new ArrayList<>();
            List<String> list_str = new ArrayList<>();
            for (Institution institution : institutions) {
                list_int.add(institution.getInstitutionId());
                list_str.add(institution.getInstitutionName());
            }
            mMapFilterId.put("发行机构", list_int);
            mMapFilterName.put("发行机构", list_str);

            mFilterInfo.setlInstitution(institutions);
            mapFilterInfo.put("发行机构", institutions);

            getProductTypes();
            //            setGridViewAdapter(list_str);

        }
    };
    Observer observer_RiskGrade = new Observer<List<RiskGrade>>() {
        @Override
        public void onCompleted() {

        }

        @DebugLog
        @Override
        public void onError(Throwable e) {

        }

        @DebugLog
        @Override
        public void onNext(List<RiskGrade> RiskGrades) {
            List<Integer> list_int = new ArrayList<>();
            List<String> list_str = new ArrayList<>();
            for (RiskGrade riskGrade : RiskGrades) {
                list_int.add(riskGrade.getRiskGradeId());
                list_str.add(riskGrade.getRiskGradeName());
            }
            mMapFilterId.put("风险等级", list_int);
            mMapFilterName.put("风险等级", list_str);

            mFilterInfo.setlRiskGrade(RiskGrades);
            mapFilterInfo.put("风险等级", RiskGrades);
            getInstitutions();
            //            setGridViewAdapter(list_str);

        }
    };

    @DebugLog
    public boolean getType(String value) {
        boolean bStop = false;
        if (!bStop) {
            List<RiskGrade> lRiskGrades = (List<RiskGrade>) mapFilterInfo.get("风险等级");
            for (int i = 0; i < lRiskGrades.size(); i++) {
                if (value.equals(lRiskGrades.get(i).getRiskGradeName())) {
                    filter_value_RiskGrade.add(lRiskGrades.get(i).getRiskGradeId());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get("发行机构");
            for (int i = 0; i < lInstitution.size(); i++) {
                if (value.equals(lInstitution.get(i).getInstitutionName())) {
                    filter_value_Institution.add(lInstitution.get(i).getInstitutionId());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<ProductType> lProductType = (List<ProductType>) mapFilterInfo.get("产品类型");
            for (int i = 0; i < lProductType.size(); i++) {
                if (value.equals(lProductType.get(i).getProductTypeName())) {
                    filter_value_ProductType.add(lProductType.get(i).getProductTypeId());
                    return bStop = true;
                }
            }
        }
        return bStop = true;

    }

    private void setAdapter(List<QueryCondition> queryConditions) {
        filterAdapter = new FilterAdapter(queryConditions, mMapFilterName, getActivity());
//        recyclerView.setLayoutManager(new AutoHeightLayoutManager(getActivity()));
//        recyclerView.setAdapter(filterAdapter);
        filterAdapter.setOnItemClickListener(this);
    }


    private void initEvent() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mDrawerContent);
            }
        });
    }

    private void initView(View view) {
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mDrawerContent = (FrameLayout) getActivity().findViewById(R.id.drawer_content);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);

    }

    @DebugLog
    @Override
    public void onItemClick(View view, int postion) {
        //        setGridViewAdapter(mQueryConditions.get(postion));
    }

    @DebugLog
    @OnClick({R.id.btn_reset, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                filter_item = true;
                filterAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_confirm:
                Gson gson = new Gson();
                FilterValues filterValues = new FilterValues();

                if (filter_value_RiskGrade.size() != 0) {
                    StringBuffer strRiskGrade = new StringBuffer();
                    for (int i = 0; i < filter_value_RiskGrade.size(); i++) {
                        if (i == 0) {
                            strRiskGrade.append("" + filter_value_RiskGrade.get(i));
                        } else {
                            strRiskGrade.append("," + filter_value_RiskGrade.get(i));
                        }
                    }
                    Log.i("Daniel", "---strRiskGrade.toString()---" + strRiskGrade.toString());
                    filterValues.setRiskGradeId(strRiskGrade.toString());
                }
                if (filter_value_Institution.size() != 0) {
                    StringBuffer strInstitution = new StringBuffer();
                    for (int i = 0; i < filter_value_Institution.size(); i++) {
                        if (i == 0) {
                            strInstitution.append("" + filter_value_Institution.get(i));
                        } else {
                            strInstitution.append("," + filter_value_Institution.get(i));
                        }
                    }
                    Log.i("Daniel", "---strProductType.toString()---" + strInstitution.toString());
                    filterValues.setInstitutionId(strInstitution.toString());
                }
                if (filter_value_ProductType.size() != 0) {
                    StringBuffer strProductType = new StringBuffer();
                    for (int i = 0; i < filter_value_ProductType.size(); i++) {
                        if (i == 0) {
                            strProductType.append("" + filter_value_ProductType.get(i));
                        } else {
                            strProductType.append("," + filter_value_ProductType.get(i));
                        }
                    }
                    Log.i("Daniel", "---strProductType.toString()---" + strProductType.toString());
                    filterValues.setProductTypeId(strProductType.toString());

                }
                String json = gson.toJson(filterValues);
                Log.i("Daniel", "---json---" + json);
                EventBus.getDefault().post(new FilterEvens(json));
                mDrawerLayout.closeDrawer(mDrawerContent);

                break;
        }
    }


    //    private void showNext() {
    //        Fragment fragment = new FilterFragmentTwo();
    //        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    //        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    //        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
    //        fragmentTransaction.replace(R.id.drawer_content, fragment);
    //        fragmentTransaction.addToBackStack(null);
    //        fragmentTransaction.commitAllowingStateLoss();
    //    }

}
