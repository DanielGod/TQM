package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by 王九东 on 2017/7/16.
 */

public class Reports {

    /**
     * objectTitle : 银行咨询测试01
     * objectModule : 04
     * id : 3
     * type : 02
     * content : now图老K
     * statusCode : 00
     * remark : undefined
     */

    private String objectTitle;
    private String objectModule;
    private int id;
    private String type;
    private String content;
    private String statusCode;
    private String remark;

    public String getObjectTitle() {
        return objectTitle;
    }

    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    public String getObjectModule() {
        return objectModule;
    }

    public void setObjectModule(String objectModule) {
        this.objectModule = objectModule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return "Reports{" +
                "objectTitle='" + objectTitle + '\'' +
                ", objectModule='" + objectModule + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
