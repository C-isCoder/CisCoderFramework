package com.baichang.android.request;

/**
 * Created by iscod.
 * Time:2016/12/9-10:23.
 */

public class HttpFactory {
    private HttpFactory() {
    }

    public static <T> T creat(Class<T> clazz) {
        return RetrofitClientHttps.getInstance().create(clazz);
    }

    public static <T> T creat(Class<T> clazz, String url) {
        return RetrofitClientHttps.getInstance(url).create(clazz);
    }


    public static <T> T creatHttp(Class<T> clazz) {
        return RetrofitClientHttp.getInstance().create(clazz);
    }

    public static <T> T creatHttp(Class<T> clazz, String url) {
        return RetrofitClientHttp.getInstance(url).create(clazz);
    }

    public static <T> T creatDownload(Class<T> clazz) {
        return DownloadClient.getInstance().create(clazz);
    }
    public static <T> T creatUpload(Class<T> clazz) {
        return UploadClient.getInstance().create(clazz);
    }
}
