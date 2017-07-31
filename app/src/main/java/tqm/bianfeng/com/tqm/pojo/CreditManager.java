package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by 王九东 on 2017/7/3.
 */

public class CreditManager {

    /**
     * lxdh : 18615250005
     * city : 天津市
     * lxr : 张三
     * county : 河东区
     */

    private String lxdh;
    private String city;
    private String lxr;
    private String county;

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Override
    public String toString() {
        return "CreditManager{" +
                "lxdh='" + lxdh + '\'' +
                ", city='" + city + '\'' +
                ", lxr='" + lxr + '\'' +
                ", county='" + county + '\'' +
                '}';
    }
}
