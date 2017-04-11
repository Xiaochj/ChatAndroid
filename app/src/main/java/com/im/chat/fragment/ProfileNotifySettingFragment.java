package com.im.chat.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import cn.leancloud.chatkit.utils.PreferenceMap;
import cn.leancloud.chatkit.utils.SpUtils;
import com.im.chat.R;
import com.im.chat.util.ChatConstants;

/**
 * 提醒通知
 * Created by cjxiao
 */
public class ProfileNotifySettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
  public static final String NOTIFY_WHEN_NEWS = "notifyWhenNews";
  public static final String VOICE_NOTIFY = "voiceNotify";
  public static final String VIBRATE_NOTIFY = "vibrateNotify";

  PreferenceMap preferenceMap;
  CheckBoxPreference notifyWhenNews, voiceNotify, vibrateNotify;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.setting_preference);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    preferenceMap = PreferenceMap.getInstance(getActivity(), SpUtils.getString(
        getActivity(),ChatConstants.KEY_USERID));
    notifyWhenNews = (CheckBoxPreference) findPreference(NOTIFY_WHEN_NEWS);
    voiceNotify = (CheckBoxPreference) findPreference(VOICE_NOTIFY);
    vibrateNotify = (CheckBoxPreference) findPreference(VIBRATE_NOTIFY);

    notifyWhenNews.setOnPreferenceChangeListener(this);
    voiceNotify.setOnPreferenceChangeListener(this);
    vibrateNotify.setOnPreferenceChangeListener(this);
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {
    String key = preference.getKey();
    boolean value = (Boolean) newValue;
    if (key.equals(NOTIFY_WHEN_NEWS)) {
      preferenceMap.setNotifyWhenNews(value);
    } else if (key.equals(VOICE_NOTIFY)) {
      preferenceMap.setVoiceNotify(value);
    } else if (key.equals(VIBRATE_NOTIFY)) {
      preferenceMap.setVibrateNotify(value);
    }
    return true;
  }
}
