package tqm.bianfeng.com.tqm.pojo.bank;

import java.util.List;

/**
 * Created by Daniel on 2017/5/11.
 */

public class BankListItems<T> {

    /**
     * total : 9
     * item : [{"activityId":11,"activityTitle":"欧洲九大精品购物村 建行龙卡信用卡享最高16%优惠","institution":"中国建设银行","activityViews":0,"imageUrl":"/upload/inform/20170511095929727.jpg","atttenNum":2},{"activityId":10,"activityTitle":"星巴克 满60元立减15元","institution":"中国建设银行","activityViews":0,"imageUrl":"/upload/inform/20170511095842624.jpg","atttenNum":0},{"activityId":9,"activityTitle":"龙卡信用卡带您玩转世界，韩国新罗免税店享优惠","institution":"中国建设银行","activityViews":0,"imageUrl":"/upload/inform/20170511095746657.jpg","atttenNum":0}]
     */

    private int total;
    private List<T> item;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItem() {
        return item;
    }

    public void setItem(List<T> item) {
        this.item = item;
    }
}
