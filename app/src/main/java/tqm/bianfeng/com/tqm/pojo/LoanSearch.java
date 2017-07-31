package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by 王九东 on 2017/7/6.
 */

public class LoanSearch  {

    private String homeShow; //是否首页展示 //非空01-首页;02-非首页

    private Integer pageNum;//非空

    private Integer pageSize;//非空

    private String city; //所在城市//非空

    private String institution; //机构名称

    private String loanType; //贷款类型

    private String crowd; //适合人群

    private String rateMin;//最小利率

    private String rateMax;//最大利率

    private String loanMoneyMin;//最小贷款金额

    private String loanMoneyMax;//最大贷款金额

    private String loanPeriodMin; //贷款期限最小值 贷款

    private String loanPeriodMax; //贷款期限最大值

    private String search;//模糊搜索内容

    private String productType; //产品类型

    private String riskGrade; //风险等级

    private String investmentModel;//投资模式

    private String investmentTermMin; //最小投资期限 理财

    private String investmentTermMax; //最大投资期限

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    public String getInvestmentModel() {
        return investmentModel;
    }

    public void setInvestmentModel(String investmentModel) {
        this.investmentModel = investmentModel;
    }

    public String getInvestmentTermMin() {
        return investmentTermMin;
    }

    public void setInvestmentTermMin(String investmentTermMin) {
        this.investmentTermMin = investmentTermMin;
    }

    public String getInvestmentTermMax() {
        return investmentTermMax;
    }

    public void setInvestmentTermMax(String investmentTermMax) {
        this.investmentTermMax = investmentTermMax;
    }

    public String getHomeShow() {
        return homeShow;
    }

    public void setHomeShow(String homeShow) {
        this.homeShow = homeShow;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getCrowd() {
        return crowd;
    }

    public void setCrowd(String crowd) {
        this.crowd = crowd;
    }

    public String getRateMin() {
        return rateMin;
    }

    public void setRateMin(String rateMin) {
        this.rateMin = rateMin;
    }

    public String getRateMax() {
        return rateMax;
    }

    public void setRateMax(String rateMax) {
        this.rateMax = rateMax;
    }

    public String getLoanMoneyMin() {
        return loanMoneyMin;
    }

    public void setLoanMoneyMin(String loanMoneyMin) {
        this.loanMoneyMin = loanMoneyMin;
    }

    public String getLoanMoneyMax() {
        return loanMoneyMax;
    }

    public void setLoanMoneyMax(String loanMoneyMax) {
        this.loanMoneyMax = loanMoneyMax;
    }

    public String getLoanPeriodMin() {
        return loanPeriodMin;
    }

    public void setLoanPeriodMin(String loanPeriodMin) {
        this.loanPeriodMin = loanPeriodMin;
    }

    public String getLoanPeriodMax() {
        return loanPeriodMax;
    }

    public void setLoanPeriodMax(String loanPeriodMax) {
        this.loanPeriodMax = loanPeriodMax;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public String toString() {
        return "LoanSearch{" +
                "homeShow='" + homeShow + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", city='" + city + '\'' +
                ", institution='" + institution + '\'' +
                ", loanType='" + loanType + '\'' +
                ", crowd='" + crowd + '\'' +
                ", rateMin='" + rateMin + '\'' +
                ", rateMax='" + rateMax + '\'' +
                ", loanMoneyMin='" + loanMoneyMin + '\'' +
                ", loanMoneyMax='" + loanMoneyMax + '\'' +
                ", loanPeriodMin='" + loanPeriodMin + '\'' +
                ", loanPeriodMax='" + loanPeriodMax + '\'' +
                ", search='" + search + '\'' +
                ", productType='" + productType + '\'' +
                ", riskGrade='" + riskGrade + '\'' +
                ", investmentModel='" + investmentModel + '\'' +
                ", investmentTermMin='" + investmentTermMin + '\'' +
                ", investmentTermMax='" + investmentTermMax + '\'' +
                '}';
    }
}
