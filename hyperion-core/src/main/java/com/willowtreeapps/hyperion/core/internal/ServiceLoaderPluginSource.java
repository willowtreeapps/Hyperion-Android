package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

class ServiceLoaderPluginSource implements PluginSource {

    @Override
    public Set<Plugin> getPlugins() {
        Set<Plugin> plugins = new HashSet<>();
        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
        for (Plugin plugin : loader) {
            plugins.add(plugin);
        }
        return plugins;
    }

}