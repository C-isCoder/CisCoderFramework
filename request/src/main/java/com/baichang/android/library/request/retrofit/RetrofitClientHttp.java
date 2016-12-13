package com.baichang.android.library.request.retrofit;


import com.baichang.android.library.comment.ConfigurationImpl;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iscod.
 * Time:2016/6/21-9:50.
 */
public class RetrofitClientHttp {
    private static RetrofitClientHttp INSTANCE;
    private Retrofit retrofit;
    private static String BaseUrl = ConfigurationImpl.get().getApiDefaultHost();

    private RetrofitClientHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //cache目录
        File cacheFile = new File(ConfigurationImpl.get().getAppContext().getCacheDir(), "netWorkCache");
        builder.cache(new Cache(cacheFile, 1024 * 1024 * 50));//50MB
        //自定义请求拦截器
        RequestInterceptor interceptor = new RequestInterceptor();
        //设置头
        builder.addInterceptor(interceptor);
        //官方请求拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        //设置超时
        builder.connectTimeout(8, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(false);
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(ResponseConverterFactory.create(GsonConverterFactory.create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    /**
     * 默认的BaseUrl = APIConstant.BASE_URL
     *
     * @return
     */
    public static RetrofitClientHttp getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitClientHttp();
        }
        return INSTANCE;
    }

    /**
     * @param baseUrl
     * @return
     */
    public static RetrofitClientHttp getInstance(String baseUrl) {
        BaseUrl = baseUrl;
        if (INSTANCE == null) {
            INSTANCE = new RetrofitClientHttp();
        }
        return INSTANCE;
    }

    /**
     * 自定义Service
     *
     * @param service 传入自定义的Service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }


}
