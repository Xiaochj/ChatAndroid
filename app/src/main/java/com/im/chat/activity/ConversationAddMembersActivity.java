package com.im.chat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.im.chat.R;
import com.im.chat.adapter.MemeberAddAdapter;
import com.im.chat.friends.FriendsManager;
import com.im.chat.model.ContactListModel;
import com.im.chat.model.ConversationType;
import com.im.chat.model.UserModel;
import com.im.chat.util.ChatUserCacheUtils;
import com.im.chat.util.ChatUserProvider;
import com.im.chat.util.ConversationUtils;
import com.im.chat.util.Utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * 群聊对话拉人页面
 * Created by cjxiao
 * TODO: ConversationChangeEvent
 */
public class ConversationAddMembersActivity extends BaseActivity {

  @Bind(R.id.member_add_rv_list)
  protected RecyclerView recyclerView;

  private LinearLayoutManager layoutManager;
  private MemeberAddAdapter adapter;
  private AVIMConversation conversation;

  public static final int OK = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_add_members_layout);
    setTitle(R.string.conversation_inviteMembers);
    //显示左上角的返回按钮
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    String conversationId = getIntent().getStringExtra(LCIMConstants.CONVERSATION_ID);
    conversation = LCChatKit.getInstance().getClient().getConversation(conversationId);

    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    adapter = new MemeberAddAdapter();
    recyclerView.setAdapter(adapter);

    setListData();
  }

  private List<ContactListModel> screenBlackNameList(){
    List<ContactListModel> source = new ArrayList<>();
    source.addAll(ChatUserProvider.getInstance().getAllUsers());
    List<ContactListModel> tempList = source;
    List<String > memberList = conversation.getMembers();
    for(int j = memberList.size() - 1; j >= 0; j--){
      for(int i = source.size() -1; i >= 0; i--){
        if(memberList.get(j).toString().equals(source.get(i).getId().toString())){
          tempList.remove(source.get(i));
        }
      }
    }
    return tempList;
  }

  /**
   * 初始化list，把已经是成员的人去掉，只存入非成员名单
   */
  private void setListData() {
    List<ContactListModel> tempList = screenBlackNameList();
    adapter.setDataList(tempList);
    adapter.notifyDataSetChanged();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem add = menu.add(0, OK, 0, R.string.common_sure);
    alwaysShowMenuItem(menu);
    SpannableString spannableString = new SpannableString(add.getTitle());
    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lcim_bottom_bar_text_black)), 0, spannableString.length(), 0);
    add.setTitle(spannableString);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    //返回按钮触发
    if(id == android.R.id.home) {
      this.onBackPressed();
      return true;
    }
    if (id == OK) {
      addMembers();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * 确定按钮触发后，加入members
   */
  private void addMembers() {
    final List<String> checkedUsers = adapter.getCheckedIds();
    final ProgressDialog dialog = showSpinnerDialog();
    if (checkedUsers.size() == 0) {
      finish();
    } else {
      if (ConversationUtils.typeOfConversation(conversation) == ConversationType.Single) {
        List<String> members = new ArrayList<String>();
        members.addAll(checkedUsers);
        members.addAll(conversation.getMembers());
        ConversationUtils.createGroupConversation(members, new AVIMConversationCreatedCallback() {
          @Override
          public void done(final AVIMConversation conversation, AVIMException e) {
            if (filterException(e)) {
              Intent intent = new Intent(ConversationAddMembersActivity.this, ChatRoomActivity.class);
              intent.putExtra(LCIMConstants.CONVERSATION_ID, conversation.getConversationId());
              startActivity(intent);
              finish();
            }
          }
        });
      } else {
        conversation.addMembers(checkedUsers, new AVIMConversationCallback() {
          @Override
          public void done(AVIMException e) {
            dialog.dismiss();
            if (filterException(e)) {
              Utils.toast(R.string.conversation_inviteSucceed);
              setResult(RESULT_OK);
              finish();
            }
          }
        });
      }
    }
  }
}
