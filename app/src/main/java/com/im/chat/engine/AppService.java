package com.im.chat.engine;

import com.im.chat.model.BaseResponse;
import com.im.chat.model.LeanchatUser;
import com.im.chat.model.UserBean;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 服务器接口
 * Created by lzb on 16/9/1.
 */
public interface AppService {

  @POST("login/in") Observable<BaseResponse> login(@Body UserBean loginInBean);
}
