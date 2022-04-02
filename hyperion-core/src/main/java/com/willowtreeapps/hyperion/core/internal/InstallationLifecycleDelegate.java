package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.os.Bundle;

import com.willowtreeapps.hyperion.core.PluginViewFactory;

import javax.inject.Inject;

@AppScope
class InstallationLifecycleDelegate extends LifecycleDelegate {
    private final PluginViewFactory factory;

    @Inject
    InstallationLifecycleDelegate(PluginViewFactory factory) {
        this.factory = factory;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        factory.create(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        factory.destroy(activity);
    }
}