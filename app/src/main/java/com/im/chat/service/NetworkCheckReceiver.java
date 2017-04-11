package com.im.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.im.chat.R;
import com.im.chat.util.Connectivity;
import com.im.chat.util.Utils;

/**
 * 网络状态监听器
 * Created by xiaochj on 2017/4/11.
 */

public class NetworkCheckReceiver extends BroadcastReceiver {
  @Override public void onReceive(Context context, Intent intent) {
    if (!Connectivity.isConnected(context)) {
      Utils.toast(context, R.string.no_connection);
    }
  }
}
