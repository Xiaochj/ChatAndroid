package com.im.chat.fragment;

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
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.im.chat.App;
import com.im.chat.R;
import com.im.chat.activity.ChatRoomActivity;
import com.im.chat.activity.ContactPersonInfoActivity;
import com.im.chat.activity.SearchActivity;
import com.im.chat.adapter.ContactsAdapter;
import com.im.chat.engine.AppEngine;
import com.im.chat.event.ContactItemClickEvent;
import com.im.chat.event.ContactRefreshEvent;
import com.im.chat.event.GroupItemClickEvent;
import com.im.chat.event.MemberLetterEvent;
import com.im.chat.model.BaseBean;
import com.im.chat.model.ContactListModel;
import com.im.chat.service.RequestContact;
import com.im.chat.util.ChatConstants;
import com.im.chat.util.ChatUserProvider;
import com.im.chat.util.ConversationUtils;
import com.im.chat.util.Utils;
import com.im.chat.viewholder.GroupItemHolder;
import de.greenrobot.event.EventBus;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 联系人列表 通讯录
 * Created by xiaochj on 14-9-17.
 *
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
        getRefreshMembers();
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
        intent.putExtra(ChatConstants.WHICH_SEARCH,false);
        ctx.startActivity(intent);
      }
    });
    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
    getMembers();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  private void getMembers(){
    refreshLayout.setRefreshing(false);
    //访问leancloud服务器拉取群聊列表
    addGroupUsers();
    //从缓存中读取通讯录
    if(!ChatUserProvider.getInstance().getAllUsers().isEmpty()){
      itemAdapter.setUserList(ChatUserProvider.getInstance().getAllUsers());
      itemAdapter.notifyDataSetChanged();
    }else{
      Utils.toast(R.string.contact_error);
    }
  }

  private void getRefreshMembers() {
    refreshLayout.setRefreshing(false);
    //访问leancloud服务器拉取群聊列表
    addGroupUsers();
    //访问自己的服务器拉去通讯录
    addChatUsers();
  }

  private void addGroupUsers(){
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
  }

  private void addChatUsers() {
    ChatUserProvider.getInstance().getAllUsers().clear();
    //请求自家服务器
    RequestContact.getInstance().getContactList(getActivity());
    RequestContact.getInstance().setRequestContactListener(new RequestContact.RequestContactImpl() {
      @Override public void onRequestContactListCallback(List<ContactListModel> models) {
        ChatUserProvider.getInstance().getAllUsers().addAll(models);
        if(!ChatUserProvider.getInstance().getAllUsers().isEmpty()){
          itemAdapter.setUserList(ChatUserProvider.getInstance().getAllUsers());
          itemAdapter.notifyDataSetChanged();
        }else{
          Utils.toast(R.string.contact_error);
        }
      }
    });
  }

  /**
   * 点击item,跳转到朋友资料页面
   * @param event
     */
  public void onEvent(ContactItemClickEvent event) {
    if(event.contactListModel.getId() != null) {
      Intent intent = new Intent(getActivity(), ContactPersonInfoActivity.class);
      intent.putExtra(ChatConstants.CONTACT_USER, event.contactListModel);
      startActivity(intent);
    }
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
