package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/5/20.
 */

public class UserActionNum {

    int num;
    String mark; //05 判断签到 0-未签到 1-签到  06-积分

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "UserActionNum{" +
                "num=" + num +
                ", mark='" + mark + '\'' +
                '}';
    }
}
