# Android框架文档说明

[TOC]

## 模块依赖关系

![](file:\desktop\依赖图.png)

## 项目结构

![](file:\desktop\项目结构.jpg)

## 模块引用

首先在工程的build.gradle文件中引入Maven库

``` java
allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven { url "https://jitpack.io" }
        maven { url "http://svn.weiidu.com:8081/nexus/content/repositories/android/" }
    }
}
```

* 网络请求 `compile 'com.baichang.android:request:1.0.0'`
* 图片加载 `compile 'com.baichang.android:imageLoader:1.0.0'`
* 基础组件 `compile 'com.baichang.android:common:1.0.0'`
* 控件集合 `compile 'com.baichang.android:widget:1.0.0'`
* 工具集合 `compile 'com.baichang.android:utils:1.0.0'`
* 二维码   `compile 'com.baichang.android:qrcode:1.0.0'`

## 项目初始化配置

1. 创建App类，继承BCApplication，并实现Configuration接口。

```java
public class App extends BCApplication implements Configuration {

    private static App instance;
    //token
    private static String TOKEN = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //配置URL TOKEN
        ConfigurationImpl.init(this);
        //友盟分享
        initShare();
    }

    private void initShare() {
        //微信
        PlatformConfig.setWeixin("wx1c368b574b528feb", "97a57cf3088035a6ae84a97e5613b1e6");
        //qq空间
        PlatformConfig.setQQZone("1105849494", "vdo9HdxqPdEbGNz6");
    }

    public static void setToken(String Token) {
        TOKEN = Token;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public String getApiDefaultHost() {
        return APIConstants.API_DEFAULT_HOST;
    }

    @Override
    public String getApiWebView() {
        return APIConstants.API_WEB_VIEW;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public String getApiUploadImage() {
        return APIConstants.API_UPLOAD_IMAGE;
    }

    @Override
    public String getApiLoadImage() {
        return APIConstants.API_LOAD_IMAGE;
    }

    @Override
    public String getToken() {
        return TextUtils.isEmpty(TOKEN) ? AppDiskCache.getToken() : TOKEN;
    }

    @Override
    public String getApiDownload() {
        return APIConstants.API_DOWNLOAD;
    }

    @Override
    public String getApiUpload() {
        return APIConstants.API_UPLOAD_IMAGE;
    }

    @Override
    public void refreshToken() {
        //TODO 刷新token
        setToken("refresh token");
    }

    @Override
    public int getAppBarColor() {
        return R.color.app_btn_color;
    }
}
```

## 网络请求（request）

##### 配置
具体项目配置APIConstants中的接口URL，并编写请求接口类Api.class并实现。  

![](file:\desktop\网络请求.jpg)  

>Api.class
``` java
public interface Api {
    //上传文件
    @Multipart
    @POST("file/upload/")
    Observable<List<String>> upload(@Part MultipartBody.Part file);

    //上传文件
    @Multipart
    @POST("file/upload/")
    Observable<List<String>> uploads(@Part List<MultipartBody.Part> files);

    //下载
    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String fileUrl);

    //咨询列表
    @POST("information/findInformationByCityId")
    Observable<List<InformationData>> getInformationList(@Body Map<String, String> map);

    //Https
    @POST("app/user/sign")
    Observable<UserData> login(@Body Map<String, String> map);

}
``` 
>ApiWrapper.class
``` java 
public class ApiWrapper implements Api {
    @Override
    public Observable<List<String>> upload(@Part MultipartBody.Part file) {
        return HttpFactory.creatUpload(Api.class).upload(file).compose(applySchedulers());
    }

    @Override
    public Observable<List<String>> uploads(@Part List<MultipartBody.Part> files) {
        return HttpFactory.creatUpload(Api.class).uploads(files).compose(applySchedulers());
    }


    @Override
    public Observable<ResponseBody> download(@Url String fileUrl) {
        //下载不需要设置线程，底层已经设置
        return HttpFactory.creatDownload(Api.class).download(fileUrl);
    }

    @Override
    public Observable<List<InformationData>> getInformationList(@Body Map<String, String> map) {
        return HttpFactory.creatHttp(Api.class).getInformationList(map).compose(applySchedulers());
    }

    @Override
    public Observable<UserData> login(@Body Map<String, String> map) {
        return HttpFactory.creatHttp(Api.class).login(map).compose(applySchedulers());
    }
    private Api getRequest() {
        return HttpFactory.creat(Api.class);
    }

    //变换操作符省略subscribeOnOn和observeOn的设置
    @SuppressWarnings("unchecked")
    final Observable.Transformer schedulersTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    @SuppressWarnings("unchecked")
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

}
```
>CommonActivity.class
``` java 
public class CommonActivity extends BaseActivity {
    private Api instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSystemBarColor(R.color.cm_black);
        super.onCreate(savedInstanceState);
    }

    public Api request() {
        if (instance == null) {
            instance = new ApiWrapper();
        }
        return instance;
    }
}
```
##### 使用
所有Activity都要继承上面的CommonActivity，如果是Fragment继承CommonFragment。 
>RequestActivity.class
``` java 
 /**
     * https
     */
    private void https() {
        Map<String, String> map = new HashMap<>();
        map.put("stationAccount", "test");
        map.put("stationPwd", "test");
        request().login(map).subscribe(new HttpSubscriber<UserData>(this).get(user -> {
            AppDiskCache.setToken(user.token);
            App.setToken(user.token);
            showMessage(AppDiskCache.getToken());
        }));
    }
```
*  `HttpSubscriber<T>();`默认没有加载框
*  `HttpSubscriber<T>(Context context);`默认加载框
*  `HttpSubscriber<T>(ProgressDialog dialog)`自定义的ProgressDialog
*  `HttpSubscriber<T>(SwipeRefreshLayout layout)`有列表的情况传入刷新控件，配合RecyclerView可以实现自动加载更多。  
``` java 
    rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (RecyclerViewUtils.isScrollBottom(recyclerView)) {
                    showMessage("滑到底部啦");
                    normalTest();
                }
            }
        });
    /**
     * 普通列表请求
     */
    private void normalTest() {
        Map<String, String> map = new HashMap<>();
        map.put("cityId", "1");
        request().getInformationList(map)
                .subscribe(new HttpSubscriber<List<InformationData>>(mRefresh).get(list -> {
                    if (isFirst) {
                        mAdapter.setData(list);
                    } else {
                        mAdapter.addData(list);
                    }
                }));
    }    
```  
具体详细使用参考test项目里面的RquestActivity.class。
##### 下载文件
ApiConstants中配置服务器下载地址
>ApiWarpper.class
``` java 
    @Override
    public Observable<ResponseBody> download(@Url String fileUrl) {
        //下载不需要设置线程，底层已经设置
        return HttpFactory.creatDownload(Api.class).download(fileUrl);
    }
```
>Activity.class
``` java 
    /**
     * 下载
     */
    private void download() {
        DownloadUtils.down(this, request().download("wandoujia-web_seo_baidu_homepage.apk"), file -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
        });
    }
```    
##### 文件上传
ApiConstants中配置服务器上传地址
>Api.class
``` java 
    //单张上传
    @Multipart
    @POST("file/upload/")
    Observable<List<String>> upload(@Part MultipartBody.Part file);

    //多张上传
    @Multipart
    @POST("file/upload/")
    Observable<List<String>> uploads(@Part List<MultipartBody.Part> files);

```
>ApiWarpper.class
``` java 
    //单张上传
    @Override
    public Observable<List<String>> upload(@Part MultipartBody.Part file) {
        return HttpFactory.creatUpload(Api.class).upload(file).compose(applySchedulers());
    }
    //多张上传
    @Override
    public Observable<List<String>> uploads(@Part List<MultipartBody.Part> files) {
        return HttpFactory.creatUpload(Api.class).uploads(files).compose(applySchedulers());
    }
```
>UploadActivity.class
``` java
    /**
     * 单张上传
     */
    private void upload() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test.png";
        Log.d(TAG, "测试图片路径：" + path);
        File file = new File(path);
        Log.d(TAG, "File：" + file.getAbsolutePath());
        request().upload(UploadUtils.getMultipartBody(file)).subscribe(new UploadSubscriber<List<String>>(this) {
            @Override
            public void onNext(List<String> list) {
                Log.d(TAG, "ImagePath：" + list.get(0));
                showMessage("上传成功");
                rvList.setVisibility(View.GONE);
                ImageLoader.loadImage(getApplicationContext(), list.get(0), ivImage);
                ivImage.setVisibility(View.VISIBLE);
            }
        });
    }
     /**
     * 多张上传
     */
    private void uploads() {
        //文件
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test.png";
        String path1 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test1.png";
        String path2 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test2.png";
        String path3 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test3.png";
        String path4 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test4.png";
        String path5 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "佰倡测试下载" + "/test5.jpg";
        Log.d(TAG, "测试图片路径：" + path);
        File file = new File(path);
        File file1 = new File(path1);
        File file2 = new File(path2);
        File file3 = new File(path3);
        File file4 = new File(path4);
        File file5 = new File(path5);
        Log.d(TAG, "File：" + file.getAbsolutePath());
        List<File> files = new ArrayList<>();
        files.add(file);
        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        request().uploads(UploadUtils.getMultipartBodysForFile(files))
                .subscribe(new UploadSubscriber<List<String>>(this) {
                    @Override
                    public void onNext(List<String> list) {
                        showMessage("上传成功");
                        for (String s : list) {
                            Log.d(TAG, "ImagePath：" + s);
                        }
                        ivImage.setVisibility(View.GONE);
                        mAdapter.setData(list);
                        rvList.setVisibility(View.VISIBLE);
                    }
                });
      //路径          
      List<String> paths = new ArrayList<>();
        paths.add(path);
        paths.add(path1);
        paths.add(path2);
        paths.add(path3);
        paths.add(path4);
        request().uploads(UploadUtils.getMultipartBodysForPath(paths))
                .subscribe(new HttpSubscriber<List<String>>(this).get(idList -> {
                    for (String s : idList) {
                        Log.d(TAG, "ImagePath：" + s);
                    }
                    ImageLoader.loadImage(this, idList.get(0), ivImage);
                }));
    }                
``` 

## 图片加载(imageLoader)
##### 配置
ApiConstants中配置加载图片的URL
##### 使用（Context要传Application的）

>size常量
``` java
    /**
     * 大图
     */
    public static final String BIG_IMAGE = "@!big";
    /**
     * 中图
     */
    public static final String MIDDLE_IMAGE = "@!middle";
    /**
     * 小图
     */
    public static final String SMALL_IMAGE = "@!small";
    /**
     * 原图
     */
    public static final String ORIGINAL_IMAGE = "";    
```
##### 加载方式一：
普通加载
``` java 
    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param size      图片大小
     * @param imageView
     */
    public static void loadImage(Context context, String url, String size, ImageView imageView) {
        if (Util.isOnMainThread()) {
            Glide.with(context)
                    .load(Image_API + url + size)
                    .crossFade()
                    .into(imageView);
        }
    }

```
##### 加载方式二：
有错误图片的加载，需要传入错误图片的资源id。
``` java 
    /**
     * 有错误图片的加载
     *
     * @param context
     * @param url
     * @param size
     * @param errorRes
     * @param imageView
     */
    public static void loadImageError(Context context, String url, String size, int errorRes, ImageView imageView) {
        if (Util.isOnMainThread()) {
            Glide.with(context)
                    .load(Image_API + url + size)
                    .error(errorRes)
                    .crossFade()
                    .into(imageView);
        }
    }
```
##### 加载方式三：
``` java 
    /**
     * 有错误图片的加载
     *
     * @param context
     * @param url
     * @param errorRes
     * @param imageView
     */
    public static void loadImageError(Context context, String url, int errorRes, ImageView imageView) {
        if (Util.isOnMainThread()) {
            Glide.with(context)
                    .load(Image_API + url)
                    .error(errorRes)
                    .crossFade()
                    .into(imageView);
        }
    }
```
##### 加载方式四：
加载本地图片,文件路径path
``` java 
    /**
     * 加载本地图片
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadLocationImage(Context context, String path, ImageView imageView) {
        if (Util.isOnMainThread()) {
            Glide.with(context)
                    .load(path)
                    .crossFade()
                    //.error(R.mipmap.place_head)
                    .into(imageView);
        }
    }
```
##### 加载方式五：
下载图片
``` java 
    /**
     * Glide 下载图片返回文件
     * 同步下载
     *
     * @param context
     * @param path
     * @return
     */
    public static File downloadImage2File(Context context, String path) {
        FutureTarget<File> future = Glide.with(context)
                .load(path)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);//Target.SIZE_ORIGINAL 原图大笑
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Glide 下载图片返回Bitmap
     *
     * @param context
     * @param path
     * @return
     */
    public static Bitmap downloadImage2Bitmap(Context context, String path) {
        Bitmap bitmap = null;
        if (Util.isOnMainThread()) {
            try {
                bitmap = Glide.with(context)
                        .load(path)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)//Target.SIZE_ORIGINAL 原图大笑
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
```

## 工具类(utils)
##### 目录
![](file:\desktop\工具类.jpg)  
##### 使用
所有工具类以大写BC开头，封装的常用工具类。  

##### BCStringUtil-String相关的类
* `static String parseEmpty(String str)`将null转化为“”.
* `static boolean isEmpty(String str)`判断一个字符串是否为null或空值.
* `static boolean compare(String str1, String str2)`对比两个字符串是否相等
* `static Boolean isMobile(String str)`手机号格式验证

##### BCToastUtil-弹出Toast
* `static void showMessage(Context context, Object content)`普通Toast,Object参数只能传资源id（int）或String，如果需要弹出的信息是int类型需要转成String类型传入。

##### BCDateUtil-日期
* `static Date getDateByFormat(String strDate, String format)`String类型的日期时间转化为Date类型.
* `static String getStringByFormat(Date date, String format)`Date类型转化为String类型.
* `static String getStringByFormat(String strDate, String format)`获取指定日期时间的字符串,用于导出想要的格式.
* `static String getStringByFormat(long milliseconds, String format)`获取milliseconds表示的日期时间的字符串.
* `static String getCurrentDate(String format)`获取表示当前日期时间的字符串.
* `static String getFirstDayOfWeek(String format)`获取本周一.
* `static String getDayOfWeek(String format, int calendarField)`获取本周的某一天.
* `static String getFirstDayOfMonth(String format)`获取本月第一天.
* `static String getLastDayOfMonth(String format)`获取本月最后一天.
* `static boolean compareTime(String date1, String date2)`日期1是否大于日期2
* `static String getNextMonday()`获得下周星期一的日期

##### BCDensityUtil-密度

* `static int dip2px(Context context, float dpValue)`根据手机的分辨率将dp的单位转成px(像素)
* `static int px2dip(Context context, float pxValue)`根据手机的分辨率将px(像素)的单位转成dp
* `static int px2sp(Context context, float pxValue)`将px值转换为sp值
* `static int sp2px(Context context, float spValue)`将sp值转换为px值
* `static  int getWindowWidth(Activity context)`屏幕宽度（像素）
* `static int getWindowHeight(Activity activity)`屏幕高度（像素）
* `static DisplayMetrics getDisplayMetrics(Context context)` 获取屏幕尺寸与密度.

##### BCDialogUtil-对话框

* `showProgressDialog(Context context, Object message)`显示加载 ProgressDialog
* `static void dismissProgressDialog()`dismiss ProgressDialog
* `static void showDialog(Context context, int colorRes, String title, String content,DialogInterface.OnClickListener confirmListener,DialogInterface.OnClickListener cancelListener)`弹出普通的 警告框，可配置确认 取消的事件监听
* `static void showDialog(Context context, int colorRes, String title, String content)`弹出警告框默认,可配置颜色
* `static void showDialog(Context context, String title, String content)`弹出警告框默认
* `static void getDialogItem(Context context, String title, String[] items,final DialogInterface.OnClickListener listener)`获取items Dialog 列表的Dialog
* `static void choiceTime(Context context, String date,DialogInterface.OnClickListener confirm,DialogInterface.OnClickListener cancel)`日期选择 自己设置 确认 取消的监听
``` java 
  BCDialogUtil.choiceTime(getAty(), BCDateUtil.getCurrentDate("yyyy-MM-dd"),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            GregorianCalendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat simpleData = new SimpleDateFormat("yyyy-MM-dd");
                            simpleData.setCalendar(date);
                            String strDate = simpleData.format(date.getTime());
                            showMessage(strDate);
                        });
   BCDialogUtil.showDialog(getAty(), "标题", "内容");
   //带输入框
   BCDialogUtil.getInputDialog(getAty(), new EditText(getAty()), "标题",
                        (dialog, which) -> {
                            showMessage("确认");
                        }, (dialog, which) -> {
                            showMessage("取消");
                        });
```

##### Pinyin(拼音类)
* `Pinyin.toPinyin(city.charAt(0)).substring(0, 1))`转拼音取首字母

##### BCToolsUtil-工具
* `static String getCurrentTime()`获取当前时间
* `static String MD5(String content)`MD5加密
* `static int createAge(String birthday)`创建年龄
* `static int getAgeByBirthday(String strBirthday)`根据用生日计算年龄
* `static String getZodica(String birthday)`根据生日获取生肖
* `static String getConstellation(String date)`根据日期获取星座
* `static void hideKeyboard(Context context)`隐藏键盘
* `static void openKeybord(EditText mEditText, Context mContext)`打开软键盘
* `static void keepDialog(DialogInterface dialog)`保持对话框显示状态
* `static void distoryDialog(DialogInterface dialog)`销毁对话框 结合 keepDialog
* `static void call(Activity activity, String number)`拨打电话 拨打界面
* `static void callAction(Activity activity, String number)`拨打电话 直接拨打
* `static void sendMessage(Activity activity, String number)`发送短信
* `static String getLocalIpAddress()`获取当前IP地址
* `static String getVersionName(Context context)`获取版本名
* `static String getVersionCode(Context context)`获取版本号
* `static String getCacheSize(Context context)`获取缓存大小
* `static void clearAllCache(Context context)`清理缓存
* `static void clearAllCache(Context context, String tips)`清理缓存-弹出提示
* `static String numberFormat(double number, String code)`数字格式化
* `static boolean checkNull(Context context, String string, int msgId)`检查字段是非为空，并弹出Toast提示
* `static boolean checkPhone(Context context, String phone, int msgId)`检查手机号是否正确，并弹出Toast提示
* `static Intent getOpenFileIntent(String filePath)`获取打开各种文件的Intent
* `static String getSuffix(String path)`根据文件路径 获取后缀名字

##### 分享
>BCUmUtil

``` java 
BCUmUtil.share(getAty(), "标题", "内容", SHARE_URL_TEST, R.mipmap.ic_launcher, null, true);

```
## 控件(widget)

##### 结构

![](file:\desktop\控件.png)

##### 照片墙
>PhotoGalleryActivity.class   
>AndroidManifest.xml(具体项目中要配置)

``` xml
   <!-- 照片墙 -->
    <activity
        android:name="com.baichang.android.widget.photoGallery.PhotoGalleryActivity"
            android:screenOrientation="portrait" />
```
``` java 
    //构造方法可传入数组或者List，第一个参数为起始位置
    //PhotoGalleryData data = new PhotoGalleryData(0, List);
    PhotoGalleryData data = new PhotoGalleryData(0, IMAGES);
    startAct(getAty(), PhotoGalleryActivity.class, data);
```
##### 照片选取裁剪
>SelectPhotoActivity.class

``` java 
 //方式一：
 PhotoSelectDialog dialog = new PhotoSelectDialog();
        dialog.setResultListener(result -> {
            BCPhotoUtil.choose(getAty(), result);
        });
        dialog.setPhotoSelectCallBack((bitmap, path) -> {
            ivImage.setImageBitmap(bitmap);
            showMessage(path);
        });
        dialog.show(getSupportFragmentManager(), "photo");
 //方式二：       
 new PhotoSelectDialog.Builder()
        //.setResultListener(this)//回调1
        .setSelectCallback(this)//回调2直接回调裁剪好的照片不需要重写Activity的onActivityResult
        .setPressColor(R.color.no_activate)
        .setNormalColor(R.color.app_btn_color)
        .setImageText("图库")
        .create()
        .show(getSupportFragmentManager(), "photo");
  //回调一      
  @Override
    public void onResult(int i) {
        BCPhotoUtil.choose(getAty(), i);
    }
  //回调二  
  @Override
    public void onResult(Bitmap bitmap, String path) {
        showMessage(path);
        ivImage.setImageBitmap(bitmap);
    }       
```
##### 轮播图banner
轮播图用的GitHub开源项目[Android图片轮播控件](https://github.com/youth5201314/banner)
>BannerActivity.class

``` java
   private void initBanner() {
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(Arrays.asList(mList));
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //mBanner.setBannerTitles(Arrays.asList(mBannerTitles));
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        mBanner.stopAutoPlay();
    }
```
>activity_banner.xml

``` xml 
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_banner"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.baichang.library.test.common.BannerActivity">

    <com.youth.banner.Banner
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="160dp"
        android:layout_centerInParent="true"
        app:indicator_drawable_selected="@drawable/app_btn_bg" />
</RelativeLayout>

``` 

|模式|图片
|---|---|
|指示器模式|![效果示例](https://raw.githubusercontent.com/youth5201314/banner/master/image/1.png)
|数字模式|![效果示例](https://raw.githubusercontent.com/youth5201314/banner/master/image/2.png)
|数字加标题模式|![效果示例](https://raw.githubusercontent.com/youth5201314/banner/master/image/3.png)
|指示器加标题模式<br>垂直显示|![效果示例](https://raw.githubusercontent.com/youth5201314/banner/master/image/4.png)
|指示器加标题模式<br>水平显示|![效果示例](https://raw.githubusercontent.com/youth5201314/banner/master/image/5.png)

|常量名称|描述|所属方法
|---|---|---|
|BannerConfig.NOT_INDICATOR| 不显示指示器和标题|setBannerStyle
|BannerConfig.CIRCLE_INDICATOR| 显示圆形指示器|setBannerStyle
|BannerConfig.NUM_INDICATOR| 显示数字指示器|setBannerStyle
|BannerConfig.NUM_INDICATOR_TITLE| 显示数字指示器和标题|setBannerStyle
|BannerConfig.CIRCLE_INDICATOR_TITLE| 显示圆形指示器和标题（垂直显示）|setBannerStyle
|BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE| 显示圆形指示器和标题（水平显示）|setBannerStyle
|BannerConfig.LEFT| 指示器居左|setIndicatorGravity
|BannerConfig.CENTER| 指示器居中|setIndicatorGravity
|BannerConfig.RIGHT| 指示器居右|setIndicatorGravity

|常量类名|
|---|
|Transformer.Default| 
|Transformer.Accordion| 
|Transformer.BackgroundToForeground| 
|Transformer.ForegroundToBackground| 
|Transformer.CubeIn| 
|Transformer.CubeOut| 
|Transformer.DepthPage| 
|Transformer.FlipHorizontal| 
|Transformer.FlipVertical| 
|Transformer.RotateDown| 
|Transformer.RotateUp| 
|Transformer.ScaleInOut| 
|Transformer.Stack| 
|Transformer.Tablet| 
|Transformer.ZoomIn| 
|Transformer.ZoomOut| 
|Transformer.ZoomOutSlide| 

|方法名|描述
|---|---|
|setBannerStyle(int bannerStyle)| 设置轮播样式（默认为CIRCLE_INDICATOR）
|setIndicatorGravity(int type)| 设置指示器位置（没有标题默认为右边,有标题时默认左边）
|isAutoPlay(boolean isAutoPlay)| 设置是否自动轮播（默认自动）
|setViewPagerIsScroll(boolean isScroll)| 设置是否允许手动滑动轮播图（默认true）
|update(List<?> imageUrls,List<String> titles)| 更新图片和标题 
|update(List<?> imageUrls)| 更新图片 
|startAutoPlay()|开始轮播
|stopAutoPlay()|结束轮播
|start()|开始进行banner渲染
|setOffscreenPageLimit(int limit)|同viewpager的方法作用一样
|setBannerTitle(String[] titles)| 设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
|setBannerTitleList(List<String> titles)| 设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
|setBannerTitles(List<String> titles)| 设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
|setDelayTime(int time)| 设置轮播图片间隔时间（单位毫秒，默认为2000）
|setImages(Object[]/List<?> imagesUrl)| 设置轮播图片(所有设置参数方法都放在此方法之前执行)
|setImages(Object[]/List<?> imagesUrl,OnLoadImageListener listener)| 设置轮播图片，并且自定义图片加载方式
|setOnBannerClickListener(this)|设置点击事件，下标是从1开始
|setOnLoadImageListener(this)|设置图片加载事件，可以自定义图片加载方式
|setImageLoader(Object implements ImageLoader)|设置图片加载器
|setOnPageChangeListener(this)|设置viewpager的滑动监听|
|setBannerAnimation(Class<? extends PageTransformer> transformer)|设置viewpager的默认动画,传值见动画表
|setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer)|设置viewpager的自定义动画

###### Attributes属性（banner布局文件中调用）
|Attributes|forma|describe
|---|---|---|
|delay_time| integer|轮播间隔时间，默认2000
|scroll_time| integer|轮播滑动执行时间，默认800
|is_auto_play| boolean|是否自动轮播，默认true
|title_background| color|reference|标题栏的背景色
|title_textcolor| color|标题字体颜色
|title_textsize| dimension|标题字体大小
|title_height| dimension|标题栏高度
|indicator_width| dimension|指示器圆形按钮的宽度
|indicator_height| dimension|指示器圆形按钮的高度
|indicator_margin| dimension|指示器之间的间距
|indicator_drawable_selected| reference|指示器选中效果
|indicator_drawable_unselected| reference|指示器未选中效果
|image_scale_type| enum |和imageview的ScaleType作用一样

##### 指示器
使用GitHub开源的指示器[MagicIndicator](https://github.com/hackware1993/MagicIndicator)

>XML

``` xml 
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="net.lucode.hackware.magicindicatordemo.MainActivity">

    <net.lucode.hackware.magicindicator.MagicIndicator
        android:id="@+id/magic_indicator"
        android:layout_width="match_parent"
        android:layout_height="40dp" />

    <android.support.v4.view.ViewPager
        android:id="@+id/view_pager"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

</LinearLayout>
```
>Activity

``` java
MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
CommonNavigator commonNavigator = new CommonNavigator(this);
commonNavigator.setAdapter(new CommonNavigatorAdapter() {

    @Override
    public int getCount() {
        return mTitleDataList == null ? 0 : mTitleDataList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
        colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
        colorTransitionPagerTitleView.setText(mTitleDataList.get(index));
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(index);
            }
        });
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        return indicator;
    }
});
magicIndicator.setNavigator(commonNavigator);
ViewPagerHelper.bind(magicIndicator, mViewPager);
//Fragment
mFramentContainerHelper = new FragmentContainerHelper(magicIndicator);
mFragmentContainerHelper.handlePageSelected(pageIndex);
```
##### 二维码
使用GitHub开源[BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)
`二维码是一个单独的模块，使用的时候不要忘了引用。`
>XML

``` xml
<cn.bingoogolapple.qrcode.zxing.ZXingView
    android:id="@+id/zxingview"
    style="@style/MatchMatch"
    app:qrcv_animTime="1000"
    app:qrcv_borderColor="@android:color/white"
    app:qrcv_borderSize="1dp"
    app:qrcv_cornerColor="@color/colorPrimaryDark"
    app:qrcv_cornerLength="20dp"
    app:qrcv_cornerSize="3dp"
    app:qrcv_maskColor="#33FFFFFF"
    app:qrcv_rectWidth="200dp"
    app:qrcv_scanLineColor="@color/colorPrimaryDark"
    app:qrcv_scanLineSize="1dp"
    app:qrcv_topOffset="90dp" />
```
属性名 | 说明 | 默认值
:----------- | :----------- | :-----------
qrcv_topOffset         | 扫描框距离 toolbar 底部的距离        | 90dp
qrcv_cornerSize         | 扫描框边角线的宽度        | 3dp
qrcv_cornerLength         | 扫描框边角线的长度        | 20dp
qrcv_cornerColor         | 扫描框边角线的颜色        | @android:color/white
qrcv_rectWidth         | 扫描框的宽度        | 200dp
qrcv_barcodeRectHeight         | 条码扫样式描框的高度        | 140dp
qrcv_maskColor         | 除去扫描框，其余部分阴影颜色        | #33FFFFFF
qrcv_scanLineSize         | 扫描线的宽度        | 1dp
qrcv_scanLineColor         | 扫描线的颜色「扫描线和默认的扫描线图片的颜色」        | @android:color/white
qrcv_scanLineMargin         | 扫描线距离上下或者左右边框的间距        | 0dp
qrcv_isShowDefaultScanLineDrawable         | 是否显示默认的图片扫描线「设置该属性后 qrcv_scanLineSize 将失效，可以通过 qrcv_scanLineColor 设置扫描线的颜色，避免让你公司的UI单独给你出特定颜色的扫描线图片」        | false
qrcv_customScanLineDrawable         | 扫描线的图片资源「默认的扫描线图片样式不能满足你的需求时使用，设置该属性后 qrcv_isShowDefaultScanLineDrawable、qrcv_scanLineSize、qrcv_scanLineColor 将失效」        | null
qrcv_borderSize         | 扫描边框的宽度        | 1dp
qrcv_borderColor         | 扫描边框的颜色        | @android:color/white
qrcv_animTime         | 扫描线从顶部移动到底部的动画时间「单位为毫秒」        | 1000
qrcv_isCenterVertical         | 扫描框是否垂直居中，该属性为true时会忽略 qrcv_topOffset 属性        | false
qrcv_toolbarHeight         | Toolbar 的高度，通过该属性来修正由 Toolbar 导致扫描框在垂直方向上的偏差        | 0dp
qrcv_isBarcode         | 是否是扫条形码        | false
qrcv_tipText         | 提示文案        | null
qrcv_tipTextSize         | 提示文案字体大小        | 14sp
qrcv_tipTextColor         | 提示文案颜色        | @android:color/white
qrcv_isTipTextBelowRect         | 提示文案是否在扫描框的底部        | false
qrcv_tipTextMargin         | 提示文案与扫描框之间的间距        | 20dp
qrcv_isShowTipTextAsSingleLine         | 是否把提示文案作为单行显示        | false
qrcv_isShowTipBackground         | 是否显示提示文案的背景        | false
qrcv_tipBackgroundColor         | 提示文案的背景色        | #22000000
qrcv_isScanLineReverse         | 扫描线是否来回移动        | true
qrcv_isShowDefaultGridScanLineDrawable         | 是否显示默认的网格图片扫描线        | false
qrcv_customGridScanLineDrawable         | 扫描线的网格图片资源        | nulll
qrcv_isOnlyDecodeScanBoxArea         | 是否只识别扫描框区域的二维码        | false

>接口说明

``` java
/**
 * 设置扫描二维码的代理
 *
 * @param delegate 扫描二维码的代理
 */
public void setDelegate(Delegate delegate)

/**
 * 显示扫描框
 */
public void showScanRect()

/**
 * 隐藏扫描框
 */
public void hiddenScanRect()

/**
 * 打开后置摄像头开始预览，但是并未开始识别
 */
public void startCamera()

/**
 * 打开指定摄像头开始预览，但是并未开始识别
 *
 * @param cameraFacing  Camera.CameraInfo.CAMERA_FACING_BACK or Camera.CameraInfo.CAMERA_FACING_FRONT
 */
public void startCamera(int cameraFacing)

/**
 * 关闭摄像头预览，并且隐藏扫描框
 */
public void stopCamera()

/**
 * 延迟1.5秒后开始识别
 */
public void startSpot()

/**
 * 延迟delay毫秒后开始识别
 *
 * @param delay
 */
public void startSpotDelay(int delay)

/**
 * 停止识别
 */
public void stopSpot()

/**
 * 停止识别，并且隐藏扫描框
 */
public void stopSpotAndHiddenRect()

/**
 * 显示扫描框，并且延迟1.5秒后开始识别
 */
public void startSpotAndShowRect()

/**
 * 打开闪光灯
 */
public void openFlashlight()

/**
 * 关闭散光灯
 */
public void closeFlashlight()
```
>QRCodeView.Delegate   扫描二维码的代理

```java
/**
 * 处理扫描结果
 *
 * @param result
 */
void onScanQRCodeSuccess(String result)

/**
 * 处理打开相机出错
 */
void onScanQRCodeOpenCameraError()
```

>QRCodeDecoder  解析二维码图片。几个重载方法都是耗时操作，请在子线程中调用。

```java
/**
 * 同步解析本地图片二维码。该方法是耗时操作，请在子线程中调用。
 *
 * @param picturePath 要解析的二维码图片本地路径
 * @return 返回二维码图片里的内容 或 null
 */
public static String syncDecodeQRCode(String picturePath)

/**
 * 同步解析bitmap二维码。该方法是耗时操作，请在子线程中调用。
 *
 * @param bitmap 要解析的二维码图片
 * @return 返回二维码图片里的内容 或 null
 */
public static String syncDecodeQRCode(Bitmap bitmap)
```

>QRCodeEncoder  创建二维码图片。几个重载方法都是耗时操作，请在子线程中调用。

```java
/**
 * 同步创建黑色前景色、白色背景色的二维码图片。该方法是耗时操作，请在子线程中调用。
 *
 * @param content 要生成的二维码图片内容
 * @param size    图片宽高，单位为px
 */
public static Bitmap syncEncodeQRCode(String content, int size)

/**
 * 同步创建指定前景色、白色背景色的二维码图片。该方法是耗时操作，请在子线程中调用。
 *
 * @param content         要生成的二维码图片内容
 * @param size            图片宽高，单位为px
 * @param foregroundColor 二维码图片的前景色
 */
public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor)

/**
 * 同步创建指定前景色、白色背景色、带logo的二维码图片。该方法是耗时操作，请在子线程中调用。
 *
 * @param content         要生成的二维码图片内容
 * @param size            图片宽高，单位为px
 * @param foregroundColor 二维码图片的前景色
 * @param logo            二维码图片的logo
 */
public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor, Bitmap logo)

/**
 * 同步创建指定前景色、指定背景色、带logo的二维码图片。该方法是耗时操作，请在子线程中调用。
 *
 * @param content         要生成的二维码图片内容
 * @param size            图片宽高，单位为px
 * @param foregroundColor 二维码图片的前景色
 * @param backgroundColor 二维码图片的背景色
 * @param logo            二维码图片的logo
 */
public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor, int backgroundColor, Bitmap logo)
```
##### 地址选择
>MainActivity.class

```  java 
  //方式一  
  BCCitySelectPop.Builder builder = new BCCitySelectPop.Builder();
  builder.setLineColor(R.color.cm_btn_orange_f)
         .setListener(this::showMessage)
         .setTextSize(16)
         .setTitleText("选择地址")
         .create(getAty())
         .show(getWindow().getDecorView());
  //方式二                      
  BCCitySelectPop pop = new BCCitySelectPop(getAty());
  pop.setListener(this::showMessage);
  pop.show(getWindow().getDecorView());
```
##### 圆形、圆角

>activity_circle_round.xml

``` xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_circle_round"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center_horizontal|center_vertical"
    android:orientation="vertical"
    tools:context="com.baichang.library.test.common.CircleRoundActivity">

    <com.baichang.android.widget.circleImageView.CircleImageView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:src="@mipmap/icon"
        app:civ_border_color="@color/app_btn_color"
        app:civ_border_width="2dp"
        app:civ_fill_color="@color/cm_black" />

    <com.baichang.android.widget.roundedImageView.RoundedImageView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_marginTop="16dp"
        android:scaleType="fitXY"
        android:src="@mipmap/icon"
        app:riv_border_color="@color/app_btn_color"
        app:riv_border_width="2dp"
        app:riv_corner_radius="10dp"
        app:riv_oval="false" />

    <com.baichang.android.widget.roundedImageView.RoundedImageView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_marginTop="16dp"
        android:scaleType="fitXY"
        android:src="@mipmap/icon"
        app:riv_border_color="@color/app_btn_color"
        app:riv_border_width="2dp"
        app:riv_oval="true" />
</LinearLayout>

```