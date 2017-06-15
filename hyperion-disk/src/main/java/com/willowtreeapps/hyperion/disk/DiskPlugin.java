package com.willowtreeapps.hyperion.disk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.internal.ComponentContextWrapper;
import com.willowtreeapps.hyperion.core.internal.Dagger;
import com.willowtreeapps.hyperion.core.internal.HyperionCoreComponent;

@AutoService(Plugin.class)
public class DiskPlugin extends Plugin {

    @NonNull
    @Override
    public String name() {
        return "Disk Monitor";
    }

    @Override
    public PluginComponent createComponent(@NonNull Context context) {
        HyperionCoreComponent hyperionCoreComponent = Dagger.getComponent(context);
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