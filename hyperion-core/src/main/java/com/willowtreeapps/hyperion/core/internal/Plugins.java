package com.willowtreeapps.hyperion.core.internal;

import android.support.annotation.NonNull;

import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Plugins {

    private final Set<Plugin> plugins;

    Plugins(@NonNull Set<Plugin> plugins) {
        this.plugins = plugins;
    }

    Set<Plugin> get() {
        return Collections.unmodifiableSet(plugins);
    }

    @NonNull
    Set<PluginModule> createModules() {
        final Set<PluginModule> modules = new HashSet<>();
        for (Plugin plugin : plugins) {
            final PluginModule module = plugin.createPluginModule();
            if (module != null) {
                modules.add(module);
            }
        }
        return modules;
    }
}