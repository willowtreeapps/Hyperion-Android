package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.Lazy;

@AppScope
class PluginRepository {

    private final PluginSource pluginSource;
    private final Lazy<Plugins> plugins = new Cached<>(new PluginsProvider());

    @Inject
    PluginRepository(PluginSource pluginSource) {
        this.pluginSource = pluginSource;
    }

    Plugins getPlugins() {
        return plugins.get();
    }

    private class PluginsProvider implements Provider<Plugins> {

        @Override
        public Plugins get() {
            final Set<Plugin> plugins = pluginSource.getPlugins();
            Set<Plugin> compatiblePlugins = new HashSet<>();
            for (Plugin plugin : plugins) {
                if (plugin.deviceMeetsMinimumApiRequirement()) {
                    compatiblePlugins.add(plugin);
                }
            }
            return new Plugins(compatiblePlugins);
        }
    }

}