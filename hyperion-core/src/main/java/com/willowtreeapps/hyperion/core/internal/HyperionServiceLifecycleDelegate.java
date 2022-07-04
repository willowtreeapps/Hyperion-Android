package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import javax.inject.Inject;

@AppScope
class HyperionServiceLifecycleDelegate extends LifecycleDelegate {

    private final CoreComponentContainer container;

    @Inject
    HyperionServiceLifecycleDelegate(CoreComponentContainer container) {
        this.container = container;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        CoreComponent component = container.getComponent(activity);
        if (component == null) {
            return;
        }
        final ServiceConnection connection = component.getServiceConnection();
        activity.bindService(
                new Intent(activity, HyperionService.class),
                connection,
                Context.BIND_AUTO_CREATE);
        component.getMenuController().onStart();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        CoreComponent component = container.getComponent(activity);
        if (component == null) {
            return;
        }
        final ServiceConnection connection = component.getServiceConnection();
        activity.unbindService(connection);
        if (connection instanceof HyperionService.Connection) {
            ((HyperionService.Connection) connection).forceDisconnect();
        }
        component.getMenuController().onStop();
    }
}