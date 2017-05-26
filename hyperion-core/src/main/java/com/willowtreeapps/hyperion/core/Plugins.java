package com.willowtreeapps.hyperion.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

final class Plugins {

    private static List<Plugin> plugins;

    static List<Plugin> get() {
        synchronized (Plugins.class) {
            if (plugins == null) {
                synchronized (Plugins.class) {
                    plugins = load();
                }
            }
        }
        return plugins;
    }

    private static List<Plugin> load() {
        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
        List<Plugin> plugins = new ArrayList<>(2);
        for (Plugin plugin : loader) {
            plugins.add(plugin);
        }
        return plugins;
    }

}