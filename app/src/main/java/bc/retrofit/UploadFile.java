package bc.retrofit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by iscod.
 * Time:2016/9/21-10:11.
 * Retrofit文件上传
 */

public class UploadFile {
    private final HttpService service = UploadClient.getInstance().create();
    private File file;

    /**
     * @param file 上传的文件
     */
    public UploadFile(@NonNull File file) {
        if (file == null) {
            throw new NullPointerException("文件不能为空");
        }
        this.file = file;
    }

    /**
     * @param filePath 文件路径
     */
    public UploadFile(@NonNull String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            throw new NullPointerException("文件路径不能为空");
        }
        file = new File(filePath);
    }

    public Observable<List<String>> upload() {
        //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        if (file.exists()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            return service.upload(body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        } else {
            throw new NullPointerException("文件不能为空");
        }
    }
}
