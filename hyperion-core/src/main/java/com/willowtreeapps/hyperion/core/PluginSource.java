package com.willowtreeapps.hyperion.core;

import com.willowtreeapps.hyperion.plugin.v1.Plugin;

import java.util.Set;

public interface PluginSource {
    Set<Plugin> getPlugins();
}