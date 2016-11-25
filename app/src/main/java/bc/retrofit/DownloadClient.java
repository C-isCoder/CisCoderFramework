package bc.retrofit;

import java.util.concurrent.TimeUnit;

import bc.http.APIConstants;
import cn.ml.base.Configuration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iscod.
 * Time:2016/9/25-13:16.
 */

public class DownloadClient {
    private static DownloadClient INSTANCE;
    private Retrofit retrofit;
    private static String BaseUrl = Configuration.getBase_Download();

    private DownloadClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //官方请求拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.addInterceptor(loggingInterceptor);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(false);
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(APIConstants.Base_Download)
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
    public static DownloadClient getInstance() {
        if (INSTANCE == null) {
            synchronized (DownloadClient.class) {
                INSTANCE = new DownloadClient();
            }
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

    /**
     * 默认的service
     *
     * @return Service
     */
    public HttpService create() {
        return retrofit.create(HttpService.class);
    }
}