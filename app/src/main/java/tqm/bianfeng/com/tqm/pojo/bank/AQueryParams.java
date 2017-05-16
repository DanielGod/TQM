package tqm.bianfeng.com.tqm.pojo.bank;

/**
 * Created by Daniel on 2017/5/12.
 */

public class AQueryParams {
    String order;
    String sort;
    String institution;

    public AQueryParams(String order, String sort, String institution) {
        this.order = order;
        this.sort = sort;
        this.institution = institution;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
