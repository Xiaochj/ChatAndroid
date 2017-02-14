package com.im.chat.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.im.chat.R;
import com.im.chat.adapter.ChatAdapter;

import cn.leancloud.chatkit.activity.LCIMConversationFragment;
import cn.leancloud.chatkit.adapter.LCIMChatAdapter;
import cn.leancloud.chatkit.event.LCIMInputBottomBarEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarLocationClickEvent;
import cn.leancloud.chatkit.event.LCIMLocationItemClickEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by wli on 16/7/11.
 */
public class ConversationFragment extends LCIMConversationFragment {

  public static final int LOCATION_REQUEST = 100;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    addBaiduView();
  }

  @Override
  protected LCIMChatAdapter getAdpter() {
    return new ChatAdapter();
  }

  private void addBaiduView() {
    View mapView = LayoutInflater.from(getContext()).inflate(R.layout.input_bottom_map_view, null);
    mapView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EventBus.getDefault().post(new LCIMInputBottomBarLocationClickEvent(
                LCIMInputBottomBarEvent.INPUTBOTTOMBAR_LOCATION_ACTION, getTag()));
      }
    });
    inputBottomBar.addActionView(mapView);
  }

  public void onEvent(LCIMInputBottomBarLocationClickEvent event) {
    LocationActivity.startToSelectLocationForResult(ConversationFragment.this, LOCATION_REQUEST);
  }

  public void onEvent(LCIMLocationItemClickEvent event) {
    if (null != event && null != event.message && event.message instanceof AVIMLocationMessage) {
      AVIMLocationMessage locationMessage = (AVIMLocationMessage) event.message;
      LocationActivity.startToSeeLocationDetail(getActivity(), locationMessage.getLocation().getLatitude(),
              locationMessage.getLocation().getLongitude());
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (Activity.RESULT_OK == resultCode) {
      switch (requestCode) {
        case LOCATION_REQUEST:
          processMap(data);
          break;
        default:
          break;
      }
    }
  }

  private void processMap(Intent intent) {
    final double latitude = intent.getDoubleExtra(LocationActivity.LATITUDE, 0);
    final double longitude = intent.getDoubleExtra(LocationActivity.LONGITUDE, 0);
    final String address = intent.getStringExtra(LocationActivity.ADDRESS);
    if (!TextUtils.isEmpty(address)) {
      AVIMLocationMessage locationMsg = new AVIMLocationMessage();
      locationMsg.setLocation(new AVGeoPoint(latitude, longitude));
      locationMsg.setText(address);
      sendMessage(locationMsg);
    } else {
      Toast.makeText(getContext(), R.string.chat_cannotGetYourAddressInfo, Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void setConversation(AVIMConversation conversation) {
    super.setConversation(conversation);
//    if (ConversationUtils.typeOfConversation(imConversation) == ConversationType.Single) {//添加转账按钮
//      addTransferView();
//    }
  }
}
