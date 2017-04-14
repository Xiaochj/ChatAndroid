package com.im.chat.util;

import android.text.TextUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.im.chat.model.UserModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiaochj on 2017/3/31.
 *
 * 1、本地存储
 * 2、避免内存、外存占用过多
 */

public class ChatUserCacheUtils {

  private static Map<String, UserModel> userMap;

  static {
    userMap = new HashMap<String, UserModel>();
  }

  //public static UserModel getCachedUser(String id) {
  //  return userMap.get(id);
  //}
  //
  //public static boolean hasCachedUser(String id) {
  //  return userMap.containsKey(id);
  //}

  public static void cacheUser(UserModel user) {
    if (null != user && !TextUtils.isEmpty(user.getId())) {
      userMap.put(user.getId(), user);
    }
  }

  //public static void cacheUsers(List<UserModel> users) {
  //  if (null != users) {
  //    for (UserModel user : users) {
  //      cacheUser(user);
  //    }
  //  }
  //}

  //public static void fetchUsers(List<String> ids) {
  //  fetchUsers(ids, null);
  //}

  //public static void fetchUsers(final List<String> ids, final ChatUserCacheUtils.CacheUserCallback cacheUserCallback) {
  //  Set<String> uncachedIds = new HashSet<String>();
  //  for (String id : ids) {
  //    if (!userMap.containsKey(id)) {
  //      uncachedIds.add(id);
  //    }
  //  }
  //
  //  if (uncachedIds.isEmpty()) {
  //    if (null != cacheUserCallback) {
  //      cacheUserCallback.done(getUsersFromCache(ids), null);
  //      return;
  //    }
  //  }
  //
  //  AVQuery<UserModel> q = UserModel.getQuery(UserModel.class);
  //  q.whereContainedIn(ChatConstants.OBJECT_ID, uncachedIds);
  //  q.setLimit(1000);
  //  q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
  //  q.findInBackground(new FindCallback<UserModel>() {
  //    @Override
  //    public void done(List<UserModel> list, AVException e) {
  //      if (null == e) {
  //        for (UserModel user : list) {
  //          userMap.put(user.getId(), user);
  //        }
  //      }
  //      if (null != cacheUserCallback) {
  //        cacheUserCallback.done(getUsersFromCache(ids), e);
  //      }
  //    }
  //  });
  //}

  //public static List<UserModel> getUsersFromCache(List<String> ids) {
  //  List<UserModel> userList = new ArrayList<UserModel>();
  //  for (String id : ids) {
  //    if (userMap.containsKey(id)) {
  //      userList.add(userMap.get(id));
  //    }
  //  }
  //  return userList;
  //}
  //
  //public static abstract class CacheUserCallback {
  //  public abstract void done(List<UserModel> userList, Exception e);
  //}



}
