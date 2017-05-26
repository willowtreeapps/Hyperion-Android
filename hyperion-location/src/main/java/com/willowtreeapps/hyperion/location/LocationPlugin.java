package com.willowtreeapps.hyperion.location;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.ComponentContextWrapper;
import com.willowtreeapps.hyperion.core.HyperionCore;
import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.Plugin;
import com.willowtreeapps.hyperion.core.PluginComponent;

@AutoService(Plugin.class)
public class LocationPlugin extends Plugin {

    @NonNull
    @Override
    public String name() {
        return "Location";
    }

    @Override
    public PluginComponent createComponent(@NonNull Context context) {
        HyperionCoreComponent hyperionCoreComponent = HyperionCore.getComponent(context);
        LocationComponent component = DaggerLocationComponent.builder()
                .hyperionCoreComponent(hyperionCoreComponent)
                .locationModule(new LocationModule())
                .build();
        return new Component(context, component);
    }

    private static final class Component extends PluginComponent {

        private final ComponentContextWrapper<LocationComponent> context;

        private Component(@NonNull Context context, LocationComponent component) {
            this.context = new ComponentContextWrapper<>(context, component);
        }
    }
}