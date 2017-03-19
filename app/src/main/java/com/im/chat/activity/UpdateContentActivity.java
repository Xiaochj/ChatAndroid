package com.im.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.im.chat.R;
import com.im.chat.util.Constants;

/**
 * 修改群聊名称
 * Created by cjxiao on 14-9-17.
 */
public class UpdateContentActivity extends BaseActivity {

  private EditText valueEdit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(com.im.chat.R.layout.update_content_layout);
    setTitle(R.string.conversation_name);
    //显示左上角的返回按钮
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    valueEdit = (EditText) findViewById(com.im.chat.R.id.valueEdit);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem item = menu.add(0, 0, 0, R.string.common_sure);
    SpannableString spannableString = new SpannableString(item.getTitle());
    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.lcim_bottom_bar_text_black)), 0, spannableString.length(), 0);
    item.setTitle(spannableString);
    alwaysShowMenuItem(menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if(id == android.R.id.home) {
      this.onBackPressed();
      return true;
    }
    if (id == 0) {
      Intent i = new Intent();
      i.putExtra(Constants.INTENT_VALUE, valueEdit.getText().toString());
      setResult(RESULT_OK, i);
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

}
