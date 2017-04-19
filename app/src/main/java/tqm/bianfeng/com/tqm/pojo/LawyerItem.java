package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/3/30.
 */

public class LawyerItem {
    /**
     * lawyerId : 2
     * lawyerName : 蒋驰律师
     * contact : 15236101580
     * avatar : /upload/avatar/20170330143718491.jpg
     * isAuthorize : 01
     */
    /**
     *  "lawyerId": 13,
     "lawyerName": "测试律师004",
     "contact": "15236101580",
     "avatar": "/upload/avatar/20170411094200552.jpg",
     "isAuthorize": "01"
     */

    private Integer lawyerId;
    private String lawyerName;
    private String contact;
    private String avatar;
    private String isAuthorize;

    public Integer getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Integer lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIsAuthorize() {
        return isAuthorize;
    }

    public void setIsAuthorize(String isAuthorize) {
        this.isAuthorize = isAuthorize;
    }

    @Override
    public String toString() {
        return "LawyerItem{" +
                "lawyerId=" + lawyerId +
                ", lawyerName='" + lawyerName + '\'' +
                ", contact='" + contact + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isAuthorize='" + isAuthorize + '\'' +
                '}';
    }
}
