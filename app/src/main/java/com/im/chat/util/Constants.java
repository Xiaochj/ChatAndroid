package com.im.chat.util;

/**
 * Created by wli on 15/8/23.
 * 用来存放各种 static final 值
 */
public class Constants {

  public static final String CONTACT_USER = getPrefixConstant("contact_user");//通讯录的item对象
  public static final String USER_ID = getPrefixConstant("user_id");//用户的唯一标示

  public static final String OBJECT_ID = "objectId";
  public static final int PAGE_SIZE = 10;
  public static final String CREATED_AT = "createdAt";
  public static final String UPDATED_AT = "updatedAt";


  public static final int ORDER_UPDATED_AT = 1;
  public static final int ORDER_DISTANCE = 0;

  private static final String LEANMESSAGE_CONSTANTS_PREFIX = "com.avoscloud.leanchatlib.";

  public static final String INVITATION_ACTION = "invitation_action";

  public static final String INTENT_KEY = getPrefixConstant("intent_key");
  public static final String INTENT_VALUE = getPrefixConstant("intent_value");

  //Notification
  public static final String NOTOFICATION_TAG = getPrefixConstant("notification_tag");
  public static final String NOTIFICATION_SINGLE_CHAT = Constants.getPrefixConstant("notification_single_chat");
  public static final String NOTIFICATION_GROUP_CHAT = Constants.getPrefixConstant("notification_group_chat");
  public static final String NOTIFICATION_SYSTEM = Constants.getPrefixConstant("notification_system_chat");

  public static String getPrefixConstant(String str) {
    return LEANMESSAGE_CONSTANTS_PREFIX + str;
  }
}
