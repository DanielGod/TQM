package tqm.bianfeng.com.tqm.pojo;

import java.io.Serializable;
import java.util.List;

import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by johe on 2017/4/11.
 */

public class LawFirmOrInstitutionDetail implements Serializable {


    /**
     * institutionId : null
     * institutionName : 渣打银行
     * institutionIcon : /upload/20170322115533134.jpg
     * contact : null
     * profile : null
     * address : null
     * lawyers : null
     */

    private int institutionId;
    private String institutionName;
    private String institutionIcon;
    private String contact;//官方电话
    private String profile;
    private String address;
    private String isCollect;
    private String website;//官网地址

    private String appLogo;//app的logo

    private String apkUrl;//app下载地址

    private String apkPackage; //apk包名

    private Integer integralNum;//积分数量

    private String business;//业务


    private List<LawyerItem> lawyers;
    private List<BankActivityItem> activities;
    private List<BankLoanItem> loans;
    private List<BankFinancItem> financs;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkPackage() {
        return apkPackage;
    }

    public void setApkPackage(String apkPackage) {
        this.apkPackage = apkPackage;
    }

    public Integer getIntegralNum() {
        return integralNum;
    }

    public void setIntegralNum(Integer integralNum) {
        this.integralNum = integralNum;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getInstitutionIcon() {
        return institutionIcon;
    }

    public void setInstitutionIcon(String institutionIcon) {
        this.institutionIcon = institutionIcon;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<LawyerItem> getLawyers() {
        return lawyers;
    }

    public void setLawyers(List<LawyerItem> lawyers) {
        this.lawyers = lawyers;
    }

    public List<BankActivityItem> getActivities() {
        return activities;
    }

    public void setActivities(List<BankActivityItem> activities) {
        this.activities = activities;
    }

    public List<BankLoanItem> getLoans() {
        return loans;
    }

    public void setLoans(List<BankLoanItem> loans) {
        this.loans = loans;
    }

    public List<BankFinancItem> getFinancs() {
        return financs;
    }

    public void setFinancs(List<BankFinancItem> financs) {
        this.financs = financs;
    }

    @Override
    public String toString() {
        return "LawFirmOrInstitutionDetail{" +
                "institutionId=" + institutionId +
                ", institutionName='" + institutionName + '\'' +
                ", institutionIcon='" + institutionIcon + '\'' +
                ", contact='" + contact + '\'' +
                ", profile='" + profile + '\'' +
                ", address='" + address + '\'' +
                ", isCollect='" + isCollect + '\'' +
                ", website='" + website + '\'' +
                ", appLogo='" + appLogo + '\'' +
                ", apkUrl='" + apkUrl + '\'' +
                ", apkPackage='" + apkPackage + '\'' +
                ", integralNum=" + integralNum +
                ", business='" + business + '\'' +
                ", lawyers=" + lawyers +
                ", activities=" + activities +
                ", loans=" + loans +
                ", financs=" + financs +
                '}';
    }
}
