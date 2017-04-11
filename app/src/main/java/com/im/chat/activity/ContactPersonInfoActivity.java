package com.im.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.view.RoundImageView;
import com.im.chat.R;
import com.im.chat.model.ContactListModel;
import com.im.chat.util.ChatConstants;
import com.im.chat.view.HeaderLayout;
import com.squareup.picasso.Picasso;

/**
 * 用户详情页，从对话详情页面和发现页面跳转过来
 * created by cjxiao
 */
public class ContactPersonInfoActivity extends BaseActivity implements OnClickListener {

  @Bind(R.id.contact_detail_avatar) RoundImageView mRoundImageView;
  @Bind(R.id.contact_detail_name) TextView mNameTv;
  @Bind(R.id.contact_detail_sex) TextView mSexTv;
  @Bind(R.id.contact_detail_mark) TextView mMark;
  @Bind(R.id.contact_detail_mail) TextView mMail;
  @Bind(R.id.contact_detail_phone) TextView mPhone;
  @Bind(R.id.contact_detail_btn) Button mButton;
  @Bind(R.id.title_layout) protected LinearLayout mHeaderLinearLayout;

  ContactListModel contactListModel;

  @Override protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_detail_layout);
    HeaderLayout headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
    headerLayout.showLeftBackButton(R.string.person_detail_title, null);
    mButton.setOnClickListener(this);
    initData();//获取传递过来的intent
  }

  private void initData() {
    contactListModel =
        (ContactListModel) getIntent().getSerializableExtra(ChatConstants.CONTACT_USER);
    Picasso.with(this)
        .load(contactListModel.getHead())
        .error(R.drawable.lcim_default_avatar_icon)
        .placeholder(R.drawable.lcim_default_avatar_icon)
        .into(mRoundImageView);
    mNameTv.setText(contactListModel.getName());
    mSexTv.setText("/" + contactListModel.getSex());
    mMark.setText(contactListModel.getSignature());
    mMail.setText(contactListModel.getMail());
    mPhone.setText(contactListModel.getMobile());
    //user = UserCacheUtils.getCachedUser(userId);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.contact_detail_btn:// 发起聊天
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra(LCIMConstants.PEER_ID, contactListModel.getId());
        startActivity(intent);
        finish();
        break;
    }
  }
}
