package com.im.chat.event;

import com.im.chat.model.LCIMRedPacketAckMessage;

/**
 * Created by wli on 16/7/14.
 */
public class RedPacketAckEvent {
  public LCIMRedPacketAckMessage ackMessage;

  public RedPacketAckEvent(LCIMRedPacketAckMessage ackMessage) {
    this.ackMessage = ackMessage;
  }
}
