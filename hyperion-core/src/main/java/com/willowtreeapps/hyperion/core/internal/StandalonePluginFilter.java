package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.HashSet;
import java.util.Set;

class StandalonePluginFilter implements PluginFilter {
    @Override
    public Set<PluginModule> filter(Set<PluginModule> plugins) {
        HashSet<PluginModule> result = new HashSet<>();
        for(PluginModule plugin : plugins) {
            if(plugin.isStandalone()) {
                result.add(plugin);
            }
        }
        return result;
    }
}

