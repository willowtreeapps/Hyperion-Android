package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.os.Bundle;

import com.willowtreeapps.hyperion.core.PluginViewFactory;

import javax.inject.Inject;

@AppScope
class InstallationLifecycleDelegate extends LifecycleDelegate {
    private final HyperionPluginView.Factory factory;

    @Inject
    InstallationLifecycleDelegate(HyperionPluginView.Factory factory) {
        this.factory = factory;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        factory.createInternal(activity, true, false);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        factory.destroy(activity);
    }
}