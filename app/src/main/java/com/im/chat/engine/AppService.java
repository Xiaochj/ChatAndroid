package com.im.chat.engine;

import com.im.chat.model.BaseBean;
import com.im.chat.model.ContactListBean;
import com.im.chat.model.LoginBean;
import com.im.chat.model.NotifyListBean;
import com.im.chat.model.NotifyListRequestBean;
import com.im.chat.model.ProfileInfoBean;
import com.im.chat.model.ProfileResumeRequestBean;
import com.im.chat.model.UserBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 服务器接口
 * Created by xiaochj on 17/3/27.
 */
public interface AppService {

  /**
   * 登录
   * @param loginInBean
   * @return
     */
  @POST("app/login/in.do")
  Observable<LoginBean> login(@Body UserBean loginInBean);

  /**
   * 登出
   * @return
     */
  @POST("app/login/out.do")
  Observable<BaseBean> logout();

  /**
   * 上传图片
   * @param base64Str
   * @return
     */
  @POST("app/image/upload.do")
  Observable<BaseBean> uploadPhoto(@Body String base64Str);

  /**
   * 獲取通告列表
   * @param notifyListRequestBean
   * @return
     */
  @POST("app/notice/list.do")
  Observable<NotifyListBean> getNotifyList(@Body NotifyListRequestBean notifyListRequestBean);

  /**
   * 更新用戶信息
   * @param profileResumeRequestBean
   * @return
     */
  @POST("app/member/update.do")
  Observable<BaseBean> setProfileResume(@Body ProfileResumeRequestBean profileResumeRequestBean);

  /**
   * 獲取用戶信息
   * @return
     */
  @POST("app/member/info.do")
  Observable<ProfileInfoBean> getProfileInfo();

  /**
   * 獲取通訊錄
   * @param contactListRequestBean
   * @return
     */
  @POST("app/member/list.do")
  Observable<ContactListBean> getContactList(@Body NotifyListRequestBean contactListRequestBean);
}
