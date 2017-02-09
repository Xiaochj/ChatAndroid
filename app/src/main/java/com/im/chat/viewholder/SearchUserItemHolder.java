package com.im.chat.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.im.chat.R;
import com.im.chat.friends.ContactPersonInfoActivity;
import com.im.chat.model.LeanchatUser;
import com.im.chat.util.Constants;
import com.squareup.picasso.Picasso;

import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;

/**
 * Created by wli on 15/12/3.
 */
public class SearchUserItemHolder extends LCIMCommonViewHolder<LeanchatUser> {

  private TextView nameView;
  private ImageView avatarView;
  private LeanchatUser leanchatUser;

  public SearchUserItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.search_user_item_layout);

    nameView = (TextView)itemView.findViewById(R.id.search_user_item_tv_name);
    avatarView = (ImageView)itemView.findViewById(R.id.search_user_item_im_avatar);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getContext(), ContactPersonInfoActivity.class);
        intent.putExtra(Constants.LEANCHAT_USER_ID, leanchatUser.getObjectId());
        getContext().startActivity(intent);
      }
    });
  }

  @Override
  public void bindData(final LeanchatUser leanchatUser) {
    this.leanchatUser = leanchatUser;
    Picasso.with(getContext()).load(leanchatUser.getAvatarUrl()).into(avatarView);
    nameView.setText(leanchatUser.getUsername());
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<SearchUserItemHolder>() {
    @Override
    public SearchUserItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new SearchUserItemHolder(parent.getContext(), parent);
    }
  };
}

