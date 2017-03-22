package com.im.chat;

import android.app.Application;
import android.os.StrictMode;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.baidu.mapapi.SDKInitializer;
import com.im.chat.model.LeanchatUser;
import com.im.chat.service.PushManager;
import com.im.chat.util.LeanchatUserProvider;
import com.im.chat.util.Utils;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by cjxiao
 */
public class App extends Application {
  public static boolean debug = true;
  public static App ctx;

  @Override
  public void onCreate() {
    super.onCreate();
    ctx = this;
    Utils.fixAsyncTaskBug();

    String appId = "x3o016bxnkpyee7e9pa5pre6efx2dadyerdlcez0wbzhw25g";
    String appKey = "057x24cfdzhffnl3dzk14jh9xo2rq6w1hy1fdzt5tv46ym78";

    LeanchatUser.alwaysUseSubUserClass(LeanchatUser.class);

    //AVObject.registerSubclass(AddRequest.class);
    //AVObject.registerSubclass(UpdateInfo.class);

    // 节省流量
    AVOSCloud.setLastModifyEnabled(true);

    LCChatKit.getInstance().setProfileProvider(new LeanchatUserProvider());
    LCChatKit.getInstance().init(this, appId, appKey);

    PushManager.getInstance().init(ctx);
    AVOSCloud.setDebugLogEnabled(debug);
    AVAnalytics.enableCrashReport(this, !debug);
    initBaiduMap();
    if (App.debug) {
      openStrictMode();
    }
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
