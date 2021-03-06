package cn.leancloud.chatkit.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;

import java.io.IOException;

/**
 * Created by lzw on 14/12/19.
 * 语音播放相关的 helper 类
 */
public class LCIMAudioHelper {
  private static LCIMAudioHelper audioHelper;
  private MediaPlayer mediaPlayer;
  private AudioFinishCallback finishCallback;
  private String audioPath;
  private boolean onceStart = false;
  private AudioManager audioManager;

  private LCIMAudioHelper(Context context) {
    mediaPlayer = new MediaPlayer();
    initAudioManager(context);
  }

  public static synchronized LCIMAudioHelper getInstance(Context context) {
    if (audioHelper == null) {
      audioHelper = new LCIMAudioHelper(context);
    }
    return audioHelper;
  }

  private void initAudioManager(Context context){
    audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    setAudioModeSpokenOff();
  }

  public void setAudioModeSpokenOn(){
    audioManager.setMode(AudioManager.MODE_NORMAL);
    audioManager.setSpeakerphoneOn(true);
  }

  public void setAudioModeSpokenOff(){
    //设置听筒播放
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
      audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    } else {
      audioManager.setMode(AudioManager.MODE_IN_CALL);
    }
    audioManager.setSpeakerphoneOn(false);
  }

  /**
   * 获取当前语音的文件地址
   *
   * @return
   */
  public String getAudioPath() {
    return audioPath;
  }

  /**
   * 停止播放
   */
  public void stopPlayer() {
    if (mediaPlayer != null) {
      tryRunFinishCallback();
      mediaPlayer.stop();
      mediaPlayer.reset();
    }
  }

  /**
   * 暂停播放
   */
  public void pausePlayer() {
    if (mediaPlayer != null) {
      tryRunFinishCallback();
      mediaPlayer.pause();
    }
  }

  /**
   * 判断当前是否正在播放
   *
   * @return
   */
  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
  }

  /**
   * 重新播放
   */
  public void restartPlayer() {
    if (mediaPlayer != null && mediaPlayer.isPlaying() == false) {
      mediaPlayer.start();
    }
  }

  public void addFinishCallback(AudioFinishCallback callback) {
    finishCallback = callback;
  }

  /**
   * 播放语音文件
   *
   * @param path
   */
  public synchronized void playAudio(String path) {
    if (onceStart) {
      mediaPlayer.reset();
    }
    tryRunFinishCallback();
    audioPath = path;
    try {
      mediaPlayer.setDataSource(path);
      mediaPlayer.prepare();
      mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
          tryRunFinishCallback();
        }
      });
      mediaPlayer.start();
      onceStart = true;
    } catch (IOException e) {
    }
  }

  private void tryRunFinishCallback() {
    if (finishCallback != null) {
      finishCallback.onFinish();
    }
  }

  public interface AudioFinishCallback {
    void onFinish();
  }
}
