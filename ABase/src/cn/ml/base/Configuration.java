package cn.ml.base;

import android.content.Context;

/**
 * Created by iscod.
 * Time:2016/11/25-9:25.
 */
public class Configuration {
    private static Context APP_CONTEXT;
    private static String BASE_URL = "Http:";
    private static String APP_NAME = "";
    private static String TOKEN = "";
    //上传图片
    private static String API_UPLOAD_IMAGE = "http://114.215.88.167:8080/fileupload/file/uploadImages";
    private static String Base_Upload = "http://114.215.88.167:8080/";
    private static String Upload_API = "fileupload/file/uploadImages";
    private static String Base_Download = "http://pkg3.fir.im/";
    //访问图片
    public static String API_LOAD_IMAGE = "http://114.215.88.167:8080/images/file/";

    private Configuration() {

    }

    public static void init(Context context) {
        setAppContext(context);
        setAppName(context.getString(R.string.app_name));
    }

    public static String getToken() {
        return TOKEN;
    }

    public static void setToken(String TOKEN) {
        Configuration.TOKEN = TOKEN;
    }


    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static String getAppName() {
        return APP_NAME;
    }

    public static Context getAppContext() {
        return APP_CONTEXT;
    }

    private static void setAppContext(Context appContext) {
        APP_CONTEXT = appContext;
    }

    private static void setAppName(String appName) {
        APP_NAME = appName;
    }

    public static String getApiUploadImage() {
        return API_UPLOAD_IMAGE;
    }

    public static void setApiUploadImage(String apiUploadImage) {
        API_UPLOAD_IMAGE = apiUploadImage;
    }

    public static String getBase_Upload() {
        return Base_Upload;
    }

    public static void setBase_Upload(String base_Upload) {
        Base_Upload = base_Upload;
    }

    public static String getUpload_API() {
        return Upload_API;
    }

    public static void setUpload_API(String upload_API) {
        Upload_API = upload_API;
    }

    public static String getBase_Download() {
        return Base_Download;
    }

    public static void setBase_Download(String base_Download) {
        Base_Download = base_Download;
    }

    public static String getApiLoadImage() {
        return API_LOAD_IMAGE;
    }

    public static void setApiLoadImage(String apiLoadImage) {
        API_LOAD_IMAGE = apiLoadImage;
    }
}
