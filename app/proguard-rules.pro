# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Farbod\Android-SDK/tools/proguard/proguard-android.txt
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


-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# To stop crashes when using Design Support Library
#-keep class android.support.design.widget.** { *; }
#-keep interface android.support.design.widget.** { *; }
#-dontwarn android.support.design.**

# To stop the error: 'Execution failed for task... Unable to compute hash of... classes.jar'
#-dontwarn java.nio.file.Files
#-dontwarn java.nio.file.Path
#-dontwarn java.nio.file.OpenOption
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# To stop error: 'com.google.android.gms...: can't find referenced class...':
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

# To stop random crashes for not finding SearchView
-keep class android.support.v7.widget.SearchView { *; }

# To be able to see line numbers in stack traces
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable