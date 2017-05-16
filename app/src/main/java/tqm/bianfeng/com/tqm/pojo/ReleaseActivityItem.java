package tqm.bianfeng.com.tqm.pojo;

import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;

/**
 * Created by johe on 2017/5/16.
 */

public class ReleaseActivityItem extends BankActivityItem{
    private String statusCode;//状态:00-待审核;01-审核通过;02-审核未通过

    private String remark;//备注
    public Integer activityId;
    public String activityTitle;
    public String institutionName;
    public Integer activityViews;
    public String institutionIcon;
    public String imageUrl;

    @Override
    public String toString() {
        return "ReleaseActivityItem{" +
                "statusCode='" + statusCode + '\'' +
                ", remark='" + remark + '\'' +
                ", activityId=" + activityId +
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


}
