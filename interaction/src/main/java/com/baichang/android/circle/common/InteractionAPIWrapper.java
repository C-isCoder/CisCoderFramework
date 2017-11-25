package com.baichang.android.circle.common;

import android.text.TextUtils;
import com.baichang.android.circle.entity.InteractionDetailData;
import com.baichang.android.circle.entity.InteractionListData;
import com.baichang.android.circle.entity.InteractionNumberData;
import com.baichang.android.circle.entity.InteractionReplyData;
import com.baichang.android.circle.entity.InteractionShareData;
import com.baichang.android.circle.entity.InteractionTypeData;
import com.baichang.android.circle.entity.InteractionUserInfo;
import com.baichang.android.circle.entity.InteractionVideoData;
import com.baichang.android.request.HttpFactory;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by iCong on 2017/2/28.
 *
 * C is a Coder
 */

public class InteractionAPIWrapper implements InteractionAPI {

  private static InteractionAPI Instance;

  public static InteractionAPI getInstance() {
    if (Instance == null) {
      synchronized (InteractionAPIWrapper.class) {
        if (Instance == null) {
          if (TextUtils.isEmpty(InteractionConfig.getInstance().getInteractionUrl())) {
            Instance = HttpFactory.creatHttp(InteractionAPI.class);
          } else {
            Instance = HttpFactory.creatHttp(InteractionAPI.class,
                InteractionConfig.getInstance().getInteractionUrl());
          }
        }
      }
    }
    return Instance;
  }

  @Override public Observable<List<String>> upload(@Part MultipartBody.Part file) {
    return HttpFactory.creatUpload(InteractionAPI.class).upload(file);
  }

  @Override public Observable<List<InteractionVideoData>> uploadVideo(@Part MultipartBody.Part file) {
    return  HttpFactory.creatUpload(InteractionAPI.class).uploadVideo(file);
  }

  @Override public Observable<List<String>> uploads(@Part List<MultipartBody.Part> files) {
    return HttpFactory.creatUpload(InteractionAPI.class).uploads(files);
  }

  @Override public Observable<ResponseBody> download(@Url String fileUrl) {
    //下载不需要设置线程，底层已经设置
    return HttpFactory.creatDownload(InteractionAPI.class).download(fileUrl);
  }

  @Override
  public Observable<List<InteractionListData>> getTrendsList(@Body Map<String, String> map) {
    return getTrendsList(map);
  }

  @Override public Observable<List<InteractionTypeData>> getTrendsType() {
    return getTrendsType();
  }

  @Override
  public Observable<InteractionDetailData> getTrendsDetail(@Body Map<String, String> map) {
    return getTrendsDetail(map);
  }

  @Override public Observable<Boolean> publish(@Body Map<String, String> map) {
    return publish(map);
  }

  @Override public Observable<Boolean> praise(@Body Map<String, String> map) {
    return praise(map);
  }

  @Override public Observable<Boolean> collect(@Body Map<String, String> map) {
    return collect(map);
  }

  @Override
  public Observable<List<InteractionListData>> getDynamics(@Body Map<String, String> map) {
    return getDynamics(map);
  }

  @Override public Observable<Boolean> delete(@Body Map<String, String> map) {
    return delete(map);
  }

  @Override public Observable<List<InteractionListData>> getCollect(@Body Map<String, String> map) {
    return getCollect(map);
  }

  @Override public Observable<InteractionNumberData> getNumbers(@Body Map<String, String> map) {
    return getNumbers(map);
  }

  @Override public Observable<Boolean> report(@Body Map<String, String> map) {
    return report(map);
  }

  @Override public Observable<List<InteractionReplyData>> getReply(@Body Map<String, String> map) {
    return getReply(map);
  }

  @Override public Observable<Boolean> comment(@Body Map<String, String> map) {
    return comment(map);
  }

  @Override public Observable<Boolean> reply(@Body Map<String, String> map) {
    return reply(map);
  }

  @Override public Observable<InteractionUserInfo> getUserInfo(@Body Map<String, String> map) {
    return getUserInfo(map);
  }

  @Override public Observable<InteractionShareData> getShareLink(@Body Map<String, String> map) {
    return getShareLink(map);
  }
}
