package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import javax.inject.Inject;

@AppScope
class HyperionServiceLifecycleDelegate extends LifecycleDelegate {

    private final CoreComponentContainer container;
    private Activity foregroundActivity;

    @Inject
    HyperionServiceLifecycleDelegate(CoreComponentContainer container) {
        this.container = container;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        foregroundActivity = activity;
        CoreComponent component = container.getComponent(activity);
        if (component == null) {
            return;
        }
        final ServiceConnection connection = component.getServiceConnection();
        foregroundActivity.bindService(
                new Intent(activity, HyperionService.class),
                connection,
                Context.BIND_AUTO_CREATE);
        component.getMenuController().onResume();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (foregroundActivity == activity) {
            CoreComponent component = container.getComponent(activity);
            if (component == null) {
                return;
            }
            final ServiceConnection connection = component.getServiceConnection();
            foregroundActivity.unbindService(connection);
            component.getMenuController().onPause();
            foregroundActivity = null;
        }
    }
}