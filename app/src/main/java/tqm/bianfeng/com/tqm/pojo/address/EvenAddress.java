package tqm.bianfeng.com.tqm.pojo.address;

/**
 * Created by 王九东 on 2017/8/8.
 */

public class EvenAddress {
    String city;
    String provience;

    public EvenAddress(String city, String provience) {
        this.city = city;
        this.provience = provience;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvience() {
        return provience;
    }

    public void setProvience(String provience) {
        this.provience = provience;
    }
}
