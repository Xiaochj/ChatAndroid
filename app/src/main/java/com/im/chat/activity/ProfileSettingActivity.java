package com.im.chat.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.im.chat.R;
import com.im.chat.view.HeaderLayout;

import butterknife.Bind;

/**
 * 提醒设置页
 * Created by lzw on 14-9-24.
 */
public class ProfileSettingActivity extends BaseActivity {

  @Bind(R.id.title_layout)
  protected LinearLayout mHeaderLinearLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(com.im.chat.R.layout.profile_setting_notify_layout);
    HeaderLayout headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
    headerLayout.showLeftBackButton(R.string.profile_notifySetting,null);
  }
}
