package cn.ml.base.retrofit;


import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLToastUtils;
import cn.ml.base.utils.ToolsUtil;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by iscod.
 * Time:2016/9/25-13:33.
 * eg:  DialogUtils.showProgressDialog(this, "正在下载···");
 * DownloadUtils.download("下载地址", file -> {
 * });
 */

public class DownloadUtils {
    private static Context mContext;

    public static void download(Context context, String url, ResultSuccessListener<File> listener) {
        mContext = context;
        MLDialogUtils.showProgressDialog(context, "正在下载····");
        //下载大文件，该注解会是Retrofit进入Main线程所以执行下载的时候要在子线程执行
        new Thread(() -> {
            DownloadClient
                    .getInstance()
                    .create()
                    .download(url)
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onStart() {
                            if (!ToolsUtil.isNetworkConnected()) {
                                MLToastUtils.showMessage(mContext, "网络未连接，稍后重试");
                                onCompleted();
                                return;
                            }
                            super.onStart();
                        }

                        @Override
                        public void onCompleted() {
                            MLDialogUtils.dismissProgressDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            MLDialogUtils.dismissProgressDialog();
                            LogUtils.logError(e);
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            File file = writeToFile(responseBody);
                            if (file != null && listener != null) {
                                listener.success(file);
                            }
                        }
                    });
        }).start();
    }

    //写入文件文件
    private static final String TAG = "DownLoadManager";

    private static String APK_CONTENTTYPE = "application/vnd.android.package-archive";

    private static String PNG_CONTENTTYPE = "image/png";

    private static String JPG_CONTENTTYPE = "image/jpg";

    private static String fileSuffix = "";

    /**
     * 不传文件名默认时间为文件名
     *
     * @param body
     * @return
     */
    public static File writeToFile(ResponseBody body) {
        return writeResponseBodyToFile(body, null);
    }

    /**
     * 传文件名，若存在就返回存在的文
     *
     * @param body
     * @param fileName
     * @return
     */
    public static File writeToFile(ResponseBody body, String fileName) {
        return writeResponseBodyToFile(body, fileName);
    }


    public static File writeResponseBodyToFile(ResponseBody body, String fileName) {

        Log.d(TAG, "contentType:>>>>" + body.contentType().toString());

        String type = body.contentType().toString();

        if (type.equals(APK_CONTENTTYPE)) {
            fileSuffix = ".apk";
        } else if (type.equals(PNG_CONTENTTYPE)) {
            fileSuffix = ".png";
        } else if (type.equals(JPG_CONTENTTYPE)) {
            fileSuffix = ".jpg";
        }

        // 其他类型同上 自己判断加入.....
        Context context = mContext;
        String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + context.getApplicationInfo().name + "下载";
        String path = File.separator + ToolsUtil.getCurrentTime() + fileSuffix;
        if (!TextUtils.isEmpty(fileName)) {
            path = File.separator + fileName + fileSuffix;
        }

        Log.d(TAG, "dir:>>>>" + fileDir);
        Log.d(TAG, "path:>>>>" + path);

        try {
            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, path);
            if (file.exists()) {
                return file;
            }
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                repair();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
                repair();
            }
        } catch (IOException e) {
            e.printStackTrace();
            repair();
        }
        repair();
        return null;
    }

    private static void repair() {
        mContext = null;
    }
}
