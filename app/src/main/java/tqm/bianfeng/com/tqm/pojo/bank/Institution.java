package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/14.
 */

public class Institution {
    private Integer institutionId;
    private String institutionName;

    @Override
    public String toString() {
        return "Institution{" +
                "institutionId=" + institutionId +
                ", institutionName='" + institutionName + '\'' +
                '}';
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
