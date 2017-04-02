package com.im.chat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xiaochj on 2017/3/31.
 */

public class SpUtils {

  /**
   * 保存String类型的数据
   * @param context
   * @param key
   * @param values
   */
  public static void putString(Context context, String key, String values) {
    SharedPreferences sp = context.getSharedPreferences("chatIm",Context.MODE_PRIVATE);
    sp.edit().putString(key,values).commit();

  }

  /**
   * 得到缓存数据
   * @param context
   * @param key
   * @return
   */
  public static String getString(Context context, String key) {
    SharedPreferences sp = context.getSharedPreferences("chatIm",Context.MODE_PRIVATE);

    return sp.getString(key,"");
  }

}
