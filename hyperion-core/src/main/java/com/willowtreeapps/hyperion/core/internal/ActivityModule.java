package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;

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
    LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
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