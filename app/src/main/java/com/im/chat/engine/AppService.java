package com.im.chat.engine;

import com.im.chat.model.BaseBean;
import com.im.chat.model.ContactListModel;
import com.im.chat.model.UserModel;
import com.im.chat.model.NotifyListModel;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 服务器接口
 * Created by xiaochj on 17/3/27.
 */
public interface AppService {

  /**
   * 登录
   */
  @FormUrlEncoded @POST("login/in.do") Observable<BaseBean<UserModel>> login(
      @Field("name") String name, @Field("password") String password);

  /**
   * 登出
   */
  @POST("login/out.do") Observable<BaseBean> logout();

  /**
   * 上传图片
   */
  @FormUrlEncoded @POST("image/upload.do") Observable<BaseBean> uploadPhoto(
      @Field("data") String base64Str);

  /**
   * 獲取通告列表 pageNo=1 && pageSize=-1 表示拿到所有数据
   *
   * @param pageNo 页码
   * @param pageSize 分页大小
   */
  @FormUrlEncoded @POST("notice/list.do") Observable<BaseBean<List<NotifyListModel>>> getNotifyList(
      @Field("pageNo") int pageNo, @Field("pageSize") int pageSize);

  /**
   * 更新用戶信息
   */
  @FormUrlEncoded @POST("member/update.do") Observable<BaseBean> setProfileResume(
      @Field("mobile") String mobile, @Field("signature") String signature,
      @Field("oldpassword") String oldpassword, @Field("password") String password,
      @Field("mail") String mail);

  /**
   * 獲取用戶信息
   */
  @POST("member/info.do") Observable<BaseBean<UserModel>> getProfileInfo();

  /**
   * 獲取通訊錄  pageNo=1 && pageSize=-1 表示拿到所有数据
   *
   * @param pageNo 页码
   * @param pageSize 分页大小
   */
  @FormUrlEncoded @POST("member/list.do")
  Observable<BaseBean<List<ContactListModel>>> getContactList(@Field("pageNo") int pageNo,
      @Field("pageSize") int pageSize);
}
