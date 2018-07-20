# Build Config
Plugin for viewing the BuildConfig values of the application.

## Configuration
For most applications, configuration is not necessary. In certain cases the name of the BuildConfig file will need to be manually configured. An example case is usage of the `applicationIdSuffix` modifier in the Android Gradle plugin `buildType` modifier.

`strings.xml` 
```xml
<!-- override BuildConfig name -->
<string name="hbc_target_build_config_name">com.example.myapp.BuildConfig</string>
```

## Notes
If minification is enabled for your debug builds, you will likely want to update proguard to keep your BuildConfig class.  

Update to match your package name.
```
-keep class com.willowtreeapps.hyperion.sample.BuildConfig { *; }
```
