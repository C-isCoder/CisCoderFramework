package com.baichang.android.circle.common;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by iCong on 2017/2/28.
 *
 * C is a Coder
 */

public interface InteractionAPI {

  //上传文件
  @Multipart
  @POST("file/uploadImages")
  Observable<List<String>> upload(@Part MultipartBody.Part file);

  //上传文件
  @Multipart
  @POST("file/uploadImages")
  Observable<List<String>> uploads(@Part List<MultipartBody.Part> files);

  //下载
  @GET
  @Streaming
  Observable<ResponseBody> download(@Url String fileUrl);

}