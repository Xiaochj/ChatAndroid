package com.im.chat.util;

import com.im.chat.R;
import com.im.chat.engine.AppEngine;
import com.im.chat.model.BaseBean;
import com.im.chat.model.ContactListModel;
import com.im.chat.model.UserModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 用户体系，将自己的用户体系和LeanCloud的用户体系串联
 * Created by cjxiao
 */
public class ChatUserProvider implements LCChatProfileProvider {

  private static ChatUserProvider customUserProvider;

  private static List<ContactListModel> partUsers = new ArrayList<>();
  private static List<LCChatKitUser> lcChatKitUsers = new ArrayList<>();
  private static List<HashMap<String, ContactListModel>> idToPartUsers = new ArrayList<>();

  public synchronized static ChatUserProvider getInstance() {
    if (null == customUserProvider) {
      customUserProvider = new ChatUserProvider();
    }
    return customUserProvider;
  }

  private ChatUserProvider() {
  }

  @Override public void fetchProfiles(List<String> list, final LCChatProfilesCallBack callBack) {
    List<LCChatKitUser> userList = new ArrayList<>();
    for (String userId : list) {
      for (LCChatKitUser user : lcChatKitUsers) {
        if (user.getUserId().equals(userId)) {
          userList.add(user);
          break;
        }
      }
    }
    callBack.done(userList, null);
  }

  public List<ContactListModel> getAllUsers() {
    return partUsers;
  }

  public List<LCChatKitUser> getLcChatKitUsers() {
    return lcChatKitUsers;
  }

  //public List<HashMap<String,ContactListModel>> getIdToPartUsers(){
  //  return idToPartUsers;
  //}

}
