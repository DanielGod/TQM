package tqm.bianfeng.com.tqm.network.api;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import tqm.bianfeng.com.tqm.pojo.CreditManager;
import tqm.bianfeng.com.tqm.pojo.InstitutionItem;
import tqm.bianfeng.com.tqm.pojo.LawFirmOrInstitutionDetail;
import tqm.bianfeng.com.tqm.pojo.ResultCode;

/**
 * Created by johe on 2017/4/11.
 */

public interface InstitutionService {

    /**
     * 获取机构列表
     * " institutionType": String 机构类别 01-律师所02-金融
     "pageNum": Integer 当前页码
     "pageSize": Integer 每页显示的记录数(首页传3)

     */
    @FormUrlEncoded
    @POST("instit/getInstitutionItem")
    Observable<List<InstitutionItem>> getInstitutionItem(@Field("institutionType") String institutionType,
                                                         @Field("userId") int userId,
                                                         @Field("pageNum") Integer pageNum,
                                                         @Field("pageSize") Integer pageSize,
                                                         @Field("city") String city);

    /**
     * 获取律师事务所详情
     */
    @GET("instit/getLawFirmDetail/{institutionId}/{userId}")
    Observable<LawFirmOrInstitutionDetail> getLawFirmDetail(@Path("institutionId") int institutionId,@Path("userId") String userId);

    /**
     * 获取机构
     *
     */
    @GET("instit/getFinanceDetail/{institutionId}/{userId}")
    Observable<LawFirmOrInstitutionDetail> getFinanceDetail(@Path("institutionId") int institutionId,@Path("userId") String userId);
    /**
     * 获取信贷经理信息
     */

    @GET("instit/getCreditManagers")
    Observable<List<CreditManager>> getCreditManagers(@Query("institutionId") int institutionId, @Query("city") String city);
    /**
     * 收藏或取消收藏
     */
    @FormUrlEncoded
    @POST("collect/saveOrUpdate")
    Observable<ResultCode> saveOrUpdate(@Field("objectId") Integer objectId,
                                        @Field("collectType") String collectType,
                                        @Field("userId") String userId,
                                        @Field("collectStatus") String collectStatus

    );

    /**
     * 获取收藏列表
     */
    @FormUrlEncoded
    @POST("collect/getInstitutionItem")
    Observable<List<InstitutionItem>> getCollectInstitutionItem(@Field("collectType") String collectType,
                                                         @Field("userId") Integer userId
                                                         );

    /**
     * 搜索机构
     */
    @FormUrlEncoded
    @POST("instit/search")
    Observable<List<InstitutionItem>> searchInstitutionItem(@Field("search") String search,
                                                            @Field("userId") int userId,
                                                            @Field("institutionType") String institutionType,
                                                         @Field("pageNum") Integer pageNum,
                                                         @Field("pageSize") Integer pageSize,
                                                            @Field("city") String city);

    /**
     * apk安装记录保存成功！
     * @param userId
     * @param packageName
     * @return
     */
    @FormUrlEncoded
    @POST("install")
    Observable<Void> install(@Field("userId") int userId,@Field("packageName") String packageName);
}
