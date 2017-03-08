package com.im.chat.engine;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 应用需要传入的header参数
 * Created by lzb on 16/9/7.
 */
public class AppInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
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
