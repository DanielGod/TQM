package tqm.bianfeng.com.tqm.pojo.result;

import tqm.bianfeng.com.tqm.pojo.ResultCode;

/**
 * Created by johe on 2017/5/12.
 */

public class ResultWithAuditCode extends ResultCode{

    String auditCode;

    public String getAuditCode() {
        return auditCode;
    }

    public void setAuditCode(String auditCode) {
        this.auditCode = auditCode;
    }

    @Override
    public String toString() {
        return "ResultWithAuditCode{" +
                "auditCode='" + auditCode + '\'' +
                '}';
    }
}
