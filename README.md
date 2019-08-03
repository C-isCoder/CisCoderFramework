# Android框架文档说明

## 更新说明：

### 2019-01-08

* 修复 request 请求无网的时候报错提示。

* `compile 'com.baichang.android.library:request:1.0.7'`
 
### 2018-11-01

* 拼音转换工具修改

* `compile 'com.baichang.android.library:utils:1.0.3'`

### 2018-10-30

* widget 修复城市选择 popupwindow 弹窗显示异常。

* `compile 'com.baichang.android.library:widget:1.0.5'`

### 2018-09-14

* request 修改请求拦截，Header 添加 jwt

* `compile 'com.baichang.android.library:request:1.0.6''`

### 2018-09-05

* request、request2 移除 Logger 模块

* `compile 'com.baichang.android.library:request:1.0.4''`

* `compile 'com.baichang.android.library:request2:2.0.3''`


### 2018-08-24

* common 包移除对 acache 缓存目录的设置，移到继承类里面实现

* `compile 'com.baichang.android.library:common:1.1.5''`

### 2018-08-15

* request、request2 移除返回值 json 格式化，新增 debug 模式（此模式下才打印请求）

* request `compile 'com.baichang.android.library:request:1.0.3''`

* request2 `compile 'com.baichang.android.library:request2:2.0.2''`

### 2018-08-14

* config 添加 isDebug() 接口，`compile 'com.baichang.android.library:config:1.0.2'` 

### 2018-06-02

* 新增友盟推送聚合版，`compile 'com.baichang.android.library:push:0.0.1'` 
```java
 //UMConfigure.setLogEnabled(true);
    UMConfigure.init(this, "5a1bbaedf29d98279600014d", "Umeng", UMConfigure./DEVICE_TYPE_PHONE/,
        "dff89f7d4349f50f241df52a91b7ffd4");
    // miui
    //MiPushRegister.register(this, "2882303761517371822", "5781737196822");
    // huawei
    //HuaWeiRegister.register(this);

    final PushAgent mPushAgent = PushAgent.getInstance(this);
    //sdk开启通知声音
    mPushAgent.setNotificationPlaySound(MsgConstant./NOTIFICATION_PLAY_SDK_ENABLE/);
    // sdk关闭通知声音
    // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
    // 通知声音由服务端控制
     mPushAgent.setNotificationPlaySound(MsgConstant./NOTIFICATION_PLAY_SERVER/);
    handler = new Handler(getMainLooper());

    // 通知点击事件
    UmengNotificationClickHandler notificationClickHandler =
        new UmengNotificationClickHandler() {
            @Override public void dealWithCustomAction(Context context, UMessage msg) {
                Intent intent = new Intent(context, SmsDetailActivity.class);
                intent.putExtra(Flag./SMS_CONTACTS_PHONE/, msg.title);
                context.startActivity(intent);
                Logger.d(/TAG/, "UmengNotificationClickHandler");
            }
        };
    mPushAgent.setNotificationClickHandler(notificationClickHandler);

    // 自定义消息
    UmengMessageHandler customMessageHandler = new UmengMessageHandler() {
        @Override public Notification getNotification(Context context, UMessage msg) {
            Logger.i(/TAG/, "UMessage: " + msg.getRaw());
            String sender = msg.title;
            String content = msg.text;
            long time = System.currentTimeMillis();
            if (mSmsManager == null) {
                mSmsManager = new SmsManager(getApplication());
            }
            // 插入数据库
            mSmsManager.insertReceiverSms(sender, content, time);
            sendBroadcast(new Intent(BroadcastFlag./HAVE_A_NEW_MESSAGE/));
            // 返回默认
            return super.getNotification(context, msg);
        }

        @Override public void dealWithCustomMessage(final Context context, final UMessage msg) {
            handler.post(new Runnable() {
                @Override public void run() {
                    Logger.i(/TAG/, "Custom UMessage: " + msg.getRaw());
                    if (TextUtils.equals("下线通知", msg.title)) {
                        Logger.r(/TAG/, "被挤下线...");
                        Intent accApp = new Intent(BroadcastFlag./EXIT_EVENT/);
                        accApp.putExtra(Flag./IS_OFFLINE_EXIT/, true);
                        context.sendBroadcast(accApp);
                    } else {
                        try {
                            JSONObject json = new JSONObject(msg.extra.get("data"));
                            String type = json.getString("type");
                            final int id = json.getInt("id");
                            if (TextUtils.equals("错误报告", type)) {
                                Logger.r(/TAG/, "\"错误报告\",指令下发。" + msg.getRaw());
                                uploadLogFile(id, false);
                            } else if (TextUtils.equals("信令", type)) {
                                Logger.r(/TAG/, "\"信令\",指令下发。" + msg.getRaw());
                                String command = json.getString("command");
                                if (TextUtils.equals(command, "开启")) {
                                    long time = json.getLong("time");
                                    new CountDownTimer(time * 1000, 1000) {
                                        @Override public void onTick(long millisUntilFinished) {
                                            Logger.setWriteSipSwitch(true);
                                        }

                                        @Override public void onFinish() {
                                            Logger.setWriteSipSwitch(false);
                                            uploadLogFile(id, true);
                                        }
                                    }.start();
                                } else {
                                    Logger.setWriteSipSwitch(false);
                                    uploadLogFile(id, true);
                                }
                            }
                        } catch (JSONException e) {
                            Logger.r(/TAG/, "push json exception: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };
    mPushAgent.setMessageHandler(customMessageHandler);
    //注册推送服务 每次调用register都会回调该接口
    mPushAgent.register(new IUmengRegisterCallback() {
        @Override public void onSuccess(String deviceToken) {
            Logger.r(/TAG/, "umeng register success: " + deviceToken);
            sendBroadcast(new Intent(BroadcastFlag./UMENG_PUSH_LOAD_SUCCESS/));
        }

        @Override public void onFailure(String s, String s1) {
            Logger.r(/TAG/, "umeng register failed: " + s + " " + s1);
        }
    });
}

```

### 2018-05-23

* common 升级到 1.0.5 `compile 'com.baichang.android.library:common:1.0.5'`

* 修复，小米市场 Acache 空指针，被拒。

### 2018-05-22

* 新增一个没有小视频的互动 `com.baichang.android.module:interaction_no_video:0.0.1`

* no video 可设置不显示互动的标题 `setIsPublishNoTitle(true)`

* no videw 列表可以直接点赞


### 2018-04-09

* 升级 logger 框架到最新版本。

* 使用方式：删除本地 gradle 缓存的 request 包，重新导入，在 Application 中初始化。

```java
private void initLogger() {
    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .methodOffset(7)
            .tag("REQUEST") // 自定义标签
  .build();
  Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
        @Override public boolean isLoggable(int priority, @Nullable String tag) {
            return BuildConfig.DEBUG;
  }
    }); }
```
### 2018-04-02

* 修复互动拍摄小视频不能使用非压缩模式。 （0.0.4 版本默认不压缩了）

* 升级版本`compile 'com.baichang.android.module:interaction:0.0.4'`

### 2018-03-30

* 修复 app 升级版本 bug。

* 升级版本： `compile 'com.baichang.android.library:common:1.0.4'`

### 2018-03-27 
  
* 修复打印机 bug，升级版本到 

`compile 'com.baichang.android.library:printerKit:0.0.2'`

* 使用方法

```java
public void print(View view) {
    HomeAllData data = new Gson().fromJson(json, HomeAllData.class);
  // 构建 Intent 数据
  Intent intent = new Intent(this, PrintService.class);
  // 打印模式 PrintService.MODEL.NORMAL 正常打印模式（默认） PrintService.MODEL.TEST 测试打印机
  intent.putExtra(PrintService.PRINT_MODEL, PrintService.MODEL.NORMAL);
  // 连接模式 (PrintService.TYPE.WIFI wifi PrintService.TYPE.BLUE 蓝牙  默认是蓝牙)
  intent.putExtra(PrintService.CONNECT_TYPE, PrintService.TYPE.WIFI);
  // 蓝牙地址 蓝牙模式必须传(设置打印机的时候 存储到本地，如果没有 提示去设置打印机)
 // Wifi 模式不需要传入地址，用户若没有连接到 wifi 会有提示，在调用 wifi 模式之前检查手机是否经连 wifi  
  intent.putExtra(PrintService.BLUETOOTH_ADDRESS, "DC:0D:30:27:0A:64");
  // Test 模式可以不传要打印的数据 * 正常模式必传。 （打印数据格式 为Vector<Byte>） 数据格式参考 官方DEMO
  intent.putExtra(PrintService.PRINT_DATA, new PrintData(TestPrintText.sendReceipt(data)));
  // 启动服务 自动打印。
  startService(intent); 
}
```
  
### 2018-03-26
  
* 修复 App 更新 bug（7.0+ 报解析包错误）

* 需要更新 基础库（common）到 1.0.3 版本

`compile 'com.baichang.android.library:common:1.0.3'`

* AndroidManifest.xml <Application> 标签 添加 FileProvider。

```xml
   <!--app更新自动安装-->
      <provider
          android:name="android.support.v4.content.FileProvider"
          android:authorities="${applicationId}.provider"
          android:exported="false"
          android:grantUriPermissions="true">
          <meta-data
               android:name="android.support.FILE_PROVIDER_PATHS"
               android:resource="@xml/file_paths" />
       </provider>
```
* 资源文件添加 res/xml/file_paths.xml

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <paths>
            <external-path path="" name="update"/>
        </paths>
    </resources>
```
* 区分 bug 版本 checkUpdateInfo() 方法改名 update()
  
```java
   private void checkAppVersion() {
           Map<String, String> map = new HashMap<>();
           map.put("platform", "1");
           map.put("version", BCToolsUtil.getVersionCode(this));
           APIWrapper.getInstance()
                   .checkAppVersion(map)
                   .compose(HttpSubscriber.<CheckVersionData>applySchedulers(this))
                   .subscribe(new HttpSubscriber<>(new HttpSuccessListener<CheckVersionData>() {
                       @Override public void success(CheckVersionData checkVersionData) {
                           new BCAppUpdateManager(getAty(), checkVersionData.url,
                                   checkVersionData.updateInfo,
                                   checkVersionData.isForce.equals("1")).update();
                       }
                   }, new HttpErrorListener() {
                       @Override public void error(Throwable throwable) {
   
                       }
                   }));
       }
```
  
### 2018-03-12
  
* 打印机新增 wifi 打印模式   

* 使用方法 
  
```java
   // app/build.gradle 导入
   'com.baichang.android.library:printerKit:0.0.1'
        
   // eg 
   public void print(View view) {
           // 构建 Intent 数据
           Intent intent = new Intent(this, PrintService.class);
           // 打印模式 PrintService.MODEL.NORMAL 正常打印模式（默认） PrintService.MODEL.TEST 测试打印机
           intent.putExtra(PrintService.PRINT_MODEL, PrintService.MODEL.TEST);
           // 连接模式 (PrintService.TYPE.WIFI wifi PrintService.TYPE.BLUE 蓝牙  默认是蓝牙)
           intent.putExtra(PrintService.CONNECT_TYPE, PrintService.TYPE.WIFI);
           // 蓝牙地址 蓝牙模式必须传(设置打印机的时候 存储到本地，如果没有 提示去设置打印机) 
           // Wifi 模式不需要传入地址，用户若没有连接到 wifi 会有提示，在调用 wifi 模式之前最好检查手机是否已经连 wifi
           intent.putExtra(PrintService.BLUETOOTH_ADDRESS, "DC:0D:30:27:0A:64");
           // Test 模式可以不传要打印的数据 * 正常模式必传。 （打印数据格式 为Vector<Byte>） 数据格式参考 官方DEMO
           intent.putExtra(PrintService.PRINT_DATA, new Vector<Byte>());
           // 启动服务 自动打印。
           startService(intent);
   }
```
    
### 2018-03-01
  
* 新增 printerKit 佳博打印机SDK封装

* 使用方法 

```java
    // app/build.gradle 导入
    'com.baichang.android.library:printerKit:0.0.1'
    
    // eg 
    public void print(View view) {
            // 构建 Intent 数据
            Intent intent = new Intent(this, PrintService.class);
            // 打印模式 PrintService.MODEL.NORMAL 正常打印模式（默认） PrintService.MODEL.TEST 测试打印机
            intent.putExtra(PrintService.PRINT_MODEL, PrintService.MODEL.TEST);
            // 蓝牙地址 必须传(设置打印机的时候 存储到本地，如果没有 提示去设置打印机)
            intent.putExtra(PrintService.BLUETOOTH_ADDRESS, "DC:0D:30:27:0A:64");
            // Test 模式可以不传要打印的数据 * 正常模式必传。 （打印数据格式 为Vector<Byte>） 数据格式参考 官方DEMO
            intent.putExtra(PrintService.PRINT_DATA, new Vector<Byte>());
            // 启动服务 自动打印。
            startService(intent);
    }
```
* 官方 Demo 打印数据构造
   
```java
   /**
        * 发送票据
        */
     public Vector<Byte> sendReceiptWithResponse() {
           EscCommand esc = new EscCommand();
           esc.addInitializePrinter();
           esc.addPrintAndFeedLines((byte) 3);
           // 设置打印居中
           esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
           // 设置为倍高倍宽
           esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
           // 打印文字
           esc.addText("Sample\n");
           esc.addPrintAndLineFeed();
   
   		/* 打印文字 */
           // 取消倍高倍宽
           esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
           // 设置打印左对齐
           esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
           // 打印文字
           esc.addText("Print text\n");
           // 打印文字
           esc.addText("Welcome to use SMARNET printer!\n");
   
   		/* 打印繁体中文 需要打印机支持繁体字库 */
           String message = "佳博智匯票據打印機\n";
           esc.addText(message, "GB2312");
           esc.addPrintAndLineFeed();
   
   		/* 绝对位置 具体详细信息请查看GP58编程手册 */
           esc.addText("智汇");
           esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
           esc.addSetAbsolutePrintPosition((short) 6);
           esc.addText("网络");
           esc.addSetAbsolutePrintPosition((short) 10);
           esc.addText("设备");
           esc.addPrintAndLineFeed();
   
   		/* 打印图片 */
           // 打印文字
           esc.addText("Print bitmap!\n");
           Bitmap b = BitmapFactory.decodeResource(getResources(),
                   R.mipmap.gprinter);
           // 打印图片
           esc.addOriginRastBitImage(b, 384, 0);
   
   		/* 打印一维条码 */
           // 打印文字
           esc.addText("Print code128\n");
           esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);
           // 设置条码可识别字符位置在条码下方
           // 设置条码高度为60点
           esc.addSetBarcodeHeight((byte) 60);
           // 设置条码单元宽度为1
           esc.addSetBarcodeWidth((byte) 1);
           // 打印Code128码
           esc.addCODE128(esc.genCodeB("SMARNET"));
           esc.addPrintAndLineFeed();
   
   		/*
            * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
   		 */
           // 打印文字
           esc.addText("Print QRcode\n");
           // 设置纠错等级
           esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31);
           // 设置qrcode模块大小
           esc.addSelectSizeOfModuleForQRCode((byte) 3);
           // 设置qrcode内容
           esc.addStoreQRCodeData("www.smarnet.cc");
           esc.addPrintQRCode();// 打印QRCode
           esc.addPrintAndLineFeed();
   
           // 设置打印左对齐
           esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
           //打印文字
           esc.addText("Completed!\r\n");
   
           // 开钱箱
           esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
           esc.addPrintAndFeedLines((byte) 8);
           // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
           esc.addQueryPrinterStatus();
           Vector<Byte> datas = esc.getCommand();
           retrun datas;
       }

```
  
### 2018-01-24
 
* 升级 compileSdkVersion 版本至 26

* 升级 buildToolsVersion 版本至 "26.0.0"

* 升级 v7 版本至 "26.1.0"

* 升级 v4 版本至 "26.1.0"

* 升级 recyclerview 版本至 "26.1.0"

* 升级 design 版本至 "26.1.0"

 
### 2017-11-25

* 互动新增发送小视频功能。
  
* 项目需要依赖以下3个：

 1. `com.baichang.android.library:interaction:0.0.2`
  
 2. `tv.danmaku.ijk.media:ijkplayer-java:0.8.4`
  
 3. `tv.danmaku.ijk.media:ijkplayer-armv7a:0.8.4`
  
* ndk 设置如下:
  
```
ndk {
  //选择要添加的对应cpu类型的.so库。
  abiFilters 'armeabi', 'x86','armeabi-v7a'
  // 还可以添加 'x86', 'x86_64', 'mips', 'mips64'
}
```

### 2017-07-06
 
* 新增请求框架 request2 模块，升级 Retrofit 版本，切换到 RxJava2 ，替换 Json 解析为 Gson ，优化

`compile 'com.baichang.android.library:request2:2.0.0'`
   
### 2017-05-11
 
* Utils 新增一个拼音转换类，HanziToPinyin 一个从Google官方源码提取的汉字转拼音。
   
```java
ArrayList<HanziToPinyin.Token> tokens =HanziToPinyin.getInstance().get(string);
```
` 返回的数据结构是一个HanziToPinyin.Token的ArrayList，HanziToPinyin.Token是HanziToPinyin中的一个公共静态外部类，其分别有type、source、target等三个成员变量，type是标识token的类型，有三种不同的取值1（拉丁文），2（拼音），3（未知），source是输入的中文，target则是中文转换后对应的拼音。`
   
### 2017-05-09
 
* 修复banner BUG
 
### 2017-05-06
 
* 互动模块进本完成

* 友盟分享大改版，升到最新版本，解决与微信支付的冲突。 
   可以设置分享的类型

```java
BCUmUtil.setShareMedia(SHARE_MEDIA[] displayList //要分享的 )
```
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
  
```java
  allprojects {
      repositories {
          jcenter()
          mavenCentral()
          maven { url "https://jitpack.io" }
          maven { url "http://svn.weiidu.com:8081/nexus/content/repositories/android/" }
      }
  }
```
* 网络请求1 `compile 'com.baichang.android.library:request:1.0.2'`

* 网络请求2  `compile 'com.baichang.android.library:request2:2.0.1'`

* 图片加载 `compile 'com.baichang.android.library:imageLoader:1.0.2'`

* 基础组件 `compile 'com.baichang.android.library:common:1.0.4'`

* 控件集合 `compile 'com.baichang.android.library:widget:1.0.5'`

* 工具集合 `compile 'com.baichang.android.library:utils:1.0.2'`

* 二维码   `compile 'com.baichang.android.library:qrcode:1.0.2'`

* 动态权限  `compile 'com.baichang.android.library:permission:1.0.1'`

* 基础配置  `compile 'com.baichang.android.library:config:1.0.1'`

* 佰昌互动  `compile 'com.baichang.android.module:interaction:0.0.4'`**可发小视频**

* 佰昌互动  `compile 'com.baichang.android.module:interaction:0.0.1'`**无小视频**

* 友盟分享  `compile 'com.baichang.android.library:umShare:2.0.1'`

* 打印机  `compile 'com.baichang.android.library:printerKit:0.0.2'`

## 项目初始化配置

* 创建App类，继承BCApplication，并实现Configuration接口。

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

