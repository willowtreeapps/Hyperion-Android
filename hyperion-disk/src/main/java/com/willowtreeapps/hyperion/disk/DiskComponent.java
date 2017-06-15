package com.willowtreeapps.hyperion.disk;

import com.willowtreeapps.hyperion.core.internal.HyperionCoreComponent;

import dagger.Component;

@Component(modules = DiskModule.class, dependencies = HyperionCoreComponent.class)
interface DiskComponent {
    void inject(DiskView view);
}