### February 22, 2023 - v0.9.37

- No user-facing functionality, this version is just a swap to a new publishing plugin with a new gpg key

### August 15, 2022 - v0.9.36

- Allows for customization where Hyperion will launch, e.g. it can now be embedded in a new Activity or a BottomSheetFragment.
- Fixes two memory leaks: one with how activities are destroyed, and the other is a leak in HyperionService described in #224.  (Fixes [#224](https://github.com/willowtreeapps/Hyperion-Android/issues/224))

### September 29, 2021 - v0.9.34

- Fixes PendingIntent flag crash when targeting API 31+ (Fixes [#228](https://github.com/willowtreeapps/Hyperion-Android/issues/228))

### June 24, 2021 - v0.9.33

- Fixes NPE caused by hyperion service disconnect (Fixes [#225](https://github.com/willowtreeapps/Hyperion-Android/issues/225))

### March 26, 2021 - v0.9.32

- Fixes memory leak caused by dangling hyperion service connections (Fixes [#220](https://github.com/willowtreeapps/Hyperion-Android/issues/220))

### January 25, 2021 - v0.9.31

- Increment target SDK version to 30 (Fixes [#191](https://github.com/willowtreeapps/Hyperion-Android/issues/191))

### October 13, 2020 - v0.9.30

- Update Core plugins to Androidx / Jetpack

### August 18, 2020 - v0.9.29

- Fixes CI publish script
- Updates AGP and gradle versions

### August 18, 2020 - v0.9.28

- Fixes [#197](https://github.com/willowtreeapps/Hyperion-Android/issues/197)
- Fixes [#190](https://github.com/willowtreeapps/Hyperion-Android/issues/190)
- [Selectable text](https://github.com/willowtreeapps/Hyperion-Android/pull/188) in crash plugin
- Fixes [#179](https://github.com/willowtreeapps/Hyperion-Android/issues/179)
- Fixes [#175](https://github.com/willowtreeapps/Hyperion-Android/issues/175)
- Adds [public API](https://github.com/willowtreeapps/Hyperion-Android/pull/177) for closing the drawer
- Adds [Timber plugin](https://github.com/willowtreeapps/Hyperion-Android/pull/174)

### January 4, 2019 - v0.9.27

- Support private variables in BuildConfig (Enabled test coverage to demonstrate private field in sample)
- Support basic printing of arrays in BuildConfig
- Added try/catch to prevent a blank screen on any future unsupported fields in the BuildConfig

### December 10, 2018 - v0.9.26

- Fixes Recorder Plugin menu layout
- Adds button to Timber Plugin to share logs

### November 26, 2018 - v0.9.25

- Fixes foreground permission issue on Android P
