package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.support.v4.util.ArrayMap;

import com.willowtreeapps.hyperion.core.PluginSource;

import java.util.Set;

import javax.inject.Inject;

@AppScope
class CoreComponentContainer {

    private final ArrayMap<Activity, CoreComponent> components = new ArrayMap<>();
    private PluginSource pluginSource;

    @Inject
    CoreComponentContainer() {
        pluginSource = new ServiceLoaderPluginSource();
    }

    void setPluginSource(PluginSource pluginSource) {
        this.pluginSource = pluginSource;
    }

    PluginSource getPluginSource() {
        return pluginSource;
    }

    CoreComponent getComponent(Activity activity) {
        return components.get(activity);
    }

    CoreComponent removeComponent(Activity activity) {
        return components.remove(activity);
    }

    CoreComponent putComponent(Activity activity, CoreComponent component) {
        return components.put(activity, component);
    }

    Set<Activity> getActivities() {
        return components.keySet();
    }

}