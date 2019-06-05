package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.core.PublicControl;
import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.ForegroundManager;

import javax.inject.Inject;

@AppScope
class PublicControlImpl implements PublicControl {

    private final CoreComponentContainer container;
    private final ForegroundManager foregroundManager;
    private float sensitivity = 3.0f;

    @Inject
    PublicControlImpl(CoreComponentContainer container, ForegroundManager foregroundManager) {
        this.container = container;
        this.foregroundManager = foregroundManager;
    }

    @Override
    public float getShakeGestureSensitivity() {
        return sensitivity;
    }

    @Override
    public void setShakeGestureSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
        for (Activity activity : container.getActivities()) {
            CoreComponent component = container.getComponent(activity);
            if (component != null) {
                component.getMenuController().setShakeGestureSensitivity(sensitivity);
            }
        }
    }

    @Override
    public void open() {
        final Activity foregroundActivity = foregroundManager.getForegroundActivity();
        if (foregroundActivity != null) {
            open(foregroundActivity);
        }
    }

    @Override
    public void open(Activity activity) {
        CoreComponent component = container.getComponent(activity);
        if (component == null) {
            return;
        }
        component.getMenuController().expand();
    }

    @Override
    public void close() {
        final Activity foregroundActivity = foregroundManager.getForegroundActivity();
        if (foregroundActivity != null) {
            close(foregroundActivity);
        }
    }

    @Override
    public void close(Activity activity) {
        CoreComponent component = container.getComponent(activity);
        if (component == null) {
            return;
        }
        component.getMenuController().collapse();
    }

    @Override
    public void setPluginSource(PluginSource pluginSource) {
        container.setPluginSource(pluginSource);
    }

    @Override
    public PluginSource getPluginSource() {
        return container.getPluginSource();
    }
}