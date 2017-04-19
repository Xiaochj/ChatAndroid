package com.im.chat;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.baidu.mapapi.SDKInitializer;
import com.im.chat.engine.AppEngine;
import com.im.chat.model.UserModel;
import com.im.chat.service.NetworkCheckReceiver;
import com.im.chat.service.PushManager;
import com.im.chat.util.ChatUserProvider;
import com.im.chat.util.Utils;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by cjxiao
 */
public class App extends MultiDexApplication {
  public static boolean debug = true;
  public static App ctx;
  private NetworkCheckReceiver networkCheckReceiver;

  @Override
  public void onCreate() {
    super.onCreate();
    ctx = this;

    //注册网络监听器
    registerNetworkCheckReceiver();

    Utils.fixAsyncTaskBug();

    //测试appid 和 appkey
    //String appId = "x3o016bxnkpyee7e9pa5pre6efx2dadyerdlcez0wbzhw25g";
    //String appKey = "057x24cfdzhffnl3dzk14jh9xo2rq6w1hy1fdzt5tv46ym78";

    String appId = "PJ6AMkl0FiKa0tuxwFmkXEWS-gzGzoHsz";
    String appKey = "ltePzMqNsy3Whpcvfmz1Rpu6";

    AppEngine.init(getApplicationContext());

    UserModel.alwaysUseSubUserClass(UserModel.class);

    //AVObject.registerSubclass(AddRequest.class);
    //AVObject.registerSubclass(UpdateInfo.class);

    // 节省流量
    AVOSCloud.setLastModifyEnabled(true);

    LCChatKit.getInstance().init(this, appId, appKey);
    LCChatKit.getInstance().setProfileProvider(ChatUserProvider.getInstance());
    PushManager.getInstance().init(ctx);
    AVOSCloud.setDebugLogEnabled(debug);
    AVAnalytics.enableCrashReport(this, !debug);
    initBaiduMap();
    if (App.debug) {
      openStrictMode();
    }
  }

  @Override public void onTerminate() {
    super.onTerminate();
    //反注册网络监听
    if(networkCheckReceiver != null) {
      unregisterReceiver(networkCheckReceiver);
    }
  }

  private void registerNetworkCheckReceiver(){
    networkCheckReceiver = new NetworkCheckReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(networkCheckReceiver,filter);
  }

  public void openStrictMode() {
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()   // or .detectAll() for all detectable problems
            .penaltyLog()
            .build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
            .detectLeakedSqlLiteObjects()
            .detectLeakedClosableObjects()
            .penaltyLog()
            //.penaltyDeath()
            .build());
  }

  private void initBaiduMap() {
    SDKInitializer.initialize(this);
  }
}
