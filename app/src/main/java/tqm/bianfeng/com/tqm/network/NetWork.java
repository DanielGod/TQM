package tqm.bianfeng.com.tqm.network;

import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tqm.bianfeng.com.tqm.network.api.BankService;
import tqm.bianfeng.com.tqm.network.api.UserService;


/**
 * Created by wjy on 16/9/1.
 */
public class NetWork {
    private static UserService userService;
    private static BankService bankService;
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


    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static Retrofit getRetrofit() {
        String url="";
            url="http://211.149.235.17:8080/tqm-web/app/";

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }
}
