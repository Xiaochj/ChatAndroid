package com.im.chat.engine;

import com.im.chat.App;
import com.im.chat.util.ChatConstants;
import com.im.chat.util.SpUtils;


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
        builder.header(ChatConstants.KEY_TOKEN,SpUtils.getString(App.ctx,ChatConstants.KEY_TOKEN));
        //builder.header(AppConstants.Http.HEADER_DEVICE_OS_TYPE, "1");
        //builder.header(AppConstants.Http.HEADER_DEVICE_OS_VERSION, DeviceInfoUtil.getOsVersion(Ctvapplication.getContext()));
        //builder.header(AppConstants.Http.HEADER_DEVICE_MODEL, DeviceInfoUtil.getModel());
        //builder.header(AppConstants.Http.HEADER_DEVICE_ID,CacheUtils.getString(Ctvapplication.getContext(),"deviceId"));
        //builder.header(AppConstants.Http.HEADER_DEVICE_COORDINATE, "{" + LocateUtils.getCachedLog()+","+LocateUtils.getCachedLat() +"}");
        //builder.header(AppConstants.Http.APP_VERSION, AppUtils.getVersionName(Ctvapplication.getContext()));
        //builder.header(AppConstants.Http.APP_CHANNEL_ID, Commons.getChannel(Ctvapplication.getContext())+"");
        return chain.proceed(builder.build());
    }
}
