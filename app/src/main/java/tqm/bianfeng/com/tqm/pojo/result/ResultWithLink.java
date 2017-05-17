package tqm.bianfeng.com.tqm.pojo.result;

import tqm.bianfeng.com.tqm.pojo.ResultCode;

/**
 * Created by johe on 2017/5/12.
 */

public class ResultWithLink extends ResultCode{

    String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "ResultWithLink{" +
                "link='" + link + '\'' +
                '}';
    }
}
