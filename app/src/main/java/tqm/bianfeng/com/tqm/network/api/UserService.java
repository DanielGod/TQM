package tqm.bianfeng.com.tqm.network.api;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;
import tqm.bianfeng.com.tqm.pojo.BankInformItem;
import tqm.bianfeng.com.tqm.pojo.MyAttention;
import tqm.bianfeng.com.tqm.pojo.ResultCode;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithUser;
import tqm.bianfeng.com.tqm.pojo.ResultCodeWithUserHeadImg;
import tqm.bianfeng.com.tqm.pojo.bank.BankActivityItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankFinancItem;
import tqm.bianfeng.com.tqm.pojo.bank.BankLoanItem;

/**
 * Created by johe on 2017/1/5.
 */

public interface UserService {

    /**
     * { //银行资讯//每页显示的记录数(首页传3)
     "informType": Integer 资讯类型: 1-银行资讯;2-热点资讯;3-财富资讯(首页非必需)
     "homeShow": String 是否首页展示:01-是;02-否
     "pageNum": Integer 当前页码
     "pageSize": Integer 每页显示的记录数(首页传3)
     }
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
    Observable<MyAttention> getMyAttention(@Path("userId")int userId);

    /**
     * 获取我的银行贷款关注
     */
    @GET("getMyAttentionOfBankLoan/{userId}")
    Observable<List<BankLoanItem>> getMyAttentionOfBankLoan(@Path("userId")int userId);

    /**
     * 获取我的银行理财关注
     */
    @GET("getMyAttentionOfBankFinanc/{userId}")
    Observable<List<BankFinancItem>> getMyAttentionOfBankFinanc(@Path("userId")int userId);

    /**
     * 获取我的银行活动关注
     */
    @GET("getMyAttentionOfBankActivity/{userId}")
    Observable<List<BankActivityItem>> getMyAttentionOfBankActivity(@Path("userId")int userId);

    /**
     * 注册或登录
     */
    @FormUrlEncoded
    @POST("register")
    Observable<ResultCodeWithUser> register(@Field("userPhone") String userPhone);

    /**
     * 短信验证
     * @return
     */
    @FormUrlEncoded
    @POST("shortMsg/identifyCode")
    Observable<String> shortMsg(@Field("phone") String  phone);


    /**
     * 首页轮播图
     */
    @GET("getImages/{imageType}")
    Observable<List<String>> getImages(@Path("imageType")String imageType);


    /**
     * http://ip/app/attention关注
     * "objectId":Integer 文章ID
     "attentionType":String 01-活动;02-理财;02-贷款
     "userId":Integer 用户Id
     "attentionStatus":String  关注状态:01-关注;02-取消
     */
    @FormUrlEncoded
    @POST("attention")
    Observable<ResultCode> attention(@Field("objectId") Integer objectId,@Field("attentionType") String attentionType,
                                     @Field("userId") Integer userId,@Field("attentionStatus") String attentionStatus);

    /**
     * 判断用户是否已关注
     */
    @FormUrlEncoded
    @POST("isAttention")
    Observable<String> isAttention(@Field("objectId") Integer objectId,@Field("attentionType") String attentionType,
                                     @Field("userId") Integer userId);
}
