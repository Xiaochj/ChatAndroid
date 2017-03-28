package com.im.chat.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.im.chat.R;
import com.im.chat.view.HeaderLayout;

import butterknife.Bind;

/**
 * 通告的详情页
 * Created by xiaochj on 2017/3/8.
 */

public class NotifyDetailActivity extends BaseActivity {

  @Bind(R.id.notify_detail_webview) WebView mWebView;
  @Bind(R.id.title_layout) protected LinearLayout mHeaderLinearLayout;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.notify_detail_layout);
    HeaderLayout headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
    headerLayout.showLeftBackButton(R.string.notification_title, null);
    headerLayout.showRightImageButton(R.drawable.notify_share, new View.OnClickListener() {
      @Override public void onClick(View v) {
        Toast.makeText(getBaseContext(), "share", Toast.LENGTH_LONG).show();
      }
    });
    if (getIntent() != null) {
      if (getIntent().getStringExtra("itemUrl") != null) {
        mWebView.loadUrl(getIntent().getStringExtra("itemUrl"));
      }
    }
  }
}
