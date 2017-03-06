package com.im.chat.engine;

/**
 * Created by lzb on 16/9/7.
 */
public class AppConstants {
    public static final String RETROFIT_CACHE_FILE_NAME = "retrofit-cache";
    public static final long RETROFIT_CACHE_MAX_SIZE = 10;


    public static class Http {

        public static final String HEADER_ACCEPT_VALUE = "application/json";
        public static final String HEADER_AUTHORIZATION = "accessToken";
        public static final int HTTP_CONNECTION_TIMEOUT = 10;
        public static final int HTTP_READ_TIMEOUT = 10;
        public static final int HTTP_WRITE_TIMEOUT = 10;
        public static final String HEADER_DEVICE_OS_TYPE = "deviceOsType";
        public static final String HEADER_DEVICE_OS_VERSION = "deviceOsVersion";
        public static final String HEADER_DEVICE_MODEL = "deviceModel";
        public static final String HEADER_DEVICE_COORDINATE = "deviceCoordinate";
        public static final String HEADER_DEVICE_ID = "deviceId";
        public static final int OK = 200;
        public static final String APP_CHANNEL_ID = "appChannelId";
        public static final String APP_VERSION = "appVersion";
    }

    public static class Share {
        public static final int SHARE_TO_SINA_WEIBO = 0;
        public static final int SHARE_TO_WXCHAT_MOMENT = 1;
        public static final int SHARE_TO_WXCHAT = 2;
        public static final int SHARE_TO_FACEBOOK = 3;
        public static final int SHARE_TO_TWITTER = 4;
        public static final int SHARE_TO_EMAIL = 5;
    }
    public static class Activity{
        public static final String HOMEPAGER_ACTION_NOT_ENTER_ADD = "homepager_action_not_enter_add";
    }
}
