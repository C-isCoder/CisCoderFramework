# Android框架文档说明

## 更新说明：
 ### 2017-07-06
 
 * 新增请求框架 request2 模块，升级 Retrofit 版本，切换到 RxJava2 ，替换 Json 解析为 Gson ，优化
 `compile 'com.baichang.android.library:request2:2.0.0'`
 
 ### 2017-05-11
 
 * Utils 新增一个拼音转换类，HanziToPinyin 一个从Google官方源码提取的汉字转拼音。
 ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(string);
 返回的数据结构是一个HanziToPinyin.Token的ArrayList，HanziToPinyin.Token是HanziToPinyin中的一个公共静态外部类，
 其分别有type、source、target等三个成员变量，type是标识token的类型，有三种不同的取值1（拉丁文），2（拼音），3（未知），source是输入的中文，target则是中文转换后对应的拼音。
 
 ### 2017-05-09
 
 * 修复banner BUG
 
 ### 2017-05-06
 
 * 互动模块进本完成
 * 友盟分享大改版，升到最新版本，解决与微信支付的冲突。 
 可以设置分享的类型(BCUmUtil.setShareMedia(SHARE_MEDIA[] displayList //要分享的 ))
 
 ### 2017-04-14
 
 * request 网络请求 新增 网络不通畅的提示。
 * widget 新增 侧滑删除控件。 gitHub : https://github.com/mcxtzhang/SwipeDelMenuLayout
 
 ### 2017-04-06
 
 * 依赖路径优化，区分 library 和 model
 * 新增互动
 
 ### 2017-04-01
 
 * Utils 包移除EventBus的引用
 * maven 库地址，版本，账号，各种包的引用整理到config.gradle文件下统一管理。
 
 ### 2017-03-31
  
 * 适配Android 6.0 提高各个包版本兼容新控件
 * 友盟分享(BCUmUtil)从Utils包中剔除，移到umShare包下面，Utils包解除umShare包的依赖。
 * 移除common包中的EventBus依赖。
 * utils 跟 common 解除依赖，有些工具类中的移动到了Common下。
 * common 新增MVP基类和EventBusData
 * 新增 动态权限适配 permission
 * 修改崩溃日志存放目录（Android/data/包名/files/崩溃日志）绕过动态权限。
 * 修改照片墙保存路径，绕过动态权限。
 
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

* 网络请求1 `compile 'com.baichang.android.library:request:1.0.1'`
* 网络请求2  `compile 'com.baichang.android.library:request2:2.0.0'`
* 图片加载 `compile 'com.baichang.android.library:imageLoader:1.0.1'`
* 基础组件 `compile 'com.baichang.android.library:common:1.0.1'`
* 控件集合 `compile 'com.baichang.android.library:widget:1.0.1'`
* 工具集合 `compile 'com.baichang.android.library:utils:1.0.1'`
* 二维码   `compile 'com.baichang.android.library:qrcode:1.0.1'`
* 动态权限  `compile 'com.baichang.android.library:permission:1.0.0'`
* 基础配置  `compile 'com.baichang.android.library:config:1.0.0'`
* 佰昌互动  `compile 'com.baichang.android.module:interaction:0.0.1'`
* 友盟分享  `compile 'com.baichang.android.library:umShare:2.0.0'`

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