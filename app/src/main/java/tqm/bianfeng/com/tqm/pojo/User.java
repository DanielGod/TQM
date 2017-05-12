package tqm.bianfeng.com.tqm.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by johe on 2017/3/14.
 *  "userId": Integer 用户ID
 "userPhone": String 用户登录帐号
 "userNickname": String 用户昵称
 "userAvatar": String 用户头像

 */

public class User extends RealmObject{

    @PrimaryKey
    int userId;
    String userPhone;
    String userNickname;
    String userAvatar="";
    String userType="";
    String applyForStatu="-1";

    public String getApplyForStatu() {
        return applyForStatu;
    }

    public void setApplyForStatu(String applyForStatu) {
        this.applyForStatu = applyForStatu;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userPhone='" + userPhone + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
