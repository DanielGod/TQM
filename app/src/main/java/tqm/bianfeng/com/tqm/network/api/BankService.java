package tqm.bianfeng.com.tqm.network.api;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.YwDksq;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankDotItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.bank.Institution;
import tqm.bianfeng.com.tqm.pojo.bank.LoanType;
import tqm.bianfeng.com.tqm.pojo.bank.ProductType;
import tqm.bianfeng.com.tqm.pojo.bank.QueryCondition;
import tqm.bianfeng.com.tqm.pojo.bank.RiskGrade;

/**
 * Created by Daniel on 2017/1/23.
 */

public interface BankService {

    /**
     * 保存渠道号
     *
     * @return
     */
    @FormUrlEncoded
    @POST("saveChannel")
    Observable<ResultCode> saveChannel(@Field("channelNo") String channelNo,
                                       @Field("deviceNo") String deviceNo);


    /**
     * 获取金融机构
     *
     * @return
     */
    @GET("getInstitutions")
    Observable<List<Institution>> getInstitutions();

    /**
     * 获取风险等级
     *
     * @return
     */
    @GET("getRiskGrades")
    Observable<List<RiskGrade>> getRiskGrades();

    /**
     * 获取产品类型
     *
     * @return
     */
    @GET("getProductTypes")
    Observable<List<ProductType>> getProductTypes();

    /**
     * 获取贷款类型
     *
     * @return
     */
    @GET("getLoanTypes")
    Observable<List<LoanType>> getLoanTypes();

    /**
     * 获取查询条件
     *
     * @param module
     * @return
     */
    @GET("queryConditions/{module}")
    Observable<List<QueryCondition>> queryConditions(@Path("module") String module);


    /**
     * 获取银行资讯
     *
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
     *
     * @return
     */
    @FormUrlEncoded
    @POST("getBankFinancItem")
    Observable<BankListItems<BankFinancItem>> getBankFinancItem(@Field("queryParams") String queryParams);


    /**
     * 获取银行贷款
     *
     * @return
     */
    @FormUrlEncoded
    @POST("getBankLoanItem")
    Observable<BankListItems<BankLoanItem>> getBankLoanItem(@Field("queryParams") String queryParams);


    /**
     * 获取银行活动
     *
     * @return
     */
    @FormUrlEncoded
    @POST("getBankActivityItem")
    Observable<BankListItems<BankActivityItem>> getBankActivityItem(@Field("search") String search,
                                                                    @Field("queryParams") String queryParams,
                                                                    @Field("homeShow") String homeShow,
                                                                    @Field("pageNum") Integer pageNum,
                                                                    @Field("pageSize") Integer pageSize,
                                                                    @Field("city") String city,
                                                                    @Field("activityType") Integer activityType);


    /**
     * 获取网点列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("dot/getBankDotItem")
    Observable<List<BankDotItem>> getBankDotItem(@Field("uids") String uids);

    /**
     * 贷款申请保存
     *
     * @return
     */
    @FormUrlEncoded
    @POST("dksq/")
    Observable<ResultCode> saveOrUpdate(@Field("dksqInfo") String dksqInfo);

    /**
     * 获取单条贷款申请信息
     *
     * @return
     */
    @GET("dksq/{sqrId}")
    Observable<YwDksq> getOne(@Path("sqrId") Integer sqrId);



}
