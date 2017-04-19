package com.im.chat.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Toast;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.im.chat.R;
import com.im.chat.util.ConversationUtils;

import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * Created by cjxiao
 */
public class ChatRoomActivity extends LCIMConversationActivity {

  private AVIMConversation conversation;

  public static final int QUIT_GROUP_REQUEST = 200;

  @Override protected void onResume() {
    super.onResume();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.chat_ativity_menu, menu);
    if (null != menu && menu.size() > 0) {
      MenuItem item = menu.getItem(0);
      item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override protected void updateConversation(AVIMConversation conversation) {
    super.updateConversation(conversation);
    this.conversation = conversation;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int menuId = item.getItemId();
    if (menuId == R.id.people) {
      if (null != conversation) {
        Intent intent = new Intent(ChatRoomActivity.this, ConversationDetailActivity.class);
        intent.putExtra(LCIMConstants.CONVERSATION_ID, conversation.getConversationId());
        startActivityForResult(intent, QUIT_GROUP_REQUEST);
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case QUIT_GROUP_REQUEST:
          finish();
          break;
      }
    }
  }

  @Override protected void getConversation(String memberId) {
    super.getConversation(memberId);
    ConversationUtils.createSingleConversation(memberId, new AVIMConversationCreatedCallback() {
      @Override public void done(AVIMConversation avimConversation, AVIMException e) {
        updateConversation(avimConversation);
      }
    });
  }
}