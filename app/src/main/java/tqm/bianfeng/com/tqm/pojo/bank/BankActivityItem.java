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
}
