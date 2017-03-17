package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/16.
 */

public class FilterValues {
    String productTypeId;
    String institutionId;
    String riskGradeId;
    String issuingCity;
    String sales_date;

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getRiskGradeId() {
        return riskGradeId;
    }

    public void setRiskGradeId(String riskGradeId) {
        this.riskGradeId = riskGradeId;
    }

    public String getIssuingCity() {
        return issuingCity;
    }

    public void setIssuingCity(String issuingCity) {
        this.issuingCity = issuingCity;
    }

    public String getSales_date() {
        return sales_date;
    }

    public void setSales_date(String sales_date) {
        this.sales_date = sales_date;
    }
}
