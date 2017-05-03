package com.im.chat.service;

import android.content.Context;
import android.content.DialogInterface;
import com.im.chat.R;
import com.im.chat.engine.AppEngine;
import com.im.chat.model.BaseBean;
import com.im.chat.model.ContactListModel;
import com.im.chat.util.Utils;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 请求服务器拉去通讯录
 * Created by xiaochj on 2017/4/22.
 */

public class RequestContact {

  private static RequestContact instance = null;
  private RequestContactImpl requestContactImpl = null;

  public void setRequestContactListener(RequestContactImpl requestContactImpl) {
    this.requestContactImpl = requestContactImpl;
  }

  public interface RequestContactImpl {
    void onRequestContactListCallback(List<ContactListModel> contactListModel);
  }

  public static RequestContact getInstance() {
    synchronized (RequestContact.class) {
      if (instance == null) {
        synchronized (RequestContact.class) {
          instance = new RequestContact();
        }
      }
    }
    return instance;
  }

  public void getContactList(Context context) {
    AppEngine.getInstance()
        .getAppService()
        .getContactList(1, -1)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<BaseBean<List<ContactListModel>>>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            return;
          }

          @Override public void onNext(BaseBean<List<ContactListModel>> listBaseBean) {
            if (listBaseBean.getCode() == 1) {
              if (listBaseBean.getData() != null) {
                if (requestContactImpl != null) {
                  requestContactImpl.onRequestContactListCallback(listBaseBean.getData());
                }
              }
            }else if(listBaseBean.getCode() == 400){
              Utils.showInfoDialog(context, context.getString(R.string.sso_tip), new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                  RequestLogout.getInstance().logoutApp(context);
                }
              });
            }
          }
        });
  }
}
