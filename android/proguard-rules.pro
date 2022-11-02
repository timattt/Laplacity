# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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

#-verbose
#-dontobfuscate
#-ignorewarnings

-dontwarn android.support.**
-dontwarn com.badlogic.gdx.backends.android.AndroidFragmentApplication
-dontwarn com.badlogic.gdx.utils.GdxBuild
-dontwarn com.badlogic.gdx.physics.box2d.utils.Box2DBuild
-dontwarn com.badlogic.gdx.jnigen.BuildTarget*
-dontwarn com.badlogic.gdx.graphics.g2d.freetype.FreetypeBuild

# If you're encountering ProGuard issues and use gdx-controllers, THIS MIGHT BE WHY!!!

# Uncomment the following line if you use the gdx-controllers official extension.
#-keep class com.badlogic.gdx.controllers.android.AndroidControllers

-keepclassmembers class com.badlogic.gdx.backends.android.AndroidInput* {
   <init>(com.badlogic.gdx.Application, android.content.Context, java.lang.Object, com.badlogic.gdx.backends.android.AndroidApplicationConfiguration);
}

-keepclassmembers class com.badlogic.gdx.physics.box2d.World {
   boolean contactFilter(long, long);
   void    beginContact(long);
   void    endContact(long);
   void    preSolve(long, long);
   void    postSolve(long, long);
   boolean reportFixture(long);
   float   reportRayFixture(long, float, float, float, float, float);
}

### ACRA ProGuard rules###

# Restore some Source file names and restore approximate line numbers in the stack traces,
# otherwise the stack traces are pretty useless
-keepattributes SourceFile,LineNumberTable

# ACRA loads Plugins using reflection
-keep class * implements org.acra.plugins.Plugin {*;}

# ACRA uses enum fields in json
-keep enum org.acra.** {*;}

# autodsl accesses constructors using reflection
-keepclassmembers class * implements org.acra.config.Configuration { <init>(...); }

# ACRA creates a proxy for this interface
-keep interface org.acra.ErrorReporter

-dontwarn android.support.**

-dontwarn com.faendir.kotlin.autodsl.DslInspect
-dontwarn com.faendir.kotlin.autodsl.DslMandatory
-dontwarn com.google.auto.service.AutoService


### ironSource ProGuard rules ###
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** { *;
}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}


### devtodev ProGuard rules ###
-keep class com.devtodev.** { *; }
-dontwarn com.devtodev.**


### Laplacity ProGuard rules ###
-keep class org.acra.config.CoreConfiguration
-keep class kotlin.jvm.internal.DefaultConstructorMarker
-keep class kotlin.internal.**
-keep class org.acra.builder.ReportBuilder
-keep class org.acra.plugins.PluginLoader
-keep class org.acra.data.CrashReportData
-keep class org.acra.builder.LastActivityManager
-keep class com.google.android.gms.internal.**
-keep class com.google.android.gms.common.internal.**
-keep class com.google.android.gms.common.api.internal.**
-keep class com.google.firebase.components.ComponentContainer
-keep class com.google.ads.AdSize
-keep class com.badlogic.gdx.Application
-keep class com.badlogic.gdx.ApplicationLogger
-keep class com.badlogic.gdx.backends.android.**
-keep class com.badlogic.gdx.ApplicationListener
-keep class com.badlogic.gdx.LifecycleListener
-keep class com.badlogic.gdx.backends.android.AndroidEventListener
-keep class com.badlogic.gdx.files.FileHandle
-keep class com.google.android.gms.dynamic.IObjectWrapper
-keep class com.google.ads.mediation.**
-keep class kotlin.jvm.functions.Function1
-keep class com.ironsource.sdk.controller.**

-keep class steelUnicorn.laplacity.BuildConfig
-keepclassmembers class steelUnicorn.laplacity.BuildConfig {
    public <fields>;
}

-keepclassmembers class com.google.android.gms.internal.appset.zzr {
    *** getAppSetIdInfo();
}

-keep class com.badlogic.gdx.scenes.scene2d.ui.**
-keepclassmembers class com.badlogic.gdx.scenes.scene2d.ui.** {
    <fields>;
}

-keepclassmembers class steelUnicorn.laplacity.utils.LevelParams {
    <fields>;
}

-keepclassmembers class steelUnicorn.laplacity.utils.LevelParams$Hint {
    <fields>;
}

-keep class com.badlogic.gdx.graphics.Color
-keep class com.badlogic.gdx.graphics.g2d.BitmapFont

-keepclassmembers class com.google.android.gms.dynamic.ObjectWrapper {
    <fields>;
}
