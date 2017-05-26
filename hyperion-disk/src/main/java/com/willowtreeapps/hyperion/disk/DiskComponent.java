package com.willowtreeapps.hyperion.disk;

import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.PluginScope;

import dagger.Component;

@PluginScope
@Component(modules = DiskModule.class, dependencies = HyperionCoreComponent.class)
interface DiskComponent {
    void inject(DiskView view);
}