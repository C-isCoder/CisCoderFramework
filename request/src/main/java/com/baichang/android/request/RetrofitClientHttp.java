package com.baichang.android.request;


import com.baichang.android.common.ConfigurationImpl;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
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
  private Retrofit newUrlRetrofit;
  private static String BaseUrl = ConfigurationImpl.get().getApiDefaultHost();

  private RetrofitClientHttp() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    //cache目录
    File cacheFile = new File(ConfigurationImpl.get().getAppContext().getCacheDir(), "netWorkCache");
    builder.cache(new Cache(cacheFile, 1024 * 1024 * 50));//50MB
    //自定义请求拦截器
    HttpLoggerInterceptor interceptor = new HttpLoggerInterceptor();
    //设置头
    builder.addInterceptor(interceptor);
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

  private RetrofitClientHttp(String baseUrl) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    //cache目录
    File cacheFile = new File(ConfigurationImpl.get().getAppContext().getCacheDir(), "netWorkCache");
    builder.cache(new Cache(cacheFile, 1024 * 1024 * 50));//50MB
    //自定义请求拦截器
    HttpLoggerInterceptor interceptor = new HttpLoggerInterceptor();
    //设置头
    builder.addInterceptor(interceptor);
    //设置超时
    builder.connectTimeout(8, TimeUnit.SECONDS);
    builder.readTimeout(20, TimeUnit.SECONDS);
    builder.writeTimeout(20, TimeUnit.SECONDS);
    //错误重连
    builder.retryOnConnectionFailure(false);
    OkHttpClient client = builder.build();
    newUrlRetrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(ResponseConverterFactory.create(GsonConverterFactory.create()))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();
  }

  /**
   * 默认的BaseUrl = APIConstant.BASE_URL
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
    return new RetrofitClientHttp(baseUrl);
  }


  /**
   * 自定义Service
   *
   * @param service 传入自定义的Service
   */
  public <T> T create(Class<T> service) {
    return retrofit.create(service);
  }

  /**
   * 自定义Service
   *
   * @param service 传入自定义的Service
   */
  public <T> T createNewUrl(Class<T> service) {
    return newUrlRetrofit.create(service);
  }
}
