package com.willowtreeapps.hyperion.recorder;

import com.willowtreeapps.hyperion.core.internal.HyperionCoreComponent;

import dagger.Component;

@Component(modules = RecorderModule.class, dependencies = HyperionCoreComponent.class)
interface RecorderComponent {
    void inject(RecorderView view);
}