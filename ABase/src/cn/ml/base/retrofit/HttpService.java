package cn.ml.base.retrofit;


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
public interface HttpService {
    //上传文件
    @Multipart
    @POST("")
    Observable<List<String>> upload(@Part MultipartBody.Part file);

    //下载
    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String fileUrl);

    //登陆 不能用这种方式因为 要加密参数 加密的参数要是json
    //@POST("user/agentLogin")
    //Observable<UserData> login(@Query("userName") String username, @Query("pwd") String pwd);

    //获取验证码
    @POST("user/msgVerify")
    Observable<Boolean> getCode(@Body Map<String, String> map);

    @POST("agent/updatePwd")
    Observable<Boolean> forgetPassword(@Body Map<String, String> map);

}

