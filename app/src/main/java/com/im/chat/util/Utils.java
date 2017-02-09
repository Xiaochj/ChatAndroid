package com.im.chat.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.im.chat.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

  public static void toast(Context context, String str) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
  }

  public static void showInfoDialog(Activity cxt, String msg, String title) {
    AlertDialog.Builder builder = getBaseDialogBuilder(cxt);
    builder.setMessage(msg)
        .setPositiveButton(cxt.getString(com.im.chat.R.string.chat_utils_right), null)
        .setTitle(title)
        .show();
  }

  public static AlertDialog.Builder getBaseDialogBuilder(Activity ctx) {
    return new AlertDialog.Builder(ctx).setTitle(com.im.chat.R.string.chat_utils_tips).setIcon(com.im.chat.R.drawable.utils_icon_info_2);
  }

  public static void fixAsyncTaskBug() {
    // android bug
    new AsyncTask<Void, Void, Void>() {

      @Override
      protected Void doInBackground(Void... params) {
        return null;
      }
    }.execute();
  }

  public static void toast(int id) {
    toast(App.ctx, id);
  }

  public static void toast(String s) {
    toast(App.ctx, s);
  }

  public static void toast(Context cxt, int id) {
    Toast.makeText(cxt, id, Toast.LENGTH_SHORT).show();
  }

  public static boolean doubleEqual(double a, double b) {
    return Math.abs(a - b) < 1E-8;
  }

  public static String getPrettyDistance(double distance) {
    if (distance < 1000) {
      int metres = (int) distance;
      return String.valueOf(metres) + App.ctx.getString(com.im.chat.R.string.discover_metres);
    } else {
      String num = String.format("%.1f", distance / 1000);
      return num + App.ctx.getString(com.im.chat.R.string.utils_kilometres);
    }
  }

  public static ProgressDialog showSpinnerDialog(Activity activity) {
    //activity = modifyDialogContext(activity);
    ProgressDialog dialog = new ProgressDialog(activity);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setCancelable(true);
    dialog.setMessage(App.ctx.getString(com.im.chat.R.string.chat_utils_hardLoading));
    if (!activity.isFinishing()) {
      dialog.show();
    }
    return dialog;
  }

  public static boolean filterException(Exception e) {
    if (e != null) {
      toast(e.getMessage());
      return false;
    } else {
      return true;
    }
  }

  public static void saveBitmap(String filePath, Bitmap bitmap) {
    File file = new File(filePath);
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file);
      if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
        out.flush();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
      } catch (Exception e) {
      }
    }
  }
}
