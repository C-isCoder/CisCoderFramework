package com.baichang.android.library.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baichang.android.library.Configuration;
import com.baichang.android.library.R;

import java.io.File;

/**
 * Created by iscod.
 * Time:2016/11/10-16:37.
 */

public class BCAppUpdateManager {

    private Context mContext;

    //是否强制升级
    private boolean mCoerce;

    //提示语
    private String updateMsg = "检查到更新版本，是否更新？";

    //返回的安装包url
    private String apkUrl = "";

    private AlertDialog noticeDialog;

    private static String msg = "";

    private DownloadManager mDownloadManager;
    private long downloadID;
    private boolean isDownloadSuccess = false;
    private BCDownloadReceiver receiver;

    public BCAppUpdateManager(Context context, String apkUrl, String updateMsg) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.updateMsg = updateMsg;
        msg = updateMsg;
        this.mCoerce = false;
        registerReceiver();
    }

    public BCAppUpdateManager(Context context, String apkUrl, String updateMsg, boolean coerce) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.updateMsg = updateMsg;
        msg = updateMsg;
        this.mCoerce = coerce;
        registerReceiver();
    }

    private void registerReceiver() {
        receiver = new BCDownloadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        mContext.registerReceiver(receiver, filter);
    }

    private void unregisterReceiver() {
        if (mContext != null) {
            mContext.unregisterReceiver(receiver);
        }
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo() {
        showNoticeDialog();
    }

    @SuppressLint("NewApi")
    private void showNoticeDialog() {
        AlertDialog.Builder builder = null;
        if (Build.VERSION.SDK_INT >= 11) {
            builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }

        builder.setTitle("提示");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downloadApk();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //强制升级
                if (mCoerce) {
                    MLAppManager.getAppManager().AppExit(mContext);
                } else {
                    dialog.dismiss();
                }
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
        setDialogTitleColor(noticeDialog, R.color.cm_app_color);
    }

    public static void setDialogTitleColor(Dialog builder, int color) {
        try {
            Context context = builder.getContext();
            int diverId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = builder.findViewById(diverId);
            divider.setBackgroundColor(context.getResources().getColor(color));

            int alertTitleId = context.getResources().getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) builder.findViewById(alertTitleId);
            alertTitle.setTextColor(context.getResources().getColor(color));
        } catch (Exception e) {

        }
    }

    public void setMsg(String msg) {
        BCAppUpdateManager.msg = msg;
    }

    /**
     * 下载apk
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void downloadApk() {
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        File file = new File(mContext.getExternalCacheDir(), "update.apk");
        Uri uri = Uri.fromFile(file);
        request.setDestinationUri(uri);
        request.setTitle(Configuration.getAppName() + "更新···");
        downloadID = mDownloadManager.enqueue(request);
    }

    /**
     * 安装apk
     */
    private void installApk() {
        Uri uri = mDownloadManager.getUriForDownloadedFile(downloadID);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
        }
        unregisterReceiver();
    }

    /**
     * 下载完成广播 and 通知点击广播
     */
    public class BCDownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                isDownloadSuccess = true;
                Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
                installApk();
            } else if (isDownloadSuccess && action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                installApk();
            }
        }
    }
}
