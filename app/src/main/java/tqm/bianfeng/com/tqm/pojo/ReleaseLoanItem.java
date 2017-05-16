package tqm.bianfeng.com.tqm.pojo;

import java.math.BigDecimal;

import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by johe on 2017/5/16.
 */

public class ReleaseLoanItem extends BankLoanItem{

    private String statusCode;//状态:00-待审核;01-审核通过;02-审核未通过

    private String remark;//备注
    public Integer loanId;
    public String loanName;
    public String loanTypeName;
    public BigDecimal rate;
    public BigDecimal loanMoney;
    public String loanPeriod;
    public String institutionName;
    public Integer loanViews;

    @Override
    public String toString() {
        return "ReleaseLoanItem{" +
                "statusCode='" + statusCode + '\'' +
                ", remark='" + remark + '\'' +
                ", loanId=" + loanId +
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
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
