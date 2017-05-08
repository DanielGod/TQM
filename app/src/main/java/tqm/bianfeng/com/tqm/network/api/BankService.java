package tqm.bianfeng.com.tqm.network.api;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankDotItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Institution;
import tqm.bianfeng.com.tqm.pojo.bank.ProductType;
import tqm.bianfeng.com.tqm.pojo.bank.QueryCondition;
import tqm.bianfeng.com.tqm.pojo.bank.RiskGrade;

/**
 * Created by Daniel on 2017/1/23.
 */

public interface BankService {



    /**
     * 获取金融机构
     * @return
     */
    @GET("getInstitutions")
    Observable<List<Institution>> getInstitutions();

    /**
     * 获取风险等级
     * @return
     */
    @GET("getRiskGrades")
    Observable<List<RiskGrade>> getRiskGrades();

    /**
     * 获取产品类型
     * @return
     */
    @GET("getProductTypes")
    Observable<List<ProductType>> getProductTypes();

    /**
     * 获取查询条件
     * @param module
     * @return
     */
    @GET("queryConditions/{module}")
    Observable<List<QueryCondition>> queryConditions(@Path("module") String module);


    /**
     * 获取银行资讯
     * @return
     */
    @FormUrlEncoded
    @POST("getBankInformItem")
    Observable<List<BankInformItem>> getBankInformItem(@Field("informType") Integer informType,
                                                       @Field("homeShow") String homeShow,
                                                       @Field("pageNum") Integer pageNum,
                                                       @Field("pageSize") Integer pageSize);

    /**
     * 获取银行理财
     * @return
     */
    @FormUrlEncoded
    @POST("getBankFinancItem")
    Observable<List<BankFinancItem>> getBankFinancItem(@Field("queryParams") String queryParams,
                                                       @Field("homeShow") String homeShow,
                                                       @Field("pageNum") Integer pageNum,
                                                       @Field("pageSize") Integer pageSize);


    /**
     * 获取银行贷款
     * @return
     */
    @FormUrlEncoded
    @POST("getBankLoanItem")
    Observable<List<BankLoanItem>> getBankLoanItem(@Field("queryParams") String queryParams,
                                                   @Field("homeShow") String homeShow,
                                                   @Field("pageNum") Integer pageNum,
                                                   @Field("pageSize") Integer pageSize);


    /**
     * 获取银行活动
     * @return
     */
    @FormUrlEncoded
    @POST("getBankActivityItem")
    Observable<List<BankActivityItem>> getBankActivityItem(@Field("homeShow") String homeShow,
                                                           @Field("pageNum") Integer pageNum,
                                                           @Field("pageSize") Integer pageSize);

    /**
     * 获取网点列表
     * @return
     */
    @FormUrlEncoded
    @POST("dot/getBankDotItem")
    Observable<List<BankDotItem>> getBankDotItem(@Field("uids") String uids);







}
