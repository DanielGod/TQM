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

public class TestFilterFragment extends Fragment {

    private static final String SARG_PARAM1 = "param1";
    private static final String SINSTITUTIONQUERY = "机构查询";
    private static final String SLOANTYPE = "贷款类型";
    private static final String SPRODUCTTYPE = "产品类型";
    private static final String SRISKGRADE = "风险等级";
    private static final int FINSTITUIONBANKINSTITUTIONQUERY = 6; //理财-银行adapter标识
    private static final int LOANBANKINSTITUTIONQUERY = 7; //贷款-银行adapter标识
    private static final int ACTIVITYINSTITUTIONQUERY = 8; //活动-机构adapter标识
    private static final int ACTIVITYBANKINSTITUTIONQUERY = 9; //活动-银行adapter标识
    private static final int IINSTITUTIONQUERY = 10; //贷款机构adapter标识
    private static final int IFINSTITUTIONQUERY = 11; //理财机构adapter标识
    private static final int ILOANTYPE = 12;//贷款类型adapter标识
    private static final int IPRODUCTTYPE = 13;//产品类型adapter标识
    private static final int IRISKGRADE = 14;//风险等级adapter标识

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.titleBar_relative)
    RelativeLayout titleBarRelative;
    @BindView(R.id.filterTitle_activityBankInstitution_cb)
    CheckBox filterTitleActivityBankInstitutionCb;
    @BindView(R.id.filter_activityBankInstitution_recyclerView)
    RecyclerView filterActivityBankInstitutionRecyclerView;
    @BindView(R.id.filterTitle_activityInstitution_cb)
    CheckBox filterTitleActivityInstitutionCb;
    @BindView(R.id.filter_activityInstitution_recyclerView)
    RecyclerView filterActivityInstitutionRecyclerView;
    @BindView(R.id.filter_activity_linear)
    LinearLayout filterActivityLinear;
    @BindView(R.id.filterTitle_loanBankInstitution_cb)
    CheckBox filterTitleLoanBankInstitutionCb;
    @BindView(R.id.filter_loanBankInstitution_recyclerView)
    RecyclerView filterLoanBankInstitutionRecyclerView;
    @BindView(R.id.filterTitle_institution_cb)
    CheckBox filterTitleInstitutionCb;
    @BindView(R.id.filter_institution_recyclerView)
    RecyclerView filterInstitutionRecyclerView;
    @BindView(R.id.filterTitle_loanType_cb)
    CheckBox filterTitleLoanTypeCb;
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
    @BindView(R.id.filterTitle_financingBankInstitution_cb)
    CheckBox filterTitleFinancingBankInstitutionCb;
    @BindView(R.id.filter_financingBankInstitution_recyclerView)
    RecyclerView filterFinancingBankInstitutionRecyclerView;
    @BindView(R.id.filterTitle_fInstitution_cb)
    CheckBox filterTitleFInstitutionCb;
    @BindView(R.id.filter_fInstitution_recyclerView)
    RecyclerView filterFInstitutionRecyclerView;
    @BindView(R.id.filterTitle_productType_cb)
    CheckBox filterTitleProductTypeCb;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.filter_productType_recyclerView)
    RecyclerView filterProductTypeRecyclerView;
    @BindView(R.id.filterTitle_riskGrade_cb)
    CheckBox filterTitleRiskGradeCb;
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
    private int mFilterType;//1:贷款 2 理财 3:活动
    private Map<String, Object> mapFilterInfo;
    public List<String> filter_value_RiskGrade;//风险等级集合
    public List<String> filter_value_Institution;//机构筛选集合
    public List<String> filter_value_loanType;//贷款类型集合
    public List<String> filter_value_ProductType;//产品类型集合

    public static boolean filter_item = false;
    public static boolean filter_all = false;
    FilterAdapter filterAdapter;
    List<String> mClearFilter;

    public TestFilterFragment() {
        // Required empty public constructor
    }

    /**
     * 广播-添加或移除筛选条件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    @DebugLog
    public void onEventMainThread(buttonViewEven event) {
        String value = event.getButtonView();
        Log.i("Daniel", "---value---" + value);
        Log.i("Daniel", "---mFilterType---" + mFilterType);
        // 接受贷款筛选条件点击处理
        if (event.isFlag()) {
            //添加筛选条件
            switch (mFilterType) {
                case 1:

                    getLoanType(value, event.isFlag());
                    break;
                case 2:
                    getFinancingType(value, event.isFlag());
                    break;
                case 3:
                    setActivityType(value, event.isFlag());
                    break;
            }

        } else {
            //移除筛选条件
            switch (mFilterType) {
                case 1:
                    getLoanType(value, event.isFlag());
                    break;
                case 2:
                    getFinancingType(value, event.isFlag());
                    break;
                case 3:
                    setActivityType(value, event.isFlag());
                    break;
            }

        }
    }

    /**
     * 添加活动筛选条件
     *
     * @param value
     * @param flag
     * @return
     */
    private boolean setActivityType(String value, boolean flag) {
        Log.i("Daniel", "---getActivityType---");
        boolean bStop = false;
        if (!bStop) {
            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get(SINSTITUTIONQUERY);
            if (filter_value_Institution == null) {
                filter_value_Institution = new ArrayList<>();
            }
            for (int i = 0; i < lInstitution.size(); i++) {
                if (value.equals(lInstitution.get(i).getInstitutionName())) {
                    if (flag) {
                        filter_value_Institution.add(lInstitution.get(i).getInstitutionName());
                    } else {
                        filter_value_Institution.remove(lInstitution.get(i).getInstitutionName());
                    }
                    return bStop = true;
                }
            }
        }
        return bStop = true;
    }

    /**
     * 清除筛选
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    @DebugLog
    public void onEventMainThread(ClearFilter event) {
        Log.i("Daniel", "---event.isClearFilter()---" + event.isClearFilter());
        if (event.isClearFilter()) {
            if (filter_value_RiskGrade != null)
                filter_value_RiskGrade.clear();
            if (filter_value_Institution != null)
                filter_value_Institution.clear();
            if (filter_value_loanType != null)
                filter_value_loanType.clear();
            if (filter_value_ProductType != null)
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
     *
     * @param et
     */
    private void setTextEmpty(EditText et) {
        et.setText("");
    }

    //    /**
    //     * 移除贷款筛选条件
    //     *
    //     * @param value
    //     * @return
    //     */
    //    private boolean removeLoanType(String value) {
    //        boolean bStop = false;
    //        if (!bStop) {
    //            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get(SINSTITUTIONQUERY);
    //            if (filter_value_Institution == null) {
    //                filter_value_Institution = new ArrayList<>();
    //            }
    //            for (int i = 0; i < lInstitution.size(); i++) {
    //                if (value.equals(lInstitution.get(i).getInstitutionName())) {
    //                    filter_value_Institution.remove(lInstitution.get(i).getInstitutionName());
    //                    return bStop = true;
    //                }
    //            }
    //        }
    //        if (!bStop) {
    //            List<LoanType> loanType = (List<LoanType>) mapFilterInfo.get(SLOANTYPE);
    //            if (filter_value_loanType == null) {
    //                filter_value_loanType = new ArrayList<>();
    //            }
    //            for (int i = 0; i < loanType.size(); i++) {
    //                if (value.equals(loanType.get(i).getLoanTypeName())) {
    //                    filter_value_loanType.remove(loanType.get(i).getLoanTypeName());
    //                    return bStop = true;
    //                }
    //            }
    //        }
    //        return bStop = true;
    //
    //    }

    /**
     * 添加或移除贷款筛选条件
     *
     * @param value
     * @param flag
     * @return
     */
    private boolean getLoanType(String value, boolean flag) {
        Log.i("Daniel", "---getLoanType---");
        boolean bStop = false;
        if (!bStop) {
            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get(SINSTITUTIONQUERY);
            if (filter_value_Institution == null) {
                filter_value_Institution = new ArrayList<>();
            }
            for (int i = 0; i < lInstitution.size(); i++) {
                if (value.equals(lInstitution.get(i).getInstitutionName())) {
                    if (flag)
                        filter_value_Institution.add(lInstitution.get(i).getInstitutionName());
                    else
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
                    if (flag)
                        filter_value_loanType.add(loanType.get(i).getLoanTypeName());
                    else
                        filter_value_loanType.remove(loanType.get(i).getLoanTypeName());
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
     * 添加或移除理财筛选条件
     *
     * @param value
     * @param flag
     * @return
     */
    public boolean getFinancingType(String value, boolean flag) {
        Log.i("Daniel", "---getFinancingType---");
        boolean bStop = false;
        if (!bStop) {
            List<RiskGrade> lRiskGrades = (List<RiskGrade>) mapFilterInfo.get(SRISKGRADE);
            if (filter_value_RiskGrade == null) {
                filter_value_RiskGrade = new ArrayList<>();
            }
            for (int i = 0; i < lRiskGrades.size(); i++) {
                if (value.equals(lRiskGrades.get(i).getRiskGradeName())) {
                    if (flag)
                        filter_value_RiskGrade.add(lRiskGrades.get(i).getRiskGradeName());
                    else
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
                    if (flag)
                        filter_value_Institution.add(lInstitution.get(i).getInstitutionName());
                    else
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
                    if (flag)
                        filter_value_ProductType.add(lProductType.get(i).getProductTypeName());
                    else
                        filter_value_ProductType.remove(lProductType.get(i).getProductTypeName());
                    return bStop = true;
                }
            }
        }
        return bStop = true;

    }
    //
    //    /**
    //     * 移除筛选条件
    //     *
    //     * @param value
    //     * @return
    //     */
    //    public boolean removeFinancingType(String value) {
    //        boolean bStop = false;
    //        if (!bStop) {
    //            List<RiskGrade> lRiskGrades = (List<RiskGrade>) mapFilterInfo.get(SRISKGRADE);
    //            if (filter_value_RiskGrade == null) {
    //                filter_value_RiskGrade = new ArrayList<>();
    //            }
    //            for (int i = 0; i < lRiskGrades.size(); i++) {
    //                if (value.equals(lRiskGrades.get(i).getRiskGradeName())) {
    //                    filter_value_RiskGrade.remove(lRiskGrades.get(i).getRiskGradeName());
    //                    return bStop = true;
    //                }
    //            }
    //        }
    //        if (!bStop) {
    //            List<Institution> lInstitution = (List<Institution>) mapFilterInfo.get(SINSTITUTIONQUERY);
    //            if (filter_value_Institution == null) {
    //                filter_value_Institution = new ArrayList<>();
    //            }
    //            for (int i = 0; i < lInstitution.size(); i++) {
    //                if (value.equals(lInstitution.get(i).getInstitutionName())) {
    //                    filter_value_Institution.remove(lInstitution.get(i).getInstitutionName());
    //                    return bStop = true;
    //                }
    //            }
    //        }
    //        if (!bStop) {
    //            List<ProductType> lProductType = (List<ProductType>) mapFilterInfo.get(SPRODUCTTYPE);
    //            if (filter_value_ProductType == null) {
    //                filter_value_ProductType = new ArrayList<>();
    //            }
    //            for (int i = 0; i < lProductType.size(); i++) {
    //                if (value.equals(lProductType.get(i).getProductTypeName())) {
    //                    filter_value_ProductType.remove(lProductType.get(i).getProductTypeName());
    //                    return bStop = true;
    //                }
    //            }
    //        }
    //        return bStop = true;
    //
    //    }

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
                filterActivityLinear.setVisibility(View.GONE);
                initLoanData();
                break;
            case 2:
                filterLoanLinear.setVisibility(View.GONE);
                filterFinancingLinear.setVisibility(View.VISIBLE);
                filterActivityLinear.setVisibility(View.GONE);
                initFinancingData();
                break;
            case 3:
                filterLoanLinear.setVisibility(View.GONE);
                filterFinancingLinear.setVisibility(View.GONE);
                filterActivityLinear.setVisibility(View.VISIBLE);
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
                        List<String> datas_bankInstitution = new ArrayList<>();
                        if (mClearFilter == null) {
                            mClearFilter = new ArrayList<String>();
                        }
                        for (Institution institution : institutions) {
                            mClearFilter.add(institution.getInstitutionName());
                            if (!"02".equals(institution.getInstitutionType())) //02:银行，03：机构
                                datas.add(institution.getInstitutionName());
                            else
                                datas_bankInstitution.add(institution.getInstitutionName());
                        }
                        mapFilterInfo.put(SINSTITUTIONQUERY, institutions);
                        switch (mFilterType) {
                            case 1:
                                //银行查询列表
                                setRecyclerViewAdapter(datas_bankInstitution, filterLoanBankInstitutionRecyclerView, LOANBANKINSTITUTIONQUERY);
                                //机构查询列表
                                setRecyclerViewAdapter(datas, filterInstitutionRecyclerView, IINSTITUTIONQUERY);
                                break;
                            case 2:
                                setRecyclerViewAdapter(datas, filterFInstitutionRecyclerView, IFINSTITUTIONQUERY);
                                setRecyclerViewAdapter(datas_bankInstitution, filterFinancingBankInstitutionRecyclerView, FINSTITUIONBANKINSTITUTIONQUERY);
                                break;
                            case 3:
                                setRecyclerViewAdapter(datas, filterActivityInstitutionRecyclerView, ACTIVITYINSTITUTIONQUERY);
                                setRecyclerViewAdapter(datas_bankInstitution, filterActivityBankInstitutionRecyclerView, ACTIVITYBANKINSTITUTIONQUERY);
                                break;
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
    GridViewAdapter gridViewAdapterACTIVITYINSTITUTIONQUERY;//活动-机构adapter
    GridViewAdapter gridViewAdapterACTIVITYBANKINSTITUTIONQUERY;//活动-银行机构adapter
    GridViewAdapter gridViewAdapterFINSTITUIONBANKINSTITUTIONQUERY;//理财-银行机构adapter
    GridViewAdapter gridViewAdapterLOANBANKINSTITUTIONQUERY;//贷款-银行机构adapter

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
            case IINSTITUTIONQUERY://贷款机构adapter标识
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
            case ACTIVITYBANKINSTITUTIONQUERY:
                gridViewAdapterACTIVITYBANKINSTITUTIONQUERY = (GridViewAdapter) recyclerView.getAdapter();
                break;
            case ACTIVITYINSTITUTIONQUERY:
                gridViewAdapterACTIVITYINSTITUTIONQUERY = (GridViewAdapter) recyclerView.getAdapter();
                break;
            case LOANBANKINSTITUTIONQUERY:

                gridViewAdapterLOANBANKINSTITUTIONQUERY = (GridViewAdapter) recyclerView.getAdapter();
                break;
            case FINSTITUIONBANKINSTITUTIONQUERY:
                gridViewAdapterFINSTITUIONBANKINSTITUTIONQUERY = (GridViewAdapter) recyclerView.getAdapter();
                break;
        }


    }

    @DebugLog
    @OnClick({R.id.btn_reset, R.id.btn_confirm, R.id.filter_redeemable_cBox, R.id.filter_notRedeemable_cBox,
            R.id.filterTitle_institution_cb, R.id.filterTitle_loanType_cb, R.id.filterTitle_fInstitution_cb, R.id.filterTitle_loanBankInstitution_cb,
            R.id.filterTitle_activityBankInstitution_cb, R.id.filterTitle_activityInstitution_cb, R.id.filterTitle_financingBankInstitution_cb,
            R.id.filterTitle_productType_cb, R.id.filterTitle_riskGrade_cb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                filter_item = true; // true:点击了重置按钮
                Log.e("Daniel", "---btn_reset---");
                switch (mFilterType) {
                    case 1:
                        notifyAdapter(gridViewAdapterINSTITUTIONQUERY);
                        notifyAdapter(gridViewAdapterILOANTYPE);
                        notifyAdapter(gridViewAdapterLOANBANKINSTITUTIONQUERY);
                        break;
                    case 2:
                        notifyAdapter(gridViewAdapterFINSTITUTIONQUERY);
                        notifyAdapter(gridViewAdapterIRISKGRADE);
                        notifyAdapter(gridViewAdapterIPRODUCTTYPE);
                        notifyAdapter(gridViewAdapterFINSTITUIONBANKINSTITUTIONQUERY);
                        break;
                    case 3:
                        notifyAdapter(gridViewAdapterACTIVITYBANKINSTITUTIONQUERY);
                        notifyAdapter(gridViewAdapterACTIVITYINSTITUTIONQUERY);
                        break;
                }
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
            case R.id.filterTitle_institution_cb:
                setFilterAll(filterTitleInstitutionCb, gridViewAdapterINSTITUTIONQUERY);
                //                setFilterTitle(filterTitleInstitutionCb,filterInstitutionRecyclerView);
                break;
            case R.id.filterTitle_loanType_cb:
                setFilterAll(filterTitleLoanTypeCb, gridViewAdapterILOANTYPE);
                //                setFilterTitle(filterTitleLoanTypeCb,filterLoanTypeRecyclerView);
                break;
            case R.id.filterTitle_fInstitution_cb:
                setFilterAll(filterTitleFInstitutionCb, gridViewAdapterFINSTITUTIONQUERY);
                //                setFilterTitle(filterTitleFInstitutionCb,filterFInstitutionRecyclerView);
                break;
            case R.id.filterTitle_productType_cb:
                setFilterAll(filterTitleProductTypeCb, gridViewAdapterIPRODUCTTYPE);
                //                setFilterTitle(filterTitleProductTypeCb,filterProductTypeRecyclerView);
                break;
            case R.id.filterTitle_riskGrade_cb:
                setFilterAll(filterTitleRiskGradeCb, gridViewAdapterIRISKGRADE);
                //                setFilterTitle(filterTitleRiskGradeCb,filterRiskGradeRecyclerView);
                break;
            case R.id.filterTitle_activityBankInstitution_cb:
                setFilterAll(filterTitleActivityBankInstitutionCb, gridViewAdapterACTIVITYBANKINSTITUTIONQUERY);
                //                setFilterTitle(filterTitleActivityBankInstitutionCb,filterActivityBankInstitutionRecyclerView);
                break;
            case R.id.filterTitle_activityInstitution_cb:
                setFilterAll(filterTitleActivityInstitutionCb, gridViewAdapterACTIVITYINSTITUTIONQUERY);
                //                setFilterTitle(filterTitleActivityInstitutionCb,filterActivityInstitutionRecyclerView);
                break;
            case R.id.filterTitle_loanBankInstitution_cb:
                setFilterAll(filterTitleLoanBankInstitutionCb, gridViewAdapterLOANBANKINSTITUTIONQUERY);
                //                setFilterTitle(filterTitleLoanBankInstitutionCb,filterLoanBankInstitutionRecyclerView);
                break;
            case R.id.filterTitle_financingBankInstitution_cb:
                setFilterAll(filterTitleFinancingBankInstitutionCb, gridViewAdapterFINSTITUIONBANKINSTITUTIONQUERY);
                //                setFilterTitle(filterTitleFinancingBankInstitutionCb,filterFinancingBankInstitutionRecyclerView);
                break;
        }
    }

    private void setFilterAll(CheckBox cb, GridViewAdapter adapter) {
        if (cb.isChecked()) {
            filter_all = true;
        } else {
            filter_all = false;
        }
        notifyAdapter(adapter);

    }

    /**
     * 重置刷新adapter
     *
     * @param adapter
     */
    private void notifyAdapter(GridViewAdapter adapter) {
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置列表的显示与隐藏
     *
     * @param
     */
    private void setFilterTitle(CheckBox cb, RecyclerView recyclerView) {
        if (cb.isChecked())
            recyclerView.setVisibility(View.GONE);
        else
            recyclerView.setVisibility(View.VISIBLE);
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
            filterValues.setProductType(getFilterStr(filter_value_ProductType));
        }
        //风险等级
        if (filter_value_RiskGrade == null) {
            filter_value_RiskGrade = new ArrayList<>();
        }
        if (filter_value_RiskGrade.size() != 0) {
            filterValues.setRiskGrade(getFilterStr(filter_value_RiskGrade));
        }
        //投资模式
        if (filterRedeemableCBox.isChecked()) {
            filterValues.setInvestmentModel("02");//02可赎回
        }
        if (filterNotRedeemableCBox.isChecked()) {
            filterValues.setInvestmentModel("01");//01不可赎回
        }
        //适合人群
        Log.i("Daniel", "---filterCrowdEt---" + filterCrowdEt.getText().toString());
        filterValues.setCrowd(filterCrowdEt.getText().toString());
        //发行城市(贷款)
        Log.i("Daniel", "---filterCityEt---" + filterCityEt.getText().toString());
        filterValues.setDistriArea(filterCityEt.getText().toString());
        //发行城市(理财)
        Log.i("Daniel", "---filterIssuingCityEt---" + filterIssuingCityEt.getText().toString());
        filterValues.setIssuingCity(filterIssuingCityEt.getText().toString());
        //利率区间
        filterValues.setRateMin(filterRateMinEt.getText().toString());
        filterValues.setRateMax(filterRateMaxEt.getText().toString());
        //贷款金额
        filterValues.setLoanMoneyMin(filterLoanMoneyMinEt.getText().toString());
        filterValues.setLoanMoneyMax(filterLoanMoneyMaxEt.getText().toString());
        //贷款期限
        filterValues.setLoanPeriodMin(filterLoanPeriodMinEt.getText().toString());
        filterValues.setLoanPerioMax(filterLoanPeriodMaxEt.getText().toString());
        //投资期限
        filterValues.setInvestmentTermMin(filterInvestmentTermIntervalMinEt.getText().toString());
        filterValues.setInvestmentTermMax(filterInvestmentTermIntervalMaxEt.getText().toString());
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
