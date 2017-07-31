package tqm.bianfeng.com.tqm.main.vehicle.bean;

/**
 * Created by 王九东 on 2017/7/12.
 */

public class XilieBean {
    /**
     * xlid : 20000054
     * xlname : 奥迪A4L
     */

    private Integer xlid;
    private String xlname;

    public Integer getXlid() {
        return xlid;
    }

    public void setXlid(Integer xlid) {
        this.xlid = xlid;
    }

    public String getXlname() {
        return xlname;
    }

    public void setXlname(String xlname) {
        this.xlname = xlname;
    }

    @Override
    public String toString() {
        return "XilieBean{" +
                "xlid='" + xlid + '\'' +
                ", xlname='" + xlname + '\'' +
                '}';
    }
}
