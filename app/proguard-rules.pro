# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in H:\java\andriodstudio\SDK/tools/proguard/proguard-android.txt
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
-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
     *;
 }
-keep class com.tencent.mm.opensdk.** {
   *;
    }
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }
-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
    }
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
        }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
       *;
    }
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    }

-keep class com.linkedin.** { *; }
-keepattributes Signature

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn in.srain.cube.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn rx.internal.util.unsafe.**
-dontwarn com.roughike.bottombar.BottomNavigationBehavior


#需要保留的东西
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.**
-keep public class com.android.vending.licensing.ILicensingService

 #实体类不参与混淆
-keep class tqm.bianfeng.com.tqm.pojo.** { *; }

##########JS接口类不混淆，否则执行不了
-dontwarn com.android.JsInterface.**
-keep class com.android.JsInterface.** {*; }

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
  native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
  public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
  public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的-keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
      public static **[] values();
      public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
      public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
      public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature

######混淆保护自己项目的部分代码以及引用的第三方jar包library########
#如果引用了v4或者v7包
-dontwarn android.support.**

#如果用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错
#gson
#-libraryjars libs/gson-2.2.2.jar
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

#客户端代码中的JavaBean(实体类)的类名与其字段名称全部变成了a、b、c、d等等字符串，这与服务端返回的json字符串中的不一致，导致解析失败。所以，解决的办法是：在进行混淆编译进行打包apk的时候，过滤掉存放所有JavaBean（实体类)的包不进行混淆编译
-keep class com.android.model.** {*;}

#-keep class rx.** { *; }
# eventbus混淆配置
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
#-dontwarn okhttp3.logging.**
#-keep class okhttp3.internal.**{*;}
#-dontwarn okio.**

-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**


# Retrofit
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keep class cn.pedant.sweetalert.** {*; }
-keep class com.jaeger.statusbaruitl.** {*; }
-keep class com.airbnb.lottie.** {*; }
-keep class me.imid.swipebacklayout.** {*; }
-keep class cn.hugeterry.coordinatortablayout.** {*; }
-keep class com.soundcloud.android.** {*; }
-dontwarn cn.pedant.sweetalert.**
-dontwarn com.jaeger.statusbaruitl.**
-dontwarn com.airbnb.lottie.**
-dontwarn me.imid.swipebacklayout.**
-dontwarn cn.hugeterry.coordinatortablayout.**
-dontwarn com.soundcloud.android.**

####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####

#百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
