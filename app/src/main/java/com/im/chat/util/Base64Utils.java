package com.im.chat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64Utils {

  /**
   * 回收图片
   */
  public static void gcBitmap(Bitmap bitmap) {
    if (bitmap != null && !bitmap.isRecycled()) {
      bitmap.recycle(); // 回收图片所占的内存
      bitmap = null;
      System.gc(); // 提醒系统及时回收
    }
  }

  /**
   * @param @param bitmap
   * @param @return 设定文件
   * @return String    返回类型
   * @throws
   * @Title: bitmapToBase64
   * @Description: TODO(Bitmap 转换为字符串)
   */
  public static String bitmapToBase64(Bitmap bitmap) {
    // 要返回的字符串
    String reslut = null;
    ByteArrayOutputStream baos = null;
    try {
      if (bitmap != null) {
        baos = new ByteArrayOutputStream();
        /**
         * 压缩只对保存有效果bitmap还是原来的大小
         */
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
        baos.flush();
        baos.close();
        // 转换为字节数组
        byte[] byteArray = baos.toByteArray();
        // 转换为字符串
        reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
      } else {
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (baos != null) {
          baos.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return reslut;
  }

  /**
   * @param @param base64String
   * @param @return 设定文件
   * @return Bitmap    返回类型
   * @throws
   * @Title: base64ToBitmap
   * @Description: TODO(base64l转换为Bitmap)
   */
  public static Bitmap base64ToBitmap(String base64String) {
    byte[] decode = Base64.decode(base64String, Base64.DEFAULT);
    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
    return bitmap;
  }
}