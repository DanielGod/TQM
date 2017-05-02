package tqm.bianfeng.com.tqm.network.api;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
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
                                                         @Field("pageNum") Integer pageNum,
                                                         @Field("pageSize") Integer pageSize);

    /**
     * 获取律师事务所详情
     *
     */
    @GET("instit/getLawFirmDetail/{institutionId}")
    Observable<LawFirmOrInstitutionDetail> getLawFirmDetail(@Path("institutionId") int institutionId);

    /**
     * 获取律师事务所详情
     *
     */
    @GET("instit/getFinanceDetail/{institutionId}")
    Observable<LawFirmOrInstitutionDetail> getFinanceDetail(@Path("institutionId") int institutionId);

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
}
