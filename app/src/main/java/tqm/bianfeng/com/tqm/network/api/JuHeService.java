package tqm.bianfeng.com.tqm.network.api;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import tqm.bianfeng.com.tqm.main.vehicle.bean.VehicleModelRequest;
import tqm.bianfeng.com.tqm.main.vehicle.bean.VehicleRequest;

/**
 * Created by 王九东 on 2017/7/11.
 */

public interface JuHeService {
    /**
     * 获取车系
     * @param key
     * @param brand
     * @return
     */
    @FormUrlEncoded
    @POST("series")
    Observable<VehicleRequest> series (@Field("key") String key ,@Field("brand") int brand );

    /**
     * 获取车型
     * @param key
     * @param series
     * @return
     */
    @FormUrlEncoded
    @POST("car")
    Observable<VehicleModelRequest> car (@Field("key") String key , @Field("series") int series );

}
