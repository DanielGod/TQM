package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/3/30.
 */

public class LawyerItem {


    /**
     * lawyerId : 15
     * lawyerName : 张敏
     * contact : 15236101580
     * avatar : /upload/avatar/20170411102409972.jpg
     * isAuthorize : 01
     * institutionName : 中银律师事务所
     * province : 浙江省
     * city : 宁波市
     * district : 江东区
     * position : 律师
     * practiceTime : 15年
     */

    private int lawyerId;
    private String lawyerName;
    private String contact;
    private String avatar;
    private String isAuthorize;
    private String institutionName;
    private String province;
    private String city;
    private String district;
    private String position;
    private String practiceTime;

    public int getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(int lawyerId) {
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

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPracticeTime() {
        return practiceTime;
    }

    public void setPracticeTime(String practiceTime) {
        this.practiceTime = practiceTime;
    }

    @Override
    public String toString() {
        return "LawyerItem{" +
                "lawyerId=" + lawyerId +
                ", lawyerName='" + lawyerName + '\'' +
                ", contact='" + contact + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isAuthorize='" + isAuthorize + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", position='" + position + '\'' +
                ", practiceTime='" + practiceTime + '\'' +
                '}';
    }
}
