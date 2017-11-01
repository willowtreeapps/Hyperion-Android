package com.willowtreeapps.hyperion.recorder;

import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.plugins.v1.Plugin;
import com.willowtreeapps.hyperion.core.plugins.v1.PluginModule;

@AutoService(Plugin.class)
public class RecorderPlugin implements Plugin {

    @NonNull
    @Override
    public PluginModule createPluginModule() {
        return new RecorderPluginModule();
    }

}