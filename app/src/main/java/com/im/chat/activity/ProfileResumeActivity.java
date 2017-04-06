package com.im.chat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.ProgressBar;
import com.im.chat.R;
import com.im.chat.engine.AppEngine;
import com.im.chat.engine.AppService;
import com.im.chat.fragment.ProfileFragment;
import com.im.chat.model.BaseBean;
import com.im.chat.util.Utils;
import com.im.chat.view.HeaderLayout;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 修改简介，手机，邮箱，密码的页
 * Created by xiaochj on 2017/3/19.
 */

public class ProfileResumeActivity extends BaseActivity implements View.OnClickListener {

  private int value = 0;
  View view = null;
  LinearLayout mHeaderLinearLayout;
  HeaderLayout headerLayout;
  EditText markEt;
  EditText phoneEt;
  EditText emailEt;
  EditText pwdOriginEt, pwdNewEt, pwdNewNextEt;
  AppService mAppservice;

  @Override public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(getIntentTypeView());
    mAppservice = AppEngine.getInstance().getAppService();
  }

  private View getIntentTypeView() {
    Intent intent = getIntent();
    value = intent.getIntExtra(ProfileFragment.PROFILE_EXTRA_KEY, ProfileFragment.PROFILE_MARK);
    switch (value) {
      case ProfileFragment.PROFILE_MARK:
        view = LayoutInflater.from(this).inflate(R.layout.profile_mark_layout, null);
        mHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.title_layout);
        headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
        headerLayout.showLeftBackButton(R.string.profile_mark_title, null);
        markEt = (EditText) view.findViewById(R.id.mark_edittext);
        markEt.setText(intent.getStringExtra(ProfileFragment.PROFILE_CONTENT_KEY));
        break;
      case ProfileFragment.PROFILE_PHONE:
        view = LayoutInflater.from(this).inflate(R.layout.profile_phone_layout, null);
        mHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.title_layout);
        headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
        headerLayout.showLeftBackButton(R.string.profile_phone_title, null);
        phoneEt = (EditText) view.findViewById(R.id.phone_edittext);
        phoneEt.setText(intent.getStringExtra(ProfileFragment.PROFILE_CONTENT_KEY));
        break;
      case ProfileFragment.PROFILE_EMAIL:
        view = LayoutInflater.from(this).inflate(R.layout.profile_email_layout, null);
        mHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.title_layout);
        headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
        headerLayout.showLeftBackButton(R.string.profile_email_title, null);
        emailEt = (EditText) view.findViewById(R.id.email_edittext);
        emailEt.setText(intent.getStringExtra(ProfileFragment.PROFILE_CONTENT_KEY));
        break;
      case ProfileFragment.PROFILE_PWD:
        view = LayoutInflater.from(this).inflate(R.layout.profile_pwd_layout, null);
        mHeaderLinearLayout = (LinearLayout) view.findViewById(R.id.title_layout);
        headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
        headerLayout.showLeftBackButton(R.string.profile_pwd, null);
        pwdOriginEt = (EditText) view.findViewById(R.id.pwd_origin_edittext);
        pwdNewEt = (EditText) view.findViewById(R.id.pwd_new_edittext);
        pwdNewNextEt = (EditText) view.findViewById(R.id.pwd_new_next_edittext);
        break;
    }
    headerLayout.showRightText(R.string.common_sure, this);
    return view;
  }

  @Override public void onClick(View v) {
    switch (value) {
      case ProfileFragment.PROFILE_MARK:
        updateDatas(null, markEt.getText().toString(), null, null, null);
        break;
      case ProfileFragment.PROFILE_PHONE:
        updateDatas(phoneEt.getText().toString(), null, null, null, null);
        break;
      case ProfileFragment.PROFILE_EMAIL:
        updateDatas(null, null, emailEt.getText().toString(), null, null);
        break;
      case ProfileFragment.PROFILE_PWD:
        boolean isEqualsPwd =
            pwdNewEt.getText().toString().equals(pwdNewNextEt.getText().toString()) ? true : false;
        //两次密码输入一致
        if (isEqualsPwd) {
          updateDatas(null, null, null, pwdOriginEt.getText().toString(),
              pwdNewEt.getText().toString());
        } else {//输入不一致
          Utils.toast(R.string.profile_resume_pwd_new_error);
        }
        break;
    }
  }

  private void updateDatas(String mobile, String signature, String mail, String oldpassword,
      String password) {
    ProgressDialog progressBar = showSpinnerDialog();
    mAppservice.setProfileResume(mobile, signature, password, mail)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<BaseBean>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            progressBar.dismiss();
            Utils.toast(R.string.profile_resume_error);
            return;
          }

          @Override public void onNext(BaseBean baseBean) {
            progressBar.dismiss();
            if (baseBean.getStatus() == 1) {
              if (baseBean.getCode() == 1) {// success
                Utils.toast(R.string.profile_resume_success);
                ProfileResumeActivity.this.finish();
              } else if (baseBean.getCode() == 800) {//原密码输入有误
                Utils.toast(R.string.profile_resume_pwd_original_error);
              } else {
                Utils.toast(R.string.profile_resume_error);
              }
            } else {
              Utils.toast(R.string.profile_resume_error);
            }
            return;
          }
        });
  }
}
