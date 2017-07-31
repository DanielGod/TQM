package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by 王九东 on 2017/7/16.
 */

public class PayTrans {

    /**
     * jynr : 业务电话展示
     * sxrq : 2017-07-27 17:28:05
     * jyje : 20
     * jylx : 02
     * jyrq : 1499333281000
     */

    private String jynr;
    private String sxrq;
    private String jyje;
    private String jylx;
    private long jyrq;

    public String getJynr() {
        return jynr;
    }

    public void setJynr(String jynr) {
        this.jynr = jynr;
    }

    public String getSxrq() {
        return sxrq;
    }

    public void setSxrq(String sxrq) {
        this.sxrq = sxrq;
    }

    public String getJyje() {
        return jyje;
    }

    public void setJyje(String jyje) {
        this.jyje = jyje;
    }

    public String getJylx() {
        return jylx;
    }

    public void setJylx(String jylx) {
        this.jylx = jylx;
    }

    public long getJyrq() {
        return jyrq;
    }

    public void setJyrq(long jyrq) {
        this.jyrq = jyrq;
    }

    @Override
    public String toString() {
        return "PayTrans{" +
                "jynr='" + jynr + '\'' +
                ", sxrq='" + sxrq + '\'' +
                ", jyje=" + jyje +
                ", jylx='" + jylx + '\'' +
                ", jyrq=" + jyrq +
                '}';
    }
}
