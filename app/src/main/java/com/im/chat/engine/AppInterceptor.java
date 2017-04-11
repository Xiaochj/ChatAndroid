package com.im.chat.engine;

import cn.leancloud.chatkit.utils.SpUtils;
import com.im.chat.App;
import com.im.chat.util.ChatConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 应用需要传入的header参数
 * Created by xiaochj on 17/3/27.
 */
public class AppInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        //userId
        builder.header(ChatConstants.KEY_USERID, SpUtils.getString(App.ctx,ChatConstants.KEY_USERID));
        //token
        builder.header(ChatConstants.KEY_TOKEN, SpUtils.getString(App.ctx,ChatConstants.KEY_TOKEN));
        return chain.proceed(builder.build());
    }
}
