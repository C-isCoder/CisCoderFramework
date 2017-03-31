# Android框架文档说明

## 更新说明：

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

* 网络请求 `compile 'com.baichang.android:request:1.0.1'`
* 图片加载 `compile 'com.baichang.android:imageLoader:1.0.1'`
* 基础组件 `compile 'com.baichang.android:common:1.0.1'`
* 控件集合 `compile 'com.baichang.android:widget:1.0.1'`
* 工具集合 `compile 'com.baichang.android:utils:1.0.1'`
* 二维码   `compile 'com.baichang.android:qrcode:1.0.1'`
* 动态权限  `compile 'com.baichang.android:permission:1.0.0'`
* 基础配置  `compile 'com.baichang.android:config:1.0.0'`

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