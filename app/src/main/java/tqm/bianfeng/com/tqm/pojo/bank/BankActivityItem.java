package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/14.
 */

public class BankActivityItem {
    public Integer activityId;
    public String activityTitle;
    public String institutionName;
    public Integer activityViews;
    public String institutionIcon;
    public String imageUrl;

    @Override
    public String toString() {
        return "BankActivityItem{" +
                "activityId=" + activityId +
                ", activityTitle='" + activityTitle + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", activityViews=" + activityViews +
                ", institutionIcon='" + institutionIcon + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Integer getActivityViews() {
        return activityViews;
    }

    public void setActivityViews(Integer activityViews) {
        this.activityViews = activityViews;
    }

    public String getInstitutionIcon() {
        return institutionIcon;
    }

    public void setInstitutionIcon(String institutionIcon) {
        this.institutionIcon = institutionIcon;
    }
}
