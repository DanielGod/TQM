package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/3/14.
 */

public class ResultCodeWithUser extends ResultCode {

    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ResultCodeWithUser{" +
                "user=" + user +
                '}';
    }
}
