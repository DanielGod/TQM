package tqm.bianfeng.com.tqm.main.vehicle.bean;

import java.util.List;

/**
 * Created by 王九东 on 2017/7/10.
 */

public class ProvinceBean {
    /**
     * citys : [{"cityID":"1","cityName":"北京市","pinyin":"beijing","proID":"1"}]
     * pin : B
     * pinyin : beijing
     * proID : 1
     * proName : 北京市
     */

    private String pin;
    private String pinyin;
    private Integer proID;
    private String proName;
    private List<CitysBean> citys;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Integer getProID() {
        return proID;
    }

    public void setProID(Integer proID) {
        this.proID = proID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public List<CitysBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CitysBean> citys) {
        this.citys = citys;
    }
}
