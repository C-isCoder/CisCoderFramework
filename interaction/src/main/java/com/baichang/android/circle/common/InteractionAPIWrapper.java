package com.baichang.android.circle.common;

import com.baichang.android.request.HttpFactory;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
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
          Instance = HttpFactory.creatHttp(InteractionAPI.class);
        }
      }
    }
    return Instance;
  }

  @Override
  public Observable<List<String>> upload(@Part MultipartBody.Part file) {
    return HttpFactory.creatUpload(InteractionAPI.class).upload(file);
  }

  @Override
  public Observable<List<String>> uploads(@Part List<MultipartBody.Part> files) {
    return HttpFactory.creatUpload(InteractionAPI.class).uploads(files);
  }


  @Override
  public Observable<ResponseBody> download(@Url String fileUrl) {
    //下载不需要设置线程，底层已经设置
    return HttpFactory.creatDownload(InteractionAPI.class).download(fileUrl);
  }

}
