package com.im.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.im.chat.R;
import cn.leancloud.chatkit.LCChatKit;
import com.im.chat.model.UserModel;
import com.im.chat.util.ChatConstants;
import com.im.chat.util.SpUtils;

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
                      if (filterException(e)) {
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
    //没登录跳到登陆页
    if (SpUtils.getString(this, ChatConstants.KEY_USERID).equals("")
        && SpUtils.getString(this, ChatConstants.KEY_TOKEN).equals("")) {
      handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
    } else {//如果用户已登陆，直接进入主页
      //UserModel.getCurrentUser().updateUserInfo();
      handler.sendEmptyMessageDelayed(GO_MAIN_MSG, SPLASH_DURATION);
    }
  }
}
