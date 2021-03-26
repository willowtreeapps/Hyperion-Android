package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.collection.SimpleArrayMap;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;

import javax.inject.Inject;

@AppScope
class HyperionIgnoreFilter implements Application.ActivityLifecycleCallbacks {

    private final SimpleArrayMap<Class<?>, Boolean> cache = new SimpleArrayMap<>(5);
    private final Lifecycle lifecycle;

    @Inject
    HyperionIgnoreFilter(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (!shouldIgnore(activity)) {
            lifecycle.onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (!shouldIgnore(activity)) {
            lifecycle.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!shouldIgnore(activity)) {
            lifecycle.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (!shouldIgnore(activity)) {
            lifecycle.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (!shouldIgnore(activity)) {
            lifecycle.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (!shouldIgnore(activity)) {
            lifecycle.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (!shouldIgnore(activity)) {
            lifecycle.onActivityDestroyed(activity);
        }
    }

    private boolean shouldIgnore(Activity activity) {
        final Class<?> clz = activity.getClass();
        if (cache.containsKey(clz)) {
            return cache.get(clz);
        } else {
            HyperionIgnore ignore = activity.getClass().getAnnotation(HyperionIgnore.class);
            boolean shouldIgnore = ignore != null;
            if (activity.getClass().getName().equals("leakcanary.internal.activity.LeakActivity")){
                shouldIgnore = true;
            }
            cache.put(clz, shouldIgnore);
            return shouldIgnore;
        }
    }
}