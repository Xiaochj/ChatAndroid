package com.im.chat.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.im.chat.model.LeanchatUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cjxiao
 * TODO
 * 1、本地存储
 * 2、避免内存、外存占用过多
 */
public class UserCacheUtils {

  public static final String KEY_USERID = "userid";
  public static final String KEY_TOKEN = "token";

  private static Map<String, LeanchatUser> userMap;

  static {
    userMap = new HashMap<String, LeanchatUser>();
  }

  public static LeanchatUser getCachedUser(String objectId) {
    return userMap.get(objectId);
  }

  public static boolean hasCachedUser(String objectId) {
    return userMap.containsKey(objectId);
  }

  public static void cacheUser(LeanchatUser user) {
    if (null != user && !TextUtils.isEmpty(user.getObjectId())) {
      userMap.put(user.getObjectId(), user);
    }
  }

  public static void cacheUsers(List<LeanchatUser> users) {
    if (null != users) {
      for (LeanchatUser user : users) {
        cacheUser(user);
      }
    }
  }

  public static void fetchUsers(List<String> ids) {
    fetchUsers(ids, null);
  }

  public static void fetchUsers(final List<String> ids, final CacheUserCallback cacheUserCallback) {
    Set<String> uncachedIds = new HashSet<String>();
    for (String id : ids) {
      if (!userMap.containsKey(id)) {
        uncachedIds.add(id);
      }
    }

    if (uncachedIds.isEmpty()) {
      if (null != cacheUserCallback) {
        cacheUserCallback.done(getUsersFromCache(ids), null);
        return;
      }
    }

    AVQuery<LeanchatUser> q = LeanchatUser.getQuery(LeanchatUser.class);
    q.whereContainedIn(Constants.OBJECT_ID, uncachedIds);
    q.setLimit(1000);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(new FindCallback<LeanchatUser>() {
      @Override
      public void done(List<LeanchatUser> list, AVException e) {
        if (null == e) {
          for (LeanchatUser user : list) {
            userMap.put(user.getObjectId(), user);
          }
        }
        if (null != cacheUserCallback) {
          cacheUserCallback.done(getUsersFromCache(ids), e);
        }
      }
    });
  }

  public static List<LeanchatUser> getUsersFromCache(List<String> ids) {
    List<LeanchatUser> userList = new ArrayList<LeanchatUser>();
    for (String id : ids) {
      if (userMap.containsKey(id)) {
        userList.add(userMap.get(id));
      }
    }
    return userList;
  }

  public static abstract class CacheUserCallback {
    public abstract void done(List<LeanchatUser> userList, Exception e);
  }

  /**
   * 保存String类型的数据
   * @param context
   * @param key
   * @param values
   */
  public static void putString(Context context, String key, String values) {
    SharedPreferences sp = context.getSharedPreferences("chatIm",Context.MODE_PRIVATE);
    sp.edit().putString(key,values).commit();

  }

  /**
   * 得到缓存数据
   * @param context
   * @param key
   * @return
   */
  public static String getString(Context context, String key) {
    SharedPreferences sp = context.getSharedPreferences("chatIm",Context.MODE_PRIVATE);

    return sp.getString(key,"");
  }
}
