package com.im.chat.model;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 登陆的model
 * Created by xiaochj on 2017/3/27.
 */

public class UserModel extends AVUser {

  String head;
  String name;
  String mobile;
  String id;
  String token;
  String type;
  String initials;
  String sex;
  String mail;
  String signature;

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getInitials() {
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public String getHead() {
    return head;
  }

  public void setHead(String head) {
    this.head = head;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public static final String USERNAME = "username";
  public static final String AVATAR = "avatar";
  public static final String LOCATION = "location";
  public static final String INSTALLATION = "installation";

  public static String getCurrentUserId() {
    UserModel currentUser = getCurrentUser(UserModel.class);
    return (null != currentUser ? currentUser.getId() : null);
  }

  public String getAvatarUrl() {
    AVFile avatar = getAVFile(AVATAR);
    if (avatar != null) {
      return avatar.getUrl();
    } else {
      return null;
    }
  }

  public void saveAvatar(String path, final SaveCallback saveCallback) {
    final AVFile file;
    try {
      file = AVFile.withAbsoluteLocalPath(getUsername(), path);
      put(AVATAR, file);
      file.saveInBackground(new SaveCallback() {
        @Override public void done(AVException e) {
          if (null == e) {
            saveInBackground(saveCallback);
          } else {
            if (null != saveCallback) {
              saveCallback.done(e);
            }
          }
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static UserModel getCurrentUser() {
    return getCurrentUser(UserModel.class);
  }

  public void updateUserInfo() {
    AVInstallation installation = AVInstallation.getCurrentInstallation();
    if (installation != null) {
      put(INSTALLATION, installation);
      saveInBackground();
    }
  }

  public AVGeoPoint getGeoPoint() {
    return getAVGeoPoint(LOCATION);
  }

  public void setGeoPoint(AVGeoPoint point) {
    put(LOCATION, point);
  }

  public static void signUpByNameAndPwd(String name, String password, SignUpCallback callback) {
    AVUser user = new AVUser();
    user.setUsername(name);
    user.setPassword(password);
    user.signUpInBackground(callback);
  }

  public void removeFriend(String friendId, final SaveCallback saveCallback) {
    unfollowInBackground(friendId, new FollowCallback() {
      @Override public void done(AVObject object, AVException e) {
        if (saveCallback != null) {
          saveCallback.done(e);
        }
      }
    });
  }

  public void findFriendsWithCachePolicy(AVQuery.CachePolicy cachePolicy,
      FindCallback<UserModel> findCallback) {
    AVQuery<UserModel> q = null;
    try {
      q = followeeQuery(UserModel.class);
    } catch (Exception e) {
    }
    q.setCachePolicy(cachePolicy);
    q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
    q.findInBackground(findCallback);
  }
}
