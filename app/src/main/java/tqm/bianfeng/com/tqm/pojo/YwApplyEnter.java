package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/5/11.
 */

public class YwApplyEnter {
    private Integer applyId;

    private String applyName;//名称(公司名称或个人称呼)

    private String proposer;//联系人

    private String contact;//联系方式

    private String idCard;//身份证号

    private String address;//地址

    private String applyType;//申请类型:1001-民间资本;1002-公司中介;2001-资方;2002-个人中介

    private String companyLogo;//公司logo

    private String companyImage;//公司照片  如：http://123,http://123

    private String companyImageOther;//公司其他图片

   private Integer applyUser;//申请用户Id

    String auditCode;//审核结果状态

    String auditRemark;//审核结果备注

    Long createDate;//申请日期

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getAuditCode() {

        return auditCode;
    }

    public void setAuditCode(String auditCode) {
        this.auditCode = auditCode;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

//    public Date getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(Date createDate) {
//        this.createDate = createDate;
//    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getCompanyImageOther() {
        return companyImageOther;
    }

    public void setCompanyImageOther(String companyImageOther) {
        this.companyImageOther = companyImageOther;
    }

    public Integer getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(Integer applyUser) {
        this.applyUser = applyUser;
    }

    @Override
    public String toString() {
        return "YwApplyEnter{" +
                "applyId=" + applyId +
                ", applyName='" + applyName + '\'' +
                ", proposer='" + proposer + '\'' +
                ", contact='" + contact + '\'' +
                ", idCard='" + idCard + '\'' +
                ", address='" + address + '\'' +
                ", applyType='" + applyType + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                ", companyImage='" + companyImage + '\'' +
                ", companyImageOther='" + companyImageOther + '\'' +
                ", applyUser=" + applyUser +
                ", auditCode='" + auditCode + '\'' +
                ", auditRemark='" + auditRemark + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
