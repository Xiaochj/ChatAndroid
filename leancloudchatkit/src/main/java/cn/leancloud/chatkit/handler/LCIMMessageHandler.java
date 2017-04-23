package cn.leancloud.chatkit.handler;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import cn.leancloud.chatkit.utils.SpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMLogUtils;
import cn.leancloud.chatkit.utils.LCIMNotificationUtils;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangxiaobo on 15/4/20.
 * AVIMTypedMessage 的 handler，socket 过来的 AVIMTypedMessage 都会通过此 handler 与应用交互
 * 需要应用主动调用 AVIMMessageManager.registerMessageHandler 来注册
 * 当然，自定义的消息也可以通过这种方式来处理
 */
public class LCIMMessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

  private Context context;
  String notificationContent = "";

  public LCIMMessageHandler(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
    if (message == null || message.getMessageId() == null) {
      LCIMLogUtils.d("may be SDK Bug, message or message id is null");
      return;
    }

    if (LCChatKit.getInstance().getCurrentUserId() == null) {
      LCIMLogUtils.d("selfId is null, please call LCChatKit.open!");
      client.close(null);
    } else {
      if (!client.getClientId().equals(LCChatKit.getInstance().getCurrentUserId())) {
        client.close(null);
      } else {
        if (!message.getFrom().equals(client.getClientId())) {
          if (LCIMNotificationUtils.isShowNotification(conversation.getConversationId())) {
            sendNotification(message, conversation);
          }
          LCIMConversationItemCache.getInstance().increaseUnreadCount(message.getConversationId());
          sendEvent(message, conversation);
        } else {
          LCIMConversationItemCache.getInstance().insertConversation(message.getConversationId());
        }
      }
    }
  }

  @Override
  public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
    super.onMessageReceipt(message, conversation, client);
  }

  /**
   * 发送消息到来的通知事件
   *
   * @param message
   * @param conversation
   */
  private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation) {
    LCIMIMTypeMessageEvent event = new LCIMIMTypeMessageEvent();
    event.message = message;
    event.conversation = conversation;
    EventBus.getDefault().post(event);
  }

  private void sendNotification(final AVIMTypedMessage message, final AVIMConversation conversation) {
    if (null != conversation && null != message) {
      notificationContent = "";
      if(message instanceof AVIMTextMessage){
        if(((AVIMTextMessage) message).getAttrs() != null){
          //如果是ios发来的信息
          if (((AVIMTextMessage) message).getAttrs().containsKey("username")
              && ((AVIMTextMessage) message).getAttrs().containsKey("conversationType")) {
            iosTextMessage(message,conversation);
          }else {
            androidTextMessage(message,conversation);
          }
        }else{
          notificationContent = ((AVIMTextMessage) message).getText();
          LCIMConstants.isSomeoneAtYou = false;
        }
      }else{
        notificationContent = context.getString(R.string.lcim_unspport_message_type);
        LCIMConstants.isSomeoneAtYou = false;
      }
      //final String notificationContent = message instanceof AVIMTextMessage ?
      //  ((AVIMTextMessage) message).getText() : context.getString(R.string.lcim_unspport_message_type);
      LCIMProfileCache.getInstance().getCachedUser(message.getFrom(), new AVCallback<LCChatKitUser>() {
        @Override
        protected void internalDone0(LCChatKitUser userProfile, AVException e) {
          if (e != null) {
            LCIMLogUtils.logException(e);
          } else if (null != userProfile) {
            String title = userProfile.getUserName();
            Intent intent = getIMNotificationIntent(conversation.getConversationId(), message.getFrom());
            LCIMNotificationUtils.showNotification(context, title, notificationContent, null, intent);
          }
        }
      });
    }
  }

  private void androidTextMessage(final AVIMTypedMessage message,AVIMConversation conversation){
    List<Object> atStrList = Arrays.asList(((AVIMTextMessage) message).getAttrs().get("at_person_detail"));
    JSONArray jsonArray0 = JSON.parseArray(atStrList.toString());
    List<String> atStringList0 = new ArrayList<>();
    for (int i = 0; i < jsonArray0.getJSONArray(0).size(); i++) {
      String str = jsonArray0.getJSONArray(0).get(i).toString();
      atStringList0.add(str);
    }
    for(String str0 : atStringList0) {
      if(notificationContent.equals(context.getString(R.string.at_you))){
        break;
      }
      final String atStr = str0.trim();
      //final String atStr = ((AVIMTextMessage) message).getAttrs().get("at_person_detail").toString().trim();
      List<Object> atIds = Arrays.asList(((AVIMTextMessage) message).getAttrs().get("at_person_members"));
      JSONArray jsonArray = JSON.parseArray(atIds.toString());
      List<String> atIdStrs = new ArrayList<>();
      for (int i = 0; i < jsonArray.getJSONArray(0).size(); i++) {
        String str = jsonArray.getJSONArray(0).get(i).toString();
        atIdStrs.add(str);
      }
      if (!atStr.isEmpty()) {
        LCIMProfileCache.getInstance().getCachedUsers(atIdStrs, new AVCallback<List<LCChatKitUser>>() {
          @Override
          protected void internalDone0(List<LCChatKitUser> lcChatKitUsers, AVException e) {
            for (LCChatKitUser lcChatKitUser : lcChatKitUsers) {
              if (lcChatKitUser.getUserName().equals(atStr)) {
                if (lcChatKitUser.getUserId().equals(SpUtils.getString(context, "userid"))) {
                  notificationContent = context.getString(R.string.at_you);
                  LCIMConstants.isSomeoneAtYou = true;
                  break;
                } else {
                  notificationContent = ((AVIMTextMessage) message).getText();
                  LCIMConstants.isSomeoneAtYou = false;
                }
              }
            }
          }
        });
      } else {
        notificationContent = ((AVIMTextMessage) message).getText();
        LCIMConstants.isSomeoneAtYou = false;
      }
    }
  }

  private void iosTextMessage(final AVIMTypedMessage message,AVIMConversation conversation){
      if (((AVIMTextMessage) message).getText().contains("@")) {
        String iosmsgText = ((AVIMTextMessage) message).getText();
        final String[] iosStrArr = iosmsgText.split("@");
        LCIMProfileCache.getInstance()
            .getCachedUsers(conversation.getMembers(), new AVCallback<List<LCChatKitUser>>() {
              @Override protected void internalDone0(List<LCChatKitUser> lcChatKitUsers,
                  AVException e) {
                loop: for (LCChatKitUser lcChatKitUser : lcChatKitUsers) {
                  for (String iosStr : iosStrArr) {
                    if (iosStr.indexOf(lcChatKitUser.getUserName()) == 0) {
                      if (lcChatKitUser.getUserId()
                          .equals(SpUtils.getString(context, "userid"))) {
                        notificationContent = context.getString(R.string.at_you);
                        LCIMConstants.isSomeoneAtYou = true;
                        break loop;
                      } else {
                        notificationContent = ((AVIMTextMessage) message).getText();
                        LCIMConstants.isSomeoneAtYou = false;
                      }
                    }
                  }
                }
              }
            });
      }else{
        notificationContent = ((AVIMTextMessage) message).getText();
        LCIMConstants.isSomeoneAtYou = false;
      }
  }

  /**
   * 点击 notification 触发的 Intent
   * 注意要设置 package 已经 Category，避免多 app 同时引用 lib 造成消息干扰
   * @param conversationId
   * @param peerId
   * @return
   */
  private Intent getIMNotificationIntent(String conversationId, String peerId) {
    Intent intent = new Intent();
    intent.setAction(LCIMConstants.CHAT_NOTIFICATION_ACTION);
    intent.putExtra(LCIMConstants.CONVERSATION_ID, conversationId);
    intent.putExtra(LCIMConstants.PEER_ID, peerId);
    //intent.setPackage(context.getPackageName());

    //点击notify跳转到app主页面
    ComponentName cn = new ComponentName(context.getPackageName(),"com.im.chat.activity.MainActivity");
    intent.setComponent(cn);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    return intent;
  }
}
