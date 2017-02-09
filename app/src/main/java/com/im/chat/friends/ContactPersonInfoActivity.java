package com.im.chat.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.im.chat.activity.BaseActivity;
import com.im.chat.activity.ChatRoomActivity;
import com.im.chat.model.LeanchatUser;
import com.im.chat.util.Constants;
import com.im.chat.util.UserCacheUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * 用户详情页，从对话详情页面和发现页面跳转过来
 */
public class ContactPersonInfoActivity extends BaseActivity implements OnClickListener {
  TextView usernameView, genderView;
  ImageView avatarView, avatarArrowView;
  LinearLayout allLayout;
  Button chatBtn, addFriendBtn;
  RelativeLayout avatarLayout, genderLayout;

  String userId = "";
  LeanchatUser user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(com.im.chat.R.layout.contact_person_info_activity);
    initData();

    findView();
    initView();
  }

  private void initData() {
    userId = getIntent().getStringExtra(Constants.LEANCHAT_USER_ID);
    user = UserCacheUtils.getCachedUser(userId);
  }

  private void findView() {
    allLayout = (LinearLayout) findViewById(com.im.chat.R.id.all_layout);
    avatarView = (ImageView) findViewById(com.im.chat.R.id.avatar_view);
    avatarArrowView = (ImageView) findViewById(com.im.chat.R.id.avatar_arrow);
    usernameView = (TextView) findViewById(com.im.chat.R.id.username_view);
    avatarLayout = (RelativeLayout) findViewById(com.im.chat.R.id.head_layout);
    genderLayout = (RelativeLayout) findViewById(com.im.chat.R.id.sex_layout);

    genderView = (TextView) findViewById(com.im.chat.R.id.sexView);
    chatBtn = (Button) findViewById(com.im.chat.R.id.chatBtn);
    addFriendBtn = (Button) findViewById(com.im.chat.R.id.addFriendBtn);
  }

  private void initView() {
    LeanchatUser curUser = LeanchatUser.getCurrentUser();
    if (curUser.equals(user)) {
      setTitle(com.im.chat.R.string.contact_personalInfo);
      avatarLayout.setOnClickListener(this);
      genderLayout.setOnClickListener(this);
      avatarArrowView.setVisibility(View.VISIBLE);
      chatBtn.setVisibility(View.GONE);
      addFriendBtn.setVisibility(View.GONE);
    } else {
      setTitle(com.im.chat.R.string.contact_detailInfo);
      avatarArrowView.setVisibility(View.INVISIBLE);
      List<String> cacheFriends = FriendsManager.getFriendIds();
      boolean isFriend = cacheFriends.contains(user.getObjectId());
      if (isFriend) {
        chatBtn.setVisibility(View.VISIBLE);
        chatBtn.setOnClickListener(this);
      } else {
        chatBtn.setVisibility(View.GONE);
        addFriendBtn.setVisibility(View.VISIBLE);
        addFriendBtn.setOnClickListener(this);
      }
    }
    updateView(user);
  }

  private void updateView(LeanchatUser user) {
    Picasso.with(this).load(user.getAvatarUrl()).into(avatarView);
    usernameView.setText(user.getUsername());
  }

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    switch (v.getId()) {
      case com.im.chat.R.id.chatBtn:// 发起聊天
        Intent intent = new Intent(ContactPersonInfoActivity.this, ChatRoomActivity.class);
        intent.putExtra(LCIMConstants.PEER_ID, userId);
        startActivity(intent);
        finish();
        break;
      case com.im.chat.R.id.addFriendBtn:// 添加好友
        AddRequestManager.getInstance().createAddRequestInBackground(this, user);
        break;
    }
  }
}