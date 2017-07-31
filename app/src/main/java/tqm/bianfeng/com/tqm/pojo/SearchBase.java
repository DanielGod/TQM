package tqm.bianfeng.com.tqm.pojo;

/**
 * Created by 王九东 on 2017/7/6.
 */

public class SearchBase {

    private String homeShow; //是否首页展示 //非空01-首页;02-非首页

    private Integer pageNum;//非空

    private Integer pageSize;//非空

    private String city; //所在城市//非空

    private LoanSearch loanSearch; //非必须条件类

    public LoanSearch getLoanSearch() {
        return loanSearch;
    }

    public void setLoanSearch(LoanSearch loanSearch) {
        this.loanSearch = loanSearch;
    }

    public String getHomeShow() {
        return homeShow;
    }

    public void setHomeShow(String homeShow) {
        this.homeShow = homeShow;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "SearchBase{" +
                "homeShow='" + homeShow + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", city='" + city + '\'' +
                ", loanSearch=" + loanSearch +
                '}';
    }
}
