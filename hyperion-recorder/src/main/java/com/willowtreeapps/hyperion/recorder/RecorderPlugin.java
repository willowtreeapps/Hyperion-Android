package com.willowtreeapps.hyperion.recorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.ComponentContextWrapper;
import com.willowtreeapps.hyperion.core.DrawerView;
import com.willowtreeapps.hyperion.core.HyperionCore;
import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.Plugin;
import com.willowtreeapps.hyperion.core.PluginComponent;


@AutoService(Plugin.class)
public class RecorderPlugin extends Plugin {

    @NonNull
    @Override
    public String name() {
        return "Screen Recorder";
    }

    @Override
    public PluginComponent createComponent(@NonNull Context context) {
        HyperionCoreComponent hyperionCoreComponent = HyperionCore.getComponent(context);
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