package tqm.bianfeng.com.tqm.pojo.bank;

import java.util.List;

/**
 * Created by Daniel on 2017/3/19.
 */

public class FilterInfo {
    List<ProductType> lProductType;
    List<RiskGrade> lRiskGrade;
    List<Institution> lInstitution;

    @Override
    public String toString() {
        return "FilterInfo{" +
                "lProductType=" + lProductType +
                ", lRiskGrade=" + lRiskGrade +
                ", lInstitution=" + lInstitution +
                '}';
    }

    public List<ProductType> getlProductType() {
        return lProductType;
    }

    public void setlProductType(List<ProductType> lProductType) {
        this.lProductType = lProductType;
    }

    public List<RiskGrade> getlRiskGrade() {
        return lRiskGrade;
    }

    public void setlRiskGrade(List<RiskGrade> lRiskGrade) {
        this.lRiskGrade = lRiskGrade;
    }

    public List<Institution> getlInstitution() {
        return lInstitution;
    }

    public void setlInstitution(List<Institution> lInstitution) {
        this.lInstitution = lInstitution;
    }
}
