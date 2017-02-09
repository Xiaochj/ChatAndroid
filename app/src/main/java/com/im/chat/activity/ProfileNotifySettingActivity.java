package com.im.chat.activity;

import android.os.Bundle;

/**
 * Created by lzw on 14-9-24.
 */
public class ProfileNotifySettingActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(com.im.chat.R.layout.profile_setting_notify_layout);
    setTitle(com.im.chat.R.string.profile_notifySetting);
  }
}
