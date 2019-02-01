package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Set;

import javax.inject.Inject;

@AppScope
class CoreComponentContainer {

    private final ArrayMap<Activity, CoreComponent> components = new ArrayMap<>();
    private final ArrayMap<Activity, Set<PluginModule>> pluginModules = new ArrayMap<>();
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

    @Nullable
    CoreComponent getComponent(Activity activity) {
        return components.get(activity);
    }

    void removeComponent(Activity activity) {
       components.remove(activity);
        Set<PluginModule> pluginModules = this.pluginModules.remove(activity);
        for (PluginModule module : pluginModules) {
            module.destroy();
        }
    }

    void putComponent(Activity activity, CoreComponent component) {
        components.put(activity, component);
        Set<PluginModule> pluginModules = component.getPluginModules();
        this.pluginModules.put(activity, pluginModules);
        PluginExtension pluginExtension = new PluginExtensionImpl(component);
        final Context context = new PluginExtensionContextWrapper(activity, pluginExtension);
        for (PluginModule module : pluginModules) {
            module.create(pluginExtension, context);
        }
    }

    Set<Activity> getActivities() {
        return components.keySet();
    }

}