package tqm.bianfeng.com.tqm.bank.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tqm.bianfeng.com.tqm.R;
import tqm.bianfeng.com.tqm.network.NetWork;
import tqm.bianfeng.com.tqm.pojo.bank.Constan;
import tqm.bianfeng.com.tqm.pojo.bank.FilterEvens;
import tqm.bianfeng.com.tqm.pojo.bank.FilterLoanVaule;
import tqm.bianfeng.com.tqm.pojo.bank.Institution;
import tqm.bianfeng.com.tqm.pojo.bank.LoanType;
import tqm.bianfeng.com.tqm.pojo.bank.ProductType;
import tqm.bianfeng.com.tqm.pojo.bank.RiskGrade;
import tqm.bianfeng.com.tqm.pojo.bank.buttonViewEven;

import static tqm.bianfeng.com.tqm.R.id.iv_back;

public class TestFilterFragment extends Fragment {

    private static final String SARG_PARAM1 = "param1";
    private static final String SINSTITUTIONQUERY = "机构查询";
    private static final String SLOANTYPE = "贷款类型";
    private static final String SPRODUCTTYPE = "贷款类型";
    private static final String SRISKGRADE = "风险等级";
    private static final int IINSTITUTIONQUERY = 10; //贷款机构adapter标识
    private static final int IFINSTITUTIONQUERY = 11; //理财机构adapter标识
    private static final int ILOANTYPE = 12;//贷款类型adapter标识
    private static final int IPRODUCTTYPE = 13;//产品类型adapter标识
    private static final int IRISKGRADE = 14;//风险等级adapter标识
    @BindView(iv_back)
    ImageView ivBack;
    @BindView(R.id.titleBar_relative)
    RelativeLayout titleBarRelative;
    @BindView(R.id.filter_institution_recyclerView)
    RecyclerView filterInstitutionRecyclerView;
    @BindView(R.id.filter_loanType_recyclerView)
    RecyclerView filterLoanTypeRecyclerView;
    @BindView(R.id.filter_crowd_et)
    EditText filterCrowdEt;
    @BindView(R.id.filter_city_et)
    EditText filterCityEt;
    @BindView(R.id.filter_rateMin_et)
    EditText filterRateMinEt;
    @BindView(R.id.filter_rateMax_et)
    EditText filterRateMaxEt;
    @BindView(R.id.filter_loanMoneyMin_et)
    EditText filterLoanMoneyMinEt;
    @BindView(R.id.filter_loanMoneyMax_et)
    EditText filterLoanMoneyMaxEt;
    @BindView(R.id.filter_loanPeriodMin_et)
    EditText filterLoanPeriodMinEt;
    @BindView(R.id.filter_loanPeriodMax_et)
    EditText filterLoanPeriodMaxEt;
    @BindView(R.id.filter_loan_linear)
    LinearLayout filterLoanLinear;
    @BindView(R.id.filter_fInstitution_recyclerView)
    RecyclerView filterFInstitutionRecyclerView;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.filter_productType_recyclerView)
    RecyclerView filterProductTypeRecyclerView;
    @BindView(R.id.filter_riskGrade_recyclerView)
    RecyclerView filterRiskGradeRecyclerView;
    @BindView(R.id.filter_redeemable_cBox)
    CheckBox filterRedeemableCBox;
    @BindView(R.id.filter_notRedeemable_cBox)
    CheckBox filterNotRedeemableCBox;
    @BindView(R.id.filter_issuingCity_et)
    EditText filterIssuingCityEt;
    @BindView(R.id.filter_investmentTermIntervalMin_et)
    EditText filterInvestmentTermIntervalMinEt;
    @BindView(R.id.filter_investmentTermIntervalMax_et)
    EditText filterInvestmentTermIntervalMaxEt;
    @BindView(R.id.filter_financing_linear)
    LinearLayout filterFinancingLinear;
    @BindView(R.id.btn_reset)
    Button btnReset;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mDrawerContent;
    private int mFilterType;//1:贷款 2 理财
    private Map<String, Object> mapFilterInfo;
    public List<String> filter_value_RiskGrade;//风险等级集合
    public List<String> filter_value_Institution;//机构筛选集合
    public List<String> filter_value_loanType;//贷款类型集合
    public List<String> filter_value_ProductType;//产品类型集合

    public static boolean filter_item = false;
    FilterAdapter filterAdapter;

    public TestFilterFragment() {
        // Required empty public constructor
    }

    /**
     * 广播-添加或移除筛选条件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    @DebugLog
    public void onEventMainThread(buttonViewEven event) {
        String value = event.getButtonView();
        if (event.isFlag()) {
            if (mFilterType == 1)//1 贷款
                // 接受贷款筛选条件点击处理
                getLoanType(value);
            else
                getFinancingType(value);

        } else {
            if (mFilterType == 1)//1 贷款
                removeLoanType(value);
            else
                removeFinancingType(value);
        }
    }

    /**
     * 清除筛选
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    @DebugLog
    public void onEventMainThread(ClearFilter event) {
        Log.i("Daniel", "---event.isClearFilter()---" + event.isClearFilter());
        if (event.isClearFilter()) {
            if (filter_value_RiskGrade!=null)
            filter_value_RiskGrade.clear();
            if (filter_value_Institution!=null)
            filter_value_Institution.clear();
            if (filter_value_loanType!=null)
            filter_value_loanType.clear();
            if (filter_value_ProductType!=null)
            filter_value_ProductType.clear();

            //适合人群
            setTextEmpty(filterCrowdEt);
            //发行城市
            setTextEmpty(filterCityEt);
            //利率区间
            setTextEmpty(filterRateMinEt);
            setTextEmpty(filterRateMaxEt);
            //贷款金额
            setTextEmpty(filterLoanMoneyMinEt);
            setTextEmpty(filterLoanMoneyMaxEt);
            //贷款期限区间
            setTextEmpty(filterLoanPeriodMinEt);
            setTextEmpty(filterLoanPeriodMaxEt);
            //投资期限区间
            setTextEmpty(filterInvestmentTermIntervalMinEt);
            setTextEmpty(filterInvestmentTermIntervalMaxEt);
            //投资模式
            filterRedeemableCBox.setChecked(false);
            filterNotRedeemableCBox.setChecked(false);
            filterRedeemableCBox.setTextColor(getActivity().getResources().getColor(R.color.black));
            filterNotRedeemableCBox.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
    }

    /**
     * 清空筛选文本框内容
     * @param et
     */
    private void setTextEmpty(EditText et) {
        et.setText("");
    }

    /**
     * 移除贷款筛选条件
     *
     * @param value
     * @return
     */
    private boolean removeLoanType(String value) {
        boolean bStop = false;
        if (!bStop) {
            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get(SINSTITUTIONQUERY);
            if (filter_value_Institution == null) {
                filter_value_Institution = new ArrayList<>();
            }
            for (int i = 0; i < lInstitution.size(); i++) {
                if (value.equals(lInstitution.get(i).getInstitutionName())) {
                    filter_value_Institution.remove(lInstitution.get(i).getInstitutionName());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<LoanType> loanType = (List<LoanType>) mapFilterInfo.get(SLOANTYPE);
            if (filter_value_loanType == null) {
                filter_value_loanType = new ArrayList<>();
            }
            for (int i = 0; i < loanType.size(); i++) {
                if (value.equals(loanType.get(i).getLoanTypeName())) {
                    filter_value_loanType.remove(loanType.get(i).getLoanTypeName());
                    return bStop = true;
                }
            }
        }
        return bStop = true;

    }

    /**
     * 添加贷款筛选条件
     *
     * @param value
     * @return
     */
    private boolean getLoanType(String value) {
        boolean bStop = false;
        if (!bStop) {
            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get(SINSTITUTIONQUERY);
            if (filter_value_Institution == null) {
                filter_value_Institution = new ArrayList<>();
            }
            for (int i = 0; i < lInstitution.size(); i++) {
                if (value.equals(lInstitution.get(i).getInstitutionName())) {
                    filter_value_Institution.add(lInstitution.get(i).getInstitutionName());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<LoanType> loanType = (List<LoanType>) mapFilterInfo.get(SLOANTYPE);
            if (filter_value_loanType == null) {
                filter_value_loanType = new ArrayList<>();
            }
            for (int i = 0; i < loanType.size(); i++) {
                if (value.equals(loanType.get(i).getLoanTypeName())) {
                    filter_value_loanType.add(loanType.get(i).getLoanTypeName());
                    return bStop = true;
                }
            }
        }
        return bStop = true;


    }

    public static TestFilterFragment newInstance(int param1) {
        TestFilterFragment fragment = new TestFilterFragment();
        Bundle args = new Bundle();
        args.putInt(SARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFilterType = getArguments().getInt(SARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //        View view = inflater.inflate(R.layout.fragment_patrol_filter, null);
        View view = inflater.inflate(R.layout.fragment_loan_filter, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);//订阅
        initView();
        return view;
    }

    /**
     * 添加理财筛选条件
     *
     * @param value
     * @return
     */
    public boolean getFinancingType(String value) {
        boolean bStop = false;
        if (!bStop) {
            List<RiskGrade> lRiskGrades = (List<RiskGrade>) mapFilterInfo.get(SRISKGRADE);
            if (filter_value_RiskGrade == null) {
                filter_value_RiskGrade = new ArrayList<>();
            }
            for (int i = 0; i < lRiskGrades.size(); i++) {
                if (value.equals(lRiskGrades.get(i).getRiskGradeName())) {
                    filter_value_RiskGrade.add(lRiskGrades.get(i).getRiskGradeName());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get(SINSTITUTIONQUERY);
            if (filter_value_Institution == null) {
                filter_value_Institution = new ArrayList<>();
            }
            for (int i = 0; i < lInstitution.size(); i++) {
                if (value.equals(lInstitution.get(i).getInstitutionName())) {
                    filter_value_Institution.add(lInstitution.get(i).getInstitutionName());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<ProductType> lProductType = (List<ProductType>) mapFilterInfo.get(SPRODUCTTYPE);
            if (filter_value_ProductType == null) {
                filter_value_ProductType = new ArrayList<>();
            }
            for (int i = 0; i < lProductType.size(); i++) {
                if (value.equals(lProductType.get(i).getProductTypeName())) {
                    filter_value_ProductType.add(lProductType.get(i).getProductTypeName());
                    return bStop = true;
                }
            }
        }
        return bStop = true;

    }

    /**
     * 移除筛选条件
     *
     * @param value
     * @return
     */
    public boolean removeFinancingType(String value) {
        boolean bStop = false;
        if (!bStop) {
            List<RiskGrade> lRiskGrades = (List<RiskGrade>) mapFilterInfo.get(SRISKGRADE);
            if (filter_value_RiskGrade == null) {
                filter_value_RiskGrade = new ArrayList<>();
            }
            for (int i = 0; i < lRiskGrades.size(); i++) {
                if (value.equals(lRiskGrades.get(i).getRiskGradeName())) {
                    filter_value_RiskGrade.remove(lRiskGrades.get(i).getRiskGradeName());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get(SINSTITUTIONQUERY);
            if (filter_value_Institution == null) {
                filter_value_Institution = new ArrayList<>();
            }
            for (int i = 0; i < lInstitution.size(); i++) {
                if (value.equals(lInstitution.get(i).getInstitutionName())) {
                    filter_value_Institution.remove(lInstitution.get(i).getInstitutionName());
                    return bStop = true;
                }
            }
        }
        if (!bStop) {
            List<ProductType> lProductType = (List<ProductType>) mapFilterInfo.get(SPRODUCTTYPE);
            if (filter_value_ProductType == null) {
                filter_value_ProductType = new ArrayList<>();
            }
            for (int i = 0; i < lProductType.size(); i++) {
                if (value.equals(lProductType.get(i).getProductTypeName())) {
                    filter_value_ProductType.remove(lProductType.get(i).getProductTypeName());
                    return bStop = true;
                }
            }
        }
        return bStop = true;

    }

    /**
     * 获取贷款类型
     */
    private void initLoanData() {
        //获取贷款类型
        NetWork.getBankService().getLoanTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LoanType>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @DebugLog
                    @Override
                    public void onNext(List<LoanType> loanTypes) {
                        List<String> datas = new ArrayList<>();
                        for (LoanType productType : loanTypes) {
                            datas.add(productType.getLoanTypeName());
                        }
                        mapFilterInfo.put(SLOANTYPE, loanTypes);
                        setRecyclerViewAdapter(datas, filterLoanTypeRecyclerView, ILOANTYPE);

                    }
                });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mDrawerContent = (FrameLayout) getActivity().findViewById(R.id.drawer_content);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mDrawerContent);
            }
        });
        Log.e("Daniel", "---mFilterType---" + mFilterType);
        if (mapFilterInfo == null) {
            mapFilterInfo = new HashMap<>();
        }
        //获取机构
        getInstitutions();

        switch (mFilterType) {
            case 1:
                filterLoanLinear.setVisibility(View.VISIBLE);
                filterFinancingLinear.setVisibility(View.GONE);
                initLoanData();
                break;
            case 2:
                filterLoanLinear.setVisibility(View.GONE);
                filterFinancingLinear.setVisibility(View.VISIBLE);
                initFinancingData();
                break;
        }
    }

    /**
     * 获取机构
     */
    private void getInstitutions() {
        //获取机构
        NetWork.getBankService().getInstitutions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Institution>>() {
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
                        List<String> datas = new ArrayList<>();
                        for (Institution institution : institutions) {
                            datas.add(institution.getInstitutionName());
                        }
                        mapFilterInfo.put(SINSTITUTIONQUERY, institutions);
                        if (mFilterType == 1) {
                            setRecyclerViewAdapter(datas, filterInstitutionRecyclerView, IINSTITUTIONQUERY);
                        } else {
                            setRecyclerViewAdapter(datas, filterFInstitutionRecyclerView, IFINSTITUTIONQUERY);
                        }
                    }
                });
    }

    /**
     * 获取产品类型
     */
    private void initFinancingData() {
        //获取产品类型
        NetWork.getBankService().getProductTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ProductType>>() {
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
                        List<String> datas = new ArrayList<>();
                        for (ProductType productType : productTypes) {
                            datas.add(productType.getProductTypeName());
                        }
                        mapFilterInfo.put(SPRODUCTTYPE, productTypes);
                        setRecyclerViewAdapter(datas, filterProductTypeRecyclerView, IPRODUCTTYPE);
                    }
                });
        //获取风险等级
        NetWork.getBankService().getRiskGrades()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RiskGrade>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                    }

                    @DebugLog
                    @Override
                    public void onNext(List<RiskGrade> riskGrades) {
                        List<String> datas = new ArrayList<>();
                        for (RiskGrade riskGrade : riskGrades) {
                            datas.add(riskGrade.getRiskGradeName());
                        }
                        mapFilterInfo.put(SRISKGRADE, riskGrades);
                        setRecyclerViewAdapter(datas, filterRiskGradeRecyclerView, IRISKGRADE);
                    }
                });
    }

    GridViewAdapter gridViewAdapterINSTITUTIONQUERY;//贷款机构adapter
    GridViewAdapter gridViewAdapterFINSTITUTIONQUERY;//理财机构adapter
    GridViewAdapter gridViewAdapterIRISKGRADE;//风险等级adapter
    GridViewAdapter gridViewAdapterIPRODUCTTYPE;//产品类型adapter
    GridViewAdapter gridViewAdapterILOANTYPE;//贷款类型adapter

    /**
     * 适配器
     *
     * @param datas
     * @param recyclerView
     * @param i
     */
    private void setRecyclerViewAdapter(List<String> datas, RecyclerView recyclerView, int i) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Constan.GRIDLAYOUTSIZE));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(), datas);
        recyclerView.setAdapter(gridViewAdapter);
        switch (i) {
            case IINSTITUTIONQUERY:
                gridViewAdapterINSTITUTIONQUERY = (GridViewAdapter) recyclerView.getAdapter();
                break;
            case IFINSTITUTIONQUERY:
                gridViewAdapterFINSTITUTIONQUERY = (GridViewAdapter) recyclerView.getAdapter();
                break;
            case ILOANTYPE:
                gridViewAdapterILOANTYPE = (GridViewAdapter) recyclerView.getAdapter();
                break;
            case IPRODUCTTYPE:
                gridViewAdapterIPRODUCTTYPE = (GridViewAdapter) recyclerView.getAdapter();
                break;
            case IRISKGRADE:
                gridViewAdapterIRISKGRADE = (GridViewAdapter) recyclerView.getAdapter();
                break;
        }


    }

    @DebugLog
    @OnClick({R.id.btn_reset, R.id.btn_confirm,R.id.filter_redeemable_cBox,R.id.filter_notRedeemable_cBox})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                filter_item = true; // true:点击了重置按钮
                if (mFilterType == 1)
                    notifyLoanAdapter();
                else
                    notifyFinancingAdapter();
                break;
            case R.id.btn_confirm:
                sendFilter();
                break;
            case R.id.filter_redeemable_cBox:
                filterRedeemableCBox.setTextColor(getActivity().getResources().getColor(R.color.white));
                filterNotRedeemableCBox.setTextColor(getActivity().getResources().getColor(R.color.black));
                filterNotRedeemableCBox.setChecked(false);
                break;
            case R.id.filter_notRedeemable_cBox:
                filterRedeemableCBox.setTextColor(getActivity().getResources().getColor(R.color.black));
                filterNotRedeemableCBox.setTextColor(getActivity().getResources().getColor(R.color.white));
                filterRedeemableCBox.setChecked(false);

                break;
        }
    }

    private void notifyLoanAdapter() {
        if (gridViewAdapterINSTITUTIONQUERY == null) {
            gridViewAdapterINSTITUTIONQUERY = new GridViewAdapter();
        }
        gridViewAdapterINSTITUTIONQUERY.notifyDataSetChanged();
        if (gridViewAdapterILOANTYPE == null) {
            gridViewAdapterILOANTYPE = new GridViewAdapter();
        }
        gridViewAdapterILOANTYPE.notifyDataSetChanged();
    }

    /**
     * 重置筛选
     */
    private void notifyFinancingAdapter() {
        if (gridViewAdapterFINSTITUTIONQUERY == null) {
            gridViewAdapterFINSTITUTIONQUERY = new GridViewAdapter();
        }
        gridViewAdapterFINSTITUTIONQUERY.notifyDataSetChanged();
        if (gridViewAdapterIRISKGRADE == null) {
            gridViewAdapterIRISKGRADE = new GridViewAdapter();
        }
        gridViewAdapterIRISKGRADE.notifyDataSetChanged();
        if (gridViewAdapterIPRODUCTTYPE == null) {
            gridViewAdapterIPRODUCTTYPE = new GridViewAdapter();
        }
        gridViewAdapterIPRODUCTTYPE.notifyDataSetChanged();

    }

    /**
     * 提交筛选
     */
    private void sendFilter() {
        Gson gson = new Gson();
        FilterLoanVaule filterValues = new FilterLoanVaule();
        //机构查询
        if (filter_value_Institution == null) {
            filter_value_Institution = new ArrayList<>();
        }
        if (filter_value_Institution.size() != 0) {
            filterValues.setInstitution(getFilterStr(filter_value_Institution));
        }
        //贷款类型
        if (filter_value_loanType == null) {
            filter_value_loanType = new ArrayList<>();
        }
        if (filter_value_loanType.size() != 0) {
            filterValues.setLoanType(getFilterStr(filter_value_loanType));
        }
        //产品类型
        if (filter_value_ProductType == null) {
            filter_value_ProductType = new ArrayList<>();
        }
        if (filter_value_ProductType.size() != 0) {
            filterValues.setLoanType(getFilterStr(filter_value_ProductType));
        }
        //风险等级
        if (filter_value_RiskGrade == null) {
            filter_value_RiskGrade = new ArrayList<>();
        }
        if (filter_value_RiskGrade.size() != 0) {
            filterValues.setLoanType(getFilterStr(filter_value_RiskGrade));
        }
        //投资模式
        if (filterRedeemableCBox.isChecked()){
            filterValues.setInvestmentModel("02");//02可赎回
        }
        if (filterNotRedeemableCBox.isChecked()){
            filterValues.setInvestmentModel("01");//01不可赎回
        }
        //适合人群
        Log.i("Daniel", "---filterCrowdEt---" + filterCrowdEt.getText().toString());
        filterValues.setCrowd(filterCrowdEt.getText().toString());
        //发行城市
        Log.i("Daniel", "---filterCityEt---" + filterCityEt.getText().toString());
        filterValues.setDistriArea(filterCityEt.getText().toString());
        //利率区间
        filterValues.setRateMin(filterRateMinEt.getText().toString());
        filterValues.setRateMax(filterRateMaxEt.getText().toString());
        //贷款金额
        filterValues.setLoanMoneyMin(filterLoanMoneyMinEt.getText().toString());
        filterValues.setLoanMoneyMax(filterLoanMoneyMaxEt.getText().toString());
        //贷款期限区间
        filterValues.setLoanPeriodMin(filterLoanPeriodMinEt.getText().toString());
        filterValues.setLoanPerioMax(filterLoanPeriodMaxEt.getText().toString());
        String json = gson.toJson(filterValues);
        Log.i("Daniel", "---json---" + json);
        EventBus.getDefault().post(new FilterEvens(json));
        mDrawerLayout.closeDrawer(mDrawerContent);
    }

    /**
     * 拼接筛选条件
     *
     * @param fList
     * @return
     */
    private String getFilterStr(List<String> fList) {
        StringBuffer strInstitution = new StringBuffer();
        for (int i = 0; i < fList.size(); i++) {
            if (i == 0) {
                strInstitution.append("" + fList.get(i));
            } else {
                strInstitution.append("," + fList.get(i));
            }
        }
        Log.i("Daniel", "---strProductType.toString()---" + strInstitution.toString());
        return strInstitution.toString();
    }

}
