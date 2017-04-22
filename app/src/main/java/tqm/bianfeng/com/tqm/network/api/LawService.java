package tqm.bianfeng.com.tqm.network.api;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;
import tqm.bianfeng.com.tqm.pojo.LawyerItem;

/**
 * Created by johe on 2017/3/30.
 */

public interface LawService {

    /**
     * 获取律师列表
     * @return
     */
    @FormUrlEncoded
    @POST("lawyer/getLawyerItem")
    Observable<List<LawyerItem>> getLawyerItem(@Field("queryParams") String queryParams,
                                               @Field("pageNum") Integer pageNum,
                                               @Field("pageSize") Integer pageSize);

    /**
     * 获取法律领域
     */
    @GET("lawyer/getSpecialFields")
    Observable<List<String>> getSpecialFields();


}
