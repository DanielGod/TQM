package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/4/11.
 */

public class InstitutionItem {
    /**
     * institutionId : 20
     * institutionName : 律师事务所测试04
     * institutionIcon : /upload/20170411134641372.jpg
     * contact : 15236101580
     * profile : 我们的理念:“诚信为本、实力为先，全心全意为客户”，我们公司秉承客户至上、服务至上的经营理念，以卓越的it服务品质、专业的技术服务实力、技术精湛的客户服务团队，保障客户在信息时代的高速路上驰骋，又以稳固、发展、忠诚、高效、团结与创新的精神，尊重人才注重技术，使客户在享受信息科技发展最新成果的同时不断获得最大的收益。

     选择我们四大理由:

     节省开支:对中小型企业，计算机数量不多，如雇专职工程师需要支付相应的工资、福利和社保等费用，而维护工作量又不大，因而支出会显得较高。而采用我们公司的服务，可以大大降低因系统维护而造成的相关费用，节约企业开支。

     经验丰富:我们为企业提供功能强大服务，包括建立系统设备档案、系统维护记录、系统维护记录分析等服务项目。我们的工程师不仅经验丰富，而且随时能获得公司强大的技术支持，保障用户系统正常运行。
     */

    private int institutionId;
    private String institutionName;
    private String institutionIcon;
    private String contact;
    private String profile;

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

    public String getInstitutionIcon() {
        return institutionIcon;
    }

    public void setInstitutionIcon(String institutionIcon) {
        this.institutionIcon = institutionIcon;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "InstitutionItem{" +
                "institutionId=" + institutionId +
                ", institutionName='" + institutionName + '\'' +
                ", institutionIcon='" + institutionIcon + '\'' +
                ", contact='" + contact + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
