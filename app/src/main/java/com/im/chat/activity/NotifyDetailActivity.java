package com.im.chat.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.im.chat.R;

import butterknife.Bind;

/**
 * 通告的详情页
 * Created by xiaochj on 2017/3/8.
 */

public class NotifyDetailActivity extends BaseActivity {

    @Bind(R.id.notify_detail_webview)
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_detail_layout);
    }
}
