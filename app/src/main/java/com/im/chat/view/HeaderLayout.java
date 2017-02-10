package com.im.chat.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

/**
 * 标题栏
 * Created by cjxiao
 */
public class HeaderLayout extends LinearLayout {
  LayoutInflater mInflater;
  RelativeLayout header;
  TextView titleView;
  LinearLayout leftContainer, rightContainer;
  Button backBtn;

  public HeaderLayout(Context context) {
    super(context);
    init();
  }

  public HeaderLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    mInflater = LayoutInflater.from(getContext());
    header = (RelativeLayout) mInflater.inflate(com.im.chat.R.layout.chat_common_base_header, null, false);
    titleView = (TextView) header.findViewById(com.im.chat.R.id.titleView);
    leftContainer = (LinearLayout) header.findViewById(com.im.chat.R.id.leftContainer);
    rightContainer = (LinearLayout) header.findViewById(com.im.chat.R.id.rightContainer);
    backBtn = (Button) header.findViewById(com.im.chat.R.id.backBtn);
    addView(header);
  }

  public void showTitle(int titleId) {
    titleView.setText(titleId);
  }

  public void showTitle(String s) {
    titleView.setText(s);
  }

  public void showLeftBackButton(OnClickListener listener) {
    showLeftBackButton(com.im.chat.R.string.chat_common_emptyStr, listener);
  }

  public void showLeftBackButton() {
    showLeftBackButton(null);
  }

  public void showLeftBackButton(int backTextId, OnClickListener listener) {
    backBtn.setVisibility(View.VISIBLE);
    backBtn.setText(backTextId);
    if (listener == null) {
      listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
          ((Activity) getContext()).finish();
        }
      };
    }
    backBtn.setOnClickListener(listener);
  }

  public void showRightImageButton(int rightResId, OnClickListener listener) {
    View imageViewLayout = mInflater.inflate(com.im.chat.R.layout.chat_common_base_header_right_image_btn, null, false);
    ImageButton rightButton = (ImageButton) imageViewLayout.findViewById(com.im.chat.R.id.imageBtn);
    rightButton.setImageResource(rightResId);
    rightButton.setOnClickListener(listener);
    rightContainer.addView(imageViewLayout);
  }
}
