package com.im.chat.activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.im.chat.fragment.LCIMConversationFragment;

import java.util.Arrays;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.utils.LCIMAudioHelper;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMConversationUtils;
import cn.leancloud.chatkit.utils.LCIMLogUtils;

/**
 * Created by cjxiao
 * 会话详情页
 * 包含会话的创建以及拉取，具体的 UI 细节在 LCIMConversationFragment 中
 * TODO:根据距离改变语音的播放是听筒还是外音
 */
public class LCIMConversationActivity extends AppCompatActivity implements SensorEventListener{

  protected LCIMConversationFragment conversationFragment;

  private SensorManager mSensorManager;
  private Sensor mSensor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lcim_conversation_activity);
    conversationFragment = (LCIMConversationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
    initByIntent(getIntent());
    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
  }

  @Override
  protected void onPause() {
    super.onPause();
    mSensorManager.unregisterListener(this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    initByIntent(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_NORMAL);
  }

  private void initByIntent(Intent intent) {
    if (null == LCChatKit.getInstance().getClient()) {
      showToast("please login first!");
      finish();
      return;
    }

    Bundle extras = intent.getExtras();
    if (null != extras) {
      //一对一聊天
      if (extras.containsKey(LCIMConstants.PEER_ID)) {
        getConversation(extras.getString(LCIMConstants.PEER_ID));
      } else if (extras.containsKey(LCIMConstants.CONVERSATION_ID)) {//群聊
        String conversationId = extras.getString(LCIMConstants.CONVERSATION_ID);
        updateConversation(LCChatKit.getInstance().getClient().getConversation(conversationId));
      } else {
        showToast("memberId or conversationId is needed");
        finish();
      }
    }

  }

  /**
   * 设置 actionBar title 以及 up 按钮事件
   *
   * @param title
   */
  protected void initActionBar(String title) {
    ActionBar actionBar = getSupportActionBar();
    if (null != actionBar) {
      if (null != title) {
        actionBar.setTitle(title);
      }
      actionBar.setDisplayUseLogoEnabled(false);
      actionBar.setDisplayHomeAsUpEnabled(true);
      finishActivity(RESULT_OK);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (android.R.id.home == item.getItemId()) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * 拿到當前聊天名字
   * @param conversation
   */
  public void getConvName(AVIMConversation conversation){
    LCIMConversationUtils.getConversationName(conversation, new AVCallback<String>() {
      @Override
      protected void internalDone0(String s, AVException e) {
        if (null != e) {
          LCIMLogUtils.logException(e);
        } else {
          if (conversation.isTransient() || conversation.getMembers().size() > 2) {
            initActionBar(s+"("+conversation.getMembers().size()+")");
          }else {
            initActionBar(s);
          }
        }
      }
    });
  }

  /**
   * 主动刷新 UI
   *
   * @param conversation
   */
  protected void updateConversation(final AVIMConversation conversation) {
    if (null != conversation) {
      conversationFragment.setConversation(conversation);
      //清空当前的未读消息数量
      LCIMConversationItemCache.getInstance().clearUnread(conversation.getConversationId());
      getConvName(conversation);
    }
  }

  /**
   * 获取 conversation
   * 为了避免重复的创建，createConversation 参数 isUnique 设为 true·
   */
  protected void getConversation(final String memberId) {
    LCChatKit.getInstance().getClient().createConversation(
      Arrays.asList(memberId), "", null, false, true, new AVIMConversationCreatedCallback() {
        @Override
        public void done(AVIMConversation avimConversation, AVIMException e) {
          if (null != e) {
            showToast(e.getMessage());
          } else {
            updateConversation(avimConversation);
          }
        }
      });
  }

  /**
   * 弹出 toast
   *
   * @param content
   */
  private void showToast(String content) {
    Toast.makeText(LCIMConversationActivity.this, content, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    //如果传感器距离达到了临界峰值
    if (event.values[0] == mSensor.getMaximumRange()) {
      LCIMAudioHelper.getInstance(this).setAudioModeSpokenOn();
    } else {
      LCIMAudioHelper.getInstance(this).setAudioModeSpokenOff();
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }
}