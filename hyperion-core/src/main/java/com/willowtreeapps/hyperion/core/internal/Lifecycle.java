package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Set;

import javax.inject.Inject;

@AppScope
public class Lifecycle implements Application.ActivityLifecycleCallbacks {

    private final Set<LifecycleDelegate> delegates;

    @Inject
    Lifecycle(Set<LifecycleDelegate> delegates) {
        this.delegates = delegates;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        for (LifecycleDelegate delegate : delegates) {
            delegate.onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        for (LifecycleDelegate delegate : delegates) {
            delegate.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        for (LifecycleDelegate delegate : delegates) {
            delegate.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        for (LifecycleDelegate delegate : delegates) {
            delegate.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        for (LifecycleDelegate delegate : delegates) {
            delegate.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        for (LifecycleDelegate delegate : delegates) {
            delegate.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        for (LifecycleDelegate delegate : delegates) {
            delegate.onActivityDestroyed(activity);
        }
    }
}