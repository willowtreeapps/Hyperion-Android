package com.willowtreeapps.hyperion.recorder;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class RecorderPlugin extends Plugin {

    @Override
    public PluginModule createPluginModule() {
        return new RecorderPluginModule();
    }

}