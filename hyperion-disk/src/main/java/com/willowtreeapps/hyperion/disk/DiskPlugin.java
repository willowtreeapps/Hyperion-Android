package com.willowtreeapps.hyperion.disk;

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
public class DiskPlugin extends Plugin {

    @NonNull
    @Override
    public String name() {
        return "Disk Monitor";
    }

    @Override
    public PluginComponent createComponent(@NonNull Context context) {
        HyperionCoreComponent hyperionCoreComponent = HyperionCore.getComponent(context);
        DiskComponent component = DaggerDiskComponent.builder()
                .hyperionCoreComponent(hyperionCoreComponent)
                .diskModule(new DiskModule(context.getFilesDir().getPath()))
                .build();
        return new Component(context, component);
    }

    private static final class Component extends PluginComponent {

        private final ComponentContextWrapper<DiskComponent> context;

        private Component(@NonNull Context context, DiskComponent component) {
            this.context = new ComponentContextWrapper<>(context, component);
        }

        @Nullable
        @Override
        public DrawerView getDrawerView() {
            return new DiskView(context);
        }
    }
}