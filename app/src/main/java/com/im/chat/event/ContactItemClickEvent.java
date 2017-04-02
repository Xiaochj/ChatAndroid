package com.im.chat.event;

import com.im.chat.model.ContactListModel;

/**
 * Created by cjxiao
 */
public class ContactItemClickEvent {

  public ContactListModel contactListModel;

  public ContactItemClickEvent(ContactListModel id) {
    contactListModel = id;
  }
}
