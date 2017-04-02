package com.im.chat.friends;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import com.im.chat.model.UserModel;
import com.im.chat.util.ChatUserCacheUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wli on 15/12/1.
 */
public class FriendsManager {

  private static volatile List<String> friendIds = new ArrayList<String>();


  public static List<String> getFriendIds() {
    return friendIds;
  }

  public static void setFriendIds(List<String> friendList) {
    friendIds.clear();
    if (friendList != null) {
      friendIds.addAll(friendList);
    }
  }

  public static void fetchFriends(boolean isForce, final FindCallback<UserModel> findCallback) {
    AVQuery.CachePolicy policy =
      (isForce ? AVQuery.CachePolicy.NETWORK_ELSE_CACHE : AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
    UserModel.getCurrentUser().findFriendsWithCachePolicy(policy, new FindCallback<UserModel>() {
      @Override
      public void done(List<UserModel> list, AVException e) {
        if (null != e) {
          findCallback.done(null, e);
        } else {
          final List<String> userIds = new ArrayList<String>();
          for (UserModel user : list) {
            userIds.add(user.getId());
          }
          ChatUserCacheUtils.fetchUsers(userIds, new ChatUserCacheUtils.CacheUserCallback() {
            @Override
            public void done(List<UserModel> list1, Exception e) {
              setFriendIds(userIds);
              findCallback.done(list1, null);
            }
          });

        }
      }
    });
  }
}
