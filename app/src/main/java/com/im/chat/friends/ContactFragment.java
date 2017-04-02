package com.im.chat.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.leancloud.chatkit.LCChatKitUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.im.chat.App;
import com.im.chat.R;
import com.im.chat.activity.ChatRoomActivity;
import com.im.chat.activity.SearchActivity;
import com.im.chat.adapter.ContactsAdapter;
import com.im.chat.engine.AppEngine;
import com.im.chat.event.ContactItemClickEvent;
import com.im.chat.event.ContactRefreshEvent;
import com.im.chat.event.GroupItemClickEvent;
import com.im.chat.event.MemberLetterEvent;
import com.im.chat.fragment.BaseFragment;
import com.im.chat.model.BaseBean;
import com.im.chat.model.ContactListModel;
import com.im.chat.util.ChatConstants;
import com.im.chat.util.ChatUserProvider;
import com.im.chat.util.Constants;
import com.im.chat.util.ConversationUtils;
import com.im.chat.util.Utils;
import com.im.chat.viewholder.GroupItemHolder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.adapter.LCIMCommonListAdapter;
import cn.leancloud.chatkit.utils.LCIMConstants;
import de.greenrobot.event.EventBus;
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
    refreshLayout.setRefreshing(false);
    //访问自己的服务器拉去通讯录
    addChatUsers();
//    FriendsManager.fetchFriends(isforce, new FindCallback<LeanchatUser>() {
//      @Override
//      public void done(List<LeanchatUser> list, AVException e) {
//        refreshLayout.setRefreshing(false);
//        itemAdapter.setUserList(list);
//        itemAdapter.notifyDataSetChanged();
//      }
//    });
  }

  private void addChatUsers() {
    ChatUserProvider.getInstance().getAllUsers().clear();
    ChatUserProvider.getInstance().getLcChatKitUsers().clear();
    AppEngine.getInstance().getAppService().getContactList(1, -1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<BaseBean<List<ContactListModel>>>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            Utils.toast(R.string.contact_error);
            return;
          }

          @Override public void onNext(BaseBean<List<ContactListModel>> listBaseBean) {
            if (listBaseBean.getStatus() == 1) {
              if (listBaseBean.getData() != null) {
                ChatUserProvider.getInstance().getAllUsers().addAll(listBaseBean.getData());
                List<ContactListModel> contactListModels = listBaseBean.getData();
                for(ContactListModel contactListModel : contactListModels){
                  ChatUserProvider.getInstance().getLcChatKitUsers().add(new LCChatKitUser(contactListModel.getId(),contactListModel.getName(),contactListModel.getHead()));
                  //HashMap<String,ContactListModel> hashMap = new HashMap<String, ContactListModel>();
                  //hashMap.put(contactListModel.getId(),contactListModel);
                  //idToPartUsers.add(hashMap);
                }
                if(!ChatUserProvider.getInstance().getAllUsers().isEmpty()){
                  itemAdapter.setUserList(ChatUserProvider.getInstance().getAllUsers());
                  itemAdapter.notifyDataSetChanged();
                }else{
                  Utils.toast(R.string.contact_error);
                }
              }
            }
          }
        });
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
    intent.putExtra(ChatConstants.CONTACT_USER, event.contactListModel);
    startActivity(intent);
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
