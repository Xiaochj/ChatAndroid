package com.im.chat.adapter;

import android.text.TextUtils;

import com.im.chat.model.ContactItem;
import com.im.chat.viewholder.ContactItemHolder;
import com.im.chat.model.LeanchatUser;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by cjxiao
 */
public class ContactsAdapter extends HeaderListAdapter<ContactItem> {

  /**
   * 在有序 memberList 中 MemberItem.sortContent 第一次出现时的字母与位置的 map
   */
  private Map<Character, Integer> indexMap = new HashMap<Character, Integer>();

  /**
   * 简体中文的 Collator
   */
  Collator cmp = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);

  public ContactsAdapter() {
    super(ContactItemHolder.class);
  }


  /**
   * 设置成员列表，然后更新索引
   * 此处会对数据以 空格、数字、字母（汉字转化为拼音后的字母） 的顺序进行重新排列
   */
  public void setUserList(List<LeanchatUser> list) {
    List<ContactItem> contactList = new ArrayList<>();
    if (null != list) {
      for (LeanchatUser user : list) {
        ContactItem item = new ContactItem();
        item.user = user;
        //去掉两边的空格
        item.sortContent = PinyinHelper.convertToPinyinString(user.getUsername(), "", PinyinFormat.WITHOUT_TONE).trim();
        contactList.add(item);
      }
    }
    //排序
    Collections.sort(contactList, new SortChineseName());
    indexMap = updateIndex(contactList);
    updateInitialsVisible(contactList);
    super.setDataList(contactList);
  }

  /**
   * 获取索引 Map
   */
  public Map<Character, Integer> getIndexMap() {
    return indexMap;
  }

  /**
   * 更新索引 Map,将首字母相同的名字归为一档
   */
  private Map<Character, Integer> updateIndex(List<ContactItem> list) {
    Character lastCharcter = '#';
    Map<Character, Integer> map = new HashMap<>();
    for (int i = 0; i < list.size(); i++) {
      String str = list.get(i).sortContent;
      if (!TextUtils.isEmpty(str)) {
        Character curChar = Character.toLowerCase(str.charAt(0));
        //如果不是a-z的字母
        if(curChar < 97 || curChar > 122 ){
          //如果在第一个位置
          if(i == 0)
            //存到map中
            map.put(lastCharcter,i);
        }else {//如果是a-z
          //将字母表分组,分别存到map中
          if (!lastCharcter.equals(curChar)) {
            map.put(curChar, i);
            lastCharcter = curChar;
          }
        }
      }
    }
    return map;
  }

  /**
   * 必须要排完序后，否则没意义
   * @param list
   */
  private void updateInitialsVisible(List<ContactItem> list) {
    if (null != list && list.size() > 0) {
      char lastInitial = ' ';
      for (int i = 0; i < list.size(); i++) {
        String str = list.get(i).sortContent;
        if (!TextUtils.isEmpty(str)) {
          //通过比较ascii码,如果不是26个字母且不是第一个索引,就把他们的索引行统一置为不可见
          if((str.charAt(0) < 97 || str.charAt(0) > 122 ) && i > 0) {
            list.get(i).initialVisible = false;
          }else {//反之,索引行都可见
            list.get(i).initialVisible = (lastInitial != str.charAt(0));
            lastInitial = str.charAt(0);
          }
        } else {
          list.get(i).initialVisible = true;
          lastInitial = ' ';
        }
      }
    }
  }

  public class SortChineseName implements Comparator<ContactItem> {

    @Override
    public int compare(ContactItem str1, ContactItem str2) {
      if (null == str1) {
        return -1;
      }
      if (null == str2) {
        return 1;
      }
      if (cmp.compare(str1.sortContent, str2.sortContent) > 0) {
        return 1;
      }else if (cmp.compare(str1.sortContent, str2.sortContent) < 0) {
        return -1;
      }
      return 0;
    }
  }
}