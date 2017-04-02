package com.im.chat.event;

import com.im.chat.model.ContactListModel;

/**
 * Created by wli on 15/12/2.
 */
public class ConversationMemberClickEvent {
  public ContactListModel contactListModel;
  public boolean isLongClick;

  public ConversationMemberClickEvent(ContactListModel contactListModel, boolean isLongClick) {
    this.contactListModel = contactListModel;
    this.isLongClick = isLongClick;
  }
}
