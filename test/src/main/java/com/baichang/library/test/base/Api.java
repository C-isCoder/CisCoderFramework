package com.baichang.library.test.base;


import com.baichang.library.test.data.InformationData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by iscod.
 * Time:2016/6/21-9:46.
 */
public interface Api {
    //上传文件
    @Multipart
    @POST("file/upload/")
    Observable<List<String>> upload(@Part MultipartBody.Part file);

    //上传文件
    @Multipart
    @POST("file/upload/")
    Observable<List<String>> uploads(@Part List<MultipartBody.Part> files);
    //下载
    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String fileUrl);

    //登陆 不能用这种方式因为 要加密参数 加密的参数要是json
    //@POST("user/agentLogin")
    //Observable<UserData> login(@Query("userName") String username, @Query("pwd") String pwd);
    //咨询列表
    @POST("information/findInformationByCityId")
    Observable<List<InformationData>> getInformationList(@Body Map<String, String> map);

    //Https
    @POST("user/login")
    Observable<Boolean> login(@Body Map<String, String> map);

    //12306
    @GET("otn/")
    Observable<ResponseBody> test();

    //tmall
    @GET("?ali_trackid=2:mm_26632322_6858406_23810104:1481078586_3k9_1046405187")
    Observable<ResponseBody> tmall();
}

