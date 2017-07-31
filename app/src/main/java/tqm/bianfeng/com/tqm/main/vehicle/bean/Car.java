package tqm.bianfeng.com.tqm.main.vehicle.bean;

/**
 * Created by 王九东 on 2017/7/11.
 */

public class Car {

    /**
     * id : 2000404
     * big_ppname : 安驰
     * pin : A
     */

    private Integer id;
    private String big_ppname;
    private String pin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBig_ppname() {
        return big_ppname;
    }

    public void setBig_ppname(String big_ppname) {
        this.big_ppname = big_ppname;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", big_ppname='" + big_ppname + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }
}
