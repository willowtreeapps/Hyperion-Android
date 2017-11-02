package com.willowtreeapps.hyperion.measurement;

import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.plugins.v1.Plugin;

@AutoService(Plugin.class)
public class MeasurementInspectorPlugin implements Plugin<MeasurementInspectorModule> {

    @NonNull
    @Override
    public MeasurementInspectorModule createPluginModule() {
        return new MeasurementInspectorModule();
    }

}