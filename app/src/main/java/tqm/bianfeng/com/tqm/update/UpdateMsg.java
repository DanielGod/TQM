package tqm.bianfeng.com.tqm.update;

/**
 * Created by johe on 2016/12/29.
 * " versionCode": String 版本号
 "versionUrl":String 版本下载地址
 " updateContent": String 更新内容

 */

public class UpdateMsg  {
    String versionCode;
    String updateUrl;
    String updateContent;

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "UpdateMsg{" +
                "versionCode='" + versionCode + '\'' +
                ", updateUrl='" + updateUrl + '\'' +
                ", updateContent='" + updateContent + '\'' +
                '}';
    }
}
