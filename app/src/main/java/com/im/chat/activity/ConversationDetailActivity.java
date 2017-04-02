package com.im.chat.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cn.leancloud.chatkit.LCChatKitUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.im.chat.R;
import com.im.chat.adapter.HeaderListAdapter;
import com.im.chat.event.ConversationMemberClickEvent;
import com.im.chat.friends.ContactPersonInfoActivity;
import com.im.chat.model.ContactListModel;
import com.im.chat.model.ConversationType;
import com.im.chat.model.UserModel;
import com.im.chat.util.ChatConstants;
import com.im.chat.util.ChatUserCacheUtils;
import com.im.chat.util.ChatUserProvider;
import com.im.chat.util.Constants;
import com.im.chat.util.ConversationUtils;

import com.im.chat.util.Utils;
import com.im.chat.viewholder.ConversationDetailItemHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * 聊天邀请页
 * Created by cjxiao
 */
public class ConversationDetailActivity extends BaseActivity {
  private static final int ADD_MEMBERS = 0;
  private static final int INTENT_NAME = 1;

  @Bind(R.id.activity_conv_detail_rv_list)
  RecyclerView recyclerView;

  GridLayoutManager layoutManager;
  HeaderListAdapter<ContactListModel> listAdapter;

  View nameLayout;
  View quitLayout;
  TextView nameTv;

  ConversationType conversationType;

  private AVIMConversation conversation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_detail_activity);
    String conversationId = getIntent().getStringExtra(LCIMConstants.CONVERSATION_ID);
    conversation = LCChatKit.getInstance().getClient().getConversation(conversationId);

    View footerView = getLayoutInflater().inflate(R.layout.conversation_detail_footer_layout, null);
    nameLayout = footerView.findViewById(R.id.name_layout);
    nameTv = (TextView)footerView.findViewById(R.id.conversation_name_tv);
    nameTv.setText(conversation.getName());
    nameLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gotoModifyNameActivity();
      }
    });
    quitLayout = footerView.findViewById(R.id.quit_layout);
    quitLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        quitGroup();
      }
    });

    layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        return (listAdapter.getItemViewType(position) == HeaderListAdapter.FOOTER_ITEM_TYPE ? layoutManager.getSpanCount() : 1);
      }
    });
    listAdapter = new HeaderListAdapter<>(ConversationDetailItemHolder.class);
    listAdapter.setFooterView(footerView);

    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(listAdapter);
    setTitle(getString(R.string.conversation_detail_title)+"("+conversation.getMembers().size()+")");
    //显示左上角的返回按钮
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    conversationType = ConversationUtils.typeOfConversation(conversation);
    setViewByConvType(conversationType);
  }

  private void setViewByConvType(ConversationType conversationType) {
    if (conversationType == ConversationType.Single) {
      nameLayout.setVisibility(View.GONE);
      quitLayout.setVisibility(View.GONE);
    } else {
      nameLayout.setVisibility(View.VISIBLE);
      quitLayout.setVisibility(View.VISIBLE);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    refresh();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem invite = menu.add(0, ADD_MEMBERS, 0, R.string.conversation_detail_invite);
    alwaysShowMenuItem(menu);
    SpannableString spannableString = new SpannableString(invite.getTitle());
    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lcim_bottom_bar_text_black)), 0, spannableString.length(), 0);
    invite.setTitle(spannableString);
    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    int menuId = item.getItemId();
    //返回按钮触发
    if(menuId == android.R.id.home) {
      this.onBackPressed();
      return true;
    }
    if (menuId == ADD_MEMBERS) {
      Intent intent = new Intent(this, ConversationAddMembersActivity.class);
      intent.putExtra(LCIMConstants.CONVERSATION_ID, conversation.getConversationId());
      startActivityForResult(intent, ADD_MEMBERS);
    }
    return super.onOptionsItemSelected(item);
  }

  private void refresh() {
    List<ContactListModel> userList = new ArrayList<>();
    List<String> ids = conversation.getMembers();
    List<ContactListModel> contactListModels = ChatUserProvider.getInstance().getAllUsers();
    for(String id : ids){
      for(ContactListModel contactListModel : contactListModels){
        if(id.equals(contactListModel.getId())){
          userList.add(contactListModel);
        }
      }
    }
    listAdapter.setDataList(userList);
    listAdapter.notifyDataSetChanged();
  }

  public void onEvent(ConversationMemberClickEvent clickEvent) {
    if (clickEvent.isLongClick) {
      removeMemeber(clickEvent.contactListModel.getId());
    } else {
      gotoPersonalActivity(clickEvent.contactListModel);
    }
  }

  /**
   * 跳到会员信息页
   * @param contactListModel
   */
  private void gotoPersonalActivity(ContactListModel contactListModel) {
    Intent intent = new Intent(this, ContactPersonInfoActivity.class);
    intent.putExtra(ChatConstants.CONTACT_USER, contactListModel);
    startActivity(intent);
  }

  private void gotoModifyNameActivity() {
    Intent intent = new Intent(this, UpdateContentActivity.class);
    intent.putExtra(Constants.INTENT_KEY, conversation.getName());
    startActivityForResult(intent, INTENT_NAME);
  }

  /**
   * 长按删除聊天好友
   * @param memberId
   */
  private void removeMemeber(final String memberId) {
    if (conversationType == ConversationType.Single) {
      return;
    }
    boolean isTheOwner = conversation.getCreator().equals(memberId);
    if (!isTheOwner) {
      new AlertDialog.Builder(this).setMessage(R.string.conversation_kickTips)
        .setPositiveButton(R.string.common_sure, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            final ProgressDialog progress = showSpinnerDialog();
            conversation.kickMembers(Arrays.asList(memberId), new AVIMConversationCallback() {
              @Override
              public void done(AVIMException e) {
                progress.dismiss();
                if (filterException(e)) {
                  Utils.toast(R.string.conversation_detail_kickSucceed);
                  refresh();
                }
              }
            });
          }
        }).setNegativeButton(R.string.chat_common_cancel, null).show();
    }
  }

  /**
   * 退出群聊
   */
  private void quitGroup() {
    new AlertDialog.Builder(this).setMessage(R.string.conversation_quit_group_tip)
      .setPositiveButton(R.string.common_sure, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          final String convid = conversation.getConversationId();
          conversation.quit(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
              if (filterException(e)) {
                LCIMConversationItemCache.getInstance().deleteConversation(convid);
                Utils.toast(R.string.conversation_alreadyQuitConv);
                setResult(RESULT_OK);
                finish();
              }
            }
          });
        }
      }).setNegativeButton(R.string.chat_common_cancel, null).show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      if (requestCode == INTENT_NAME) {
        String newName = data.getStringExtra(Constants.INTENT_VALUE);
        nameTv.setText(newName);
        updateName(conversation, newName, new AVIMConversationCallback() {
          @Override
          public void done(AVIMException e) {
            if (filterException(e)) {
              refresh();
            }
          }
        });
      } else if (requestCode == ADD_MEMBERS) {
        refresh();
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  public void updateName(final AVIMConversation conv, String newName, final AVIMConversationCallback callback) {
    conv.setName(newName);
    conv.updateInfoInBackground(new AVIMConversationCallback() {
      @Override
      public void done(AVIMException e) {
        if (e != null) {
          if (callback != null) {
            callback.done(e);
          }
        } else {
          if (callback != null) {
            callback.done(null);
          }
        }
      }
    });
  }
}
