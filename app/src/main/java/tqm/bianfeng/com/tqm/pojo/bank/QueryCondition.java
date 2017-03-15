package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/3/14.
 */

public class QueryCondition {
    private String queryName;
    private String queryCode;
    private String queryApi;
    private String queryType;

    @Override
    public String toString() {
        return "QueryCondition{" +
                "queryName='" + queryName + '\'' +
                ", queryCode='" + queryCode + '\'' +
                ", queryApi='" + queryApi + '\'' +
                ", queryType='" + queryType + '\'' +
                '}';
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryCode() {
        return queryCode;
    }

    public void setQueryCode(String queryCode) {
        this.queryCode = queryCode;
    }

    public String getQueryApi() {
        return queryApi;
    }

    public void setQueryApi(String queryApi) {
        this.queryApi = queryApi;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
