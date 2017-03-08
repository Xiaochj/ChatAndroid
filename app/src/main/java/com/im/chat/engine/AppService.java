package com.im.chat.engine;

import com.im.chat.model.BaseResponse;
import com.im.chat.model.UserBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 服务器接口
 * Created by lzb on 16/9/1.
 */
public interface AppService {

  @POST("login/in")
  Observable<BaseResponse> login(@Body UserBean loginInBean);
}
