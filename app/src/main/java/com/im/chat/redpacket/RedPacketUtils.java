package com.im.chat.redpacket;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationMemberCountCallback;
import com.im.chat.R;
import com.im.chat.event.RedPacketAckEvent;
import com.im.chat.model.LCIMRedPacketAckMessage;
import com.im.chat.model.LCIMRedPacketMessage;
import com.im.chat.model.LCIMTransferMessage;
import com.im.chat.model.LeanchatUser;
import com.im.chat.util.UserCacheUtils;
import com.yunzhanghu.redpacketsdk.RPGroupMemberListener;
import com.yunzhanghu.redpacketsdk.RPSendPacketCallback;
import com.yunzhanghu.redpacketsdk.RPValueCallback;
import com.yunzhanghu.redpacketsdk.RedPacket;
import com.yunzhanghu.redpacketsdk.bean.RPUserBean;
import com.yunzhanghu.redpacketsdk.bean.RedPacketInfo;
import com.yunzhanghu.redpacketsdk.bean.TokenData;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;
import com.yunzhanghu.redpacketui.utils.RPRedPacketUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import de.greenrobot.event.EventBus;

/**
 * Created by ustc on 2016/5/31.
 */
public class RedPacketUtils {

  private static RedPacketUtils mRedPacketUtil;

  private RequestQueue mQueue;

  private RedPacketUtils() {
  }

  public static RedPacketUtils getInstance() {
    if (mRedPacketUtil == null) {
      synchronized (RedPacketUtils.class) {
        if (mRedPacketUtil == null) {
          mRedPacketUtil = new RedPacketUtils();
        }

      }
    }
    return mRedPacketUtil;
  }

  public void startRedPacket(final FragmentActivity activity, final AVIMConversation imConversation, final int itemType, final String toUserId, final RPSendPacketCallback callback) {
    final RedPacketInfo redPacketInfo = new RedPacketInfo();
    if (itemType == RPConstant.RP_ITEM_TYPE_GROUP) {
      /**
       * 发送专属红包用的,获取群组成员
       */
      RedPacket.getInstance().setRPGroupMemberListener(new RPGroupMemberListener() {
        @Override
        public void getGroupMember(String s, final RPValueCallback<List<RPUserBean>> rpValueCallback) {
          initRpGroupMember(imConversation.getMembers(), new GetGroupMemberCallback() {
            @Override
            public void groupInfoSuccess(List<RPUserBean> rpUserList) {
              rpValueCallback.onSuccess(rpUserList);
            }

            @Override
            public void groupInfoError() {

            }
          });
        }
      });
      imConversation.getMemberCount(new AVIMConversationMemberCountCallback() {
        @Override
        public void done(Integer integer, AVIMException e) {
          redPacketInfo.toGroupId = imConversation.getConversationId();
          redPacketInfo.groupMemberCount = integer;
          RPRedPacketUtil.getInstance().startRedPacket(activity, itemType, redPacketInfo, callback);
        }
      });
    } else {
      redPacketInfo.toUserId = toUserId;
      LeanchatUser leanchatUser = UserCacheUtils.getCachedUser(toUserId);
      if (leanchatUser != null) {
        redPacketInfo.toNickName = TextUtils.isEmpty(leanchatUser.getUsername()) ? "none" : leanchatUser.getUsername();
        redPacketInfo.toAvatarUrl = TextUtils.isEmpty(leanchatUser.getAvatarUrl()) ? "" : leanchatUser.getAvatarUrl();
      }
      RPRedPacketUtil.getInstance().startRedPacket(activity, itemType, redPacketInfo, callback);
    }
  }

  /**
   * 发送红包之后设置红包消息的数据
   */
  public LCIMRedPacketMessage createRPMessage(Context context, RedPacketInfo redPacketInfo) {
    String selfName = LeanchatUser.getCurrentUser().getUsername();
    String selfID = LeanchatUser.getCurrentUserId();

    LCIMRedPacketMessage redPacketMessage = new LCIMRedPacketMessage();
    redPacketMessage.setGreeting(redPacketInfo.redPacketGreeting);
    redPacketMessage.setRedPacketId(redPacketInfo.redPacketId);
    redPacketMessage.setSponsorName(context.getResources().getString(R.string.leancloud_luckymoney));
    redPacketMessage.setRedPacketType(redPacketInfo.redPacketType);
    redPacketMessage.setReceiverId(redPacketInfo.toUserId);
    redPacketMessage.setMoney(true);
    redPacketMessage.setSenderName(selfName);
    redPacketMessage.setSenderId(selfID);
    return redPacketMessage;
  }

  public LCIMTransferMessage createTRMessage(RedPacketInfo redPacketInfo) {
    LCIMTransferMessage transferMessage = new LCIMTransferMessage();
    transferMessage.setTransferAmount(redPacketInfo.redPacketAmount);
    transferMessage.setTransferTime(redPacketInfo.transferTime);
    transferMessage.setTransferMessage(true);
    transferMessage.setTransferToUserId(redPacketInfo.toUserId);
    return transferMessage;
  }

  /**
   * 群红包中发专属红包用的
   * 根据一个群成员的id集合,查出群成员的具体信息,发专属红包时需要传群成员信息
   */

  public void initRpGroupMember(List<String> ids, final GetGroupMemberCallback callback) {
    final List<RPUserBean> rpUserList = new ArrayList<RPUserBean>();

    UserCacheUtils.fetchUsers(ids, new UserCacheUtils.CacheUserCallback() {
      RPUserBean rpUserBean;

      @Override
      public void done(List<LeanchatUser> userList, Exception e) {
        if (userList != null) {
          for (int i = 0; i < userList.size(); i++) {
            rpUserBean = new RPUserBean();
            if (!LeanchatUser.getCurrentUserId().equals(userList.get(i).getObjectId())) {

              rpUserBean.userId = userList.get(i).getObjectId();
              rpUserBean.userNickname = userList.get(i).getUsername();
              if (!TextUtils.isEmpty(userList.get(i).getAvatarUrl())) {
                rpUserBean.userAvatar = userList.get(i).getAvatarUrl();
              } else {
                rpUserBean.userAvatar = "none";
              }
              rpUserList.add(rpUserBean);
            }
          }
        }
        /**
         * 查到数据进行回调
         */
        if (rpUserList != null && rpUserList.size() > 0) {
          callback.groupInfoSuccess(rpUserList);
        } else {
          callback.groupInfoError();
        }
      }
    });

  }

  /**
   * 获取sign
   *
   * @param context
   */
  public void getRedPacketSign(Context context, final GetSignInfoCallback callback) {
    String mockUrl = "http://rpv2.yunzhanghu.com/api/sign?duid=" + LeanchatUser.getCurrentUserId();
    mQueue = Volley.newRequestQueue(context);
    StringRequest stringRequest = new StringRequest(mockUrl, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        try {
          if (s != null) {
            JSONObject jsonObj = new JSONObject(s);
            final String partner = jsonObj.getString("partner");
            final String userId = jsonObj.getString("user_id");
            final String timestamp = jsonObj.getString("timestamp");
            final String sign = jsonObj.getString("sign");
            /**
             * 零钱页和领取红包和发红包时都需要
             */
            TokenData mTokenData = new TokenData();
            mTokenData.authPartner = partner;
            mTokenData.appUserId = userId;
            mTokenData.timestamp = timestamp;
            mTokenData.authSign = sign;
            callback.signInfoSuccess(mTokenData);
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        callback.signInfoError(volleyError.toString());
      }
    });
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 2, 2));
    mQueue.add(stringRequest);
  }

  /**
   * 打开红包之后发送回执消息
   */
  public void sendRedPacketAckMsg(String senderId, String senderNickname, String selfId, String selfName, LCIMRedPacketMessage message) {
    LCIMRedPacketAckMessage ackMessage = new LCIMRedPacketAckMessage();
    ackMessage.setSenderId(senderId);
    ackMessage.setSenderName(senderNickname);
    ackMessage.setRecipientId(selfId);
    ackMessage.setRecipientName(selfName);
    ackMessage.setRedPacketType(message.getRedPacketType());
    ackMessage.setGreeting(message.getGreeting());
    ackMessage.setSponsorName(message.getSponsorName());
    ackMessage.setMoney(true);
    EventBus.getDefault().post(new RedPacketAckEvent(ackMessage));
  }

  /**
   * 接收回执消息
   */
  public int receiveRedPacketAckMsg(LCIMRedPacketAckMessage typedMessage, int ITEM_TEXT_RED_PACKET_NOTIFY, int ITEM_TEXT_RED_PACKET_NOTIFY_MEMBER) {
    String selfId = LCChatKit.getInstance().getCurrentUserId();
    if (!TextUtils.isEmpty(typedMessage.getSenderId()) && !TextUtils.isEmpty(typedMessage.getRecipientId())) {
      return typedMessage.getSenderId().equals(selfId) || typedMessage.getRecipientId().equals(selfId)
              ? ITEM_TEXT_RED_PACKET_NOTIFY : ITEM_TEXT_RED_PACKET_NOTIFY_MEMBER;
    } else {
      return ITEM_TEXT_RED_PACKET_NOTIFY_MEMBER;
    }
  }
}
