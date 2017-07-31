package tqm.bianfeng.com.tqm.main.vehicle.adapter;

/**
 * Created by 王九东 on 2017/7/12.
 */
public class VehicleChiocelEvent {
    Integer id;
    String action;
    public VehicleChiocelEvent(String action,Integer id) {
        this.id = id;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
