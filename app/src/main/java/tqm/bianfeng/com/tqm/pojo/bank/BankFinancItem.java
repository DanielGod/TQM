package tqm.bianfeng.com.tqm.pojo.bank;

import java.math.BigDecimal;

/**
 * Created by Daniel on 2017/3/14.
 */

public class BankFinancItem {
    private Integer financId;
    private String productName;
    private String institutionName;
    private String issuingCity;
    private String riskGradeName;
    private String investmentTerm;
    private BigDecimal annualReturn;
    private BigDecimal purchaseMoney;
    private Integer financViews;

    @Override
    public String toString() {
        return "BankFinancItem{" +
                "financId=" + financId +
                ", productName='" + productName + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", issuingCity='" + issuingCity + '\'' +
                ", riskGradeName='" + riskGradeName + '\'' +
                ", investmentTerm='" + investmentTerm + '\'' +
                ", annualReturn=" + annualReturn +
                ", purchaseMoney=" + purchaseMoney +
                ", financViews=" + financViews +
                '}';
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

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getIssuingCity() {
        return issuingCity;
    }

    public void setIssuingCity(String issuingCity) {
        this.issuingCity = issuingCity;
    }

    public String getRiskGradeName() {
        return riskGradeName;
    }

    public void setRiskGradeName(String riskGradeName) {
        this.riskGradeName = riskGradeName;
    }

    public String getInvestmentTerm() {
        return investmentTerm;
    }

    public void setInvestmentTerm(String investmentTerm) {
        this.investmentTerm = investmentTerm;
    }

    public BigDecimal getAnnualReturn() {
        return annualReturn;
    }

    public void setAnnualReturn(BigDecimal annualReturn) {
        this.annualReturn = annualReturn;
    }

    public BigDecimal getPurchaseMoney() {
        return purchaseMoney;
    }

    public void setPurchaseMoney(BigDecimal purchaseMoney) {
        this.purchaseMoney = purchaseMoney;
    }

    public Integer getFinancViews() {
        return financViews;
    }

    public void setFinancViews(Integer financViews) {
        this.financViews = financViews;
    }
}
