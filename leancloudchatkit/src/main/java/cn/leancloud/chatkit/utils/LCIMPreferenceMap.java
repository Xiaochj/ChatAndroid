package cn.leancloud.chatkit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import cn.leancloud.chatkit.R;

/**
 * Created by xiaochj on 2017/4/9.
 */

public class LCIMPreferenceMap {

  private Context ctx;
  public static final String NOTIFY_WHEN_NEWS = "notifyWhenNews";
  public static final String VOICE_NOTIFY = "voiceNotify";
  public static final String VIBRATE_NOTIFY = "vibrateNotify";
  SharedPreferences pref;
  SharedPreferences.Editor editor;
  public static LCIMPreferenceMap instance;

  public LCIMPreferenceMap(Context cxt, String prefName) {
    this.ctx = cxt;
    pref = cxt.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    editor = pref.edit();
  }

  public static LCIMPreferenceMap getInstance(Context ctx,String prefName) {
    if (instance == null) {
      instance = new LCIMPreferenceMap(ctx.getApplicationContext(), prefName);
    }
    return instance;
  }

  public boolean isNotifyWhenNews() {
    return pref.getBoolean(NOTIFY_WHEN_NEWS,
        ctx.getResources().getBoolean(R.bool.defaultNotifyWhenNews));
  }

  public void setNotifyWhenNews(boolean notifyWhenNews) {
    editor.putBoolean(NOTIFY_WHEN_NEWS, notifyWhenNews).commit();
  }

  boolean getBooleanByResId(int resId) {
    return ctx.getResources().getBoolean(resId);
  }

  public boolean isVoiceNotify() {
    return pref.getBoolean(VOICE_NOTIFY,
        getBooleanByResId(R.bool.defaultVoiceNotify));
  }

  public void setVoiceNotify(boolean voiceNotify) {
    editor.putBoolean(VOICE_NOTIFY, voiceNotify).commit();
  }

  public boolean isVibrateNotify() {
    return pref.getBoolean(VIBRATE_NOTIFY,
        getBooleanByResId(R.bool.defaultVibrateNotify));
  }

  public void setVibrateNotify(boolean vibrateNotify) {
    editor.putBoolean(VIBRATE_NOTIFY, vibrateNotify).commit();
  }

}
