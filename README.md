# Hyperion

[![CircleCI](https://circleci.com/gh/willowtreeapps/Hyperion-Android.svg?style=svg&circle-token=3d0158d85c451692a4ce0ee18eb12617f67206eb)](https://circleci.com/gh/willowtreeapps/Hyperion-Android)
[![Maven Central](https://img.shields.io/maven-central/v/com.willowtreeapps.hyperion/hyperion-core.svg)](https://search.maven.org/search?q=g:com.willowtreeapps.hyperion)
[![License MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)]()
[![Public Yes](https://img.shields.io/badge/Public-yes-green.svg?style=flat)]()

![Hyperion Logo](art/Hyperion-Logo.png)

# Hyperion - App Inspection Tool

## What is it?

Hyperion is a hidden plugin drawer that can easily be integrated into any app. The drawer sits discreetly 🙊 under the app so that it is there when you need it and out of the way when you don't. Hyperion plugins are designed to make inspection of your app quick and simple.

Please see our announcement [blog post](https://willowtreeapps.com/ideas/introducing-hyperion-for-android) for a feature showcase.

![Demo Attribute inspector](https://images.ctfassets.net/3cttzl4i3k1h/1KhiROG0wcSi8QYa6iYGI0/0cd93ebf5a67012c09e16964032ea7e6/image2new.gif)

## Third-Party Plugins

Be one of the first to create a third-party plugin. The plugin creation guide is a work in progress, but if you are feeling ambitious you can reference the plugins we have already created.

To create your own plugin, implement the Plugin interface and expose the implementation as a service. The plugins made available in this repository leverage Google's [AutoService](https://github.com/google/auto/tree/master/service) annotation processor to generate the service metadata and simplify the process.

## How To Show Hyperion Plugin List

Once Hyperion is integrated into your app, simply shake your phone to activate. If you are running your app on an emulator, you can manually open the menu by calling `Hyperion.open(Activity activity)`. You can also open the menu by selecting the foreground notification that appears while the client app is in the foreground.

### Conditional Startup

Hyperion enables itself automatically for all Activities in your application. To disable for specific Activities, annotate them with `@HyperionIgnore`. To disable for all Activities, set a `meta-data` field in your `AndroidManifest.xml`:

```xml
    <meta-data
        android:name="com.willowtreeapps.hyperion.core.enableOnStart"
        android:value="false"/>
```

`Hyperion#open(Activity)` will have no affect if Hyperion has not been enabled. You can reenable for future Activities with `Hyperion.enable()`.

To embed the Hyperion Plugin Menu into your own Activity, acquire a `PluginViewFactory` from `Hyperion#getPluginViewFactory` and call `create(Activity)`:

```Java
View pluginView = Hyperion.getPluginViewFactory().create(activity);
```

See [FactoryActivity](hyperion-sample/src/main/java/com/willowtreeapps/hyperion/sample/FactoryActivity.java) for more details.

## Sample App

Want to learn how to use Hyperion? The sample app will teach you!

Build the example project by cloning the repo, run `./gradlew assemble` from the root directory, then open in Android Studio and run.

## Requirements

min SDK 15

### Download

--------

Download via Maven:

```xml
<dependency>
  <groupId>com.willowtreeapps.hyperion</groupId>
  <artifactId>hyperion-core</artifactId>
  <version>0.9.34</version>
</dependency>
```

or Gradle:

```groovy
debugImplementation 'com.willowtreeapps.hyperion:hyperion-core:0.9.34'
```

If you reference Hyperion from your code, you should also compile the no-op artifact for release variants. For most users, this will not be necessary:

```groovy
releaseImplementation 'com.willowtreeapps.hyperion:hyperion-core-no-op:0.9.34'
```

### Usage

--------

Include the core library along with any number of plugins.

```groovy
debugImplementation 'com.willowtreeapps.hyperion:hyperion-core:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-attr:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-build-config:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-crash:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-disk:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-geiger-counter:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-measurement:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-phoenix:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-recorder:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-shared-preferences:0.9.34'
debugImplementation 'com.willowtreeapps.hyperion:hyperion-timber:0.9.34'
```

## Adding Plugins

Hyperion plugins need to be added into the app at build time.
By default, Hyperion automatically finds every plugin that is available in the project using the Java Service Locator.

## Contributing to Hyperion

Contributions are welcome. Please see the [Contributing guidelines](CONTRIBUTING.md).

Hyperion has adopted a [code of conduct](CODE_OF_CONDUCT.md) defined by the [Contributor Covenant](http://contributor-covenant.org), the same used by the [Swift language](https://swift.org) and countless other open source software teams.

## Plugin Descriptions

The following is a list of all plugins that integrate with Hyperion. Please make a pull request if you would like to see your plugin here:

### Core Plugins

- [Hyperion-Attr](/hyperion-attr) - Inspect views and adjust their attributes.
- [Hyperion-Build-Config](/hyperion-build-config) - View application BuildConfig values.
- [Hyperion-Crash](/hyperion-crash) - Show alternative activity when app crashes with the crash details. No UI for this module within drawer.
- [Hyperion-Disk](/hyperion-disk) - Browse, delete, or share your app\'s files.
- [Hyperion-Geiger-Counter](/hyperion-geiger-counter) - Check animation performance by listening for dropped frames. Please turn up the media volume. Haptic feedback is also supported. Inspired by [KMCGeigerCounter](https://github.com/kconner/KMCGeigerCounter).
- [Hyperion-Measurement](/hyperion-measurement) - Tap views to measure the distances between them.
- [Hyperion-Phoenix](/hyperion-phoenix) - Clear local storage and relaunch the app.
- [Hyperion-Recorder](/hyperion-recorder) - Record, save, and share a video of your app.
- [Hyperion-Shared-Preferences](/hyperion-shared-preferences) - View and edit your app\'s key-value storage.
- [Hyperion-SQLite](/hyperion-sqlite) - Browse SQLite databases within your app.
- [Hyperion-Timber](/hyperion-timber) - View Timber recorded log messages.

### Third Party Plugins

- [Hyperion-Chuck](https://github.com/Commit451/Hyperion-Chuck) - Plugin which adds a button to inspect OkHttp traffic using [Chuck](https://github.com/jgilfelt/chuck)
- [Hyperion-AppInfo](https://github.com/STAR-ZERO/Hyperion-AppInfo) - Plugin which shows screen of details about an application
- [Hyperion-Simple-Item](https://github.com/takahirom/Hyperion-Simple-Item) - Plugin which adds simple menus
- [Hyperion-DBFlow-Manager](https://github.com/wajahatkarim3/DBFlowManager-Hyperion-Plugin) - Plugin which adds a button in inspect [DBFlow](https://github.com/Raizlabs/DBFlow) databases and apply queries on it locally.
- [Hyperion-kfin-state-machine](https://github.com/ToxicBakery/kfin-state-machine-hyperion) - Plugin for checking the state of registered [kfin-state-machine](https://github.com/ToxicBakery/kfin-state-machine) instances.
- [Hyperion-Device-Info](https://github.com/DroidsOnRoids/FoQA#device-info-plugin) - View device market name and Android version.
- [Hyperion-Font-Scale](https://github.com/DroidsOnRoids/FoQA#font-scale-plugin) - Change system font scale.
- [Hyperion-Host-Interceptor](https://github.com/MiSikora/Hyperion-Host-Interceptor) - Intercept OkHttp client host at runtime.

## License

Hyperion is available under the MIT license. See the LICENSE file for more info.

# About WillowTree

![WillowTree Logo](art/willowtree_logo.png)

We build apps, responsive sites, bots—any digital product that lives on a screen—for the world’s leading companies. Our elite teams challenge themselves to build extraordinary experiences by bridging the latest strategy and design thinking with enterprise-grade software development.

Interested in working on more unique projects like Hyperion? Check out our [careers page](http://willowtreeapps.com/careers?utm_campaign=hyperion-gh).
