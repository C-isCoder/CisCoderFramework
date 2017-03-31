package com.baichang.android.request;

import com.baichang.android.config.ConfigurationImpl;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 自定义请求拦截器，处理请求的token，加密，打印日志等
 * Created by iCong on 2016/9/17.
 */
@Deprecated
public class RequestInterceptor implements Interceptor {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        RequestBody requestBody = originalRequest.body();
        HttpUrl url = originalRequest.url();
        HttpUrl.Builder newUrl = url.newBuilder();
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        String parameter = buffer.readString(UTF_8);
        buffer.flush();
        buffer.close();
        String token = ConfigurationImpl.get().getToken();
        String md5 = ParameterUtils.MD5(parameter);
        newUrl.addQueryParameter("sign", md5).addQueryParameter("token", token);
        Logger.i("RequestParams:\n" +
                "param->[T_T] ：" + parameter + "\n" +
                "url->[=_=]   ：" + url + "\n" +
                "sign->[o_o]  ：" + md5 + "\n" +
                "token->[$_$] ：" + token + "\n" +
                "method->[^_^]：" + originalRequest.method());
        Request.Builder requestBuilder = originalRequest.newBuilder()
                .method(originalRequest.method(), originalRequest.body()).url(newUrl.build());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}

