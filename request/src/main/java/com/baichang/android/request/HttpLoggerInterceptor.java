package com.baichang.android.request;

import com.baichang.android.config.ConfigurationImpl;
import com.orhanobut.logger.Logger;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 自定义请求拦截器，处理请求的token，加密，打印日志等
 * Created by iCong on 2016/9/17.
 */
public class HttpLoggerInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /**
         * 正常请求
         */
        NORMAL,
        /**
         * 下载
         */
        DOWNLOAD,
        /**
         * 上传
         */
        UPLOAD,
    }

    private Level mLevel;

    public HttpLoggerInterceptor() {
        this.mLevel = Level.NORMAL;
    }

    public HttpLoggerInterceptor(Level level) {
        this.mLevel = level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request newRequest = null;
        RequestBody requestBody = request.body();
        if (mLevel == Level.NORMAL) {
            HttpUrl url = request.url();
            HttpUrl.Builder newUrl = url.newBuilder();
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            String parameter = buffer.readString(UTF8);
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
                    "method->[^_^]：" + request.method());
            Request.Builder requestBuilder = request.newBuilder()
                    .method(request.method(), request.body()).url(newUrl.build());
            newRequest = requestBuilder.build();
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            if (mLevel == Level.NORMAL) {
                response = chain.proceed(newRequest);
            } else {
                response = chain.proceed(request);
            }
        } catch (Exception e) {
            Logger.e("<-- HTTP FAILED: " + e.getMessage());
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        Logger.d("Answer:\n" + "url:" + response.request().url() + "\n" + "timer:" + tookMs + "ms");
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer responseBuffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                return response;
            }
        }
        if (!isPlaintext(responseBuffer)) {
            return response;
        }
        if (contentLength != 0) {
            Logger.json(responseBuffer.clone().readString(charset));
        }
        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}

