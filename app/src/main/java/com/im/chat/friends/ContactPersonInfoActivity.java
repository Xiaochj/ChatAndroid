package com.im.chat.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.im.chat.R;
import com.im.chat.activity.BaseActivity;
import com.im.chat.activity.ChatRoomActivity;
import com.im.chat.model.LeanchatUser;
import com.im.chat.util.Constants;
import com.im.chat.util.UserCacheUtils;
import com.im.chat.view.HeaderLayout;

import butterknife.Bind;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.view.RoundImageView;

/**
 * 用户详情页，从对话详情页面和发现页面跳转过来
 * created by cjxiao
 */
public class ContactPersonInfoActivity extends BaseActivity implements OnClickListener {

  @Bind(R.id.contact_detail_avatar)
  RoundImageView mRoundImageView;
  @Bind(R.id.contact_detail_name)
  TextView mNameTv;
  @Bind(R.id.contact_detail_sex)
  TextView mSexTv;
  @Bind(R.id.contact_detail_mark)
  TextView mMark;
  @Bind(R.id.contact_detail_mail)
  TextView mMail;
  @Bind(R.id.contact_detail_phone)
  TextView mPhone;
  @Bind(R.id.contact_detail_btn)
  Button mButton;
  @Bind(R.id.title_layout)
  protected LinearLayout mHeaderLinearLayout;

  String userId = "";
  LeanchatUser user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_detail_layout);
    HeaderLayout headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
    headerLayout.showLeftBackButton(R.string.person_detail_title,null);
    initData();//获取传递过来的intent
    initView();//初始化view，塞给那些textview
  }

  private void initData() {
    userId = getIntent().getStringExtra(Constants.LEANCHAT_USER_ID);
    user = UserCacheUtils.getCachedUser(userId);
  }

  private void initView() {
    LeanchatUser curUser = LeanchatUser.getCurrentUser();
    //if (curUser.equals(user)) {
    //  setTitle(com.im.chat.R.string.contact_personalInfo);
    //  avatarLayout.setOnClickListener(this);
    //  genderLayout.setOnClickListener(this);
    //  avatarArrowView.setVisibility(View.VISIBLE);
    //  chatBtn.setVisibility(View.GONE);
    //  addFriendBtn.setVisibility(View.GONE);
    //} else {
    //  setTitle(com.im.chat.R.string.contact_detailInfo);
    //  avatarArrowView.setVisibility(View.INVISIBLE);
    //  List<String> cacheFriends = FriendsManager.getFriendIds();
    //  boolean isFriend = cacheFriends.contains(user.getObjectId());
    //  if (isFriend) {
    //    chatBtn.setVisibility(View.VISIBLE);
    //    chatBtn.setOnClickListener(this);
    //  } else {
    //    chatBtn.setVisibility(View.GONE);
    //    addFriendBtn.setVisibility(View.VISIBLE);
    //    addFriendBtn.setOnClickListener(this);
    //  }
    //}
    //updateView(user);
  }

  private void updateView(LeanchatUser user) {
    //Picasso.with(this).load(user.getAvatarUrl()).into(avatarView);
    //usernameView.setText(user.getUsername());
  }

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    switch (v.getId()) {
      case R.id.contact_detail_btn:// 发起聊天
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra(LCIMConstants.PEER_ID, userId);
        startActivity(intent);
        finish();
        break;
    }
  }
}
