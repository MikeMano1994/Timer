# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Workspace\Softwares\android-sdk-windows/tools/proguard/proguard-android.txt
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


######################友盟统计需要添加##开始######################
-keepclassmembers class * {
    public <init> (org.json.JSONObject);
}
#由于SDK需要引用导入工程的资源文件，通过了反射机制得到资源引用文件R.java，
#但是在开发者通过proguard等混淆/优化工具处理apk时，proguard可能会将R.java删除，
#如果遇到这个问题，请在proguard配置文件中添加keep命令（Tip：如果混淆时遇到再把下面的打开）
#-keep public class [您的应用包名].R$*{
#    public static final int *;
#}
#如果您使用5.0.0及以上版本的SDK，请添加如下命令
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
######################友盟统计需要添加##结束######################