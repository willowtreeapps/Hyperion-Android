package com.willowtreeapps.hyperion.attr;

import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.PluginScope;

import dagger.Component;

@PluginScope
@Component(modules = AttributeInspectorModule.class, dependencies = HyperionCoreComponent.class)
interface AttributeInspectorComponent {
    void inject(AttributeInspectorView view);
    void inject(AttributeDetailView view);
}