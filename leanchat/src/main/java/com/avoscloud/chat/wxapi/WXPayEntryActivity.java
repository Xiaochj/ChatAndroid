package com.avoscloud.chat.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yunzhanghu.redpacketsdk.RPWxPayCallback;
import com.yunzhanghu.redpacketsdk.RedPacket;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

  private IWXAPI api;

  private RPWxPayCallback mRPWxPayCallback;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    api = WXAPIFactory.createWXAPI(this, null);
    api.handleIntent(getIntent(), this);
    mRPWxPayCallback = RedPacket.getInstance().getWxPayCallback();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    api.handleIntent(intent, this);
    mRPWxPayCallback = RedPacket.getInstance().getWxPayCallback();
  }

  @Override
  public void onReq(BaseReq req) {
  }

  @Override
  public void onResp(BaseResp resp) {
    mRPWxPayCallback = RedPacket.getInstance().getWxPayCallback();
    int errCode = resp.errCode;
    switch (errCode) {
      case 0:
        if (mRPWxPayCallback != null) {
          mRPWxPayCallback.WxPaySuccess();
        }
        finish();
        break;
      case -1:
        if (mRPWxPayCallback != null) {
          mRPWxPayCallback.WxPayError(-1);
        }
        finish();
        break;
      case -2:
        if (mRPWxPayCallback != null) {
          mRPWxPayCallback.WxPayError(-2);
        }
        finish();
        break;
    }
  }
}