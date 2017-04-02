package com.im.chat.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.im.chat.R;
import com.im.chat.event.ConversationMemberClickEvent;
import com.im.chat.model.ContactListModel;
import com.squareup.picasso.Picasso;

import cn.leancloud.chatkit.view.RoundImageView;
import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;
import de.greenrobot.event.EventBus;

/**
 * image和textview的viewholder
 * Created by cjxiao
 */
public class ConversationDetailItemHolder extends LCIMCommonViewHolder<ContactListModel> {

  RoundImageView avatarView;
  TextView nameView;
  ContactListModel contactListModel;

  public ConversationDetailItemHolder(Context context, ViewGroup root) {
    super(context, root, com.im.chat.R.layout.conversation_member_item);
    avatarView = (RoundImageView)itemView.findViewById(com.im.chat.R.id.avatar);
    nameView = (TextView)itemView.findViewById(com.im.chat.R.id.username);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != contactListModel) {
          EventBus.getDefault().post(new ConversationMemberClickEvent(contactListModel, false));
        }
      }
    });

    itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (null != contactListModel) {
          EventBus.getDefault().post(new ConversationMemberClickEvent(contactListModel, true));
        }
        return true;
      }
    });
  }

  @Override
  public void bindData(ContactListModel user) {
    contactListModel = user;
    if (null != user) {
      Picasso.with(getContext()).load(user.getHead()).placeholder(R.drawable.lcim_default_avatar_icon).into(avatarView);
      nameView.setText(user.getName());
    } else {
      avatarView.setImageResource(0);
      nameView.setText("");
    }
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<ConversationDetailItemHolder>() {
    @Override
    public ConversationDetailItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new ConversationDetailItemHolder(parent.getContext(), parent);
    }
  };
}
