package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/14.
 */

public class BankFinancItem {
    private Integer financId;
    private String productName;
    private String institution;
    private String issuingCity;
    private String riskGrade;
    private String investmentTerm;
    private String annualReturn;
    private String purchaseMoney;
    private Integer financViews;
    private String presaleStatus;
    private String institutionIcon;

    public String getInstitutionIcon() {
        return institutionIcon;
    }

    public void setInstitutionIcon(String institutionIcon) {
        this.institutionIcon = institutionIcon;
    }

    public String getPresaleStatus() {
        return presaleStatus;
    }

    public void setPresaleStatus(String presaleStatus) {
        this.presaleStatus = presaleStatus;
    }

    public Integer getFinancId() {
        return financId;
    }

    public void setFinancId(Integer financId) {
        this.financId = financId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getIssuingCity() {
        return issuingCity;
    }

    public void setIssuingCity(String issuingCity) {
        this.issuingCity = issuingCity;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    public String getInvestmentTerm() {
        return investmentTerm;
    }

    public void setInvestmentTerm(String investmentTerm) {
        this.investmentTerm = investmentTerm;
    }

    public String getAnnualReturn() {
        return annualReturn;
    }

    public void setAnnualReturn(String annualReturn) {
        this.annualReturn = annualReturn;
    }

    public String getPurchaseMoney() {
        return purchaseMoney;
    }

    public void setPurchaseMoney(String purchaseMoney) {
        this.purchaseMoney = purchaseMoney;
    }

    public Integer getFinancViews() {
        return financViews;
    }

    public void setFinancViews(Integer financViews) {
        this.financViews = financViews;
    }
}
