package com.im.chat.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.leancloud.chatkit.viewholder.LCIMCommonViewHolder;

/**
 * Created by cjxiao
 */
public class CommonHeaderItemHolder extends LCIMCommonViewHolder {
  LinearLayout rootLayout;

  public CommonHeaderItemHolder(Context context, ViewGroup root) {
    super(context, root, com.im.chat.R.layout.common_header_item_layout);
    rootLayout = (LinearLayout)itemView.findViewById(com.im.chat.R.id.common_header_root_view);
  }

  public void setView(View view) {
    rootLayout.removeAllViews();
    if (null != view) {
      rootLayout.addView(view);
    }
  }

  @Override
  public void bindData(Object o) {}
}
