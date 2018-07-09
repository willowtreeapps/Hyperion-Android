# Build Config
Plugin for viewing the BuildConfig values of the application.

## Notes
If minification is enabled for your debug builds, you will likely want to update proguard to keep your BuildConfig class.  

Update to match your package name.
```
-keep class com.willowtreeapps.hyperion.sample.BuildConfig { *; }
```
