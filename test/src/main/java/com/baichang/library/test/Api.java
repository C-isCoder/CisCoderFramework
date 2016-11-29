package com.baichang.library.test;


import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iscod.
 * Time:2016/6/21-9:46.
 */
public interface Api {

    //咨询列表
    @POST("information/findInformationByCityId")
    Observable<List<InformationData>> getInformationList(@Body Map<String, String> map);
}

