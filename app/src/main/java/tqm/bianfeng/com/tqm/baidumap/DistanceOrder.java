package tqm.bianfeng.com.tqm.baidumap;

import java.util.Comparator;

import tqm.bianfeng.com.tqm.pojo.bank.BankDotItem;

/**
 * Created by Daniel on 2017/5/8.
 */

public class DistanceOrder implements Comparator<BankDotItem> {

    @Override
    public int compare(BankDotItem o1, BankDotItem o2) {
        return o1.getDistance()-o2.getDistance();
    }
}
