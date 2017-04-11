package com.im.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.KeyEvent;
import cn.leancloud.chatkit.utils.SpUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.im.chat.R;
import cn.leancloud.chatkit.LCChatKit;
import com.im.chat.service.NetworkCheckReceiver;
import com.im.chat.util.ChatConstants;
import com.im.chat.util.Connectivity;
import com.im.chat.util.Utils;

/**
 * 欢迎页
 */
public class EntrySplashActivity extends BaseActivity {

  public static final int SPLASH_DURATION = 2000;
  private static final int GO_MAIN_MSG = 1;
  private static final int GO_LOGIN_MSG = 2;

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case GO_MAIN_MSG:
          LCChatKit.getInstance()
              .open(SpUtils.getString(EntrySplashActivity.this, ChatConstants.KEY_USERID),
                  new AVIMClientCallback() {
                    @Override public void done(AVIMClient avimClient, AVIMException e) {
                      if (e == null) {
                        Intent intent = new Intent(EntrySplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                      }
                    }
                  });
          break;
        case GO_LOGIN_MSG:
          Intent intent = new Intent(EntrySplashActivity.this, EntryLoginActivity.class);
          EntrySplashActivity.this.startActivity(intent);
          finish();
          break;
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.entry_splash_layout);
    //判断联网了没
    if(Connectivity.isConnected(this)) {
      //没登录跳到登陆页
      if (SpUtils.getString(this, ChatConstants.KEY_USERID).equals("") && SpUtils.getString(this,
          ChatConstants.KEY_TOKEN).equals("")) {
        handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
      } else {//如果用户已登陆，直接进入主页
        //UserModel.getCurrentUser().updateUserInfo();
        handler.sendEmptyMessageDelayed(GO_MAIN_MSG, SPLASH_DURATION);
      }
    }else{//没联网，跳转到登陆页
      handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  /**
   * 禁用返回键中断
   */
  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      return true;
    }
    return false;
  }
}
