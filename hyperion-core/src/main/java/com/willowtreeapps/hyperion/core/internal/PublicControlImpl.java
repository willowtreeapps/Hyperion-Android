package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import androidx.core.view.ViewCompat;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.core.PublicControl;
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
    public boolean open() {
        final Activity foregroundActivity = foregroundManager.getForegroundActivity();
        if (foregroundActivity != null) {
            return open(foregroundActivity);
        }
        return false;
    }

    @Override
    public boolean open(Activity activity) {
        CoreComponent component = container.getComponent(activity);
        if (component == null) {
            return false;
        }
        return component.getMenuController().expand();
    }

    @Override
    public boolean close() {
        final Activity foregroundActivity = foregroundManager.getForegroundActivity();
        if (foregroundActivity != null) {
            return close(foregroundActivity);
        }
        return false;
    }

    @Override
    public boolean close(Activity activity) {
        CoreComponent component = container.getComponent(activity);
        if (component == null) {
            return false;
        }
        return component.getMenuController().collapse();
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