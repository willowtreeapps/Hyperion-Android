package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.willowtreeapps.hyperion.plugin.v1.ForegroundManager;
import com.willowtreeapps.hyperion.plugin.v1.OnApplicationBackgroundListener;
import com.willowtreeapps.hyperion.plugin.v1.OnApplicationForegroundListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@AppScope
class ForegroundManagerImpl implements ForegroundManager {

    private final List<OnApplicationForegroundListener> foregroundListeners = new ArrayList<>();
    private final List<OnApplicationBackgroundListener> backgroundListeners = new ArrayList<>();
    private Activity activity;
    private boolean appForeground;

    @Inject
    ForegroundManagerImpl() {

    }

    @Override
    public Activity getForegroundActivity() {
        return activity;
    }

    void setForegroundActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean isApplicationForeground() {
        return appForeground;
    }

    void setApplicationForeground(boolean appForeground) {
        this.appForeground = appForeground;
    }

    @Override
    public void addOnApplicationForegroundListener(@NonNull OnApplicationForegroundListener listener) {
        foregroundListeners.add(listener);
    }

    @Override
    public boolean removeOnApplicationForegroundListener(@NonNull OnApplicationForegroundListener listener) {
        return foregroundListeners.remove(listener);
    }

    @Override
    public void addOnApplicationBackgroundListener(@NonNull OnApplicationBackgroundListener listener) {
        backgroundListeners.add(listener);
    }

    @Override
    public boolean removeOnApplicationBackgroundListener(@NonNull OnApplicationBackgroundListener listener) {
        return backgroundListeners.remove(listener);
    }

    void notifyAppForegrounded(Activity activity) {
        for (OnApplicationForegroundListener listener : foregroundListeners) {
            listener.onApplicationForeground(activity.getApplicationContext());
        }
    }

    void notifyAppBackgrounded(Activity activity) {
        for (OnApplicationBackgroundListener listener : backgroundListeners) {
            listener.onApplicationBackground(activity.getApplicationContext());
        }
    }
}