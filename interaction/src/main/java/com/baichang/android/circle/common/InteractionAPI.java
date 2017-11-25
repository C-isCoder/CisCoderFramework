package com.baichang.android.circle.common;

import com.baichang.android.circle.entity.InteractionDetailData;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.entity.InteractionNumberData;
import com.baichang.android.circle.entity.InteractionReplyData;
import com.baichang.android.circle.entity.InteractionShareData;
import com.baichang.android.circle.entity.InteractionTypeData;
import com.baichang.android.circle.entity.InteractionUserInfo;
import com.baichang.android.circle.entity.InteractionVideoData;
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
  @POST("file/uploadVoice")
  Observable<List<InteractionVideoData>> uploadVideo(@Part MultipartBody.Part file);

  //上传文件
  @Multipart
  @POST("file/uploadImages")
  Observable<List<String>> uploads(@Part List<MultipartBody.Part> files);

  //下载
  @GET
  @Streaming
  Observable<ResponseBody> download(@Url String fileUrl);

  // 互动列表
  @POST("trends/findTrendsList")
  Observable<List<InteractionListData>> getTrendsList(@Body Map<String, String> map);

  // 互动类别
  @POST("trendstype/findTrendsTypeList")
  Observable<List<InteractionTypeData>> getTrendsType();

  // 互动详情
  @POST("trends/getTrendsDetail")
  Observable<InteractionDetailData> getTrendsDetail(@Body Map<String, String> map);

  // 互动发表
  @POST("trends/addTrends")
  Observable<Boolean> publish(@Body Map<String, String> map);

  // 互动点赞/取消赞
  @POST("trends/praiseTrends")
  Observable<Boolean> praise(@Body Map<String, String> map);

  // 互动收藏/取消收藏
  @POST("trends/collectionTrends")
  Observable<Boolean> collect(@Body Map<String, String> map);

  // 互动动态
  @POST("trends/findDynamics")
  Observable<List<InteractionListData>> getDynamics(@Body Map<String, String> map);

  // 互动删除
  @POST("trends/deleteTrends")
  Observable<Boolean> delete(@Body Map<String, String> map);

  // 互动收藏列表
  @POST("trends/findCollectionTrends")
  Observable<List<InteractionListData>> getCollect(@Body Map<String, String> map);

  // 互动 动态、收藏、回复数量
  @POST("trends/getNumbers")
  Observable<InteractionNumberData> getNumbers(@Body Map<String, String> map);

  // 互动 举报
  @POST("trends/reportTrends")
  Observable<Boolean> report(@Body Map<String, String> map);

  // 互动 回复列表
  @POST("trends/findReplayTrends")
  Observable<List<InteractionReplyData>> getReply(@Body Map<String, String> map);

  // 互动 评论
  @POST("trends/commentTrends")
  Observable<Boolean> comment(@Body Map<String, String> map);

  // 互动 回复
  @POST("trends/replayTrendsComment")
  Observable<Boolean> reply(@Body Map<String, String> map);

  // 互动 用户信息
  @POST("trendsuser/getUserInfo")
  Observable<InteractionUserInfo> getUserInfo(@Body Map<String, String> map);

  // 互动 分享连接
  @POST("trends/getShareLink")
  Observable<InteractionShareData> getShareLink(@Body Map<String, String> map);
}