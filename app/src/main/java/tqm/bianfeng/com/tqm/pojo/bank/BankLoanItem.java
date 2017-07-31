package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/14.
 */

public class BankLoanItem {
        /**
         * loanId : 55
         * loanName : 房贷
         * loanType : 质押贷
         * rateMin : 2.3
         * rateMax : 3.6
         * loanMoneyMin : 5
         * loanMoneyMax : 20
         * loanPeriodMin : 30
         * loanPeriodMax : 60
         * institution : 东元资本公司
         * loanViews : 0
         * atttenNum : 0
         */
        private int loanId;
        private String loanName;
        private String loanType;
        private String rateMin;
        private String rateMax;
        private String loanMoneyMin;
        private String loanMoneyMax;
        private String loanPeriodMin;
        private String loanPeriodMax;
        private String institution;
        private int loanViews;
        private int atttenNum;
    private String institutionIcon;

    public String getInstitutionIcon() {
        return institutionIcon;
    }

    public void setInstitutionIcon(String institutionIcon) {
        this.institutionIcon = institutionIcon;
    }

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

    @Override
    public String toString() {
        return "BankLoanItem{" +
                "loanId=" + loanId +
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
                ", institutionIcon='" + institutionIcon + '\'' +
                '}';
    }
}
