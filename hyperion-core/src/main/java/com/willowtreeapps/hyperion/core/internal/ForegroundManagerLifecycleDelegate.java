package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;

import javax.inject.Inject;

@AppScope
class ForegroundManagerLifecycleDelegate extends LifecycleDelegate {

    private final ForegroundManagerImpl foregroundManager;
    private int activityCount = 0;

    @Inject
    ForegroundManagerLifecycleDelegate(ForegroundManagerImpl foregroundManager) {
        this.foregroundManager = foregroundManager;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        foregroundManager.setForegroundActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        final Activity foregroundActivity = foregroundManager.getForegroundActivity();
        if (foregroundActivity == activity) {
            foregroundManager.setForegroundActivity(null);
        }
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        if (!appIsForegrounded()) {
            foregroundManager.notifyAppForegrounded(activity);
            activityCount = 0;
        }
        activityCount++;
        foregroundManager.setApplicationForeground(appIsForegrounded());
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        activityCount--;
        if (!appIsForegrounded()) {
            foregroundManager.notifyAppBackgrounded(activity);
            activityCount = 0;
        }
        foregroundManager.setApplicationForeground(appIsForegrounded());
    }

    private boolean appIsForegrounded() {
        return activityCount > 0;
    }
}