package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/5/16.
 */

public class ReleaseActivityItem {
    private String statusCode;//状态:00-待审核;01-审核通过;02-审核未通过

    private String remark;//备注
    private int activityId;
    private String activityTitle;
    private String institution;
    private int activityViews;
    private String imageUrl;
    private int atttenNum;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public int getActivityViews() {
        return activityViews;
    }

    public void setActivityViews(int activityViews) {
        this.activityViews = activityViews;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAtttenNum() {
        return atttenNum;
    }

    public void setAtttenNum(int atttenNum) {
        this.atttenNum = atttenNum;
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

    @Override
    public String toString() {
        return "ReleaseActivityItem{" +
                "statusCode='" + statusCode + '\'' +
                ", remark='" + remark + '\'' +
                ", activityId=" + activityId +
                ", activityTitle='" + activityTitle + '\'' +
                ", institution='" + institution + '\'' +
                ", activityViews=" + activityViews +
                ", imageUrl='" + imageUrl + '\'' +
                ", atttenNum=" + atttenNum +
                '}';
    }
}
