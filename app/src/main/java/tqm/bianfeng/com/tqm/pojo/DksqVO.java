package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by 王九东 on 2017/6/29.
 */

public class DksqVO extends YwDksq {
    private String lqfs; //领取方式:01-免费;02-付费

    private String zfje;//支付金额

    private String zfzt; //支付状态:00-未支付;01-已支付

    private String zfrq; //支付日期

    private Long lqrq;//领取日期


    public String getLqfs() {
        return lqfs;
    }

    public void setLqfs(String lqfs) {
        this.lqfs = lqfs;
    }

    public String getZfje() {
        return zfje;
    }

    public void setZfje(String zfje) {
        this.zfje = zfje;
    }

    public String getZfzt() {
        return zfzt;
    }

    public void setZfzt(String zfzt) {
        this.zfzt = zfzt;
    }

    public String getZfrq() {
        return zfrq;
    }

    public void setZfrq(String zfrq) {
        this.zfrq = zfrq;
    }

    public Long getLqrq() {
        return lqrq;
    }

    public void setLqrq(Long lqrq) {
        this.lqrq = lqrq;
    }

    @Override
    public String toString() {
        return "DksqVO{" +
                "lqfs='" + lqfs + '\'' +
                ", zfje='" + zfje + '\'' +
                ", zfzt='" + zfzt + '\'' +
                ", zfrq='" + zfrq + '\'' +
                ", lqrq='" + lqrq + '\'' +
                '}';
    }
}
