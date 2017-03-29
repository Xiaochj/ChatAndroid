package com.im.chat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.im.chat.R;
import com.im.chat.engine.AppEngine;
import com.im.chat.engine.Urls;
import com.im.chat.model.BaseBean;
import com.im.chat.model.LeanchatUser;
import com.im.chat.model.LoginModel;
import com.im.chat.model.UserBean;
import com.im.chat.util.UserCacheUtils;
import com.im.chat.util.Utils;
import com.im.chat.view.HeaderLayout;

import butterknife.Bind;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登录页
 * created by cjxiao
 */
public class EntryLoginActivity extends BaseActivity {

  @Bind(com.im.chat.R.id.activity_login_et_username)
  public EditText userNameView;

  @Bind(com.im.chat.R.id.activity_login_et_password)
  public EditText passwordView;

  @Bind(R.id.title_layout)
  protected LinearLayout mHeaderLinearLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(com.im.chat.R.layout.entry_login_activity);
    HeaderLayout headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
    TextView tv = (TextView)headerLayout.findViewById(R.id.titleView);
    tv.setText(R.string.login_title);
  }

  @OnClick(com.im.chat.R.id.activity_login_btn_login)
  public void onLoginClick(View v) {
    login();
//    final ProgressDialog dialog = showSpinnerDialog();//test
//    final String name = userNameView.getText().toString().trim();
//    final String password = passwordView.getText().toString().trim();
//    loginLeanchat(name,password,dialog);
  }

  private void login() {
    final String name = userNameView.getText().toString().trim();
    final String password = passwordView.getText().toString().trim();

    if (TextUtils.isEmpty(name)) {
      Utils.toast(com.im.chat.R.string.username_cannot_null);
      return;
    }

    if (TextUtils.isEmpty(password)) {
      Utils.toast(com.im.chat.R.string.password_can_not_null);
      return;
    }

    final ProgressDialog dialog = showSpinnerDialog();
    //发送账号密码，请求自家服务器，验证通过
    //UserBean userBean = new UserBean(name,password);
    AppEngine.getInstance().getAppService().login(name,password).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseBean<LoginModel>>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        //自家服务器失败
        dialog.dismiss();
        Utils.toast(R.string.login_error);
        return;
      }

      @Override
      public void onNext(BaseBean<LoginModel> loginBean) {
        if(loginBean.getStatus() == 1) {
          if(loginBean.getData() != null) {
            //自家服务器成功，存取token和id,訪問leancloud的服務器
            UserCacheUtils.putString(getApplicationContext(), Urls.KEY_USERID, loginBean.getData().getId());
            UserCacheUtils.putString(getApplicationContext(), Urls.KEY_TOKEN, loginBean.getData().getToken());
            loginLeanchat(name, password, dialog);
          }
        }else{
          dialog.dismiss();
          Utils.toast(R.string.login_error);
        }
      }
    });
  }

  private void loginLeanchat(final String name, final String password,
      final ProgressDialog dialog) {
    LeanchatUser.logInInBackground(name, password, new LogInCallback<LeanchatUser>() {
      @Override
      public void done(LeanchatUser avUser, AVException e) {
        //请求leancloud服务器，如果登陆成功,直接进行实时通讯
        if (e == null) {
          dialog.dismiss();
          imLogin();
        }else{//如果登录失败，那么请求leancloud的注册接口，假装注册leancloud
          LeanchatUser.signUpByNameAndPwd(name, password, new SignUpCallback() {
            @Override
            public void done(AVException e) {
              dialog.dismiss();
              //再进行实时通讯
              if(e == null) {
                imLogin();
              }
            }
          });
        }
      }
    }, LeanchatUser.class);
  }

  /**
   * 因为 leancloud 实时通讯与账户体系是完全解耦的，所以此处需要先 LeanchatUser.logInInBackground
   * 如果验证账号密码成功，然后再 openClient 进行实时通讯
   */
  public void imLogin() {
    LCChatKit.getInstance().open(LeanchatUser.getCurrentUserId(), new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        if (filterException(e)) {
          Intent intent = new Intent(EntryLoginActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        }
      }
    });
  }
}
