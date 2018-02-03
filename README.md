# Hyperion

![Hyperion Logo](https://github.com/willowtreeapps/Hyperion-Android/art/Hyperion-Logo.png)

# Hyperion - App Inspection Tool

## What is it?

Hyperion is a hidden plugin drawer that can easily be integrated into any app. The drawer sits discreetly 🙊 under the app so that it is there when you need it and out of the way when you don't. Hyperion plugins are designed to make inspection of your app quick and simple.

## Third-Party Plugins
Calling all developers!!! Be one of the first to create a third-party plugin. The plugin creation guide is a work in progress, but if you are feeling ambitious you can reference the plugins we have already created.

To create your own plugin, implement the Plugin interface and expose the implementation as a service. The plugins made available in this repository leverage Google's [AutoService][1] annotation processor to generate the service metadata and simplify the process.

## How To Show Hyperion Plugin List
Once Hyperion is integrated into your app, simply shake your phone to activate. If you are running your app on an emulator, you can manually open the menu by calling `Hyperion.open(Activity activity)`. You can also open the menu by selecting the foreground notification that appears while the client app is in the foreground.

## Sample App
Want to learn how to use Hyperion? The sample app will teach you!

Build the example project by cloning the repo, run `./gradlew assemble` from the root directory, then open in Android Studio and run.

## Requirements
min SDK 15

Download
--------

Download via Maven:
```xml
<dependency>
  <groupId>com.willowtreeapps.hyperion</groupId>
  <artifactId>hyperion-core</artifactId>
  <version>0.9.14</version>
</dependency>
```
or Gradle:
```groovy
debugImplementation 'com.willowtreeapps.hyperion:hyperion-core:0.9.14'
```

Usage
-----

Include the core library along with any number of plugins.

```groovy
debugImplementation 'com.willowtreeapps.hyperion:hyperion-core:0.9.14'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-attr:0.9.14'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-measurement:0.9.14'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-disk:0.9.14'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-recorder:0.9.14'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-phoenix:0.9.14'
```

## Adding Plugins
Hyperion plugins need to be added into the app at build time.
By default, Hyperion automatically finds every plugin that is available in the project using the Java Service Locator.

## Contributing to Hyperion
Contributions are welcome. Please see the [Contributing guidelines](CONTRIBUTING.md).

Hyperion has adopted a [code of conduct](CODE_OF_CONDUCT.md) defined by the [Contributor Covenant](http://contributor-covenant.org), the same used by the [Swift language](https://swift.org) and countless other open source software teams.

## License
Hyperion is available under the MIT license. See the LICENSE file for more info.

# About WillowTree!
![WillowTree Logo](https://github.com/willowtreeapps/Hyperion-Android/art/willowtree_logo.png)

We build apps, responsive sites, bots—any digital product that lives on a screen—for the world’s leading companies. Our elite teams challenge themselves to build extraordinary experiences by bridging the latest strategy and design thinking with enterprise-grade software development.

Interested in working on more unique projects like Hyperion? Check out our [careers page](http://willowtreeapps.com/careers?utm_campaign=hyperion-gh).

Hyperion
======

An app debugging & inspection tool for Android.



Read more about the built-in plugins in their individual module READMEs.

 [1]: https://github.com/google/auto/tree/master/service