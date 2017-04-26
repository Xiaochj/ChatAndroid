package com.im.chat.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.im.chat.R;
import com.im.chat.util.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMPathUtils;

/**
 * Created by cjxiao
 * 图片详情页，聊天时点击图片则会跳转到此页面
 */
public class LCIMImageActivity extends AppCompatActivity {

  private ImageView imageView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lcim_chat_image_brower_layout);
    imageView = (ImageView) findViewById(R.id.imageView);
    Intent intent = getIntent();
    String path = intent.getStringExtra(LCIMConstants.IMAGE_LOCAL_PATH);
    String url = intent.getStringExtra(LCIMConstants.IMAGE_URL);
    if (TextUtils.isEmpty(path)) {
      Picasso.with(this).load(url).into(imageView);
    } else {
      Picasso.with(this).load(new File(path)).into(imageView);
    }
    imageView.setDrawingCacheEnabled(true);
    imageView.setOnClickListener(v -> LCIMImageActivity.this.finish());
    imageView.setOnLongClickListener(v -> onLongClick());
  }

  private boolean onLongClick() {
    new AlertDialog.Builder(this).setMessage(com.im.chat.R.string.is_save_pic)
        .setPositiveButton(com.im.chat.R.string.common_sure, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            if (imageView.getDrawingCache() != null) {
              final ProgressDialog progress = showSpinnerDialog();
              try {
                File file = LCIMPathUtils.getPictureDir(LCIMImageActivity.this);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                imageView.getDrawingCache()
                    .compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                progress.dismiss();
                Utils.toast(LCIMImageActivity.this, R.string.has_save_pic);
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(uri);
                LCIMImageActivity.this.sendBroadcast(intent);
              } catch (Exception e) {
                return;
              }
            }
          }
        }).setNegativeButton(com.im.chat.R.string.chat_common_cancel, null).show();
    return false;
  }

  protected ProgressDialog showSpinnerDialog() {
    ProgressDialog dialog = new ProgressDialog(this);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setCancelable(true);
    dialog.setMessage(getString(com.im.chat.R.string.chat_utils_hardLoading));
    if (!isFinishing()) {
      dialog.show();
    }
    return dialog;
  }
}
