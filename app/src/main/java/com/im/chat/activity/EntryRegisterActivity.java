package com.im.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.im.chat.App;
import com.im.chat.model.LeanchatUser;
import com.im.chat.util.Utils;

import cn.leancloud.chatkit.LCChatKit;

public class EntryRegisterActivity extends BaseActivity {
  View registerButton;
  EditText usernameEdit, passwordEdit, emailEdit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(com.im.chat.R.layout.entry_register_activity);
    findView();
    setTitle(App.ctx.getString(com.im.chat.R.string.register));
    registerButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        register();
      }
    });
  }

  private void findView() {
    usernameEdit = (EditText) findViewById(com.im.chat.R.id.usernameEdit);
    passwordEdit = (EditText) findViewById(com.im.chat.R.id.passwordEdit);
    emailEdit = (EditText) findViewById(com.im.chat.R.id.ensurePasswordEdit);
    registerButton = findViewById(com.im.chat.R.id.btn_register);
  }

  private void register() {
    final String name = usernameEdit.getText().toString();
    final String password = passwordEdit.getText().toString();
    String againPassword = emailEdit.getText().toString();
    if (TextUtils.isEmpty(name)) {
      Utils.toast(com.im.chat.R.string.username_cannot_null);
      return;
    }

    if (TextUtils.isEmpty(password)) {
      Utils.toast(com.im.chat.R.string.password_can_not_null);
      return;
    }
    if (!againPassword.equals(password)) {
      Utils.toast(com.im.chat.R.string.password_not_consistent);
      return;
    }

    LeanchatUser.signUpByNameAndPwd(name, password, new SignUpCallback() {
      @Override
      public void done(AVException e) {
        if (e != null) {
          Utils.toast(App.ctx.getString(com.im.chat.R.string.registerFailed) + e.getMessage());
        } else {
          Utils.toast(com.im.chat.R.string.registerSucceed);
          imLogin();
        }
      }
    });
  }

  private void imLogin() {
    LCChatKit.getInstance().open(LeanchatUser.getCurrentUserId(), new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        if (filterException(e)) {
          Intent intent = new Intent(EntryRegisterActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        }
      }
    });
  }

}
