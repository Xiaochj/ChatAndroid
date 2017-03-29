package com.im.chat.engine;

import com.im.chat.model.BaseBean;
import com.im.chat.model.ContactListModel;
import com.im.chat.model.LoginModel;
import com.im.chat.model.NotifyListBean;
import com.im.chat.model.ProfileInfoModel;
import com.im.chat.model.ProfileResumeRequestBean;

import java.util.List;

import retrofit2.http.Body;
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
   * @param mobile
   * @param password
   * @return
     */
  @FormUrlEncoded
  @POST("login/in.do")
  Observable<BaseBean<LoginModel>> login(@Field("mobile")String mobile,@Field("password")String password);

  /**
   * 登出
   * @return
     */
  @POST("login/out.do")
  Observable<BaseBean> logout();

  /**
   * 上传图片
   * @param base64Str
   * @return
     */
  @POST("image/upload.do")
  Observable<BaseBean> uploadPhoto(@Body String base64Str);

  /**
   * 獲取通告列表
   * @param pageNo 页码
   * @param pageSize 分页大小
   * @return
     */
  @FormUrlEncoded
  @POST("notice/list.do")
  Observable<NotifyListBean> getNotifyList(@Field("pageNo") int pageNo,@Field("pageSize") int pageSize);

  /**
   * 更新用戶信息
   * @param profileResumeRequestBean
   * @return
     */
  @POST("member/update.do")
  Observable<BaseBean> setProfileResume(@Body ProfileResumeRequestBean profileResumeRequestBean);

  /**
   * 獲取用戶信息
   * @return
     */
  @POST("member/info.do")
  Observable<BaseBean<ProfileInfoModel>> getProfileInfo();

  /**
   * 獲取通訊錄
   * @param
   * @return
     */
  @FormUrlEncoded
  @POST("member/list.do")
  Observable<BaseBean<List<ContactListModel>>> getContactList(@Field("pageNo") int pageNo,@Field("pageSize") int pageSize);
}
