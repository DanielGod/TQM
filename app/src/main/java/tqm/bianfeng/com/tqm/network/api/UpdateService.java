package tqm.bianfeng.com.tqm.network.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;
import tqm.bianfeng.com.tqm.update.UpdateMsg;

/**
 * Created by johe on 2016/12/29.
 */

public interface UpdateService {

    /**
     * 版本更新
     */
    @GET("getVersion")
    Observable<UpdateMsg> getVersion();

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
