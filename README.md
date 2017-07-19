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
  <version>0.1-SNAPSHOT</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.willowtreeapps.hyperion:hyperion-core:0.1-SNAPSHOT'
```

Usage
-----

Include the core library along with any number of plugins.

```groovy
debugCompile 'com.willowtreeapps.hyperion:hyperion-core:0.1-SNAPSHOT'
debugCompile 'com.willowtreeapps.hyperion:hyperion-attr:0.1-SNAPSHOT'
debugCompile 'com.willowtreeapps.hyperion:hyperion-measurement:0.1-SNAPSHOT'
debugCompile 'com.willowtreeapps.hyperion:hyperion-disk:0.1-SNAPSHOT'
debugCompile 'com.willowtreeapps.hyperion:hyperion-recorder:0.1-SNAPSHOT'
```

Read more about the built-in plugins in their individual module READMEs.

To create your own plugin, implement the Plugin interface and expose the implementation as a service. The plugins made available in this repository leverage Google's [AutoService][1] annotation processor to generate the service metadata and simplify the process.

 [1]: https://github.com/google/auto/tree/master/service