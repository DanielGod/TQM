package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/14.
 */

public class BankActivityItem {
    private Integer activityId;
    private String activityTitle;
    private String institutionName;
    private Integer activityViews;
    private String institutionIcon;

    @Override
    public String toString() {
        return "BankActivityItem{" +
                "activityId=" + activityId +
                ", activityTitle='" + activityTitle + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", activityViews=" + activityViews +
                ", institutionIcon='" + institutionIcon + '\'' +
                '}';
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
