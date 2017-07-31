package tqm.bianfeng.com.tqm.main.vehicle.bean;

import java.util.List;

/**
 * Created by 王九东 on 2017/7/11.
 */

public class VehicleRequest {

    /**
     * reason : 成功返回
     * result : {"pinpai_list":[{"ppid":"3000047","ppname":"一汽大众(奥迪)","xilie":[{"xlid":"20000054","xlname":"奥迪A4L"},{"xlid":"20000056","xlname":"奥迪A6L"},{"xlid":"20000055","xlname":"奥迪A6"},{"xlid":"20000156","xlname":"奥迪Q5"},{"xlid":"20000155","xlname":"奥迪Q3"},{"xlid":"20000528","xlname":"奥迪A3"},{"xlid":"20000053","xlname":"奥迪A4"},{"xlid":"20000001","xlname":"奥迪100"},{"xlid":"20000002","xlname":"奥迪200"}]},{"ppid":"4000002","ppname":"奥迪 audi(进口)","xilie":[{"xlid":"30001017","xlname":"奥迪A8(进口)"},{"xlid":"30001018","xlname":"奥迪A8-Langversion(进口)"},{"xlid":"30000162","xlname":"奥迪A1(进口)"},{"xlid":"30000166","xlname":"奥迪A3(进口)"},{"xlid":"30000167","xlname":"奥迪A4(进口)"},{"xlid":"30000170","xlname":"奥迪A5(进口)"},{"xlid":"30000174","xlname":"奥迪A7(进口)"},{"xlid":"30000789","xlname":"奥迪R8(进口)"},{"xlid":"30000814","xlname":"奥迪RS5(进口)"},{"xlid":"30000856","xlname":"奥迪S6(进口)"},{"xlid":"30000858","xlname":"奥迪S7(进口)"},{"xlid":"30000859","xlname":"奥迪S8(进口)"},{"xlid":"30000760","xlname":"奥迪Q3(进口)"},{"xlid":"30000816","xlname":"奥迪RS7(进口)"},{"xlid":"30000852","xlname":"奥迪S3(进口)"},{"xlid":"30000891","xlname":"奥迪SQ5(进口)"},{"xlid":"30000912","xlname":"奥迪TT(进口)"},{"xlid":"30000855","xlname":"奥迪S5(进口)"},{"xlid":"30000914","xlname":"奥迪TTS(进口)"},{"xlid":"30000176","xlname":"奥迪A8-W12(进口)"},{"xlid":"30000177","xlname":"奥迪A8L(进口)"},{"xlid":"30000762","xlname":"奥迪Q5(进口)"},{"xlid":"30000767","xlname":"奥迪Q7(进口)"},{"xlid":"30000173","xlname":"奥迪A6(进口)"},{"xlid":"30000815","xlname":"奥迪RS6(进口)"}]}]}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        private List<PinpaiListBean> pinpai_list;

        public List<PinpaiListBean> getPinpai_list() {
            return pinpai_list;
        }

        public void setPinpai_list(List<PinpaiListBean> pinpai_list) {
            this.pinpai_list = pinpai_list;
        }

        public static class PinpaiListBean {

            private List<XilieBean> xilie;

            public List<XilieBean> getXilie() {
                return xilie;
            }

            public void setXilie(List<XilieBean> xilie) {
                this.xilie = xilie;
            }

        }
    }
}
