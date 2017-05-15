package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/5/13.
 */

public class ErrorReport {

    int id;
    int objectId=0;
    String objectModule;
    String type;
    String content;

    int userId;
    String objectTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectTitle() {
        return objectTitle;
    }

    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getObjectModule() {
        return objectModule;
    }

    public void setObjectModule(String objectModule) {
        this.objectModule = objectModule;
    }

}
