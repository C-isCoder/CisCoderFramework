package com.baichang.library.test.base;


import com.baichang.android.request.HttpResponse;
import com.baichang.library.test.model.InformationData;
import com.baichang.library.test.model.UserData;

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

    //咨询列表
    @POST("information/findInformationByCityId")
    Observable<List<InformationData>> getInformationList(@Body Map<String, String> map);

    //Https
    @POST("app/user/sign")
    Observable<UserData> login(@Body Map<String, String> map);

}

