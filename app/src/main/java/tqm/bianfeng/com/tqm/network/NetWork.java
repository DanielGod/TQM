package tqm.bianfeng.com.tqm.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tqm.bianfeng.com.tqm.network.api.BankService;
import tqm.bianfeng.com.tqm.network.api.InstitutionService;
import tqm.bianfeng.com.tqm.network.api.LawService;
import tqm.bianfeng.com.tqm.network.api.UpdateService;
import tqm.bianfeng.com.tqm.network.api.UserService;
import tqm.bianfeng.com.tqm.update.DownloadProgressInterceptor;
import tqm.bianfeng.com.tqm.update.DownloadProgressListener;


/**
 * Created by wjy on 16/9/1.
 */
public class NetWork {

//    public static final String LOAD="http://tqmadmin.dijiaapp.com/tqm-web";
//    public static final String LOAD="http://139.224.17.235:8080/tqm-web";
//    public static final String LOAD="http://211.149.235.17:8083/tqm-web";//测试
//     public static final String LOAD="http://139.224.17.235:8080/tqm-web";//生产
        public static final String LOAD="http://api.tongqianmao.cn";

    //public static final String LOAD="http://tqmadmin.dijiaapp.com";

    private static UserService userService;
    private static BankService bankService;
    private static UpdateService updateService;
    private static LawService lawService;
    private static InstitutionService institutionService;
    public static InstitutionService getInstitutionService() {
        if (institutionService == null) {
            Retrofit retrofit = getRetrofit();
            institutionService = retrofit.create(InstitutionService.class);
        }
        return institutionService;
    }
    public static LawService getLawService() {
        if (lawService == null) {
            Retrofit retrofit = getRetrofit();
            lawService = retrofit.create(LawService.class);
        }
        return lawService;
    }
    public static UserService getUserService() {
        if (userService == null) {
            Retrofit retrofit = getRetrofit();
            userService = retrofit.create(UserService.class);
        }
        return userService;
    }

    public static BankService getBankService() {
        if (bankService == null) {
            Retrofit retrofit = getRetrofit();
            bankService = retrofit.create(BankService.class);
        }
        return bankService;
    }
    public static UpdateService getUpdateService(String url,DownloadProgressListener listener) {
        if (updateService == null) {
            Retrofit retrofit = getRetrofitDownload(url,listener);
            updateService = retrofit.create(UpdateService.class);
        }
        return updateService;
    }
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static Retrofit getRetrofit() {
        String url="";
            url=LOAD+"/app/";

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }
    private static Retrofit getRetrofitDownload(String url,DownloadProgressListener listener) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new DownloadProgressInterceptor(listener))
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }
}
