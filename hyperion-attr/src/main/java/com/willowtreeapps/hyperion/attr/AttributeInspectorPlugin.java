package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

@AutoService(Plugin.class)
public class AttributeInspectorPlugin implements Plugin<AttributeInspectorModule> {

    @NonNull
    @Override
    public AttributeInspectorModule createPluginModule() {
        return new AttributeInspectorModule();
    }

}