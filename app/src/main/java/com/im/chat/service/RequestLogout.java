package com.im.chat.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.utils.SpUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.im.chat.activity.EntryLoginActivity;
import com.im.chat.engine.AppEngine;
import com.im.chat.model.BaseBean;
import com.im.chat.model.UserModel;
import com.im.chat.util.ChatConstants;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 退出app
 * Created by xiaochj on 2017/5/3.
 */

public class RequestLogout {

  private static RequestLogout instance = null;

  public static RequestLogout getInstance() {
    synchronized (RequestLogout.class) {
      if (instance == null) {
        synchronized (RequestContact.class) {
          instance = new RequestLogout();
        }
      }
    }
    return instance;
  }

  public void logoutApp(Context context) {
    if (LCChatKit.getInstance().getCurrentUserId() != null) {
      LCChatKit.getInstance().close(new AVIMClientCallback() {
        @Override public void done(AVIMClient avimClient, AVIMException e) {
        }
      });
    }
    PushManager.getInstance().unsubscribeCurrentUserChannel();
    UserModel.logOut();
    //登出自己的服务器
    AppEngine.getInstance()
        .getAppService()
        .logout()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<BaseBean>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            return;
          }

          @Override public void onNext(BaseBean baseBean) {
            if (baseBean.getCode() == 1) {
              //清除本地userid和token缓存
              SpUtils.putString(context, ChatConstants.KEY_USERID, "");
              SpUtils.putString(context, ChatConstants.KEY_TOKEN, "");
              ((Activity) context).finish();
              Intent intent = new Intent(context, EntryLoginActivity.class);
              context.startActivity(intent);
            }
          }
        });
  }
}
