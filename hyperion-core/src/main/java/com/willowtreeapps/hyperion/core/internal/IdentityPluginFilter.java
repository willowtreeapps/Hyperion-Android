package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Set;

class IdentityPluginFilter implements PluginFilter {
    @Override
    public Set<PluginModule> filter(Set<PluginModule> plugins) {
        return plugins;
    }
}
