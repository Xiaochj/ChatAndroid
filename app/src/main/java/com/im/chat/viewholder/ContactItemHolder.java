package com.im.chat.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.im.chat.R;
import com.im.chat.event.ContactItemClickEvent;
import com.im.chat.model.ContactItem;
import com.squareup.picasso.Picasso;

import cn.leancloud.chatkit.view.RoundImageView;
import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;
import de.greenrobot.event.EventBus;

/**
 * 通讯录的列表页item viewholder
 * Created by cjxiao
 */
public class ContactItemHolder extends LCIMCommonViewHolder<ContactItem> {

  TextView alpha;
  TextView nameView;
  RoundImageView avatarView;

  public ContactItem contactItem;

  public ContactItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.common_user_item);
    initView();
  }

  public void initView() {
    alpha = (TextView)itemView.findViewById(R.id.alpha);
    nameView = (TextView)itemView.findViewById(R.id.tv_friend_name);
    avatarView = (RoundImageView)itemView.findViewById(R.id.img_friend_avatar);

    //点击
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EventBus.getDefault().post(new ContactItemClickEvent(contactItem.user.getId()));
      }
    });

//    //长按
//    itemView.setOnLongClickListener(new View.OnLongClickListener() {
//      @Override
//      public boolean onLongClick(View v) {
//        EventBus.getDefault().post(new ContactItemLongClickEvent(contactItem.user.getId()));
//        return true;
//      }
//    });
  }

  @Override
  public void bindData(ContactItem memberItem) {
    contactItem = memberItem;
    //索引行
    alpha.setVisibility(memberItem.initialVisible ? View.VISIBLE : View.GONE);
    if (!TextUtils.isEmpty(memberItem.sortContent)) {
      alpha.setText(String.valueOf(Character.toUpperCase(memberItem.sortContent.charAt(0))));
    } else {
      alpha.setText("");
    }
    Picasso.with(getContext()).load(memberItem.user.getHead())
      .placeholder(R.drawable.lcim_default_avatar_icon).into(avatarView);
    nameView.setText(memberItem.user.getName());
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<ContactItemHolder>() {
    @Override
    public ContactItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new ContactItemHolder(parent.getContext(), parent);
    }
  };
}
