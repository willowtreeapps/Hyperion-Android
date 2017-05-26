package com.willowtreeapps.hyperion.recorder;

import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.PluginScope;

import dagger.Component;

@PluginScope
@Component(modules = RecorderModule.class, dependencies = HyperionCoreComponent.class)
interface RecorderComponent {
    void inject(RecorderView view);
}