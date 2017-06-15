package com.willowtreeapps.hyperion.recorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.internal.ComponentContextWrapper;
import com.willowtreeapps.hyperion.core.internal.Dagger;
import com.willowtreeapps.hyperion.core.internal.HyperionCoreComponent;


@AutoService(Plugin.class)
public class RecorderPlugin extends Plugin {

    @NonNull
    @Override
    public String name() {
        return "Screen Recorder";
    }

    @Override
    public PluginComponent createComponent(@NonNull Context context) {
        HyperionCoreComponent hyperionCoreComponent = Dagger.getComponent(context);
        RecorderComponent component = DaggerRecorderComponent.builder()
                .hyperionCoreComponent(hyperionCoreComponent)
                .recorderModule(new RecorderModule())
                .build();
        return new Component(context, component);
    }

    private static final class Component extends PluginComponent {

        private final ComponentContextWrapper<RecorderComponent> context;

        private Component(@NonNull Context context, RecorderComponent component) {
            this.context = new ComponentContextWrapper<>(context, component);
        }

        @Nullable
        @Override
        public DrawerView getDrawerView() {
            return new RecorderView(context);
        }
    }
}