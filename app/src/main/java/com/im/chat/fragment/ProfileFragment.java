package com.im.chat.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.im.chat.R;
import com.im.chat.activity.EntryLoginActivity;
import com.im.chat.activity.ProfileResumeActivity;
import com.im.chat.activity.ProfileSettingActivity;
import com.im.chat.engine.AppEngine;
import com.im.chat.engine.Urls;
import com.im.chat.model.BaseBean;
import com.im.chat.model.LeanchatUser;
import com.im.chat.model.ProfileInfoModel;
import com.im.chat.service.PushManager;
import com.im.chat.util.Base64Utils;
import com.im.chat.util.PathUtils;
import com.im.chat.util.UserCacheUtils;
import com.im.chat.util.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的页
 * Created by cjxiao
 */
public class ProfileFragment extends BaseFragment {

  public static final String PROFILE_EXTRA_KEY = "profileExtrasKey";
  public static final String PROFILE_CONTENT_KEY = "profileContentKey";
  public static final int PROFILE_MARK = 0;
  public static final int PROFILE_PHONE = 1;
  public static final int PROFILE_EMAIL = 2;
  public static final int PROFILE_PWD = 3;

  private static final int IMAGE_PICK_REQUEST = 1;
  private static final int CROP_REQUEST = 2;

  @Bind(R.id.profile_avatar)
  ImageView mAvatarView;
  @Bind(R.id.profile_name)
  TextView mNameTv;
  @Bind(R.id.profile_sex)
  TextView mSexTv;
  @Bind(R.id.profile_mark)
  TextView mMarkTv;
  @Bind(R.id.profile_phone)
  TextView mPhoneTv;
  @Bind(R.id.profile_email)
  TextView mEmailTv;
  @Bind(R.id.profile_version)
  TextView mVersion;
  @Bind(R.id.profile_logout_btn)
  TextView mLogoutBtn;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.profile_fragment, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    headerLayout.showTitle(R.string.profile_title);
    mVersion.setText(Utils.getVersionName(this.getActivity()));
  }

  @Override
  public void onResume() {
    super.onResume();
    refresh();
  }

  private void refresh() {
    //LeanchatUser curUser = LeanchatUser.getCurrentUser();
    //if(curUser.getAvatarUrl() != null)
    //  Picasso.with(getContext()).load(curUser.getAvatarUrl()).into(mAvatarView);
    AppEngine.getInstance().getAppService().getProfileInfo().subscribeOn(Schedulers.io()).
        observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseBean<ProfileInfoModel>>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {
        return;
      }

      @Override public void onNext(BaseBean<ProfileInfoModel> profileInfoModelBaseBean) {
        if(profileInfoModelBaseBean.getStatus() == 1){
          if(profileInfoModelBaseBean.getData() != null){
            //获取用户信息
            ProfileInfoModel profileInfoModel = profileInfoModelBaseBean.getData();
            if(profileInfoModel.getHead() != null)
              Picasso.with(getContext()).load(profileInfoModel.getHead()).into(mAvatarView);
            if(profileInfoModel.getName() != null)
              mNameTv.setText(profileInfoModel.getName());
            if(profileInfoModel.getSex()!=null)
              mSexTv.setText(profileInfoModel.getSex());
            if(profileInfoModel.getMobile()!= null)
              mPhoneTv.setText(profileInfoModel.getMobile());
            if(profileInfoModel.getSignature() != null)
              mMarkTv.setText(profileInfoModel.getSignature());
            if(profileInfoModel.getMail() != null)
              mEmailTv.setText(profileInfoModel.getMail());
          }
        }
      }
    });
  }

  /**
   * 头像
   */
  @OnClick(R.id.profile_avatar)
  public void onAvatarClick() {
    Intent intent = new Intent(Intent.ACTION_PICK, null);
    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(intent, IMAGE_PICK_REQUEST);
  }

  /**
   * 简介
   */
  @OnClick(R.id.profile_mark_layout)
  public void onNotifyMarkClick(){
    Intent intent = new Intent(getContext(), ProfileResumeActivity.class);
    intent.putExtra(PROFILE_EXTRA_KEY,PROFILE_MARK);
    intent.putExtra(PROFILE_CONTENT_KEY,mMarkTv.getText().toString());
    getContext().startActivity(intent);
  }

  /**
   * 手机
   */
  @OnClick(R.id.profile_phone_layout)
  public void onNotifyPhoneClick(){
    Intent intent = new Intent(getContext(), ProfileResumeActivity.class);
    intent.putExtra(PROFILE_EXTRA_KEY,PROFILE_PHONE);
    intent.putExtra(PROFILE_CONTENT_KEY,mPhoneTv.getText().toString());
    getContext().startActivity(intent);
  }

  /**
   * 邮箱
   */
  @OnClick(R.id.profile_email_layout)
  public void onNotifyEmailClick(){
    Intent intent = new Intent(getContext(), ProfileResumeActivity.class);
    intent.putExtra(PROFILE_EXTRA_KEY,PROFILE_EMAIL);
    intent.putExtra(PROFILE_CONTENT_KEY,mEmailTv.getText().toString());
    getContext().startActivity(intent);
  }

  /**
   * 修改密码
   */
  @OnClick(R.id.profile_change_pwd)
  public void onNotifyChangePwdClick(){
    Intent intent = new Intent(getContext(), ProfileResumeActivity.class);
    intent.putExtra(PROFILE_EXTRA_KEY,PROFILE_PWD);
    getContext().startActivity(intent);
  }

  /**
   * 设置
   */
  @OnClick(R.id.profile_notifysetting_view)
  public void onNotifySettingClick() {
    Intent intent = new Intent(ctx, ProfileSettingActivity.class);
    ctx.startActivity(intent);
  }

  /**
   * 登出
   */
  @OnClick(R.id.profile_logout_btn)
  public void onLogoutClick() {
    LCChatKit.getInstance().close(new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
      }
    });
    PushManager.getInstance().unsubscribeCurrentUserChannel();
    LeanchatUser.logOut();
    //登出自己的服务器
    AppEngine.getInstance().getAppService().logout().subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseBean>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {
        Utils.toast(R.string.logout_error);
        return;
      }

      @Override public void onNext(BaseBean baseBean) {
        if(baseBean.getCode() == 1){
          //清除本地userid和token缓存
          UserCacheUtils.putString(getActivity(), Urls.KEY_USERID,"");
          UserCacheUtils.putString(getActivity(), Urls.KEY_TOKEN,"");
          getActivity().finish();
          Intent intent = new Intent(ctx, EntryLoginActivity.class);
          ctx.startActivity(intent);
        }else{
          Utils.toast(R.string.logout_error);
        }
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == IMAGE_PICK_REQUEST) {
        Uri uri = data.getData();
        startImageCrop(uri, 200, 200, CROP_REQUEST);
      } else if (requestCode == CROP_REQUEST) {
        uploadCropAvatar(data);//上传图片
      }
    }
  }

  public Uri startImageCrop(Uri uri, int outputX, int outputY, int requestCode) {
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    intent.putExtra("crop", "true");
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    intent.putExtra("outputX", outputX);
    intent.putExtra("outputY", outputY);
    intent.putExtra("scale", true);
    String outputPath = PathUtils.getAvatarTmpPath();
    Uri outputUri = Uri.fromFile(new File(outputPath));
    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
    intent.putExtra("return-data", true);
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    intent.putExtra("noFaceDetection", false); // face detection
    startActivityForResult(intent, requestCode);
    return outputUri;
  }

  private void uploadCropAvatar(Intent data) {
    Bundle extras = data.getExtras();
    String base64 = null;
    if (extras != null) {
      Bitmap bitmap = extras.getParcelable("data");
      if (bitmap != null) {
        base64 = Base64Utils.bitmapToBase64(bitmap);
        final ProgressDialog dialog = showSpinnerDialog();
        AppEngine.getInstance().getAppService().uploadPhoto(base64).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseBean>() {
          @Override public void onCompleted() {

          }

          @Override public void onError(Throwable e) {
            dialog.dismiss();
            Utils.toast(R.string.upload_avatar_error);
            if (bitmap != null && bitmap.isRecycled() == false) {
              bitmap.recycle();
            }
            return;
          }

          @Override public void onNext(BaseBean baseBean) {
            dialog.dismiss();
            if(baseBean.getStatus() == 1){
              Utils.toast(R.string.upload_avatar_success);
              mAvatarView.setImageBitmap(bitmap);
            }else{
              Utils.toast(R.string.upload_avatar_error);
            }
            if (bitmap != null && bitmap.isRecycled() == false) {
              bitmap.recycle();
            }
          }
        });
      }
    }
  }
}
