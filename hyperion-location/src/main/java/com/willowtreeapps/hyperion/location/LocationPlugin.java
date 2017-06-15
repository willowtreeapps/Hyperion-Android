package com.willowtreeapps.hyperion.location;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.internal.ComponentContextWrapper;
import com.willowtreeapps.hyperion.core.internal.Dagger;
import com.willowtreeapps.hyperion.core.internal.HyperionCoreComponent;

@AutoService(Plugin.class)
public class LocationPlugin extends Plugin {

    @NonNull
    @Override
    public String name() {
        return "Location";
    }

    @Override
    public PluginComponent createComponent(@NonNull Context context) {
        HyperionCoreComponent hyperionCoreComponent = Dagger.getComponent(context);
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