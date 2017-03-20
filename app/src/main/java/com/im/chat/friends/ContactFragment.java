package com.im.chat.friends;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.im.chat.App;
import com.im.chat.R;
import com.im.chat.activity.ChatRoomActivity;
import com.im.chat.activity.SearchActivity;
import com.im.chat.adapter.ContactsAdapter;
import com.im.chat.event.ContactItemClickEvent;
import com.im.chat.event.ContactItemLongClickEvent;
import com.im.chat.event.ContactRefreshEvent;
import com.im.chat.event.GroupItemClickEvent;
import com.im.chat.event.MemberLetterEvent;
import com.im.chat.fragment.BaseFragment;
import com.im.chat.model.LeanchatUser;
import com.im.chat.util.Constants;
import com.im.chat.util.ConversationUtils;
import com.im.chat.viewholder.GroupItemHolder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.adapter.LCIMCommonListAdapter;
import cn.leancloud.chatkit.utils.LCIMConstants;
import de.greenrobot.event.EventBus;

/**
 * 联系人列表 通讯录
 * <p/>
 * TODO
 * 1、替换 Fragment 的 title
 * 2、优化 findFriends 代码，现在还是冗余
 */

public class ContactFragment extends BaseFragment {

  @Bind(R.id.activity_square_members_srl_list)
  protected SwipeRefreshLayout refreshLayout;

  @Bind(R.id.contact_search_layout)
  protected LinearLayout mSearchLayout;

  @Bind(R.id.activity_square_members_rv_list)
  protected RecyclerView recyclerView;
  private ContactsAdapter itemAdapter;
  LinearLayoutManager layoutManager;

  private View headerView;
  RecyclerView recyclerViewForGroup;
  LinearLayoutManager layoutManagerForGroup;
  private LCIMCommonListAdapter<AVIMConversation> itemAdapterForGroup;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    View view = inflater.inflate(R.layout.contact_fragment, container, false);
    headerView = inflater.inflate(R.layout.contact_qunliao_layout, container, false);
    ButterKnife.bind(this, view);

    recyclerViewForGroup = (RecyclerView)headerView.findViewById(R.id.group_list_view);
    layoutManagerForGroup = new LinearLayoutManager(getActivity());
    recyclerViewForGroup.setLayoutManager(layoutManagerForGroup);
    itemAdapterForGroup = new LCIMCommonListAdapter<>(GroupItemHolder.class);
    recyclerViewForGroup.setAdapter(itemAdapterForGroup);

    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    itemAdapter = new ContactsAdapter();
    itemAdapter.setHeaderView(headerView);
    recyclerView.setAdapter(itemAdapter);

    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        getMembers(false);
      }
    });
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    headerLayout.showTitle(App.ctx.getString(R.string.contact));
    //search的按钮
    mSearchLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(ctx, SearchActivity.class);
        ctx.startActivity(intent);
      }
    });
    EventBus.getDefault().register(this);
    getMembers(false);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  private void getMembers(final boolean isforce) {
    ConversationUtils.findGroupConversationsIncludeMe(new AVIMConversationQueryCallback() {
      @Override
      public void done(List<AVIMConversation> conversations, AVIMException e) {
        if (filterException(e)) {
          refreshLayout.setRefreshing(false);
          itemAdapterForGroup.setDataList(conversations);
          itemAdapterForGroup.notifyDataSetChanged();
        }
      }
    });
    FriendsManager.fetchFriends(isforce, new FindCallback<LeanchatUser>() {
      @Override
      public void done(List<LeanchatUser> list, AVException e) {
        refreshLayout.setRefreshing(false);
        itemAdapter.setUserList(list);
        itemAdapter.notifyDataSetChanged();
      }
    });
  }

  public void showDeleteDialog(final String memberId) {
    new AlertDialog.Builder(ctx).setMessage(R.string.contact_deleteContact)
      .setPositiveButton(R.string.common_sure, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          final ProgressDialog dialog1 = showSpinnerDialog();
          LeanchatUser.getCurrentUser().removeFriend(memberId, new SaveCallback() {
            @Override
            public void done(AVException e) {
              dialog1.dismiss();
              if (filterException(e)) {
                getMembers(true);
              }
            }
          });
        }
      }).setNegativeButton(R.string.chat_common_cancel, null).show();
  }

  public void onEvent(ContactRefreshEvent event) {
    getMembers(true);
  }

  /**
   * 点击item,跳转到朋友资料页面
   * @param event
     */
  public void onEvent(ContactItemClickEvent event) {
    Intent intent = new Intent(getActivity(), ContactPersonInfoActivity.class);
//    intent.putExtra(LCIMConstants.PEER_ID, event.memberId);
    intent.putExtra(Constants.LEANCHAT_USER_ID, event.memberId);
    startActivity(intent);
  }

  /**
   * 长按item
   * @param event
     */
  public void onEvent(ContactItemLongClickEvent event) {
    showDeleteDialog(event.memberId);
  }

  /**
   * 跳转到群聊页面
   * @param event
     */
  public void onEvent(GroupItemClickEvent event) {
    Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
    intent.putExtra(LCIMConstants.CONVERSATION_ID, event.conversationId);
    startActivity(intent);
  }

//  /**
//   * 长按group list item
//   * @param event
//   */
//  public void onEvent(GroupItemLongClickEvent event) {
//    showDeleteDialog(event.conversationId);
//  }

  /**
   * 处理 LetterView 发送过来的 MemberLetterEvent
   * 会通过 MembersAdapter 获取应该要跳转到的位置，然后跳转
   */
  public void onEvent(MemberLetterEvent event) {
    Character targetChar = Character.toLowerCase(event.letter);
    if (itemAdapter.getIndexMap().containsKey(targetChar)) {
      int index = itemAdapter.getIndexMap().get(targetChar);
      if (index >= 0 && index < itemAdapter.getItemCount()) {
        // 此处 index + 1 是因为 ContactsAdapter 有 header
        layoutManager.scrollToPositionWithOffset(index + 1, 0);
      }
    }
  }

}
