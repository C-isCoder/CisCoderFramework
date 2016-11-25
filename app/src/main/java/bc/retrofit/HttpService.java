package bc.retrofit;



import java.util.List;
import java.util.Map;

import bc.http.APIConstants;
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
    @POST(APIConstants.Upload_API)
    Observable<List<String>> upload(@Part MultipartBody.Part file);

    //下载
    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String fileUrl);

    //获取验证码
    @POST("user/msgVerify")
    Observable<Boolean> getCode(@Body Map<String, String> map);

    //获取客户详情
    @POST("client/updateClientDetailById")
    Observable<Boolean> editClient(@Body Map<String, String> map);

    //在线报备
    @POST("client/addReportedClient")
    Observable<Boolean> reportClient(@Body Map<String, String> map);

    //修改资料
    @POST("agent/updatePictureById")
    Observable<Boolean> modifyAvatar(@Body Map<String, String> map);

    //身份认证
    @POST("agent/addAgentCertified")
    Observable<Boolean> submitAudit(@Body Map<String, String> map);

    //消息列表
    @POST("feedBack/addFeedbackByAgentId")
    Observable<Boolean> submitSuggest(@Body Map<String, String> map);

    //提现
    @POST("agentCash/addAgentCash")
    Observable<Boolean> submitMoney(@Body Map<String, String> map);

    //修改手机号
    @POST("agent/updateAccountById")
    Observable<Boolean> modifyPhone(@Body Map<String, String> map);

    //修改密码
    @POST("agent/updatePwdById")
    Observable<Boolean> modifyPassword(@Body Map<String, String> map);

    //绑定银行卡
    @POST("bankCard/addBankCarByAgentId")
    Observable<Boolean> bindBankCard(@Body Map<String, String> map);

    //忘记密码
    @POST("agent/updatePwd")
    Observable<Boolean> forgetPassword(@Body Map<String, String> map);

}

