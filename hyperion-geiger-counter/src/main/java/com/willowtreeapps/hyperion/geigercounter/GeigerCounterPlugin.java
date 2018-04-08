package com.willowtreeapps.hyperion.geigercounter;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class GeigerCounterPlugin extends Plugin {

    public static final String LOG_TAG = "Hyperion Geiger Counter";

    @Override
    public PluginModule createPluginModule() {
        return new GeigerCounterModule();
    }

}