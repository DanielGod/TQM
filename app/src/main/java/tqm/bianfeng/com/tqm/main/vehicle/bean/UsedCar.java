package tqm.bianfeng.com.tqm.main.vehicle.bean;

import java.util.List;

/**
 * Created by 王九东 on 2017/7/11.
 */

public class UsedCar {
     String pinName;
     List<Car> carList;

     public String getPinName() {
          return pinName;
     }

     public void setPinName(String pinName) {
          this.pinName = pinName;
     }

     public List<Car> getCarList() {
          return carList;
     }

     public void setCarList(List<Car> carList) {
          this.carList = carList;
     }

     @Override
     public String toString() {
          return "UsedCar{" +
                  "pinName='" + pinName + '\'' +
                  ", carList=" + carList +
                  '}';
     }
}
