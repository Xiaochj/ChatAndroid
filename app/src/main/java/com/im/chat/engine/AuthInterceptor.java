//package com.im.chat.engine;
//
//import com.imib.cctv.taobao.Ctvapplication;
//import com.imib.cctv.util.CacheUtils;
//
//import java.io.IOException;
//
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * Created by lzb on 16/9/7.
// */
//public class AuthInterceptor implements Interceptor {
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request.Builder builder = chain.request().newBuilder();
//        String accessToken = CacheUtils.getString(Ctvapplication.getContext(), "accessToken");
//        builder.header(AppConstants.Http.HEADER_AUTHORIZATION, accessToken);
//        return chain.proceed(builder.build());
//    }
//}
