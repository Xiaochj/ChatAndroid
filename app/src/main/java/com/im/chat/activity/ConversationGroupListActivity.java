package com.im.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import cn.leancloud.chatkit.adapter.LCIMCommonListAdapter;
import cn.leancloud.chatkit.utils.LCIMConstants;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.im.chat.R;
import com.im.chat.App;
import com.im.chat.event.GroupItemClickEvent;
import com.im.chat.util.ConversationUtils;
import com.im.chat.viewholder.GroupItemHolder;

import java.util.List;

/**
 * Created by lzw on 14-10-7.
 */
public class ConversationGroupListActivity extends BaseActivity {

  @Bind(R.id.activity_group_list_srl_view)
  protected RecyclerView recyclerView;

  LinearLayoutManager layoutManager;
  private LCIMCommonListAdapter<AVIMConversation> itemAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.group_list_activity);
    initView();

    setTitle(App.ctx.getString(R.string.conversation_group));
    refreshGroupList();
  }

  private void initView() {
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    itemAdapter = new LCIMCommonListAdapter<>(GroupItemHolder.class);
    recyclerView.setAdapter(itemAdapter);
  }

  private void refreshGroupList() {
    ConversationUtils.findGroupConversationsIncludeMe(new AVIMConversationQueryCallback() {
      @Override
      public void done(List<AVIMConversation> conversations, AVIMException e) {
        if (filterException(e)) {
          itemAdapter.setDataList(conversations);
          itemAdapter.notifyDataSetChanged();
        }
      }
    });
  }

  public void onEvent(GroupItemClickEvent event) {
    Intent intent = new Intent(ConversationGroupListActivity.this, ChatRoomActivity.class);
    intent.putExtra(LCIMConstants.CONVERSATION_ID, event.conversationId);
    startActivity(intent);
  }
}
