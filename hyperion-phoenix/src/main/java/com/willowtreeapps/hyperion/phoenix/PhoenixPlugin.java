package com.willowtreeapps.hyperion.phoenix;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class PhoenixPlugin extends Plugin {

    @Override
    public PluginModule createPluginModule() {
        return new PhoenixModule();
    }
}