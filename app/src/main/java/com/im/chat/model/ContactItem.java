package com.im.chat.model;

/**
 * Created by cjxiao
 */
public class ContactItem {
  public ContactListModel user;
  public String sortContent;
  public boolean initialVisible;

  public ContactListModel getUser() {
    return user;
  }

  public void setUser(ContactListModel user) {
    this.user = user;
  }

  public String getSortContent() {
    return sortContent;
  }

  public void setSortContent(String sortContent) {
    this.sortContent = sortContent;
  }

  public boolean isInitialVisible() {
    return initialVisible;
  }

  public void setInitialVisible(boolean initialVisible) {
    this.initialVisible = initialVisible;
  }
}
