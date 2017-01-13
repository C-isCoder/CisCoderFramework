package com.baichang.library.test.base;

import com.baichang.android.request.HttpFactory;
import com.baichang.android.request.HttpResponse;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.library.test.model.InformationData;
import com.baichang.library.test.model.UserData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by iscod.
 * Time:2016/12/5-16:22.
 */

public class ApiWrapper implements Api {
    @Override
    public Observable<List<String>> upload(@Part MultipartBody.Part file) {
        return HttpFactory.creatUpload(Api.class).upload(file);
    }

    @Override
    public Observable<List<String>> uploads(@Part List<MultipartBody.Part> files) {
        return HttpFactory.creatUpload(Api.class).uploads(files);
    }


    @Override
    public Observable<ResponseBody> download(@Url String fileUrl) {
        //下载不需要设置线程，底层已经设置
        return HttpFactory.creatDownload(Api.class).download(fileUrl);
    }

    @Override
    public Observable<List<InformationData>> getInformationList(@Body Map<String, String> map) {
        //return HttpFactory.creatHttp(Api.class).getInformationList(map);
        return HttpFactory.creatHttp(Api.class).getInformationList(map);
    }

    @Override
    public Observable<UserData> login(@Body Map<String, String> map) {
        return HttpFactory.creatHttp(Api.class).login(map);
    }

    private Api getRequest() {
        return HttpFactory.creat(Api.class);
    }

}
