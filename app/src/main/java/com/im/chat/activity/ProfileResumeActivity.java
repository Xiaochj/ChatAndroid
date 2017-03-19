package com.im.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.im.chat.R;
import com.im.chat.fragment.ProfileFragment;
import com.im.chat.view.HeaderLayout;

/**
 * 修改简介，手机，邮箱，密码的页
 * Created by xiaochj on 2017/3/19.
 */

public class ProfileResumeActivity extends BaseActivity implements View.OnClickListener{

    private int value = 0;

    View view = null;
    LinearLayout mHeaderLinearLayout;
    HeaderLayout headerLayout;

    EditText markEt;

    EditText phoneEt;

    EditText emailEt;

    EditText pwdOriginEt;
    EditText pwdNewEt;
    EditText pwdNewNextEt;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(getIntentTypeView());
    }

    private View getIntentTypeView(){
        Intent intent = getIntent();
        value = intent.getIntExtra(ProfileFragment.PROFILE_EXTRA_KEY,ProfileFragment.PROFILE_MARK);
        switch (value){
            case ProfileFragment.PROFILE_MARK:
                view = LayoutInflater.from(this).inflate(R.layout.profile_mark_layout,null);
                mHeaderLinearLayout = (LinearLayout)view.findViewById(R.id.title_layout);
                headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
                headerLayout.showLeftBackButton(R.string.profile_mark_title,null);
                markEt = (EditText)view.findViewById(R.id.mark_edittext);
                break;
            case ProfileFragment.PROFILE_PHONE:
                view = LayoutInflater.from(this).inflate(R.layout.profile_phone_layout,null);
                mHeaderLinearLayout = (LinearLayout)view.findViewById(R.id.title_layout);
                headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
                headerLayout.showLeftBackButton(R.string.profile_phone_title,null);
                phoneEt = (EditText)view.findViewById(R.id.phone_edittext);
                break;
            case ProfileFragment.PROFILE_EMAIL:
                view = LayoutInflater.from(this).inflate(R.layout.profile_email_layout,null);
                mHeaderLinearLayout = (LinearLayout)view.findViewById(R.id.title_layout);
                headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
                headerLayout.showLeftBackButton(R.string.profile_email_title,null);
                emailEt = (EditText)view.findViewById(R.id.email_edittext);
                break;
            case ProfileFragment.PROFILE_PWD:
                view = LayoutInflater.from(this).inflate(R.layout.profile_pwd_layout,null);
                mHeaderLinearLayout = (LinearLayout)view.findViewById(R.id.title_layout);
                headerLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.headerLayout);
                headerLayout.showLeftBackButton(R.string.profile_pwd,null);
                pwdOriginEt = (EditText)view.findViewById(R.id.pwd_origin_edittext);
                pwdNewEt = (EditText)view.findViewById(R.id.pwd_new_edittext);
                pwdNewNextEt = (EditText)view.findViewById(R.id.pwd_new_next_edittext);
                break;
        }
        headerLayout.showRightText(R.string.common_sure,this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (value){
            case ProfileFragment.PROFILE_MARK:
                break;
            case ProfileFragment.PROFILE_PHONE:
                break;
            case ProfileFragment.PROFILE_EMAIL:
                break;
            case ProfileFragment.PROFILE_PWD:
                break;
        }
    }
}
