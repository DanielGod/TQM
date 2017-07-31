package tqm.bianfeng.com.tqm.network.api;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 王九东 on 2017/6/30.
 */

public interface PayService {
    /**
     * 获取联系方式支付
     * @param orderId
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("pay/dkOrderPay")
    Observable<Map<String,String>> dkOrderPay(@Field("orderId") String orderId, @Field("userId") Integer  userId);

    /**
     * 信贷经理电话展示支付
     * @param payType 01-12个月 02-6个月 03-3个月
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("pay/dhOrderPay")
    Observable<Map<String,String>> dhOrderPay(@Field("payType") String payType, @Field("userId") Integer  userId);

}
