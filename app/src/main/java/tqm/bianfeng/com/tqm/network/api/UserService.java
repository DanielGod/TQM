package tqm.bianfeng.com.tqm.network.api;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;
import tqm.bianfeng.com.tqm.main.vehicle.bean.VehicleSubmitResultCode;
import tqm.bianfeng.com.tqm.pojo.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.Card;
import tqm.bianfeng.com.tqm.pojo.DksqVO;
import tqm.bianfeng.com.tqm.pojo.LawyerItem;
import tqm.bianfeng.com.tqm.pojo.MyAttention;
import tqm.bianfeng.com.tqm.pojo.PayTrans;
import tqm.bianfeng.com.tqm.pojo.ReleaseActivityItem;
import tqm.bianfeng.com.tqm.pojo.ReleaseLoanItem;
import tqm.bianfeng.com.tqm.pojo.Reports;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithCompanyFile;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithUser;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithUserHeadImg;
import tqm.bianfeng.com.tqm.pojo.UserActionNum;
import tqm.bianfeng.com.tqm.pojo.UserPointA;
import tqm.bianfeng.com.tqm.pojo.YwBankActivity;
import tqm.bianfeng.com.tqm.pojo.YwDksq;
import tqm.bianfeng.com.tqm.pojo.YwRzsq;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankListItems;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;
import tqm.bianfeng.com.tqm.pojo.result.ResultCodeWithImgPathList;
import tqm.bianfeng.com.tqm.pojo.result.ResultWithLink;
import tqm.bianfeng.com.tqm.update.UpdateMsg;

/**
 * Created by johe on 2017/1/5.
 */

public interface UserService {

    /**
     * { //银行资讯//每页显示的记录数(首页传3)
     * "informType": Integer 资讯类型: 1-银行资讯;2-热点资讯;3-财富资讯(首页非必需)
     * "homeShow": String 是否首页展示:01-是;02-否
     * "pageNum": Integer 当前页码
     * "pageSize": Integer 每页显示的记录数(首页传3)
     * }
     */
    @FormUrlEncoded
    @POST("getBankInformItem")
    Observable<List<BankInformItem>> getBankInformItem(@Field("informType") Integer informType,
                                                       @Field("homeShow") String homeShow,
                                                       @Field("pageNum") Integer pageNum,
                                                       @Field("pageSize") Integer pageSize);

    /**
     * //用户头像上传
     */
    @Multipart
    @POST("uploadAvatars")
    Observable<ResultCodeWithUserHeadImg> uploadAvatars(
            @Part MultipartBody.Part file,
            @Part("userId") Integer userId
    );

    /**
     * 获取我的关注情况
     */
    @GET("getMyAttention/{userId}")
    Observable<MyAttention> getMyAttention(@Path("userId") int userId);

    /**
     * 获取我的银行贷款关注
     */
    @GET("getMyAttentionOfBankLoan/{userId}")
    Observable<List<BankLoanItem>> getMyAttentionOfBankLoan(@Path("userId") int userId);

    /**
     * 获取我的银行理财关注
     */
    @GET("getMyAttentionOfBankFinanc/{userId}")
    Observable<List<BankFinancItem>> getMyAttentionOfBankFinanc(@Path("userId") int userId);

    /**
     * 获取我的银行活动关注
     */
    @GET("getMyAttentionOfBankActivity/{userId}")
    Observable<BankListItems<BankActivityItem>> getMyAttentionOfBankActivity(@Path("userId") int userId);

    /**
     * 注册或登录
     */
    @FormUrlEncoded
    @POST("register")
    Observable<ResultCodeWithUser> register(@Field("userPhone") String userPhone,@Field("channelNo") String channelNo);

    /**
     * 短信验证
     *
     * @return
     */
    @FormUrlEncoded
    @POST("shortMsg/identifyCode")
    Observable<String> shortMsg(@Field("phone") String phone);


    /**
     * 首页轮播图
     */
    @GET("getImages/{imageType}")
    Observable<List<String>> getImages(@Path("imageType") String imageType);


    /**
     * http://ip/app/attention关注
     * "objectId":Integer 文章ID
     * "attentionType":String 01-活动;02-理财;02-贷款
     * "userId":Integer 用户Id
     * "attentionStatus":String  关注状态:01-关注;02-取消
     */
    @FormUrlEncoded
    @POST("attention")
    Observable<ResultCode> attention(@Field("objectId") Integer objectId, @Field("attentionType") String attentionType,
                                     @Field("userId") Integer userId, @Field("attentionStatus") String attentionStatus);

    /**
     * 判断用户是否已关注
     */
    @FormUrlEncoded
    @POST("isAttention")
    Observable<String> isAttention(@Field("objectId") Integer objectId, @Field("attentionType") String attentionType,
                                   @Field("userId") Integer userId);

    /**
     * 版本更新
     */
    @GET("getVersion")
    Observable<UpdateMsg> getVersion();

    /**
     * 入驻申请执照上传
     */
    @Multipart
    @POST("uploadCompanyFile")
    Observable<ResultCodeWithCompanyFile> uploadCompanyFile(
            @Part MultipartBody.Part file
    );

    /**
     * 入驻申请保存
     * "proposer":String 申请人
     * "companyName":String 公司名称
     * "businessLicense":String 营业执照
     * "contact":String 联系方式
     */
    @FormUrlEncoded
    @POST("saveEnter")
    Observable<ResultCode> saveEnter(@Field("proposer") String proposer, @Field("companyName") String companyName,
                                     @Field("businessLicense") String businessLicense, @Field("contact") String contact);

    /**
     * 我的关注/浏览记录
     * 01活动 02理财 03 贷款 04资讯 05律师
     */
    @GET("{type}/01/{userId}")
    Observable<List<BankActivityItem>> getMyAttentionItem01(@Path("type") String type, @Path("userId") int userId);

    @GET("{type}/02/{userId}")
    Observable<List<BankFinancItem>> getMyAttentionItem02(@Path("type") String type, @Path("userId") int userId);

    @GET("{type}/03/{userId}")
    Observable<List<BankLoanItem>> getMyAttentionItem03(@Path("type") String type, @Path("userId") int userId);

    @GET("{type}/04/{userId}")
    Observable<List<BankInformItem>> getMyAttentionItem04(@Path("type") String type, @Path("userId") int userId);

    @GET("{type}/05/{userId}")
    Observable<List<LawyerItem>> getMyAttentionItem05(@Path("type") String type, @Path("userId") int userId);

    /**
     * 浏览记录
     * 01活动 02理财 03 贷款 04资讯 05律师
     */
    @GET("history/{type}/01/{userId}")
    Observable<List<BankActivityItem>> getBrowseHistoryItem01(@Path("type") String type, @Path("userId") int userId);

    @GET("history/{type}/02/{userId}")
    Observable<List<BankFinancItem>> getBrowseHistoryItem02(@Path("type") String type, @Path("userId") int userId);

    @GET("history/{type}/03/{userId}")
    Observable<List<BankLoanItem>> getBrowseHistoryItem03(@Path("type") String type, @Path("userId") int userId);

    @GET("history/{type}/04/{userId}")
    Observable<List<BankInformItem>> getBrowseHistoryItem04(@Path("type") String type, @Path("userId") int userId);

    @GET("history/{type}/05/{userId}")
    Observable<List<LawyerItem>> getBrowseHistoryItem05(@Path("type") String type, @Path("userId") int userId);

    /**
     * 清除浏览记录
     */
    @GET("history/deleteHistory/{userId}")
    Observable<ResultCode> deleteHistory(@Path("userId") int userId);


    /**
     * 上传公司三张图片
     */
    @Multipart
    @POST("rzsq/upload")
    Observable<ResultCodeWithImgPathList> uploadCompanyFile(@Part List<MultipartBody.Part> zichifile);

    /**
     * 提交信息 applyEnter
     */
    @FormUrlEncoded
    @POST("rzsq/")
    Observable<ResultCode> save(@Field("rzsq") String rzsq);


    /**
     * 获取入驻申请信息
     */
    @GET("rzsq/{userId}")
    Observable<YwRzsq> getOne(@Path("userId") int userId);

    /**
     * 查看入驻申请当前状态
     * sqr 申请人Id
     */
    @GET("rzsq/shzt/{sqr}")
    Observable<Map<String,String>> getShzt(@Path("sqr") int sqr);

    /**
     * 纠错举报
     */
    @FormUrlEncoded
    @POST("errorReport/")
    Observable<ResultCode> saveErrorReport(@Field("errorReport") String errorReport);

    /**
     * 获取发布的活动
     */
    @GET("activity/{userId}")
    Observable<List<ReleaseActivityItem>> getBankActivityItem(@Path("userId") int userId);

    /**
     * 获取发布的贷款
     */
    @GET("loan/{userId}")
    Observable<List<ReleaseLoanItem>> getReleaseLoanItem(@Path("userId") int userId);

    /**
     * 发布贷款信息删除
     */
    @GET("loan/delete/{loanId}")
    Observable<ResultCode> deleteloanById(@Path("loanId") int loanId);

    /**
     * 发布贷款信息删除
     */
    @GET("activity/delete/{activityId}")
    Observable<ResultCode> deleteactivityById(@Path("activityId") int activityId);

    /**
     * 富文本图片上传
     */
    @Multipart
    @POST("activity/upload")
    Observable<ResultWithLink> upload(
            @Part MultipartBody.Part file
    );

    /**
     * 发布活动文章图片上传
     */
    @Multipart
    @POST("activity/uploadImg")
    Observable<ResultWithLink> uploadImg(
            @Part MultipartBody.Part file
    );

    /**
     * 发布活动
     */
    @FormUrlEncoded
    @POST("activity/save")
    Observable<ResultCode> saveReleaseActivity(@Field("imageUrl") String imageUrl, @Field("activityTitle") String activityTitle,
                                               @Field("institution") String institution, @Field("activityContent") String activityContent,
                                               @Field("createUser") int createUser
    );

    /**
     * 发布活动
     */
    @FormUrlEncoded
    @POST("activity/edit")
    Observable<ResultCode> editReleaseActivity(@Field("activityId") int activityId,@Field("imageUrl") String imageUrl, @Field("activityTitle") String activityTitle,
                                               @Field("institution") String institution, @Field("activityContent") String activityContent,
                                               @Field("createUser") int createUser
    );

    /**
     * 获取活动
     */
    @GET("activity/{activityId}/{userId}")
    Observable<YwBankActivity> getReleaseActivity(@Path("activityId") int activityId, @Path("userId") int userId);


    /**
     * 获取用户收藏关注等数量
     */
    @GET("getStatistics/{userId}")
    Observable<List<UserActionNum>> getStatistics(@Path("userId") int userId);

    /**
     *获取订单对象
     * @param viewType 01-待领取列表 02-已领取列表 03-显示数量
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("dksq/getItem")
    Observable<List<YwDksq>> getItem(@Field("viewType") String  viewType, @Field("userId") Integer  userId);

    /**
     *获取订单详情
     * @param dksqId
     * @param viewType
     * @return
     */
    @GET("dksq/view/{userId}/{dksqId}/{viewType}")
    Observable<DksqVO> getDksqVO(@Path("userId") Integer userId,@Path("dksqId") String dksqId, @Path("viewType") String viewType);

    /**
     * 获取详情是否免费
     * @param userPhone
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("dksq/isFree")
    Observable<String> isFree(@Field("userPhone") String userPhone, @Field("userId") Integer  userId);

    /**
     * 付费
     * @param dksqId
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("dksq/saveLqmx")
    Observable<ResultCode> saveLqmx(@Field("dksqId") String dksqId, @Field("userId") Integer  userId);

    /**
     * 车辆评估提交
     * @param carAssess
     * @return
     */
    @FormUrlEncoded
    @POST("car/")
    Observable<VehicleSubmitResultCode> saveVehicleInformation(@Field("carAssess") String carAssess);

    /**
     * 名片上传
     * @param file
     * @param userId
     * @return
     */
    @Multipart
    @POST("card/upload")
    Observable<ResultCode> saveVehicleCollection(@Part MultipartBody.Part file,@Part("userId") Integer userId);

    /**
     * 获取用户积分列表
     * @param userId
     * @return
     */
    @GET("getUserPointItem/{userId}")
    Observable<List<UserPointA>> getUserPointItem(@Path("userId") Integer userId);

    /**
     * 用户实现签到
     * @param userId
     * @return
     */
    @GET("sign/{userId}")
    Observable<ResultCode> sign(@Path("userId") Integer userId);

    /**
     * 名片收集
     * @param userId
     * @return
     */
    @GET("card/{userId}")
    Observable<List<Card>> getCard(@Path("userId") Integer userId);

    /**
     * 纠错记录
     * @param userId
     * @return
     */
    @GET("getReports/{userId}")
    Observable<List<Reports>> getReports(@Path("userId") Integer userId);

    /**
     * 交易接口
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("pay/getPayTrans")
    Observable<List<PayTrans>> getPayTrans(@Field("userId")  Integer userId);

    /**
     * 上传通讯录
     * @param addressBook
     * @return
     */
    @FormUrlEncoded
    @POST("saveAddressBook")
    Observable<ResultCode> saveAddressBook(@Field("addressBook")  String addressBook);


}
