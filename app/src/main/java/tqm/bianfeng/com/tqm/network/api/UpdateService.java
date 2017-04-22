package tqm.bianfeng.com.tqm.network.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by johe on 2016/12/29.
 */

public interface UpdateService {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
