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
import tqm.bianfeng.com.tqm.network.api.JuHeService;
import tqm.bianfeng.com.tqm.network.api.LawService;
import tqm.bianfeng.com.tqm.network.api.PayService;
import tqm.bianfeng.com.tqm.network.api.UpdateService;
import tqm.bianfeng.com.tqm.network.api.UserService;
import tqm.bianfeng.com.tqm.update.DownloadProgressInterceptor;
import tqm.bianfeng.com.tqm.update.DownloadProgressListener;


/**
 * Created by wjy on 16/9/1.
 */
public class NetWork {
    // TODO: 2017/8/4 地址
//    public static final String LOAD="http://tqmadmin.dijiaapp.com/tqm-web";
//    public static final String LOAD="http://139.224.17.235:8080/test";
    public static final String LOAD="http://211.149.235.17:8083/tqm-web";//测试
//     public static final String LOAD="http://139.224.17.235:8091/tqm-web";//生产
//    public static final String LOAD="http://api.tongqianmao.cn";//正式环境
//    public static final String LOAD="http://tqmadmin.dijiaapp.com";//测试环境
    private static UserService userService;
    private static BankService bankService;
    private static UpdateService updateService;
    private static LawService lawService;
    private static PayService payService;
    private static InstitutionService institutionService;
    private static JuHeService juHeService;

    public static JuHeService getJuHeService() {
        if (juHeService == null) {
            Retrofit retrofit = getJuHeRetrofit();
            juHeService = retrofit.create(JuHeService.class);
        }
        return juHeService;
    }

    public static InstitutionService getInstitutionService() {
        if (institutionService == null) {
            Retrofit retrofit = getRetrofit();
            institutionService = retrofit.create(InstitutionService.class);
        }
        return institutionService;
    }
    public static PayService getPayService() {
        if (payService == null) {
            Retrofit retrofit = getRetrofit();
            payService = retrofit.create(PayService.class);
        }
        return payService;
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

    private static Retrofit getJuHeRetrofit() {
        String url="http://v.juhe.cn/usedcar/";
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }

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
