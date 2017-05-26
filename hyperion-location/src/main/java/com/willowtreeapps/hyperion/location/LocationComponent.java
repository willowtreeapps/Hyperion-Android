package com.willowtreeapps.hyperion.location;

import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.PluginScope;

import dagger.Component;

@PluginScope
@Component(modules = LocationModule.class, dependencies = HyperionCoreComponent.class)
interface LocationComponent {
}