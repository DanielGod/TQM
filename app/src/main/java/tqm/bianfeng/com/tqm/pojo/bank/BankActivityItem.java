package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/5/11.
 */

public class BankActivityItem {
    /**
     * activityId : 11
     * activityTitle : 欧洲九大精品购物村 建行龙卡信用卡享最高16%优惠
     * institution : 中国建设银行
     * activityViews : 0
     * imageUrl : /upload/inform/20170511095929727.jpg
     * atttenNum : 2
     */

    private Integer activityId; // 活动Id

    private String activityTitle; // 活动标题

    private String institution; // 所属机构

    private Integer activityViews;//浏览量

    private String imageUrl; //活动图片

    private Integer atttenNum;//关注量
    private String statusCode;//状态:00-待审核;01-审核通过;02-审核未通过
    private String remark;//备注
    private String articlePath;

    private String institutionIcon; // 机构图片


    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public void setActivityViews(Integer activityViews) {
        this.activityViews = activityViews;
    }

    public void setAtttenNum(Integer atttenNum) {
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

    public String getInstitutionIcon() {
        return institutionIcon;
    }

    public void setInstitutionIcon(String institutionIcon) {
        this.institutionIcon = institutionIcon;
    }

    public String getArticlePath() {
        return articlePath;
    }

    public void setArticlePath(String articlePath) {
        this.articlePath = articlePath;
    }

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

    @Override
    public String toString() {
        return "BankActivityItem{" +
                "activityId=" + activityId +
                ", activityTitle='" + activityTitle + '\'' +
                ", institution='" + institution + '\'' +
                ", activityViews=" + activityViews +
                ", imageUrl='" + imageUrl + '\'' +
                ", atttenNum=" + atttenNum +
                '}';
    }
}
