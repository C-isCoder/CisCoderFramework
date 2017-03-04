package com.baichang.android.request;

import android.text.TextUtils;
import android.util.Log;

import com.baichang.android.common.ConfigurationImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;

import java.io.Reader;
import java.io.StringReader;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iscod on 2016/6/22.
 */
public class ResponseConverterFactory extends Converter.Factory {
    private final Gson mGson;
    private final GsonConverterFactory mGsonConverterFactory;

    public static ResponseConverterFactory create(GsonConverterFactory mGsonConverterFactory) {
        return create(new Gson(), mGsonConverterFactory);
    }

    public static ResponseConverterFactory create(Gson mGson, GsonConverterFactory mGsonConverterFactory) {
        return new ResponseConverterFactory(mGson, mGsonConverterFactory);
    }

    private ResponseConverterFactory(Gson mGson, GsonConverterFactory mGsonConverterFactory) {
        if (mGson == null) throw new NullPointerException("mGson == null");
        this.mGson = mGson;
        this.mGsonConverterFactory = mGsonConverterFactory;
    }

    /**
     * 服务器相应处理
     * 根据具体Result API 自定义处理逻辑
     *
     * @param mType
     * @param annotations
     * @param retrofit
     * @return 返回Data相应的实体
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type mType,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> mAdapter = mGson.getAdapter(TypeToken.get(mType));
        return new BaseResponseBodyConverter<>(mAdapter, mType);//响应
    }

    /**
     * 请求处理
     * request body 我们无需特殊处理，直接返回 GsonConverterFactory 创建的 converter。
     *
     * @param mType
     * @param parameterAnnotations
     * @param methodAnnotations
     * @param retrofit
     * @return 返回 GsonConverterFactory 创建的 converter
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type mType,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return mGsonConverterFactory.requestBodyConverter(mType, parameterAnnotations,
                methodAnnotations, retrofit);
    }

    /**
     * 自定义的result Api处理逻辑
     *
     * @param <T> 泛型
     */
    private class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final TypeAdapter<T> mAdapter;
        private Type mType;//泛型，当服务器返回的数据为数组的时候回用到
        //error
        private static final String SERVICE_ERROR = "请求服务器异常";
        private static final String DATA_ERROR = "请求数据异常";

        private BaseResponseBodyConverter(TypeAdapter<T> mAdapter, Type mType) {
            this.mAdapter = mAdapter;
            this.mType = mType;
        }

        /**
         * 自定义转换器-处理服务器返回数据
         *
         * @param response
         * @return 返回data的实体or列表
         * @throws IOException
         */
        @Override
        public T convert(ResponseBody response) throws IOException {
            String strResponse = response.string();
            if (TextUtils.isEmpty(strResponse)) {
                throw new HttpException(SERVICE_ERROR);
            }
            //TODO 以后重构点，不用JSONObject解析，换成Gson。
            try {
                JSONObject jb = new JSONObject(strResponse);
                // 服务器状态
                int service_state = jb.getInt("state");
                if (service_state != 1) {
                    // 服务器异常
                    throw new HttpException(jb.getString("msg"));
                }
                // 接口状态
                int ret_state = jb.getJSONObject("res").getInt("code");
                if (ret_state == 40000) {
                    if (jb.getJSONObject("res").isNull("data")) {
                        throw new HttpException(SERVICE_ERROR);
                    }
                    String parameters = jb.getJSONObject("res").get("data").toString();
                    if (TextUtils.isEmpty(parameters)) {
                        throw new HttpException(DATA_ERROR);
                    }
                    return mGson.fromJson(parameters,mType);
                  //return mAdapter.fromJson(parameters);//解析单纯字符串的时候出问题。
                } else if (ret_state == 30000) {
                    ConfigurationImpl.get().refreshToken();
                    throw new HttpException(jb.getJSONObject("res").getString("msg"));
                } else {
                    // 接口异常
                    throw new HttpException(jb.getJSONObject("res").getString("msg"));
                }
            } catch (Exception e) {
                throw new HttpException(e.getMessage());
            } finally {
                response.close();
            }
        }
    }
}


