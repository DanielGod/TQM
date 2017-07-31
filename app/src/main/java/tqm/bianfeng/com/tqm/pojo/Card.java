package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by 王九东 on 2017/7/16.
 */

public class Card {

    /**
     * id : 7
     * cardUrl : /upload/card/20170714094705283.jpg
     * statusCode : 00
     * createDate : 1499996825000
     */

    private int id;
    private String remark;
    private String cardUrl;
    private String statusCode;
    private long createDate;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", remark='" + remark + '\'' +
                ", cardUrl='" + cardUrl + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
