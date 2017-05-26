package com.willowtreeapps.hyperion.measurement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.ComponentContextWrapper;
import com.willowtreeapps.hyperion.core.ContentView;
import com.willowtreeapps.hyperion.core.HyperionCore;
import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.Plugin;
import com.willowtreeapps.hyperion.core.PluginComponent;

@AutoService(Plugin.class)
public class MeasurementInspectorPlugin extends Plugin {

    @NonNull
    @Override
    public String name() {
        return "Measurement Inspector";
    }

    @Override
    public PluginComponent createComponent(@NonNull Context context) {
        HyperionCoreComponent hyperionCoreComponent = HyperionCore.getComponent(context);
        MeasurementInspectorComponent component = DaggerMeasurementInspectorComponent.builder()
                .hyperionCoreComponent(hyperionCoreComponent)
                .measurementInspectorModule(new MeasurementInspectorModule())
                .build();
        return new Component(context, component);
    }

    private static final class Component extends PluginComponent {

        private final ComponentContextWrapper<MeasurementInspectorComponent> context;

        private Component(@NonNull Context context, MeasurementInspectorComponent component) {
            this.context = new ComponentContextWrapper<>(context, component);
        }

        @Nullable
        @Override
        public ContentView getContentView() {
            return new MeasurementInspectorView(context);
        }
    }
}