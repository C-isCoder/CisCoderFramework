package com.baichang.android.common;

import android.Manifest;
import android.Manifest.permission;
import android.Manifest.permission_group;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 12-12-10
 * Time: 上午12:56
 * Description:
 */
public class BCCrashHandler implements Thread.UncaughtExceptionHandler {

  private static final String TAG = BCCrashHandler.class.getSimpleName();

  //系统默认的UncaughtException处理类
  private Thread.UncaughtExceptionHandler mDefaultHandler;
  //CrashHandler实例
  private static BCCrashHandler INSTANCE = new BCCrashHandler();
  //程序的Context对象
  private Context mContext;
  //用来存储设备信息和异常信息
  private Map<String, String> infos = new HashMap<String, String>();

  //用于格式化日期,作为日志文件名的一部分
  private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

  //写入文件的信息
  private String errorMsg;

  /**
   * 保证只有一个CrashHandler实例
   */
  private BCCrashHandler() {
  }

  /**
   * 获取CrashHandler实例 ,单例模式
   */
  public static BCCrashHandler getInstance() {
    return INSTANCE;
  }

  /**
   * 初始化
   */
  public void init(Context context) {
    mContext = context;
    //获取系统默认的UncaughtException处理器
    mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    //设置该CrashHandler为程序的默认处理器
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  /**
   * 当UncaughtException发生时会转入该函数来处理
   */
  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    //腾讯bugly
    //CrashReport.postCatchedException(ex);
    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      Log.e(TAG, "error : ", e);
    }
    //不加上不打印
    handleException(ex);

    //重新启动程序
    Intent i = mContext.getPackageManager()
        .getLaunchIntentForPackage(mContext.getPackageName());
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    i.putExtra("data", "1");
    mContext.startActivity(i);
    android.os.Process.killProcess(android.os.Process.myPid());

  }


  /**
   * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
   *
   * @return true:如果处理了该异常信息;否则返回false.
   */
  private boolean handleException(Throwable ex) {
    if (ex == null) {
      return false;
    }
    //收集设备参数信息
    collectDeviceInfo();
    //保存日志文件
    saveCrashInfo2File(ex);
    ex.printStackTrace();
    return true;
  }

  /**
   * 收集设备参数信息
   */
  public void collectDeviceInfo() {
    try {
      PackageManager pm = mContext.getPackageManager();
      PackageInfo pi;
      pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
      if (pi != null) {
        String versionName = pi.versionName == null ? "null" : pi.versionName;
        String versionCode = pi.versionCode + "";
        infos.put("versionName", versionName);
        infos.put("versionCode", versionCode);
      }
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(TAG, "an error occured when collect package info", e);
    }
    Field[] fields = Build.class.getDeclaredFields();
    for (Field field : fields) {
      try {
        field.setAccessible(true);
        infos.put(field.getName(), field.get(null).toString());
        Log.d(TAG, field.getName() + " : " + field.get(null));
      } catch (Exception e) {
        Log.e(TAG, "an error occured when collect crash info", e);
      }
    }
  }

  File _crashLogDirFile = null;

  private File getCrashLogFolder() {
    PackageManager manager = mContext.getPackageManager();
    ApplicationInfo info = null;
    try {
      info = manager.getApplicationInfo(mContext.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    String appName = (String) manager.getApplicationLabel(info);
    if (_crashLogDirFile == null) {
      //File SDCardRoot = Environment.getExternalStorageDirectory();
      File SDCardRoot = mContext.getExternalFilesDir(appName + "异常日志/");
      _crashLogDirFile = SDCardRoot;
      _crashLogDirFile.mkdirs();
    }
    return _crashLogDirFile;

  }

  /**
   * 保存错误信息到文件中
   *
   * @return 返回文件名称, 便于将文件传送到服务器
   */
  private String saveCrashInfo2File(Throwable ex) {

    StringBuffer sb = new StringBuffer();
    for (Map.Entry<String, String> entry : infos.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      sb.append(key + "=" + value + "\n");
    }

    Writer writer = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);
    ex.printStackTrace(printWriter);
    Throwable cause = ex.getCause();
    while (cause != null) {
      cause.printStackTrace(printWriter);
      cause = cause.getCause();
    }
    printWriter.close();
    String result = writer.toString();
    sb.append(result);
    try {
      long timestamp = System.currentTimeMillis();
      String time = formatter.format(new Date());
      String fileName = "crash-" + time + "-" + timestamp + ".txt";
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        Log.d(TAG, String.format("crash log dir is %s", getCrashLogFolder().getAbsolutePath()));
        File file = new File(getCrashLogFolder(), fileName);
        Log.d(TAG, String.format("carsh log file is %s", file.getAbsolutePath()));
        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
        fos.write(sb.toString().getBytes());
        fos.close();
      }
      errorMsg = sb.toString();
      return fileName;
    } catch (Exception e) {
      Log.e(TAG, "an error occured while writing file...", e);
    }
    return null;
  }
}
