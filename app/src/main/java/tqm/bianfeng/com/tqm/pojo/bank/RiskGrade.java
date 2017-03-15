package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/14.
 */

public class RiskGrade {
    private Integer  riskGradeId;
    private String riskGradeName;

    @Override
    public String toString() {
        return "RiskGrade{" +
                "riskGradeId=" + riskGradeId +
                ", riskGradeName='" + riskGradeName + '\'' +
                '}';
    }

    public Integer getRiskGradeId() {
        return riskGradeId;
    }

    public void setRiskGradeId(Integer riskGradeId) {
        this.riskGradeId = riskGradeId;
    }

    public String getRiskGradeName() {
        return riskGradeName;
    }

    public void setRiskGradeName(String riskGradeName) {
        this.riskGradeName = riskGradeName;
    }
}
