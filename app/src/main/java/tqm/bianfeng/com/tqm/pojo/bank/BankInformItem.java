package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/14.
 */

public class BankInformItem {
    private Integer informId;
    private String informTitle;
    private String releaseDate;
    private String institutionName;
    private Integer informType;
    private Integer informViews;

    @Override
    public String toString() {
        return "BankInformItem{" +
                "informId=" + informId +
                ", informTitle='" + informTitle + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", informType=" + informType +
                ", informViews=" + informViews +
                '}';
    }

    public Integer getInformId() {
        return informId;
    }

    public void setInformId(Integer informId) {
        this.informId = informId;
    }

    public String getInformTitle() {
        return informTitle;
    }

    public void setInformTitle(String informTitle) {
        this.informTitle = informTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Integer getInformType() {
        return informType;
    }

    public void setInformType(Integer informType) {
        this.informType = informType;
    }

    public Integer getInformViews() {
        return informViews;
    }

    public void setInformViews(Integer informViews) {
        this.informViews = informViews;
    }
}
