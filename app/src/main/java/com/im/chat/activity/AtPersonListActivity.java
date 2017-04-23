package com.im.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.adapter.LCIMCommonListAdapter;
import cn.leancloud.chatkit.cache.LCIMProfileCache;
import cn.leancloud.chatkit.utils.LCIMConstants;
import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.im.chat.App;
import com.im.chat.R;
import com.im.chat.adapter.ContactsAdapter;
import com.im.chat.engine.AppEngine;
import com.im.chat.event.ContactItemClickEvent;
import com.im.chat.event.ContactRefreshEvent;
import com.im.chat.event.GroupItemClickEvent;
import com.im.chat.event.MemberLetterEvent;
import com.im.chat.fragment.LCIMConversationFragment;
import com.im.chat.model.BaseBean;
import com.im.chat.model.ContactListModel;
import com.im.chat.util.ChatConstants;
import com.im.chat.util.ChatUserProvider;
import com.im.chat.util.ConversationUtils;
import com.im.chat.util.Utils;
import com.im.chat.view.HeaderLayout;
import com.im.chat.viewholder.GroupItemHolder;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * "@" 列表
 * Created by xiaochj on 2017/4/22.
 */

public class AtPersonListActivity extends BaseActivity {

  public static final int REQUEST_SEARCH = 1;

  @Bind(R.id.activity_square_members_srl_list)
  protected SwipeRefreshLayout refreshLayout;
  @Bind(R.id.contact_search_layout) protected LinearLayout mSearchLayout;
  @Bind(R.id.activity_square_members_rv_list) protected RecyclerView recyclerView;
  LinearLayoutManager layoutManager;
  private ContactsAdapter itemAdapter;
  LinearLayout mHeaderLinearLayout;
  HeaderLayout headerLayout;

  @Override protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    View view = LayoutInflater.from(this).inflate(R.layout.contact_fragment, null);
    setContentView(view);
    ButterKnife.bind(this, view);
    mHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.title_layout);
    headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
    headerLayout.showLeftBackButton(R.string.at_person, null);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    itemAdapter = new ContactsAdapter();
    recyclerView.setAdapter(itemAdapter);
    //search的按钮
    mSearchLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(AtPersonListActivity.this, SearchActivity.class);
        intent.putExtra(ChatConstants.WHICH_SEARCH,true);
        startActivityForResult(intent,REQUEST_SEARCH);
      }
    });
    refreshLayout.setEnabled(false);
    getMembers();
  }

  private void getMembers() {
    if (getIntent() != null) {
      if (getIntent().getExtras() != null) {
        List<String> idList = getIntent().getExtras().getStringArrayList(ChatConstants.AT_PERSON);
        LCIMProfileCache.getInstance()
            .getCachedUsers(idList, new AVCallback<List<LCChatKitUser>>() {
              @Override
              protected void internalDone0(List<LCChatKitUser> lcChatKitUsers, AVException e) {
                List<ContactListModel> contactListModelList = new ArrayList<>();
                for (LCChatKitUser lcChatKitUser : lcChatKitUsers) {
                  ContactListModel contactListModel = new ContactListModel();
                  contactListModel.setHead(lcChatKitUser.getAvatarUrl());
                  contactListModel.setName(lcChatKitUser.getUserName());
                  contactListModelList.add(contactListModel);
                }
                itemAdapter.setUserList(contactListModelList);
                itemAdapter.notifyDataSetChanged();
              }
            });
      }
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_SEARCH) {
      Intent intent = new Intent();
      intent.putExtra(ChatConstants.AT_PERSON_DETAIL, data.getStringExtra(ChatConstants.AT_PERSON_SEARCH)+"  ");
      setResult(LCIMConversationFragment.REQUEST_AT_PERSON_DETAIL,intent);
      finish();
    }
  }

  public void onEvent(ContactItemClickEvent event) {
    Intent intent = new Intent();
    intent.putExtra(ChatConstants.AT_PERSON_DETAIL, event.contactListModel.getName()+"  ");
    setResult(LCIMConversationFragment.REQUEST_AT_PERSON_DETAIL,intent);
    finish();
  }

  /**
   * 处理 LetterView 发送过来的 MemberLetterEvent
   * 会通过 MembersAdapter 获取应该要跳转到的位置，然后跳转
   */
  public void onEvent(MemberLetterEvent event) {
    Character targetChar = Character.toLowerCase(event.letter);
    if (itemAdapter.getIndexMap().containsKey(targetChar)) {
      int index = itemAdapter.getIndexMap().get(targetChar);
      if (index >= 0 && index < itemAdapter.getItemCount()) {
        layoutManager.scrollToPositionWithOffset(index, 0);
      }
    }
  }
}
