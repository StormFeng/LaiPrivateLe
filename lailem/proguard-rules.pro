# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android\sdk/tools/proguard/proguard-android.txt
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

#-ignorewarnings
# 指定代码的压缩级别
-optimizationpasses 5
-dontusemixedcaseclassnames
# 是否混淆第三方jar
-dontskipnonpubliclibraryclasses
-dontpreverify
-keepattributes SourceFile,LineNumberTable
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# webview + js
# keep 使用 webview 的类
-keepclassmembers class com.goldnet.mobile.activity.InfoDetailActivity {
   public *;
}
# keep 使用 webview 的类的所有的内部类
-keepclassmembers   class com.goldnet.mobile.activity.InfoDetailActivity$*{
    *;
}

# 保持哪些类不被混淆
-keep class android.** {*; }
-keep public class * extends android.view
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.pm
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    void onClick*(...);
}
-keepclasseswithmembers class * {
    *** *Callback(...);
}
# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}




#Lailem
-keep class com.lailem.app.bean.**{*;}
-keep interface com.lailem.app.bean.**{*;}
-keep class com.lailem.app.jsonbean.**{*;}
-keep interface com.lailem.app.jsonbean.**{*;}
-keep class com.lailem.app.widget.** {*;}
-keep interface com.lailem.app.widget.**{*;}
-keep class com.lailem.app.** implements java.io.Serializable {*;}
-keep class com.lailem.app.chat.model.** {*;}
-keep class com.lailem.app.dao.** {*;}

#趣拍
-dontwarn com.alibaba.**
-keep class com.alibaba.**
-keepclassmembers class com.alibaba.** {
    *;
}
-keep class com.taobao.**
-keepclassmembers class com.taobao.** {
    *;
}

-dontwarn com.google.common.**
-dontwarn com.fasterxml.jackson.**
-dontwarn com.amap.api.**
-dontwarn net.jcip.annotations.**

-keepattributes Annotation,EnclosingMethod,Signature
-keep class com.fasterxml.jackson.**
-keepclassmembers class com.fasterxml.jackson.** {
    *;
}

-keep class com.duanqu.**
-keepclassmembers class com.duanqu.** {
    *;
}

#百度地图
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

#CIA
-dontwarn cn.ciaapp.**
-keep class cn.ciaapp.**{*;}

#GreenDao
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

#Gson
-keepattributes Signature
-keep class sun.misc.Unsafe{*;}
-keep class com.google.gson.stream.**{*;}
-keep class com.google.gson.examples.android.model.**{*;}
-keep class com.google.gson.**{*;}

-dontwarn android.support.**
-keep class android.support.**{*;}
-keep class com.google.**{*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


# 微信
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
# 新浪微博
-keep class com.sina.**{*;}


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

#讯飞语音
-dontwarn com.iflytek.**
-keepattributes Signature
-keep class com.iflytek.**{*;}

#zxing
-dontwarn com.google.zxing.**
-keep  class com.google.zxing.**{*;}

#dagger
-keep class dagger.** { *; }
-dontwarn dagger.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @dagger.* <fields>;
}
-keepclasseswithmembernames class * {
    @dagger.* <methods>;
}


