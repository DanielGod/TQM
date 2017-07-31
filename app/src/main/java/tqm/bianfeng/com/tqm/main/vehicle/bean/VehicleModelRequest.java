package tqm.bianfeng.com.tqm.main.vehicle.bean;

import java.util.List;

/**
 * Created by 王九东 on 2017/7/12.
 */

public class VehicleModelRequest {

    /**
     * reason : 成功返回
     * result : {"data":[{"pyear":2015,"chexing_list":[{"id":"20022441","cxname":"逸致 2015款 180E CVT跨界版","pyear":"2014","price":"16.28"}]},{"pyear":2014,"chexing_list":[{"id":"20018527","cxname":"逸致 2014款 星耀 180G 1.8L CVT舒适多功能版","pyear":"2013","price":"17.98"},{"id":"20018520","cxname":"逸致 2014款 星耀 160E 1.6L 手动精英版","pyear":"2013","price":"14.98"}]}]}
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
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

    }
}
