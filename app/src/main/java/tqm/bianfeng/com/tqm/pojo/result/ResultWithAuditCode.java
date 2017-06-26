package tqm.bianfeng.com.tqm.pojo.result;

import tqm.bianfeng.com.tqm.pojo.ResultCode;

/**
 * Created by johe on 2017/5/12.
 */

public class ResultWithAuditCode extends ResultCode{

    String shzt;

    @Override
    public String toString() {
        return "ResultWithAuditCode{" +
                "shzt='" + shzt + '\'' +
                '}';
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }
}
