package com.willowtreeapps.hyperion.location;

import com.willowtreeapps.hyperion.core.internal.HyperionCoreComponent;

import dagger.Component;

@Component(modules = LocationModule.class, dependencies = HyperionCoreComponent.class)
interface LocationComponent {
}