package tqm.bianfeng.com.tqm.main.vehicle.bean;

import java.util.List;

/**
 * Created by 王九东 on 2017/7/12.
 */

public class DataBean {
    /**
     * pyear : 2015
     * chexing_list : [{"id":"20022441","cxname":"逸致 2015款 180E CVT跨界版","pyear":"2014","price":"16.28"}]
     */

    private int pyear;
    private List<ChexingListBean> chexing_list;

    public int getPyear() {
        return pyear;
    }

    public void setPyear(int pyear) {
        this.pyear = pyear;
    }

    public List<ChexingListBean> getChexing_list() {
        return chexing_list;
    }

    public void setChexing_list(List<ChexingListBean> chexing_list) {
        this.chexing_list = chexing_list;
    }
}
