package com.avoscloud.chat.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avoscloud.chat.R;
import com.avoscloud.chat.model.LCIMTransferMessage;
import com.avoscloud.chat.model.LeanchatUser;
import com.yunzhanghu.redpacketsdk.bean.RedPacketInfo;
import com.yunzhanghu.redpacketsdk.constant.RPConstant;
import com.yunzhanghu.redpacketui.utils.RPRedPacketUtil;

import cn.leancloud.chatkit.viewholder.LCIMChatItemHolder;

/**
 * 转账
 */
public class ChatItemTransferHolder extends LCIMChatItemHolder {

  private TextView mTvTransfer;

  private RelativeLayout mTransferLayout;

  LCIMTransferMessage transferMessage;

  public ChatItemTransferHolder(Context context, ViewGroup root, boolean isLeft) {
    super(context, root, isLeft);
  }

  @Override
  public void initView() {
    super.initView();
    if (isLeft) {
      conventLayout.addView(View.inflate(getContext(),
              R.layout.lc_chat_item_left_text_transfer_layout, null));
    } else {
      conventLayout.addView(View.inflate(getContext(),
              R.layout.lc_chat_item_right_text_transfer_layout, null)); /*转账view*/
    }
    mTransferLayout = (RelativeLayout) itemView.findViewById(R.id.transfer_layout);
    mTvTransfer = (TextView) itemView.findViewById(R.id.tv_transfer_amount);

    mTransferLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != transferMessage) {
          openTransfer(getContext(), transferMessage);
        }
      }
    });
  }

  @Override
  public void bindData(Object o) {
    super.bindData(o);
    AVIMMessage message = (AVIMMessage) o;
    if (message instanceof LCIMTransferMessage) {
      transferMessage = (LCIMTransferMessage) message;
      mTvTransfer.setText(String.format("%s元", transferMessage.getTransferAmount()));
    }
  }

  /**
   * 打开转账红包方法
   *
   * @param context
   * @param message
   */
  private void openTransfer(final Context context, final LCIMTransferMessage message) {
    RPRedPacketUtil.getInstance().openTransferPacket(context, wrapperTransferInfo(message));
  }

  /**
   * 封装打开转账红包所需参数
   *
   * @param message EMMessage
   * @return RedPacketInfo
   */
  private RedPacketInfo wrapperTransferInfo(LCIMTransferMessage message) {
    String transferAmount = message.getTransferAmount();
    String time = message.getTransferTime();
    RedPacketInfo redPacketInfo = new RedPacketInfo();
    redPacketInfo.messageDirect = getMessageDirect(message);
    redPacketInfo.redPacketAmount = transferAmount;
    redPacketInfo.transferTime = time;
    return redPacketInfo;
  }

  private String getMessageDirect(LCIMTransferMessage message) {
    String selfId = LeanchatUser.getCurrentUserId();
    String messageDirect; /*判断发送还是接收*/
    if (message.getFrom() != null && message.getFrom().equals(selfId)) {
      messageDirect = RPConstant.MESSAGE_DIRECT_SEND;
    } else {
      messageDirect = RPConstant.MESSAGE_DIRECT_RECEIVE;
    }
    return messageDirect;
  }
}
