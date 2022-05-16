package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Set;

interface PluginFilter {
    Set<PluginModule> filter(Set<PluginModule> plugins);
}