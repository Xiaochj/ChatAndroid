package com.im.chat.model;

/**
 * 用户bean
 * Created by xiaochj on 2017/3/8.
 */

public class UserBean {

  String name;
  String password;

  public UserBean(String name,String password){
    this.name = name;
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
