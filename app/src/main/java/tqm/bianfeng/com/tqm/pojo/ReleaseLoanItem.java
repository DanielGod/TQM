package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/5/16.
 */

public class ReleaseLoanItem {

    private String statusCode;//状态:00-待审核;01-审核通过;02-审核未通过

    private String remark;//备注
    private int loanId;
    private String loanName;
    private String loanType;
    private float rateMin;
    private float rateMax;
    private float loanMoneyMin;
    private float loanMoneyMax;
    private int loanPeriodMin;
    private int loanPeriodMax;
    private String institution;
    private int loanViews;
    private int atttenNum;

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public float getRateMin() {
        return rateMin;
    }

    public void setRateMin(float rateMin) {
        this.rateMin = rateMin;
    }

    public float getRateMax() {
        return rateMax;
    }

    public void setRateMax(float rateMax) {
        this.rateMax = rateMax;
    }

    public float getLoanMoneyMin() {
        return loanMoneyMin;
    }

    public void setLoanMoneyMin(float loanMoneyMin) {
        this.loanMoneyMin = loanMoneyMin;
    }

    public float getLoanMoneyMax() {
        return loanMoneyMax;
    }

    public void setLoanMoneyMax(float loanMoneyMax) {
        this.loanMoneyMax = loanMoneyMax;
    }

    public int getLoanPeriodMin() {
        return loanPeriodMin;
    }

    public void setLoanPeriodMin(int loanPeriodMin) {
        this.loanPeriodMin = loanPeriodMin;
    }

    public int getLoanPeriodMax() {
        return loanPeriodMax;
    }

    public void setLoanPeriodMax(int loanPeriodMax) {
        this.loanPeriodMax = loanPeriodMax;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public int getLoanViews() {
        return loanViews;
    }

    public void setLoanViews(int loanViews) {
        this.loanViews = loanViews;
    }

    public int getAtttenNum() {
        return atttenNum;
    }

    public void setAtttenNum(int atttenNum) {
        this.atttenNum = atttenNum;
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

    @Override
    public String toString() {
        return "ReleaseLoanItem{" +
                "statusCode='" + statusCode + '\'' +
                ", remark='" + remark + '\'' +
                ", loanId=" + loanId +
                ", loanName='" + loanName + '\'' +
                ", loanType='" + loanType + '\'' +
                ", rateMin=" + rateMin +
                ", rateMax=" + rateMax +
                ", loanMoneyMin=" + loanMoneyMin +
                ", loanMoneyMax=" + loanMoneyMax +
                ", loanPeriodMin=" + loanPeriodMin +
                ", loanPeriodMax=" + loanPeriodMax +
                ", institution='" + institution + '\'' +
                ", loanViews=" + loanViews +
                ", atttenNum=" + atttenNum +
                '}';
    }
}
