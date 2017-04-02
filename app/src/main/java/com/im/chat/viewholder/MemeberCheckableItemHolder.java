package com.im.chat.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.im.chat.R;
import com.im.chat.model.ContactListModel;
import com.im.chat.model.UserModel;
import com.squareup.picasso.Picasso;

import cn.leancloud.chatkit.view.RoundImageView;
import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;

/**
 * Created by wli on 15/12/2.
 */
public class MemeberCheckableItemHolder extends LCIMCommonViewHolder<ContactListModel> {

  private RoundImageView avatarView;
  private TextView nameView;
  private CheckBox checkBox;
  private OnItemHolderCheckedChangeListener checkedChangeListener;

  public MemeberCheckableItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.conversation_add_members_item);
    avatarView = (RoundImageView) itemView.findViewById(R.id.avatar);
    nameView = (TextView) itemView.findViewById(R.id.username);
    checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        checkBox.toggle();
      }
    });

    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkedChangeListener.onCheckedChanged(isChecked);
      }
    });
  }

  public void setOnCheckedChangeListener(OnItemHolderCheckedChangeListener listener) {
    checkedChangeListener = listener;
  }

  @Override public void bindData(ContactListModel user) {
    if (null != user) {
      Picasso.with(getContext())
          .load(user.getHead())
          .error(R.drawable.lcim_default_avatar_icon)
          .placeholder(R.drawable.lcim_default_avatar_icon)
          .into(avatarView);
      nameView.setText(user.getName());
    } else {
      avatarView.setImageResource(0);
      nameView.setText("");
    }
  }

  public void setChecked(boolean checked) {
    checkBox.setChecked(checked);
  }

  public static ViewHolderCreator HOLDER_CREATOR =
      new ViewHolderCreator<MemeberCheckableItemHolder>() {
        @Override
        public MemeberCheckableItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
          return new MemeberCheckableItemHolder(parent.getContext(), parent);
        }
      };

  public interface OnItemHolderCheckedChangeListener {
    public void onCheckedChanged(boolean isChecked);
  }
}
