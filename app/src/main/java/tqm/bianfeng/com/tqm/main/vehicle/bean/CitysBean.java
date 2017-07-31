package tqm.bianfeng.com.tqm.main.vehicle.bean;

/**
 * Created by 王九东 on 2017/7/10.
 */

public class CitysBean {
    /**
     * cityID : 1
     * cityName : 北京市
     * pinyin : beijing
     * proID : 1
     */

    private Integer cityID;
    private String cityName;
    private String pinyin;
    private String proID;

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }
}
