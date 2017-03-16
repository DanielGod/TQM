package tqm.bianfeng.com.tqm.update;

/**
 * Created by johe on 2016/12/29.
 * " versionCode": String 版本号
 "versionUrl":String 版本下载地址
 " updateContent": String 更新内容

 */

public class UpdateMsg  {
    String versionCode;
    String versionUrl;
    String updateContent;

    public UpdateMsg (){

    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    @Override
    public String toString() {
        return "UpdateMsg{" +
                "versionCode='" + versionCode + '\'' +
                ", versionUrl='" + versionUrl + '\'' +
                ", updateContent='" + updateContent + '\'' +
                '}';
    }
}
