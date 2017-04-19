# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AS\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#不混淆反射类
-keepclasseswithmembernames class com.im.chat.viewholder.** {*;}
-keepclasseswithmembernames class cn.leancloud.chatkit.adapter.LCIMCommonListAdapter{*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-keepattributes EnclosingMethod

#retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** {*;}
-keepclasseswithmembers class * {
  @retrofit2.http.* <methods>;
}

-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

-keep public class * extends android.app.Fragment
-keep class android.support.graphics.drawable.** {*;}

-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**

#glide
-dontwarn com.bumptech.glide.**
-keep public class com.bumptech.glide.** {*;}

#Baidu
-dontwarn com.baidu.**
-dontwarn com.baidu.location.**
-keep class com.baidu.** {*;}
-keep class com.baidu.location.** {*;}
-keep class vi.com.gdi.bgl.android.**{*;}

#tencent
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.** {*;}

#prettytime
-dontwarn org.ocpsoft.prettytime.**
-keep class org.ocpsoft.prettytime.** {*;}

#http
-dontwarn org.apache.**
-keep class org.apache.** {*;}

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#eventbus
-dontwarn de.greenrobot.event.**
-keep class de.greenrobot.event.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

#okhttp3
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-keep class okhttp3.internal.** {*;}

#picasso
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** {*;}

#rx
-dontwarn rx.**
-keepclassmembers class rx.** { *; }

#Gson
-keep class com.google.gson.stream.** {*;}
-keepattributes EnclosingMethod

# retrolambda
-dontwarn java.lang.invoke.*

#jpinyin
-dontwarn com.github.stuxuhai.jpinyin.**
-keep class com.github.stuxuhai.jpinyin.** {*;}

#实体类
-keep class com.im.chat.model.** {*;}


#leancloud sdk   https://leancloud.cn/docs/android_faq.html#代码混淆怎么做
-dontwarn com.jcraft.jzlib.**
-keep class com.jcraft.jzlib.**  { *;}

-dontwarn sun.misc.**
-keep class sun.misc.** { *;}

-dontwarn sun.security.**
-keep class sun.security.** { *; }

-dontwarn com.google.**
-keep class com.google.** { *;}

-dontwarn com.avos.**
-keep class com.avos.** { *;}

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** {*;}

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-dontwarn org.jivesoftware.smack.**
-keep class org.jivesoftware.smack.** { *;}

-dontwarn com.loopj.**
-keep class com.loopj.** { *;}

-dontwarn org.xbill.**
-keep class org.xbill.** { *;}

-keepattributes *Annotation*
