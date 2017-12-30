Hyperion
======

An app debugging & inspection tool for Android.

Download
--------

Download via Maven:
```xml
<dependency>
  <groupId>com.willowtreeapps.hyperion</groupId>
  <artifactId>hyperion-core</artifactId>
  <version>1.0-RC1</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.willowtreeapps.hyperion:hyperion-core:1.0-RC1'
```

Usage
-----

Include the core library along with any number of plugins.

```groovy
debugCompile 'com.willowtreeapps.hyperion:hyperion-core:1.0-RC1'
debugCompile 'com.willowtreeapps.hyperion:hyperion-attr:1.0-RC1'
debugCompile 'com.willowtreeapps.hyperion:hyperion-measurement:1.0-RC1'
debugCompile 'com.willowtreeapps.hyperion:hyperion-disk:1.0-RC1'
debugCompile 'com.willowtreeapps.hyperion:hyperion-recorder:1.0-RC1'
```

Read more about the built-in plugins in their individual module READMEs.

To create your own plugin, implement the Plugin interface and expose the implementation as a service. The plugins made available in this repository leverage Google's [AutoService][1] annotation processor to generate the service metadata and simplify the process.

 [1]: https://github.com/google/auto/tree/master/service