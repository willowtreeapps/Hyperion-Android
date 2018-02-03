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
        final ServiceConnection connection = container.getComponent(activity).getServiceConnection();
        foregroundActivity.bindService(
                new Intent(activity, HyperionService.class),
                connection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (foregroundActivity == activity) {
            final ServiceConnection connection = container.getComponent(activity).getServiceConnection();
            foregroundActivity.unbindService(connection);
            foregroundActivity = null;
        }
    }
}