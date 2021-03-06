package com.im.chat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.im.chat.R;
import com.im.chat.view.HeaderLayout;

/**
 * 对话列表页面
 * Created by cjxiao
 */
public class ConversationListFragment extends LCIMConversationListFragment {

  protected HeaderLayout headerLayout;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
    headerLayout.showTitle(R.string.conversation_messages);
    return view;
  }
}
