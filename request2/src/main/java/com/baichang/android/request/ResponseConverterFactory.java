package com.baichang.android.request;

import android.text.TextUtils;
import com.baichang.android.config.ConfigurationImpl;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iCong
 *
 * Date:2017年7月6日
 */
public class ResponseConverterFactory extends Converter.Factory {
  private final Gson mGson;

  public static ResponseConverterFactory create() {
    return new ResponseConverterFactory();
  }

  private ResponseConverterFactory() {
    mGson = new Gson();
  }

  /**
   * 服务器相应处理
   * 根据具体Result API 自定义处理逻辑
   *
   * @return 返回Data相应的实体
   */
  @Override public Converter<ResponseBody, ?> responseBodyConverter(Type mType,
      Annotation[] annotations, Retrofit retrofit) {
    return new BaseResponseBodyConverter<>(mType);//响应
  }

  /**
   * 请求处理
   * request body 我们无需特殊处理，直接返回 GsonConverterFactory 创建的 converter。
   *
   * @return 返回 GsonConverterFactory 创建的 converter
   */
  @Override public Converter<?, RequestBody> requestBodyConverter(Type mType,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    return GsonConverterFactory.create()
        .requestBodyConverter(mType, parameterAnnotations, methodAnnotations, retrofit);
  }

  /**
   * 自定义的result Api处理逻辑
   *
   * @param <T> 泛型
   */
  private class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Type mType;//泛型，当服务器返回的数据为数组的时候回用到
    // error
    private static final String SERVICE_ERROR = "请求服务器异常";
    // state
    private static final int SERVICE_STATE_SUCCESS = 1;
    private static final int REQUEST_SUCCESS = 40000;
    private static final int TOKEN_ERROR = 30000;

    private BaseResponseBodyConverter(Type mType) {
      this.mType = mType;
    }

    /**
     * 自定义转换器-处理服务器返回数据
     *
     * @return 返回data的实体or列表
     */
    @Override public T convert(ResponseBody response) {
      try {
        String strResponse = response.string();
        if (TextUtils.isEmpty(strResponse)) {
          throw new HttpException(SERVICE_ERROR);
        }
        //TODO 以后重构点，不用JSONObject解析，换成Gson。
        HttpResponse httpResponse = mGson.fromJson(strResponse, HttpResponse.class);
        int state = httpResponse.getState();
        // 服务器状态
        if (state != SERVICE_STATE_SUCCESS) {
          throw new HttpException(httpResponse.getMsg());
        }
        // 接口状态
        int port_state = httpResponse.getRes().getCode();
        if (port_state == REQUEST_SUCCESS) {
          return mGson.fromJson(mGson.toJson(httpResponse.getRes().getData()), mType);
        } else if (port_state == TOKEN_ERROR) {
          ConfigurationImpl.get().refreshToken();
          throw new HttpException(httpResponse.getRes().getMsg());
        } else {
          throw new HttpException(httpResponse.getRes().getMsg());
        }
      } catch (IOException e) {
        throw new HttpException(e.getMessage());
      } finally {
        response.close();
      }
    }
  }
}


