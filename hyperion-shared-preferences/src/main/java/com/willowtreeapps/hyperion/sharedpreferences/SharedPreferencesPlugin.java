package com.willowtreeapps.hyperion.sharedpreferences;

import android.support.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class SharedPreferencesPlugin extends Plugin {

    @Nullable
    @Override
    public PluginModule createPluginModule() {
        return new SharedPreferencesPluginModule();
    }

}
