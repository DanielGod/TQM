package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/3/14.
 */

public class BankInformItem {
    /**
     * informId : 2
     * informTitle : 银行咨询测试01
     * releaseDate : 2017-03-14
     * institutionName : 东元资本公司
     * informType : 1
     * informViews : 1
     */

    private int informId;
    private String informTitle;
    private String releaseDate;
    private String institutionName;
    private int informType;
    private int informViews;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getInformId() {
        return informId;
    }

    public void setInformId(int informId) {
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

    public int getInformType() {
        return informType;
    }

    public void setInformType(int informType) {
        this.informType = informType;
    }

    public int getInformViews() {
        return informViews;
    }

    public void setInformViews(int informViews) {
        this.informViews = informViews;
    }

    @Override
    public String toString() {
        return "BankInformItem{" +
                "informId=" + informId +
                ", informTitle='" + informTitle + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", informType=" + informType +
                ", informViews=" + informViews +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
