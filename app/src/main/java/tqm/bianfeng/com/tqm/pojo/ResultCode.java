package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by johe on 2017/3/14.
 */

public class ResultCode {

    public static final int SECCESS=10001;
    public static final int ERROR=10000;

     String msg;
     int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }
}
