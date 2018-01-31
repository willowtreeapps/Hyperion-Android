package com.willowtreeapps.hyperion.phoenix;

import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

@AutoService(Plugin.class)
public class PhoenixPlugin implements Plugin<PhoenixModule> {

    @Override
    @NonNull
    public PhoenixModule createPluginModule() {
        return new PhoenixModule();
    }
}