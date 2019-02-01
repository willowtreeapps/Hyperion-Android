package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;

import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Collections;
import java.util.Set;

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

    @Provides
    @ActivityScope
    static Set<PluginModule> modules(PluginRepository pluginRepository) {
        return pluginRepository.getPlugins().createModules();
    }
}