package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;

@Module
abstract class ActivityModule {

    @Provides
    static LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
    }

    @Provides
    static Resources provideResources(Activity activity) {
        return activity.getResources();
    }

    @Provides
    static DisplayMetrics provideDisplayMetrics(Resources resources) {
        return resources.getDisplayMetrics();
    }

}