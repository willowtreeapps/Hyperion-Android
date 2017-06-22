package com.willowtreeapps.hyperion.disk;

import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.plugins.Plugin;
import com.willowtreeapps.hyperion.core.plugins.PluginModule;

@AutoService(Plugin.class)
public class DiskPlugin implements Plugin {

    @NonNull
    @Override
    public PluginModule createPluginModule() {
        return new DiskModule();
    }

}