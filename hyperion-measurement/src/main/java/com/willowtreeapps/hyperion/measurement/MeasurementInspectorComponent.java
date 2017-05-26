package com.willowtreeapps.hyperion.measurement;

import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.PluginScope;

import dagger.Component;

@PluginScope
@Component(modules = MeasurementInspectorModule.class, dependencies = HyperionCoreComponent.class)
interface MeasurementInspectorComponent {
    void inject(MeasurementInspectorView view);
}