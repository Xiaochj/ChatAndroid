package com.im.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import cn.leancloud.chatkit.adapter.LCIMChatAdapter;

/**
 * Created by wli on 16/7/11.
 */
public class ChatAdapter extends LCIMChatAdapter {

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return super.onCreateViewHolder(parent, viewType);
  }

  /**
   * 判断是什么消息类型
   */

  @Override
  public int getItemViewType(int position) {
    AVIMMessage message = messageList.get(position);
    if (null != message && message instanceof AVIMTypedMessage) {
      AVIMTypedMessage typedMessage = (AVIMTypedMessage) message;
      boolean isMe = fromMe(typedMessage);
    }
    return super.getItemViewType(position);
  }
}
