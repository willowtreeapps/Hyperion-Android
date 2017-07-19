package com.willowtreeapps.hyperion.location;

import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.plugins.Plugin;
import com.willowtreeapps.hyperion.core.plugins.PluginModule;

@AutoService(Plugin.class)
public class LocationPlugin implements Plugin {

    @NonNull
    @Override
    public PluginModule createPluginModule() {
        return new LocationModule();
    }
}