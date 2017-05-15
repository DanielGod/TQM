package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/4/11.
 */

public class InstitutionItem {

    /**
     * institutionId : 20
     * institutionName : 浙江立涌律师事务所
     * institutionIcon : /upload/20170428132533651.png
     * contact : 15236101580
     * isAuthorize : null
     * institutionTypeLabel : null
     * isCollect : 02
     */

    private int institutionId;
    private String institutionName;
    private String institutionIcon;
    private String contact;
    private String isAuthorize;
    private String institutionTypeLabel;
    private String isCollect;

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getInstitutionTypeLabel() {
        return institutionTypeLabel;
    }

    public void setInstitutionTypeLabel(String institutionTypeLabel) {
        this.institutionTypeLabel = institutionTypeLabel;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getIsAuthorize() {
        return isAuthorize;
    }

    public void setIsAuthorize(String isAuthorize) {
        this.isAuthorize = isAuthorize;
    }

    public String getInstitutionIcon() {
        return institutionIcon;
    }

    public void setInstitutionIcon(String institutionIcon) {
        this.institutionIcon = institutionIcon;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    @Override
    public String toString() {
        return "InstitutionItem{" +
                "institutionId=" + institutionId +
                ", institutionName='" + institutionName + '\'' +
                ", institutionIcon='" + institutionIcon + '\'' +
                ", contact='" + contact + '\'' +
                ", isAuthorize=" + isAuthorize +
                ", institutionTypeLabel=" + institutionTypeLabel +
                ", isCollect='" + isCollect + '\'' +
                '}';
    }
}
