package com.willowtreeapps.hyperion.location;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class LocationPlugin extends Plugin {

    @Override
    public PluginModule createPluginModule() {
        return new LocationModule();
    }
}