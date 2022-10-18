# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5 # 指定代码的压缩级别
-dontusemixedcaseclassnames # 是否使用大小写混合
-dontskipnonpubliclibraryclasses# 是否混淆第三方jar
-dontpreverify# 混淆时是否做预校验
-keepattributes SourceFile,LineNumberTable										# 混淆号错误信息里带上代码行
-verbose# 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*# 混淆时所采用的算法
-ignorewarnings
-renamesourcefileattribute SourceFile

-keepclasseswithmembernames class * { # 保持 native 方法不被混淆
native <methods>;
}
-keepclasseswithmembernames class * implements android.view.View{ *; }
-keepclassmembers class * implements android.view.View{ *; }
-keep class * implements android.view.View{ *; }

#保证实体类不被混淆
-keep class * implements  android.support.v4.app.Fragment{ *; }
-keep class * implements android.os.Parcelable{ *; }
-keepclasseswithmembernames class * implements android.os.Parcelable{ *; }
-keepclassmembers class * implements android.os.Parcelable {*; }

#Gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# umeng
-dontwarn com.umeng.**
-keep class com.umeng.**{*;}
-keep class com.umeng.message.* {
public <fields>;
public <methods>;
}
-keep class com.umeng.message.protobuffer.* {
public <fields>;
public <methods>;
}
-keep class com.squareup.wire.* {
public <fields>;
public <methods>;
}
-keep class com.umeng.message.local.* {
public <fields>;
public <methods>;
}
-keep class org.android.agoo.impl.*{
public <fields>;
public <methods>;
}
-keep class org.android.agoo.service.* {*;}
-keep class org.android.spdy.**{*;}

#友盟分享
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
-keep class com.tencent.mm.sdk.modelmsg.** implements 	com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
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


#友盟推送
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn org.apache.thrift.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class com.wallstreetcn.alien.R$*{
   public static final int *;
}

##避免log打印输出
# -assumenosideeffects class android.util.Log {
#      public static *** v(...);
#      public static *** d(...);
#      public static *** i(...);
#      public static *** w(...);
# }

#下拉刷新类库
-keep class in.srain.**{*;}
-keepclassmembernames class in.srain.**{*;}

#realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**
-keep class * implements io.realm.RealmObject{*;}


# rxjava
-dontwarn rx.**
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}
# rxjava2
-dontwarn io.reactivex.**
-keep class io.reactivex.schedulers.Schedulers {
    public static <methods>;
}
-keep class io.reactivex.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.TestScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class io.reactivex.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}


##talking data
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}

##umeng 统计
-keepclassmembers class * {
 public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep public class com.wallstreetcn.news.R$*{
public static final int *;
}

##多渠道打包
-keep class com.mcxiaoke.packer.**{*;}

##阿里百川反馈
-keep class com.alibaba.sdk.android.feedback.impl.FeedbackServiceImpl {*;}
-keep class com.alibaba.sdk.android.feedback.impl.FeedbackAPI {*;}
-keep class com.alibaba.sdk.android.feedback.util.IWxCallback {*;}
-keep class com.alibaba.sdk.android.feedback.util.IUnreadCountCallback{*;}
-keep class com.alibaba.sdk.android.feedback.FeedbackService{*;}
-keep public class com.alibaba.mtl.log.model.LogField {public *;}
-keep class com.taobao.securityjni.**{*;}
-keep class com.taobao.wireless.security.**{*;}
-keep class com.ut.secbody.**{*;}
-keep class com.taobao.dp.**{*;}
-keep class com.alibaba.wireless.security.**{*;}
-keep class com.ta.utdid2.device.**{*;}

#语音播报
-keep class com.wallstreetcn.voicecloud.** {*;}

#科大讯飞
-keep class com.iflytek.**{*;}
-keep interface com.iflytek.**{*;}
-keepattributes Signature
-keepattributes *Annotation*


# ProGuard configurations for Bugtags
-keepattributes LineNumberTable,SourceFile

-keep class com.bugtags.library.** {*;}
-dontwarn com.bugtags.library.**
-keep class io.bugtags.** {*;}
-dontwarn io.bugtags.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient

#金证
-dontwarn com.bairuitech.**
-keep class com.bairuitech.**{*;}
-dontwarn com.itrus.raapi.**
-keep class com.itrus.raapi.**{*;}
-dontwarn com.org.apache.**
-keep class com.org.apache.**{*;}
-dontwarn org.apache.**
-keep class org.apache.**{*;}
-dontwarn org.**
-keep class org.**{*;}
-dontwarn com.google.**
-keep class com.google.**{*;}
-dontwarn com.kwl.common.utils.**
-keep class com.kwl.common.utils.**{*;}
-dontwarn com.kwl.common.utils.**
-keep class com.kwl.common.utils.**{*;}
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.kwlstock.sdk.**
-keep class com.kwlstock.sdk.**{*;}
-dontwarn com.pakh.video.sdk.**
-keep class com.pakh.video.sdk.**{*;}
-keep class util.**{*;}
-keep class com.openacc.**{*;}
-keep class com.pakh.**{*;}
-keep class com.pingan.**{*;}


# End Bugtags

# 华为pro
-keep class com.huawei.android.sdk.drm.**{*;}

-keep class com.huawei.android.pushagent.**{*;}
-keep class com.huawei.android.pushselfshow.**{*;}
-keep class com.huawei.android.microkernel.**{*;}
-keep class com.baidu.mapapi.**{*;}

#支付宝
-keep class com.alipay.**{*;}

# router
-keep class com.kronos.router.**{*;}

# bugly

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
# tinker混淆规则
-dontwarn com.tencent.tinker.**
-keep class com.tencent.tinker.** { *; }

-dontwarn com.tencent.**
-keep class com.tencent.**{*;}
-keep class com.tencent.tinker.**{*;}
-keep class * implements com.tencent.tinker.loader.app.DefaultApplicationLike
-keep class * implements android.app.Application
-keep public class * implements com.tencent.tinker.loader.app.ApplicationLiefCycle{*;}
-keep class com.tencent.tinker.loader.** {*;}
-keep public class * extends com.tencent.tinker.loader.TinkerLoader {*;}
-keep public class * extends com.tencent.tinker.loader.app.TinkerApplication {*;}



# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
#NGPACKER
-keep class com.mcxiaoke.**{*;}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**
-dontwarn com.wallstreetcn.**


 -keep class org.jetbrains.kotlin.** { *; }
 -keep class org.jetbrains.annotations.** { *; }
 -keepclassmembers class ** { @org.jetbrains.annotations.ReadOnly public *; }
 -keep class kotlin.reflect.**{*;}
 -keep class kotlin.**{*;}

 -dontwarn com.tencent.bugly.**
 -keep public class com.tencent.bugly.**{*;}

 -keep class com.micker.data.model.**
 -keepnames class * implements android.os.Parcelable {
  public static final ** CREATOR;
  }

  -keep class com.tencent.wxop.** {
     *;
  }
  -keep class com.twitter.** { *; }
  -keep class com.umeng.socialize.impl.ImageImpl {*;}


#httpdns
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.ta.**{*;}
-keep class com.ut.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.ta.**
-dontwarn com.ut.**

#cps
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.ta.**{*;}
-keep class com.ut.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.ta.**
-dontwarn com.ut.**
-keepclasseswithmembernames class ** {
native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.alipay.** {*;}
-dontwarn com.alipay.**
-keep class anet.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**

#hotfix
#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/buidl/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
#防止inline
-dontoptimize

#man
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.ta.**{*;}
-keep class com.ut.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.ta.**
-dontwarn com.ut.**

#feedback
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.ta.**{*;}
-keep class com.ut.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.ta.**
-dontwarn com.ut.**

-keep class android.support.**{*;}

-keep class com.micker.data.**{*;}

-keep class **.R$* {  *;  }

-keep class com.micker.aqhy.application.initial.*