package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.util.Log;

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
            final HyperionMenuLayout menu = activity.findViewById(R.id.hyperion_menu);
            menu.setShakeGestureSensitivity(sensitivity);
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
        final HyperionMenuLayout menu = activity.findViewById(R.id.hyperion_menu);
        if (menu == null) {
            Log.d("Hyperion", "Could not find Hyperion menu in this activity.");
            return;
        }
        menu.expand();
    }

}