package tqm.bianfeng.com.tqm.pojo.bank;

import java.math.BigDecimal;

/**
 * Created by Daniel on 2017/3/14.
 */

public class BankLoanItem {
    private Integer loanId;
    private String loanName;
    private String loanTypeName;
    private BigDecimal rate;
    private BigDecimal loanMoney;
    private String loanPeriod;
    private String institutionName;
    private Integer loanViews;

    @Override
    public String
    toString() {
        return "BankLoanItem{" +
                "loanId=" + loanId +
                ", loanName='" + loanName + '\'' +
                ", loanTypeName='" + loanTypeName + '\'' +
                ", rate=" + rate +
                ", loanMoney=" + loanMoney +
                ", loanPeriod='" + loanPeriod + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", loanViews=" + loanViews +
                '}';
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public void setLoanTypeName(String loanTypeName) {
        this.loanTypeName = loanTypeName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Integer getLoanViews() {
        return loanViews;
    }

    public void setLoanViews(Integer loanViews) {
        this.loanViews = loanViews;
    }
}
