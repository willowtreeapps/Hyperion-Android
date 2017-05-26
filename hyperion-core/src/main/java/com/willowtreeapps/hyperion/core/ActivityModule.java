package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import dagger.Module;
import dagger.Provides;

@Module
class ActivityModule {

    private final Activity activity;

    ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return this.activity;
    }

    @Provides
    Resources provideResources(Activity activity) {
        return activity.getResources();
    }

    @Provides
    DisplayMetrics provideDisplayMetricd(Resources resources) {
        return resources.getDisplayMetrics();
    }

}