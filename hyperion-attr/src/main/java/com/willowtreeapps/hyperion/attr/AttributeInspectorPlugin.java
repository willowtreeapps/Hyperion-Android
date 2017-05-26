package com.willowtreeapps.hyperion.attr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.ComponentContextWrapper;
import com.willowtreeapps.hyperion.core.ContentView;
import com.willowtreeapps.hyperion.core.DrawerView;
import com.willowtreeapps.hyperion.core.HyperionCore;
import com.willowtreeapps.hyperion.core.HyperionCoreComponent;
import com.willowtreeapps.hyperion.core.Plugin;
import com.willowtreeapps.hyperion.core.PluginComponent;

@AutoService(Plugin.class)
public class AttributeInspectorPlugin extends Plugin {

    @NonNull
    @Override
    public String name() {
        return "Attribute Inspector";
    }

    @Override
    public PluginComponent createComponent(@NonNull Context context) {
        HyperionCoreComponent hyperionCoreComponent = HyperionCore.getComponent(context);
        AttributeInspectorComponent component = DaggerAttributeInspectorComponent.builder()
                .hyperionCoreComponent(hyperionCoreComponent)
                .attributeInspectorModule(new AttributeInspectorModule())
                .build();
        return new Component(context, component);
    }

    private static final class Component extends PluginComponent {

        private final ComponentContextWrapper<AttributeInspectorComponent> context;

        private Component(@NonNull Context context, AttributeInspectorComponent component) {
            this.context = new ComponentContextWrapper<>(context, component);
        }

        @Nullable
        @Override
        public ContentView getContentView() {
            return new AttributeInspectorView(context);
        }

        @Nullable
        @Override
        public DrawerView getDrawerView() {
            return new AttributeDetailView(context);
        }
    }
}