package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/5/13.
 */

public class FilterLoanVaule {
    String institution;
    String loanType;
    String crowd;
    String distriArea;
    String rateMin;
    String rateMax;
    String loanMoneyMin;
    String loanMoneyMax;
    String loanPeriodMin;
    String loanPerioMax;
    String productType;
    String riskGrade;
    String investmentModel;
    String issuingCity;
    String investmentTermMin;
    String investmentTermMax;

    @Override
    public String toString() {
        return "FilterLoanVaule{" +
                "institution='" + institution + '\'' +
                ", loanType='" + loanType + '\'' +
                ", crowd='" + crowd + '\'' +
                ", distriArea='" + distriArea + '\'' +
                ", rateMin='" + rateMin + '\'' +
                ", rateMax='" + rateMax + '\'' +
                ", loanMoneyMin='" + loanMoneyMin + '\'' +
                ", loanMoneyMax='" + loanMoneyMax + '\'' +
                ", loanPeriodMin='" + loanPeriodMin + '\'' +
                ", loanPerioMax='" + loanPerioMax + '\'' +
                ", productType='" + productType + '\'' +
                ", riskGrade='" + riskGrade + '\'' +
                ", investmentModel='" + investmentModel + '\'' +
                ", issuingCity='" + issuingCity + '\'' +
                ", investmentTermMin='" + investmentTermMin + '\'' +
                ", investmentTermMax='" + investmentTermMax + '\'' +
                '}';
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

    public String getDistriArea() {
        return distriArea;
    }

    public void setDistriArea(String distriArea) {
        this.distriArea = distriArea;
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

    public String getLoanPerioMax() {
        return loanPerioMax;
    }

    public void setLoanPerioMax(String loanPerioMax) {
        this.loanPerioMax = loanPerioMax;
    }

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

    public String getIssuingCity() {
        return issuingCity;
    }

    public void setIssuingCity(String issuingCity) {
        this.issuingCity = issuingCity;
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
}
