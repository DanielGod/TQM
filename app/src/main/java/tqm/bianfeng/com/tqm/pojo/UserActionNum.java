package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/5/20.
 */

public class UserActionNum {

    int num;
    String mark;

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
