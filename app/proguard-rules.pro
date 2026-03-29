# Add project specific ProGuard rules here.
-keep class com.filmnot.data.model.** { *; }
-keep class com.filmnot.data.db.entity.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
