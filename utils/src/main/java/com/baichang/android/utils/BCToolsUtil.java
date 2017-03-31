/**
 * FlieName:LTToolUtils.java
 * Destribution:
 * Author:michael
 * 2013-5-17 下午4:04:18
 */
package com.baichang.android.utils;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author michael
 */
public class BCToolsUtil {

  private static final String[] zodiacArr = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};

  private static final String[] constellationArr = {"水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
      "天蝎座", "射手座", "魔羯座"};

  private static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

  /**
   * 获取当前时间
   *
   * @return String
   */
  public static String getTime() {
    Calendar ca = Calendar.getInstance();
    int year = ca.get(Calendar.YEAR);// 获取年份
    int month = ca.get(Calendar.MONTH);// 获取月份
    int day = ca.get(Calendar.DATE);// 获取日
    int minute = ca.get(Calendar.MINUTE);// 分
    int hour = ca.get(Calendar.HOUR);// 小时
    int second = ca.get(Calendar.SECOND);// 秒
    return String.format("%d%d%d%d%d%d", year, month, day, minute, hour, second);
  }

  /**
   * 手机号验证
   *
   * @param phone 手机号
   * @return boolean
   */
  public static boolean isCellphone(String phone) {
    String s = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[57]))\\d{8}$";
    return phone.matches(s);
  }

  /**
   * 手机号格式化
   *
   * @return 123****8900
   */
  private String phoneFormat(String mobile) {
    if (BCStringUtil.isMobile(mobile)) {
      if (mobile.length() == 11) {
        mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
      }
    }
    return mobile;
  }

  /**
   * MD5加密
   *
   * @param content 加密内容
   * @return String
   */
  public static String MD5(String content) {
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.reset();
      messageDigest.update(content.getBytes("UTF-8"));
    } catch (Exception e) {
      return null;
    }

    byte[] byteArray = messageDigest.digest();
    StringBuilder md5StrBuff = new StringBuilder();
    for (int i = 0; i < byteArray.length; i++) {
      if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
        md5StrBuff.append("0").append(
            Integer.toHexString(0xFF & byteArray[i]));
      } else {
        md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
      }
    }
    return md5StrBuff.toString().toLowerCase();
  }

  /**
   * 创建年龄
   *
   * @param birthday 生日
   * @return Age
   */
  public static int createAge(String birthday) {
    if (birthday == null) {
      return 0;
    }
    long d = Long.parseLong(birthday);
    Date data = new Date(d);
    Calendar c = Calendar.getInstance();
    c.setTime(data);
    int dy = c.get(Calendar.YEAR);
    int dm = c.get(Calendar.MONTH) + 1;
    Calendar now = Calendar.getInstance();
    int nowY = now.get(Calendar.YEAR);
    int nowM = now.get(Calendar.MONTH) + 1;
    float m = (nowY - dy + (nowM - dm) / 12f);
    return Integer.parseInt(String.valueOf(new BigDecimal(m).setScale(0, BigDecimal.ROUND_HALF_UP)));
  }


  /**
   * 根据用生日计算年龄
   *
   * @param strBirthday 生日
   * @return 年龄
   */
  public static int getAgeByBirthday(String strBirthday) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
    Date birthday = null;
    try {
      birthday = sdf.parse(strBirthday);
    } catch (ParseException e) {
      return 0;
    }
    Calendar cal = Calendar.getInstance();

    if (cal.before(birthday)) {
      throw new IllegalArgumentException(
          "The birthDay is before Now.It's unbelievable!");
    }

    int yearNow = cal.get(Calendar.YEAR);
    int monthNow = cal.get(Calendar.MONTH) + 1;
    int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

    cal.setTime(birthday);
    int yearBirth = cal.get(Calendar.YEAR);
    int monthBirth = cal.get(Calendar.MONTH) + 1;
    int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

    int age = yearNow - yearBirth;

    if (monthNow <= monthBirth) {
      if (monthNow == monthBirth) {
        if (dayOfMonthNow < dayOfMonthBirth) {
          age--;
        }
      } else {
        age--;
      }
    }
    return age;
  }

  /**
   * 根据生日获取生肖
   *
   * @param birthday 生日
   * @return 生肖
   */
  public static String getZodica(String birthday) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date d = null;
    try {
      d = sdf.parse(birthday);
    } catch (ParseException e) {
      return "";
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    return zodiacArr[calendar.get(Calendar.YEAR) % 12];
  }

  /**
   * 根据日期获取星座
   *
   * @param date 日期
   * @return 星座
   */
  public static String getConstellation(String date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date d = null;
    try {
      d = sdf.parse(date);
    } catch (ParseException e) {
      return "";
    }
    Calendar time = Calendar.getInstance();
    time.setTime(d);

    int month = time.get(Calendar.MONTH);
    int day = time.get(Calendar.DAY_OF_MONTH);
    if (day < constellationEdgeDay[month]) {
      month = month - 1;
    }
    if (month >= 0) {
      return constellationArr[month];
    }
    return constellationArr[11];
  }

  /**
   * 隐藏键盘
   * 如果输入法在窗口上已经显示，则隐藏，反之则显示
   *
   * @param context 上下文
   */
  public static void hideKeyboard(Context context) {
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  /**
   * 保持对话框显示状态
   *
   * @param dialog Dialog
   */
  public static void keepDialog(DialogInterface dialog) {
    try {
      Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
      field.setAccessible(true);
      field.set(dialog, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 销毁对话狂  结合 keepDialog
   *
   * @param dialog Dialog
   */
  public static void distoryDialog(DialogInterface dialog) {
    try {
      Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
      field.setAccessible(true);
      field.set(dialog, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 打开软键盘
   *
   * @param mEditText 输入框
   * @param mContext 上下文
   */
  public static void openKeybord(EditText mEditText, Context mContext) {
    InputMethodManager imm = (InputMethodManager) mContext
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY);
  }

  /**
   * 关闭软键盘
   *
   * @param mEditText 输入框
   * @param mContext 上下文
   */
  public static void closeKeybord(EditText mEditText, Context mContext) {
    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
  }

  /**
   * 打开键盘.
   *
   * @param context the context
   */
  public static void showSoftInput(Context context) {
    InputMethodManager inputMethodManager = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  /**
   * 关闭键盘事件.
   *
   * @param context the context
   */
  public static void closeSoftInput(Context context) {
    InputMethodManager inputMethodManager = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
      inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
          .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }

  /**
   * 拨打电话 拨打界面
   *
   * @param activity Activity
   * @param number 电话号码
   */
  public static void call(Activity activity, String number) {
    //跳到拨号界面不呼叫 ACTION_DIAL
    //直接呼叫ACTION_CALL
    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
    activity.startActivity(intent);
  }

  /**
   * 发送短信
   *
   * @param activity Activity
   * @param number 电话号码
   */
  public static void sendMessage(Activity activity, String number) {
    Uri uri = Uri.parse("smsto:" + number);
    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
    it.putExtra("sms_body", "");
    activity.startActivity(it);
  }

  /**
   * 获取当前IP地址
   *
   * @return IP地址
   */
  public static String getLocalIpAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
        NetworkInterface networkInterface = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses();
            enumIpAddress.hasMoreElements(); ) {
          InetAddress inetAddress = enumIpAddress.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            return inetAddress.getHostAddress();
          }
        }
      }
    } catch (SocketException ex) {
      //     Log.e(LOG_TAG, ex.toString());
    }
    return null;
  }

  /**
   * 获取版本名
   *
   * @param context 上下文
   * @return 版本名
   */
  public static String getVersionName(Context context) {
    // 获取packageManager的实例
    PackageManager packageManager = context.getPackageManager();
    // getPackageName()是你当前类的包名，0代表是获取版本信息
    try {
      PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
      return packInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      return "";
    }
  }

  /**
   * 获取版本号
   *
   * @param context 上下文
   * @return 版本号
   */
  public static String getVersionCode(Context context) {
    // 获取packageManager的实例
    PackageManager packageManager = context.getPackageManager();
    // getPackageName()是你当前类的包名，0代表是获取版本信息
    try {
      PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
      return String.valueOf(packInfo.versionCode);
    } catch (PackageManager.NameNotFoundException e) {
      return "";
    }
  }

  /**
   * 获取缓存大小
   *
   * @param context 上下文
   * @return 缓存
   */
  public static String getCacheSize(Context context) {
    long cacheSize = 0;
    try {
      cacheSize = getFolderSize(context.getCacheDir());
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        cacheSize += getFolderSize(context.getExternalCacheDir());
      }
    } catch (Exception ignored) {
    }
    return getFormatSize(cacheSize);
  }

  /**
   * 清理缓存
   *
   * @param context 上下文
   */
  public static void clearAllCache(Context context) {
    deleteDir(context.getCacheDir());
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      deleteDir(context.getExternalCacheDir());
    }
  }

  /**
   * 清理缓存
   *
   * @param context,tips 弹出提示
   */
  public static void clearAllCache(Context context, String tips) {
    deleteDir(context.getCacheDir());
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      deleteDir(context.getExternalCacheDir());
    }
    BCToastUtil.showMessage(context, tips);
  }

  private static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    assert dir != null;
    return dir.delete();
  }

  // 获取文件
  //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
  //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
  private static long getFolderSize(File file) throws Exception {
    long size = 0;
    try {
      File[] fileList = file.listFiles();
      for (int i = 0; i < fileList.length; i++) {
        // 如果下面还有文件
        if (fileList[i].isDirectory()) {
          size = size + getFolderSize(fileList[i]);
        } else {
          size = size + fileList[i].length();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return size;
  }

  /**
   * 格式化磁盘大小
   *
   * @param size 大小
   * @return 大小
   */
  public static String getFormatSize(double size) {
    double kiloByte = size / 1024;
    if (kiloByte < 1) {
      return "0K";
    }
    double megaByte = kiloByte / 1024;
    if (megaByte < 1) {
      BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
      return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
          .toPlainString() + "KB";
    }
    double gigaByte = megaByte / 1024;
    if (gigaByte < 1) {
      BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
      return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
          .toPlainString() + "MB";
    }
    double teraBytes = gigaByte / 1024;
    if (teraBytes < 1) {
      BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
      return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
          .toPlainString() + "GB";
    }
    BigDecimal result4 = new BigDecimal(teraBytes);
    return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
        + "TB";
  }

  /**
   * 获取当前系统时间
   *
   * @return 当前时间
   */
  public static String getCurrentTime() {
    String str = "";
    Calendar c = Calendar.getInstance();
    str = str + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH)
        + " " + +c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
    return str;
  }

  /**
   * 时间加减
   *
   * @param h1 小时
   * @param m1 分钟
   * @param h2 要加上的小时
   * @param m2 要加上的分钟
   * @return 结果
   */
  public static String HourAddMinus(int h1, int m1, int h2, int m2) {
    if (h2 > 0) {
      h1 += h2;
    }
    if (m2 > 0) {
      m1 += m2;
    }
    if (m1 >= 60) {
      h1 += m1 / 60;
      m1 = m1 % 60;
    }
    if (h1 >= 24) {
      h1 = 0;
    }
    return String.valueOf((h1 < 10 ? "0" + (h1) : (h1))) + ":" + ((m1 < 10) ? "0" + (m1) : (m1));
  }

  /**
   * 数字格式化
   *
   * @param number 要格式化的数字
   * @param code eg:0.00 保留的小数点
   */
  public static String numberFormat(double number, String code) {
    DecimalFormat df = new DecimalFormat(code);
    return df.format(number);
  }

  /**
   * 检查字段是非为空，并弹出Toast提示
   *
   * @param context 上下文
   * @param string 字段
   * @param msgId 资源id
   * @return 结果
   */
  public static boolean checkNull(Context context, String string, int msgId) {
    if (TextUtils.isEmpty(string)) {
      Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show();
      return true;
    } else {
      return false;
    }
  }

  /**
   * 检查字段是非为空，并弹出Toast提示
   *
   * @param context 上下文
   * @param string 字段
   * @param message 资源id
   * @return 结果
   */
  public static boolean checkNull(Context context, String string, String message) {
    if (TextUtils.isEmpty(string)) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
      return true;
    } else {
      return false;
    }
  }

  /**
   * 检查手机号是否正确，并弹出Toast提示
   *
   * @param context 上下文
   * @param phone 手机号
   * @param msgId 资源id
   * @return 是返回false不是返回true；
   */
  public static boolean checkPhone(Context context, String phone, int msgId) {
    if (!BCStringUtil.isMobile(phone)) {
      Toast.makeText(context, context.getString(msgId), Toast.LENGTH_SHORT).show();
      return true;
    } else {
      return false;
    }
  }

  /**
   * 检查手机号是否正确，并弹出Toast提示
   *
   * @param context 上下文
   * @param phone 手机号
   * @param message 消息
   * @return 是返回false不是返回true；
   */
  public static boolean checkPhone(Context context, String phone, String message) {
    if (!BCStringUtil.isMobile(phone)) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
      return true;
    } else {
      return false;
    }
  }

  /**
   * 获取打开各种文件的Intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getOpenFileIntent(String filePath) {
    File file = new File(filePath);
    if (!file.exists()) {
      return null;
    }
        /* 取得扩展名 */
    String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
    if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
        end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
      return getAudioFileIntent(filePath);
    } else if (end.equals("3gp") || end.equals("mp4")) {
      return getAudioFileIntent(filePath);
    } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
        end.equals("jpeg") || end.equals("bmp")) {
      return getImageFileIntent(filePath);
    } else if (end.equals("apk")) {
      return getApkFileIntent(filePath);
    } else if (end.equals("ppt")) {
      return getPptFileIntent(filePath);
    } else if (end.equals("xls")) {
      return getExcelFileIntent(filePath);
    } else if (end.equals("doc")) {
      return getWordFileIntent(filePath);
    } else if (end.equals("pdf")) {
      return getPdfFileIntent(filePath);
    } else if (end.equals("chm")) {
      return getChmFileIntent(filePath);
    } else if (end.equals("txt")) {
      return getTextFileIntent(filePath, false);
    } else {
      return getAllIntent(filePath);
    }
  }

  /**
   * Android获取一个用于打开APK文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getAllIntent(String filePath) {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(Intent.ACTION_VIEW);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "*/*");
    return intent;
  }

  /**
   * Android获取一个用于打开APK文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getApkFileIntent(String filePath) {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(Intent.ACTION_VIEW);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "application/vnd.android.package-archive");
    return intent;
  }

  /**
   * Android获取一个用于打开VIDEO文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getVideoFileIntent(String filePath) {

    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("oneshot", 0);
    intent.putExtra("configchange", 0);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "video/*");
    return intent;
  }

  /**
   * Android获取一个用于打开AUDIO文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getAudioFileIntent(String filePath) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("oneshot", 0);
    intent.putExtra("configchange", 0);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "audio/*");
    return intent;
  }

  /**
   * Android获取一个用于打开Html文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getHtmlFileIntent(String filePath) {
    Uri uri = Uri.parse(filePath).buildUpon()
        .encodedAuthority("com.android.htmlfileprovider")
        .scheme("content").encodedPath(filePath).build();
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.setDataAndType(uri, "text/html");
    return intent;
  }

  /**
   * Android获取一个用于打开图片文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getImageFileIntent(String filePath) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "image/*");
    return intent;
  }

  /**
   * Android获取一个用于打开PPT文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getPptFileIntent(String filePath) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
    return intent;
  }

  /**
   * Android获取一个用于打开Excel文件的intent
   *
   * @param filePaht 文件路径
   * @return Intent
   */
  public static Intent getExcelFileIntent(String filePaht) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Uri uri = Uri.fromFile(new File(filePaht));
    intent.setDataAndType(uri, "application/vnd.ms-excel");
    return intent;
  }

  /**
   * Android获取一个用于打开Word文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getWordFileIntent(String filePath) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "application/msword");
    return intent;
  }

  /**
   * Android获取一个用于打开CHM文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getChmFileIntent(String filePath) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "application/x-chm");
    return intent;
  }

  /**
   * Android获取一个用于打开文本文件的intent
   *
   * @param filePath 文件路径
   * @param paramBoolean 是否是文件
   * @return Intent
   */
  public static Intent getTextFileIntent(String filePath, boolean paramBoolean) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (paramBoolean) {
      Uri uri1 = Uri.parse(filePath);
      intent.setDataAndType(uri1, "text/plain");
    } else {
      Uri uri2 = Uri.fromFile(new File(filePath));
      intent.setDataAndType(uri2, "text/plain");
    }
    return intent;
  }

  /**
   * Android获取一个用于打开PDF文件的intent
   *
   * @param filePath 文件路径
   * @return Intent
   */
  public static Intent getPdfFileIntent(String filePath) {
    Intent intent = new Intent("android.intent.action.VIEW");
    intent.addCategory("android.intent.category.DEFAULT");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Uri uri = Uri.fromFile(new File(filePath));
    intent.setDataAndType(uri, "application/pdf");
    return intent;
  }

  /**
   * 根据文件路径 获取后缀名字
   *
   * @param path 路径
   * @return 后缀
   */
  public static String getSuffix(String path) {
         /* 取得扩展名 */
    return path.substring(path.lastIndexOf(".") + 1, path.length()).toLowerCase();
  }

}
